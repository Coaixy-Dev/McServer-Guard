package ren.lawliet.Java;

import ren.lawliet.Java.F2kDoor.ArgsParser;

/**
 * @author Coaixy
 * @createTime 2024-07-13
 * @packageName ren.lawliet.Java
 */

public class Main {
    public static void main(String[] args) {
        ArgsParser argsParser = new ArgsParser(args);
        argsParser.parseArgs();
    }
}