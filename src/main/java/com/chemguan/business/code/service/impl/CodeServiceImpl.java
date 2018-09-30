package com.chemguan.business.code.service.impl;


import com.chemguan.business.code.model.CodeSchema;
import com.chemguan.business.code.model.CodeTable;
import com.chemguan.business.code.service.CodeService;
import com.chemguan.business.dbutil.*;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.beetl.core.Configuration;
import org.beetl.core.GroupTemplate;
import org.beetl.core.Template;
import org.beetl.core.resource.ClasspathResourceLoader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import java.io.*;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;


/**
 * Created by lcy on 17/6/28.
 */
@Service
@Transactional(readOnly = true, propagation = Propagation.NOT_SUPPORTED)
@Slf4j
public class CodeServiceImpl implements CodeService {
    private static final long serialVersionUID = -3827321264317955429L;

    private static final String projectBasePath="E:\\IdeaProject\\AutoCode\\src\\main\\java";

    private static final String templateBasePath="E:\\IdeaProject\\AutoCode\\src\\main\\resources\\templates";

    @Autowired
    private FamilyDbUtils familyDbUtils;


    public List<CodeTable> getCodeTablesBySchema(CodeSchema schema) {
        List<CodeTable> tables = new ArrayList<>();
        Connection conn = familyDbUtils.getConnection(schema);
        ResultSet rs = null;
        DatabaseMetaData dbmd = null;
        CodeTable ct = null;
        DbColumnModel model=null;

        try {
            dbmd = conn.getMetaData();
            rs = dbmd.getTables(null, schema.getDbName(), "%", null);
            while (rs.next()) {
                List<DbColumnModel> models = new ArrayList<>();
                ct = new CodeTable();
                ct.setTableName(rs.getString("TABLE_NAME"));


                ResultSet rscolum = null;
                rscolum = dbmd.getColumns(null, schema.getDbName(), ct.getTableName().toLowerCase(), null);
                dbmd.getPrimaryKeys(null, null, ct.getTableName().toLowerCase());
                while (rscolum.next()) {
                    model = new DbColumnModel();
                    model.copyColumnFromSqlResult(rscolum);
                    models.add(model);
                }
                ct.setListColum(models);
                tables.add(ct);
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new ServiceException("数据库连接信息错误,或数据库不存在数据表!");
        } finally {
            this.killConnection(rs, conn);
        }
        return tables;
    }





    @Override
    public String generateCode(CodeSchema codeSchema) throws Exception {
        String filePath = null;
        List<DbTableModel> tableModels = new ArrayList<DbTableModel>();
        List<DbColumnModel> models = null;
        ResultSet rs = null;
        DatabaseMetaData dbmd = null;
        CodeTable ct = null;
        DbTableModel tableModel = null;
        DbColumnModel model = null;
        Connection conn = familyDbUtils.getConnection(codeSchema);

        if (null == codeSchema.getTables() || "".equals(codeSchema.getTables().trim()))
            throw new ServiceException("没有选择任何表！");
        if (conn == null)
            throw new ServiceException("数据库连接错误！");

        String[] tables = codeSchema.getTables().split(",");

        try {
            dbmd = conn.getMetaData();
            for (String table : tables) {
                String tableName=table.split("-")[0];
                String[] columName=null;
                try {
                    String chooseColums=table.split("-")[1];
                    columName = chooseColums.split("\\$");
                }catch (Exception e){

                }

                // table
                tableModel = new DbTableModel();
                models = new ArrayList<DbColumnModel>();
                tableModel.setTableName(tableName.toLowerCase());
                rs = dbmd.getTables(null, codeSchema.getDbName(), tableName,  new String[] { "TABLE" });
                while (rs.next()) {
                    if(rs.getString("TABLE_NAME").equals(table)){
                        String remarks = rs.getString("REMARKS");
                        tableModel.setRemarks(remarks);
                    }
                }

                rs = dbmd.getColumns(null, codeSchema.getDbName(), tableName.toLowerCase(), null);
                dbmd.getPrimaryKeys(null, null, tableName.toLowerCase());
                while (rs.next()) {
                    model = new DbColumnModel();
                    model.copyColumnFromSqlResult(rs);
                    if(columName!=null){
                        for(int i=0;i<columName.length;i++){
                            if(columName[i].equals(model.getColName())){
                                System.out.println("111");
                                model.setChoose(true);
                            }
                        }
                    }
                    models.add(model);
                }

                rs = dbmd.getPrimaryKeys(null, codeSchema.getDbName(), tableName.toLowerCase());
                while (rs.next()) {
                    String column = rs.getString("COLUMN_NAME");
                    for (DbColumnModel cm : models) {
                        if (column.equals(cm.getColName())) {
                            cm.setKey(true);
                            break;
                        }
                    }
                }
                tableModel.setColumnModels(models);
                tableModels.add(tableModel);
            }
        } catch (Exception e) {
            throw new ServiceException("生成表模型错误", e);
        } finally {
            this.killConnection(rs, conn);
        }

        codeSchema.setBasePackage("com.chemguan");
        codeSchema.setViewPackage("manager");
        this.productCodeFromTable(codeSchema, tableModels);
        return filePath;
    }



    /**
     * 通过freemarker 成功多层代码
     *
     * @param tableModels
     * @return
     */
    private void productCodeFromTable(CodeSchema codeSchema,
                                        List<DbTableModel> tableModels)
            throws Exception {
        Map root = null;

        Map templateRoot = null;

        List<File> fileList = new ArrayList<File>();
        File file = null;
        //逻辑代码文件地址
        String dirPath =projectBasePath;
        //视图层代码文件地址
        String templatePath=templateBasePath;

        //将. 换/
        String pathPattern = codeSchema.getBasePackage().replaceAll("\\.", "\\" + File.separator);

        //存在删除
        dirPath = dirPath + File.separator + pathPattern;
        File dirF = new File(dirPath);
        dirF.deleteOnExit();

        templatePath=templatePath+File.separator +codeSchema.getViewPackage();
        File dirT = new File(templatePath);
        dirT.deleteOnExit();

        //生成
        for (DbTableModel dbTableModel : tableModels) {
            root = new HashMap<String, Object>();
            String basePackage = codeSchema.getBasePackage();
            root.put("author", codeSchema.getAuthor());
            root.put("basePackage", basePackage);
            root.put("corePackage", ProjectConstant.BASE_PACKAGE);
            root.put("table", dbTableModel);

            templateRoot= new HashMap<String, Object>();
            templateRoot.put("table",dbTableModel);
            templateRoot.put("ctxPath","${ctxPath}");
            templateRoot.put("includetop","<%layout(\"include/top.html\"){}%>");
            templateRoot.put("includeleft","<%layout(\"include/left.html\"){}%>");
            templateRoot.put("pageview","<%layout(\"include/pageview.html\"){}%>");

            String newPath = dirPath;

            String viewPath=templatePath;

            this.generateEntityModel(newPath, dbTableModel, root, "Entity");

            this.generateEntityModel(newPath, dbTableModel, root, "Mapper");

            this.generateEntityModel(newPath, dbTableModel, root, "Service");

            this.generateEntityModel(newPath, dbTableModel, root, "ServiceImpl");

            this.generateEntityModel(newPath, dbTableModel, root, "Repository");

            this.generateEntityModel(newPath, dbTableModel, root, "Controller");

            this.generateEntityModel(newPath, dbTableModel, root, "ModelController");

            this.generateEntityModel(viewPath, dbTableModel, templateRoot, "findall");

            this.generateEntityModel(viewPath, dbTableModel, templateRoot, "modify");
        }
    }

    /**
     * 关闭 connection
     *
     * @param rs
     * @param conn
     */
    private void killConnection(ResultSet rs, Connection conn) {
        try {
            if (null != rs)
                rs.close();
            if (null != conn)
                conn.close();
        } catch (SQLException e) {
            log.error(e.getMessage());
        }
    }





    /**
     * model  构建  entity 层
     *
     * @param dirPath
     * @param dbTableModel
     * @param root
     * @return
     */
    private File generateEntityModel(String dirPath, DbTableModel dbTableModel, Map root, String suffix) throws Exception {
        //源文件夹
        File dirFile = null;
        //目标文件
        File file = null;
        FileWriter out = null;
        if (suffix.equals("Repository"))
            dirFile = new File(dirPath + File.separator + "dao");
        else if (suffix.equals("ServiceImpl"))
            dirFile = new File(dirPath + File.separator + "service" + File.separator + "impl");
        else if(suffix.equals("findall")||suffix.equals("modify"))
            dirFile = new File(dirPath);
        else
            dirFile = new File(dirPath + File.separator + suffix.toLowerCase());
        //不存在 创建文件夹
        if (!dirFile.exists())
            dirFile.mkdirs();



        if (suffix.equals("Repository"))
            file = new File(dirPath + File.separator + "dao" +
                    File.separator + dbTableModel.getEntityName() + suffix + ".java");
        else if (suffix.equals("ServiceImpl"))
            file = new File(dirPath + File.separator + "service" + File.separator + "impl" +
                    File.separator + dbTableModel.getEntityName() + suffix + ".java");
        else if (suffix.equals("Entity"))
            file = new File(dirPath + File.separator + suffix.toLowerCase() +
                    File.separator + dbTableModel.getEntityName() + ".java");
        else if (suffix.equals("Mapper"))
            file = new File(dirPath + File.separator + suffix.toLowerCase() +
                    File.separator + dbTableModel.getEntityName() + suffix + ".xml");
        else if (suffix.equals("findall")||suffix.equals("modify"))
            file = new File(dirPath + File.separator +
                    dbTableModel.getEntityNameLowcase() +suffix.toLowerCase() + ".html");
        else
            file = new File(dirPath + File.separator + suffix.toLowerCase() +
                    File.separator + dbTableModel.getEntityName() + suffix + ".java");
        try {
            out = new FileWriter(file);
            ClasspathResourceLoader resourceLoader = new ClasspathResourceLoader("templates/codetemplate/");
            Configuration cfg = Configuration.defaultConfiguration();
            GroupTemplate gt = new GroupTemplate(resourceLoader, cfg);
            Template temp = gt.getTemplate(suffix.toLowerCase()+".html");
            temp.binding(root);
            temp.renderTo(out);
        } finally {
            try {
                if (out != null)
                    out.close();
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
        return file;
    }
}
