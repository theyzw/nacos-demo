package com.yk.mysql.maker;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class YkMaker {

    private static final String VM_DIR = "mysql-maker/src/main/java/com/yk/mysql/maker/resource";

    public static void main(String[] args) {
        Map<String, List<String>> groupMap = new HashMap<>();
        List<String> groupList;
        String tablePrefix;
        String tablePostfix;

        groupList = new ArrayList<>();
        groupList.add("sys_user");
        groupMap.put(null, groupList);

        tablePrefix = "";
        tablePostfix = "";

        String userDir = System.getProperty("user.dir") + File.separator;

        System.out.println("项目主目录:" + userDir);

        new MakerInsertSelect(VM_DIR)
            .make("com.yk.demo.mysql", userDir, "mysql-demo",
                groupMap, tablePrefix, tablePostfix, true);
    }

}
