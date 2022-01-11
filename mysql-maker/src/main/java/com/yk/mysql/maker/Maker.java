package com.yk.mysql.maker;

import com.yk.mysql.maker.bean.Table;
import com.yk.mysql.maker.util.CodeUtil;
import com.yk.mysql.maker.util.DBUtil;
import com.yk.mysql.maker.util.FileUtil;
import com.yk.mysql.maker.util.VelocityUtil;

import java.io.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

public class Maker {

    private static final String ENC = "UTF-8";

    public Maker() {
        new Maker("mysql-maker/src/main/java/com/yk/mysql/maker/resource");
    }

    public Maker(String vmDir) {
        try {
            String userDir = System.getProperty("user.dir");
            VelocityUtil.initVelocityEngine(
                userDir + File.separator + vmDir);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void make(String basePackage,
                     String outputRoot,
                     String modular,
                     Map<String, List<String>> groupMap,
                     String tablePrefix,
                     String tablePostfix) {
        String outputRootApi =
                outputRoot + modular + File.separator + modular + "-api" + File.separator + "src" + File.separator +
                        "main" + File.separator + "java";
        String outputRootProvider = outputRoot + modular + File.separator + modular + "-provider" + File.separator +
                "src" + File.separator + "main" + File.separator + "java";
        for (Entry<String, List<String>> entry : groupMap.entrySet()) {
            String group = entry.getKey();
            List<String> groupList = entry.getValue();
            for (String tableName : groupList) {
                Table table = DBUtil.getTable(tableName);
                Map<String, Object> data = new HashMap<String, Object>();
                data.put("util", new CodeUtil());
                data.put("table", table);
                data.put("tableName", tableName.substring(0, tableName.length() - tablePostfix.length()));

                // dto
                makeJava(data, basePackage, group, tableName, tablePrefix, tablePostfix, "dto", "Dto", outputRootApi,
                        "dto.vm");

                // entity
                makeJava(data, basePackage, group, tableName, tablePrefix, tablePostfix, "entity", "Entity",
                        outputRootProvider, "entity.vm");

                // param
                makeJava(data, basePackage, group, tableName, tablePrefix, tablePostfix, "param", "QueryParam",
                        outputRootApi, "query.vm");

                // service
                // makeJava(data, basePackage, group, tableName, tablePrefix, tablePostfix, "service",
                //                "Service", outputRootApi, "service.vm");

                // dao
                makeJava(data, basePackage, group, tableName, tablePrefix, tablePostfix, "dao", "Mapper",
                        outputRootProvider, "dao.vm");

                // manager
                makeJava(data, basePackage, group, tableName, tablePrefix, tablePostfix, "manager", "Manager",
                        outputRootProvider, "manager.vm");

                // ManagerImpl
                makeJava(data, basePackage, group, tableName, tablePrefix, tablePostfix, "manager.impl", "ManagerImpl",
                        outputRootProvider, "manager.impl.vm");

                // ServiceImpl
                makeJava(data, basePackage, group, tableName, tablePrefix, tablePostfix, "service.impl", "ServiceImpl",
                        outputRootProvider, "service.impl.vm");

                // xml
                makeFile(data, "xml.vm",
                        outputRootProvider + File.separator + ".." + File.separator + "resources" + File.separator +
                                "mybatis",
                        CodeUtil.convertClassNameToPath((String) data.get("daoName"), "xml"));
            }
        }
    }

    protected void makeJava(Map<String, Object> data,
                            String basePackage,
                            String group,
                            String tableName,
                            String tablePrefix,
                            String tablePostfix,
                            String common,
                            String variableSuffix,
                            String outputRoot,
                            String template) {
        String classPackage = CodeUtil.getPackage(basePackage, common, group);
        String variable = CodeUtil.getVariable(
                tableName.substring(tablePrefix.length(), tableName.length() - tablePostfix.length())) + variableSuffix;
        String name = CodeUtil.getUpperCaseVariable(variable);
        String className = CodeUtil.getClassName(classPackage, name);
        common = CodeUtil.standard(common);
        data.put(common + "Package", classPackage);
        data.put(common + "Variable", variable);
        //        name = getName(common, name);
        data.put(common + "Name", name);
        //        className = getName(common, className);
        data.put(common + "ClassName", className);
        makeFile(data, template, outputRoot, CodeUtil.convertClassNameToPath(className, "java"));
    }

    //    private String getName(String common, String name) {
    //        if (common.equals("service")) {
    //            name = name.substring(0, name.lastIndexOf("Service")) + "CrudService";
    //        } else if (common.equals("serviceImpl")) {
    //            name = name.substring(0, name.lastIndexOf("ServiceImpl")) + "CrudServiceImpl";
    //        }
    //        return name;
    //    }

    protected void makeFile(Map<String, Object> data, String resource, String outputRoot, String path) {
        BufferedWriter bw = null;
        try {
            File file = FileUtil.getFile(outputRoot, path);
            bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file), ENC));
            VelocityUtil.merge(data, resource, bw, ENC);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (bw != null) {
                try {
                    bw.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

}
