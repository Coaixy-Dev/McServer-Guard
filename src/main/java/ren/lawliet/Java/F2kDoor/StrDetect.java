package ren.lawliet.Java.F2kDoor;

import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;

import java.io.IOException;
import java.util.ArrayList;

/**
 * @author Coaixy
 * @createTime 2024-07-15
 * @packageName ren.lawliet.Java.F2kDoor
 */

public class StrDetect {
    private static final String TARGET_STRING = "setop";
    private static final int BUFFER_SIZE = TARGET_STRING.length();

    public record detectStatus(boolean status, ArrayList<String> className, ArrayList<Integer> line) {
        @Override
        public String toString() {
            return "detectStatus{" +
                    "status=" + status +
                    ", className=" + className +
                    ", line=" + line +
                    '}' + "\n";
        }
    }

    public static detectStatus detectString(ClassFinder.ClassInfo classInfo) throws IOException {
        ClassReader classReader = new ClassReader(classInfo.classInputStream());
        StringBuilder buffer = new StringBuilder();

        ArrayList<String> classNameList = new ArrayList<>();
        ArrayList<Integer> lineList = new ArrayList<>();
        final Boolean[] detectFlag = {false};

        classReader.accept(new ClassVisitor(Opcodes.ASM9) {
            private int currentLine = -1;

            @Override
            public MethodVisitor visitMethod(int access, String name, String descriptor, String signature, String[] exceptions) {
                return new MethodVisitor(Opcodes.ASM9) {
                    @Override
                    public void visitLineNumber(int line, org.objectweb.asm.Label start) {
                        super.visitLineNumber(line, start);
                        currentLine = line;
                    }

                    @Override
                    public void visitLdcInsn(Object value) {
                        if (value instanceof String str) {
                            buffer.append(str);
                            if (buffer.length() > BUFFER_SIZE) {
                                buffer.delete(0, buffer.length() - BUFFER_SIZE);
                            }
                            if (buffer.toString().equalsIgnoreCase(TARGET_STRING)) {
                                detectFlag[0] = true;
                                classNameList.add(classInfo.className());
                                lineList.add(currentLine);
                            }
                        }
                    }
                };
            }
        }, 0);
        if (detectFlag[0]) {
            // Return
            return new detectStatus(true, classNameList, lineList);
        }
        return new detectStatus(true, null, null);
    }
}
