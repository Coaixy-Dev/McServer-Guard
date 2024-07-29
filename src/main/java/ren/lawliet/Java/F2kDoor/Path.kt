package ren.lawliet.Java.F2kDoor

import java.io.IOException
import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.Paths

/**
 * @author Coaixy
 * @createTime 2024-07-14
 * @packageName ren.lawliet.Java.F2kDoor
 */
object Path {
    val pluginPathArray: ArrayList<String>
        get() {
            val pluginPathArray = ArrayList<String>()
            val dicPathStr = "/plugins"
            val selfPathStr = System.getProperty("user.dir")
            val dicPathObj = Paths.get(selfPathStr + dicPathStr)
            try {
                Files.walk(dicPathObj).filter { path: Path? -> Files.isRegularFile(path) }
                    .filter { path: Path? -> Files.exists(path) }
                    .filter { path: Path -> path.toString().endsWith(".jar") }.forEach { path: Path ->
                        pluginPathArray.add(path.toString())
                    }
            } catch (e: IOException) {
                System.err.println("Plugin Directory is not find of " + e.message)
            }

            return pluginPathArray
        }
}
