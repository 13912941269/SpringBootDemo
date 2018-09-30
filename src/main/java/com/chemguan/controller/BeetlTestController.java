package com.chemguan.controller;

import com.chemguan.dbmapper.VipUserMapper;
import com.chemguan.model.VipUser;
import com.chemguan.util.fileio.ExcelBean;
import com.chemguan.util.fileio.ExcelUtil;
import com.chemguan.util.fileio.FileUtil;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.json.MappingJackson2JsonView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by sw on 2018-06-12.
 */
@Controller
@RequestMapping("pc")
public class BeetlTestController {

    @Autowired
    private VipUserMapper dao;

    @Value("${fileinfo.imgRoot}")
    private String imgRoot;

    /**
     * beetl模板文件测试
     * @param type
     * @param modelMap
     * @return
     */
    @RequestMapping("beetltest/{type}")
    public ModelAndView beetltest(@PathVariable String type, ModelMap modelMap){
        ModelAndView mav = new ModelAndView();
        List<VipUser> list = dao.selectAllUser();
        modelMap.addAttribute("list", list);
        modelMap.addAttribute("email", "1367215312@qq.com");
        if ("html".equals(type)){
            mav.setViewName("/hello");
            return mav;
        }else if("json".equals(type)){
            MappingJackson2JsonView view = new MappingJackson2JsonView();
            view.setAttributesMap(modelMap);
            mav.setView(view);
            return mav;
        }
        return null;
    }


    /**
     * beetl模板文件测试
     * @return
     */
    @RequestMapping("index")
    public ModelAndView index(){

        return null;
    }


    /**
     * beetl模板文件测试
     * @return
     */
    @RequestMapping("webchat")
    public ModelAndView webchat(Integer oppenid, Integer tooppenid){
        ModelAndView mav = new ModelAndView();
        mav.addObject("oppenid",oppenid);
        mav.addObject("tooppenid",tooppenid);
        mav.setViewName("websocket/onchat");
        return mav;
    }



    /**
     * 文件上传
     * @return
     */
    @RequestMapping("fileuploadpage")
    public ModelAndView fileuploadpage() throws URISyntaxException {
        System.out.print(Thread.currentThread().getContextClassLoader().getResource("").toURI().getPath());
        ModelAndView mav = new ModelAndView();
        mav.setViewName("fileio/fileio");
        return mav;
    }


    /**
     * 文件上传,下载
     * @return
     */
    @RequestMapping("fileupload")
    public void fileupload(HttpServletRequest request, HttpServletResponse response) throws Exception {
        FileUtil util=new FileUtil();
        String file = util.saveImage(request, response, imgRoot,"file");
        String filepath=imgRoot+file;
        util.downLoad(filepath,response,true);
        return;
    }


    /**
     * 导入excel
     * @param request
     * @return
     * @throws Exception
     */
    @RequestMapping("import")
    public void impotr(HttpServletRequest request) throws Exception {
        //数据导入
        List<List<Object>> listob = ExcelUtil.getBankListByExcel(request,"excelfile");
        for(List<Object> list:listob){
            for(Object object:list){
                System.out.println(object.toString());
            }
        }
    }


    /**
     * 导出至excel
     * @param request
     * @param response
     * @throws Exception
     */
    @RequestMapping("/export")
    public void export(HttpServletRequest request, HttpServletResponse response) throws Exception{
        ExcelUtil util=new ExcelUtil();
        List<ExcelBean> excel=new ArrayList<>();
        Map<Integer,List<ExcelBean>> map=new LinkedHashMap<>();
        XSSFWorkbook xssfWorkbook=null;
        //设置标题栏
        excel.add(new ExcelBean("序号","id",0));
        excel.add(new ExcelBean("opneid","oppenid",0));
        excel.add(new ExcelBean("姓名","nickname",0));
        excel.add(new ExcelBean("头像","img",0));
        excel.add(new ExcelBean("手机号","phone",0));
        excel.add(new ExcelBean("真实姓名","realname",0));
        map.put(0, excel);
        String sheetName = "测试";
        List<VipUser> list=new ArrayList<VipUser>();
        VipUser user1=new VipUser();
        user1.setStaffname("name1");
        user1.setAvatar("img1");
        VipUser user2=new VipUser();
        user2.setStaffname("name2");
        user2.setAvatar("img2");
        VipUser user3=new VipUser();
        user3.setStaffname("name3");
        user3.setAvatar("img3");
        VipUser user4=new VipUser();
        user4.setStaffname("name4");
        user4.setAvatar("img4");
        list.add(user1);
        list.add(user2);
        list.add(user3);
        list.add(user4);
        //调用ExcelUtil的方法
        xssfWorkbook = ExcelUtil.createExcelFile(VipUser.class, list, map, sheetName);
        util.downLoadExcel(xssfWorkbook,sheetName,response);
    }
}
