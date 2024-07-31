package ren.lawliet.Java.F2kDoor;

import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedHashSet;

/**
 * @author Coaixy
 * @createTime 2024-07-15
 * @packageName ren.lawliet.Java.F2kDoor
 */

public class ArgsParser {
    private final String[] args;
    private final ArrayList<String> types = new ArrayList<>();
    private final ArrayList<String> selectType = new ArrayList<>();
    private final ArrayList<String> pluginPathStrList = Path.getPluginPathArray();

    public ArgsParser(String[] args) {
        this.args = args;
        types.add("String");
        types.add("Plugin");
        types.add("Socket");
    }

    public String helpMsg() {
        return """
                Usage: java -jar McServer-Guard.jar [options]
                Options:
                    -h, --help      Show this help message and exit
                    -t, --type      [String,Plugin,Socket] Select mode for McServer-Guard
                Example:
                    java -jar McServer-Guard.jar -t String
                """;
    }

    public void parseArgs() {
        if (args.length == 0) {
            System.out.println(helpMsg());
            System.out.println("Now Running Default Type - String\n");
            StringRunner();
        }
        for (String arg : args) {
            if (arg.equalsIgnoreCase("-h") || arg.equalsIgnoreCase("--help")) {
                System.out.println(helpMsg());
                System.exit(0);
            }
            if (arg.equalsIgnoreCase("-t") || arg.equalsIgnoreCase("--type")) {
                if (args.length == 1) {
                    System.err.println("Please select a mode");
                    System.exit(0);
                }
                for (int i = 1; i < args.length; i++) {
                    if (types.contains(args[i])) {
                        selectType.add(args[i]);
                    } else {
                        System.err.println("Please select a correct mode");
                        System.exit(0);
                    }
                }
                for (String selected : selectType) {
                    switch (selected) {
                        case "String" -> StringRunner();
                        case "Plugin" -> PluginRunner();
                        case "Socket" -> SocketRunner();
                    }
                }
            }
        }
    }

    private void SocketRunner() {

    }

    private void PluginRunner() {

    }

    private void StringRunner() {
        int count = 0;
        // get all class inputStream
        ArrayList<ClassFinder.ClassInfo> classInfoList = ClassFinder.getClassInputStreamMap(pluginPathStrList);
        String tempJarFileName = "";
        for (ClassFinder.ClassInfo classInfo : classInfoList) {
            try {
                StrDetect.detectStatus detectStatus = StrDetect.detectString(classInfo);
                if (detectStatus.status()) {
                    count = count + 1;

                    // remove duplicates items
                    ArrayList<String> classNameList = new ArrayList<>(new LinkedHashSet<>(detectStatus.className()));
                    ArrayList<Integer> lineList = new ArrayList<>(new LinkedHashSet<>(detectStatus.line()));
                    String jarFileName = detectStatus.jarFileName();

                    // format print
                    if (!tempJarFileName.equalsIgnoreCase(jarFileName)) {
                        System.out.println("Found SetOP in " + jarFileName +
                                "\n---------------------------\n");
                        tempJarFileName = jarFileName;
                    }
                    for (int i = 0; i < classNameList.size(); i++) {
                        System.out.println("Class Name: " + classNameList.get(i) + "\nLine: " + lineList.get(i) + "\n");
                        if (i == classNameList.size() - 1) {
                            System.out.println("---------------------------\n");
                        }
                    }

                }
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        if (count == 0) {
            System.out.println("SetOp is not found \n");
        }
    }

}
