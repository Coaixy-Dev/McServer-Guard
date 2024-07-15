package ren.lawliet.Java.F2kDoor;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

/**
 * @author Coaixy
 * @createTime 2024-07-15
 * @packageName ren.lawliet.Java.F2kDoor
 */

public class ClassFinder {
    /**
     * Class Info
     *
     * @param jarFileName
     * @param className
     * @param classInputStream
     */
    public record ClassInfo(String jarFileName, String className, InputStream classInputStream) {
        @Override
        public String toString() {
            return jarFileName + " " + className + " " + classInputStream.toString() + "\n";
        }
    }

    /**
     * Get Class Input Stream Map
     *
     * @param jarFilePathList Jar File Path List
     * @return
     */
    public static ArrayList<ClassInfo> getClassInputStreamMap(ArrayList<String> jarFilePathList) {
        ArrayList<ClassInfo> classInfoList = new ArrayList<>();
        // Load all class files from jar files
        for (String jarFilePathStr : jarFilePathList) {
            try (JarFile jarFile = new JarFile(jarFilePathStr)) {
                Enumeration<JarEntry> entries = jarFile.entries();
                while (entries.hasMoreElements()) {
                    JarEntry entry = entries.nextElement();
                    if (entry.isDirectory()) {
                        continue;
                    }
                    InputStream is = jarFile.getInputStream(entry);
                    byte[] data = is.readAllBytes();
                    if (entry.getName().endsWith(".class")) {
                        classInfoList.add(new ClassInfo(jarFilePathStr, entry.getName(), new ByteArrayInputStream(data)));
                    }

                }
            } catch (IOException e) {
                System.out.println("Error: " + e.getMessage() + " " + jarFilePathStr);

            }
        }
        return classInfoList;
    }
}
