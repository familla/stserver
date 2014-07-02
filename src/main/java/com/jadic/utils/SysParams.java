package com.jadic.utils;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Properties;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SysParams {

    private final static SysParams sysParams = new SysParams();
    private KKConfig kkConfig;
    
    private int localTcpPort;

    /* 数据库相关参数 */
    private String jdbcDriver;
    private String jdbcUrl;
    private String dbUserName;
    private String dbUserPass;

    private List<JDBCConfig> jdbcList;

    public static SysParams getInstance() {
        return sysParams;
    }

    private SysParams() {
        String configFilePath = "conf.properties";
        kkConfig = new KKConfig(configFilePath);
        jdbcList = new ArrayList<JDBCConfig>();
        this.loadSysParams();
    }

    public void loadSysParams() {
        this.localTcpPort = kkConfig.getIntValue("localTcpPort");
        int jdbcCount = kkConfig.getIntValue("jdbcCount");
        String jdbcName, jdbcDriver, jdbcUrl, dbUserName, dbUserPass = null;
        for (int i = 0; i < jdbcCount; i++) {
            jdbcName = getDefaultStr(kkConfig.getStrValue("jdbcName" + (i + 1)), "jdbc" + (i + 1));
            jdbcDriver = kkConfig.getStrValue("jdbcDriver" + (i + 1));
            jdbcUrl = kkConfig.getStrValue("jdbcUrl" + (i + 1));
            dbUserName = kkConfig.getStrValue("dbUserName" + (i + 1));
            dbUserPass = kkConfig.getStrValue("dbUserPass" + (i + 1));
            if (!isStringsNullOrEmpty(jdbcDriver, jdbcUrl, dbUserName, dbUserPass)) {
                JDBCConfig jdbcConfig = new JDBCConfig(jdbcName, jdbcDriver, jdbcUrl, dbUserName, dbUserPass);
                this.jdbcList.add(jdbcConfig);
            }
        }
    }

    /**
     * 获取配置参数内容
     * 
     * @return
     */
    public String getSysParamStrs() {
        String split = "\n                  * ";
        StringBuilder sBuilder = new StringBuilder();
        sBuilder.append(split);
        sBuilder.append("jdbc.count=").append(this.jdbcList.size());
        JDBCConfig jdbcConfig = null;
        for (int i = 0; i < this.jdbcList.size(); i++) {
            jdbcConfig = this.jdbcList.get(i);
            sBuilder.append(split);
            sBuilder.append("jdbcUrl" + (i + 1)).append(jdbcConfig.getJdbcUrl());
            sBuilder.append(split);
            sBuilder.append("jdbcDriver" + (i + 1)).append(jdbcConfig.getJdbcDriver());
            sBuilder.append(split);
            sBuilder.append("dbUserName" + (i + 1)).append(jdbcConfig.getDbUserName());
            sBuilder.append(split);
            sBuilder.append("dbUserPass" + (i + 1)).append(jdbcConfig.getDbUserPass());
        }

        sBuilder.append(split);
        sBuilder.append("localTcpPort:").append(this.localTcpPort);

        return sBuilder.toString();
    }

    public static String getDefaultStr(String expectedVal, String defaultVal) {
        if (KKTool.isStrNullOrBlank(expectedVal)) {
            return KKTool.isStrNullOrBlank(defaultVal) ? "" : defaultVal;
        }
        return expectedVal;
    }

    public static boolean isStringsNullOrEmpty(String... strs) {
        if (strs != null) {
            for (String s : strs) {
                if (s == null || s.length() == 0) {
                    return true;
                }
            }
            return false;
        }
        return true;
    }

    public String getJdbcDriver() {
        return jdbcDriver;
    }

    public String getJdbcUrl() {
        return jdbcUrl;
    }

    public String getDbUserName() {
        return dbUserName;
    }

    public String getDbUserPass() {
        return dbUserPass;
    }

    public List<JDBCConfig> getJdbcListCopy() {
        return new ArrayList<JDBCConfig>(jdbcList);
    }

    public int getLocalTcpPort() {
        return localTcpPort;
    }

}

class KKConfig {
    
    private final static Logger logger = LoggerFactory.getLogger(KKConfig.class);
    
    private Properties properties;
    private FileInputStream inputStream;
    private String configFileName;

    public KKConfig(String configFilePath) {
        this.properties = new Properties();
        this.configFileName = KKConfig.class.getClassLoader().getResource(configFilePath).getPath();
        try {
            this.inputStream = new FileInputStream(configFileName);
            this.properties.load(this.inputStream);
        } catch (FileNotFoundException ex) {
            logger.error("Can't find config file[{}]", configFilePath);
        } catch (IOException ex) {
            logger.error("Read config file IOException", ex);
        } finally {
            try {
                if (this.inputStream != null)
                    this.inputStream.close();
            } catch (IOException e) {
            }
        }
    }

    public String getStrValue(String key) {
        if (this.properties.containsKey(key)) {
            return this.properties.getProperty(key, "").trim();
        }
        return "";
    }

    public int getIntValue(String key) {
        if (this.properties.containsKey(key)) {
            try {
                return Integer.parseInt(this.properties.getProperty(key, "0"));
            } catch (Exception e) {
                return 0;
            }
        }
        return 0;
    }

    public boolean writeValue(Map<String, String> params) {
        FileOutputStream fos;
        try {
            fos = new FileOutputStream(this.configFileName);
            Set<Map.Entry<String, String>> paramSet = params.entrySet();
            Iterator<Entry<String, String>> ite = paramSet.iterator();
            while (ite.hasNext()) {
                Entry<String, String> param = ite.next();
                this.properties.setProperty(param.getKey(), param.getValue());
            }
            this.properties.store(fos, "set");
            return true;
        } catch (FileNotFoundException e1) {
            logger.error("write to config file err: {} not found", configFileName);
        } catch (IOException e) {
            logger.error("Write to config file err: IOException", e);
        }
        return false;
    }
}