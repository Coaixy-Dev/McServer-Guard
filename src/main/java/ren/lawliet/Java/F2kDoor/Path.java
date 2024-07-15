package ren.lawliet.Java.F2kDoor;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;

/**
 * @author Coaixy
 * @createTime 2024-07-14
 * @packageName ren.lawliet.Java.F2kDoor
 */


public class Path {
    public static ArrayList<String> getPluginPathArray() {
        ArrayList<String> pluginPathArray = new ArrayList<>();
        String dicPathStr = "/plugins";
        String selfPathStr = System.getProperty("user.dir");
        java.nio.file.Path dicPathObj = Paths.get(selfPathStr + dicPathStr);
        try {
            Files.walk(dicPathObj).filter(Files::isRegularFile).filter(Files::exists)
                    .filter(path -> path.toString().endsWith(".jar")).forEach(path -> {
                        pluginPathArray.add(path.toString());
                    });
        } catch (IOException e) {
            System.err.println("没有找到Plugins目录 " + e.getMessage());
        }

        return pluginPathArray;
    }
}
