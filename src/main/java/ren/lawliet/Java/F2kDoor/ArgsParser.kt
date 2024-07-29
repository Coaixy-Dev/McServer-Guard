package ren.lawliet.Java.F2kDoor

import java.io.IOException

/**
 * @author Coaixy
 * @createTime 2024-07-15
 * @packageName ren.lawliet.Java.F2kDoor
 */
class ArgsParser(private val args: Array<String>) {
    private val types = ArrayList<String>()
    private val selectType = ArrayList<String>()
    private val pluginPathStrList: ArrayList<String> = Path.pluginPathArray

    init {
        types.add("String")
        types.add("Plugin")
        types.add("Socket")
    }

    fun helpMsg(): String {
        return """
                Usage: java -jar McServer-Guard.jar [options]
                Options:
                    -h, --help      Show this help message and exit
                    -t, --type      [String,Plugin,Socket] Select mode for McServer-Guard
                Example:
                    java -jar McServer-Guard.jar -t String
                
                """.trimIndent()
    }

    fun parseArgs() {
        if (args.isEmpty()) {
            println(helpMsg())
            println("Now Running Default Type - String\n")
            StringRunner()
        }
        for (arg in args) {
            if (arg.equals("-h", ignoreCase = true) || arg.equals("--help", ignoreCase = true)) {
                println(helpMsg())
                System.exit(0)
            }
            if (arg.equals("-t", ignoreCase = true) || arg.equals("--type", ignoreCase = true)) {
                if (args.size == 1) {
                    System.err.println("Please select a mode")
                    System.exit(0)
                }
                for (i in 1 until args.size) {
                    if (types.contains(args[i])) {
                        selectType.add(args[i])
                    } else {
                        System.err.println("Please select a correct mode")
                        System.exit(0)
                    }
                }
                println(selectType)
            }
        }
    }

    private fun StringRunner() {
        var count = 0
        // get all class inputStream
        val classInfoList = ClassFinder.getClassInputStreamMap(pluginPathStrList)
        var tempJarFileName = ""
        for (classInfo in classInfoList) {
            try {
                val detectStatus = StrDetect.detectString(classInfo)
                if (detectStatus.status) {
                    count = count + 1

                    // remove duplicates items
                    val classNameList = ArrayList(detectStatus.className?.let { LinkedHashSet(it) }!!)
                    val lineList = ArrayList(LinkedHashSet(detectStatus.line!!))
                    val jarFileName = detectStatus.jarFileName

                    // format print
                    if (!tempJarFileName.equals(jarFileName, ignoreCase = true)) {
                        println(
                            """
                                Found SetOP in $jarFileName
                                ---------------------------
                                
                                """.trimIndent()
                        )
                        tempJarFileName = jarFileName.toString()
                    }
                    for (i in classNameList.indices) {
                        println(
                            """
                                Class Name: ${classNameList[i]}
                                Line: ${lineList[i]}
                                
                            """.trimIndent()
                        )
                        if (i == classNameList.size - 1) {
                            println("---------------------------\n")
                        }
                    }
                }
            } catch (e: IOException) {
                throw RuntimeException(e)
            }
        }
        if (count == 0) {
            println("SetOp is not found \n")
        }
    }
}
