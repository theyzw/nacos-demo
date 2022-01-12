package com.yk.mysql.maker;

import com.yk.mysql.maker.bean.Table;
import com.yk.mysql.maker.util.CodeUtil;
import com.yk.mysql.maker.util.DBUtil;
import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

public class MakerInsertSelect extends Maker {

    public MakerInsertSelect() {
        super();
    }

    public MakerInsertSelect(String vmDir) {
        super(vmDir);
    }

    /**
     * @param basePackage
     * @param outputRoot
     * @param modular
     * @param groupMap
     * @param tablePrefix
     * @param tablePostfix
     * @param splitApiProvider 是否分隔-api和-provider
     */
    public void make(String basePackage,
                     String outputRoot,
                     String modular,
                     Map<String, List<String>> groupMap,
                     String tablePrefix,
                     String tablePostfix,
                     boolean splitApiProvider) {
        String outputRootApi, outputRootProvider;
        if (splitApiProvider) {
            outputRootApi =
                outputRoot + modular + File.separator + modular + "-client" + File.separator + "src" + File.separator +
                    "main" + File.separator + "java";
            outputRootProvider = outputRoot + modular + File.separator + modular + "-provider" + File.separator +
                "src" + File.separator + "main" + File.separator + "server";
        } else {
            outputRootApi =
                outputRoot + modular + File.separator + "src" + File.separator +
                    "main" + File.separator + "java";
            outputRootProvider = outputRoot + modular + File.separator +
                "src" + File.separator + "main" + File.separator + "java";
        }

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
                makeJava(data, basePackage, group, tableName, tablePrefix, tablePostfix, "query", "Query",
                    outputRootApi, "query.vm");

                // service
                makeJava(data, basePackage, group, tableName, tablePrefix, tablePostfix, "service", "Service",
                    outputRootApi, "service.vm");

                // dao
                makeJava(data, basePackage, group, tableName, tablePrefix, tablePostfix, "dao", "Dao",
                    outputRootProvider, "dao.vm");

                // ServiceImpl
                makeJava(data, basePackage, group, tableName, tablePrefix, tablePostfix, "service.impl", "ServiceImpl",
                    outputRootProvider, "service.impl.vm");

                // xml
                makeFile(data, "xml-insert-select.vm",
                    outputRootProvider + File.separator + ".." + File.separator + "resources" + File.separator
                        + "mapper",
                    CodeUtil.convertClassNameToPath((String) data.get("daoName"), "xml"));
            }
        }
    }

}
