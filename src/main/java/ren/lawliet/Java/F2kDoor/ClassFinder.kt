package ren.lawliet.Java.F2kDoor

import java.io.ByteArrayInputStream
import java.io.IOException
import java.io.InputStream
import java.util.jar.JarFile

/**
 * @author Coaixy
 * @createTime 2024-07-15
 * @packageName ren.lawliet.Java.F2kDoor
 */
object ClassFinder {
    /**
     * Get Class Input Stream Map
     *
     * @param jarFilePathList Jar File Path List
     * @return
     */
    fun getClassInputStreamMap(jarFilePathList: ArrayList<String>): ArrayList<ClassInfo> {
        val classInfoList = ArrayList<ClassInfo>()
        // Load all class files from jar files
        for (jarFilePathStr in jarFilePathList) {
            try {
                JarFile(jarFilePathStr).use { jarFile ->
                    val entries = jarFile.entries()
                    while (entries.hasMoreElements()) {
                        val entry = entries.nextElement()
                        if (entry.isDirectory) {
                            continue
                        }
                        val `is` = jarFile.getInputStream(entry)
                        val data = `is`.readAllBytes()
                        if (entry.name.endsWith(".class")) {
                            classInfoList.add(ClassInfo(jarFilePathStr, entry.name, ByteArrayInputStream(data)))
                        }
                    }
                }
            } catch (e: IOException) {
                println("Error: " + e.message + " " + jarFilePathStr)
            }
        }
        return classInfoList
    }

    /**
     * Class Info
     *
     * @param jarFileName
     * @param className
     * @param classInputStream
     */
    @JvmRecord
    data class ClassInfo(
        @JvmField val jarFileName: String,
        @JvmField val className: String,
        @JvmField val classInputStream: InputStream
    ) {
        override fun toString(): String {
            return "$jarFileName $className $classInputStream\n"
        }
    }
}
