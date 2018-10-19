package com.chemguan.business.code.model;


import com.chemguan.business.dbutil.DbModel;
import lombok.Data;
import java.io.Serializable;

/**
 * Created by sw on 18/10/8.
 *
 * code schema
 */
@Data
public class CodeSchema implements Serializable {
    private static final long serialVersionUID = 1154645476624841358L;

    private String ip;

    private String db;

    private String url;

    private String dbName;

    private String username;

    private String password;

    private String port;

    private String dbProduct;

    private String driverClass;

    private String basePackage;

    private String viewPackage;

    private String author;

    private String tables;

    private String userId;

    public static long getSerialVersionUID() {
        return serialVersionUID;
    }


    public CodeSchema() {
    }

    public String getUrl() {
        if(DbModel.MYSQL.getType().equals(getDb()))
            return "jdbc:mysql://" + getIp() + ":" + getPort() + "/" + getDbName();
        return url;
    }

    public String getDriverClass() {
        if(DbModel.MYSQL.getType().equals(getDb()))
            return "com.mysql.jdbc.Driver";
        return driverClass;
    }


}
