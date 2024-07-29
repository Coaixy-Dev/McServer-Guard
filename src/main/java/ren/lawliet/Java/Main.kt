package ren.lawliet.Java

import ren.lawliet.Java.F2kDoor.ArgsParser

/**
 * @author Coaixy
 * @createTime 2024-07-13
 * @packageName ren.lawliet.Java
 */
object Main {
    @JvmStatic
    fun main(args: Array<String>) {
        val argsParser = ArgsParser(args)
        argsParser.parseArgs()
    }
}