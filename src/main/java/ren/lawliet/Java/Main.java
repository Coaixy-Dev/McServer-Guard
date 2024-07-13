package ren.lawliet.Java;

import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;

import javax.swing.text.html.parser.Entity;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Enumeration;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

/**
 * @author Coaixy
 * @createTime 2024-07-13
 * @packageName ren.lawliet.Java
 */

public class Main {
    private static final String TARGET_STRING = "setop";
    private static final int BUFFER_SIZE = TARGET_STRING.length();
    public static void main(String[] args) {

        String jarFilePath = "/Users/coaixy/Downloads/mc-alg-1.0-SNAPSHOT.jar";
        try(JarFile jarFile = new JarFile(jarFilePath)) {
            Enumeration<JarEntry> entries = jarFile.entries();
            while (entries.hasMoreElements()) {
                JarEntry entry = entries.nextElement();
                if (entry.isDirectory()) {
                    continue;
                }

                try (InputStream is = jarFile.getInputStream(entry)) {
                    if (entry.getName().endsWith(".class")) {
                        extractStringsFromClassFile(is, entry.getName());
                    }
                }
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    private static void extractStringsFromClassFile(InputStream is, String className) throws IOException {
        ClassReader classReader = new ClassReader(is);
        StringBuilder buffer = new StringBuilder();

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
                        if (value instanceof String) {
                            String str = (String) value;
                            buffer.append(str);
                            if (buffer.length() > BUFFER_SIZE) {
                                buffer.delete(0, buffer.length() - BUFFER_SIZE);
                            }
                            if (buffer.toString().equalsIgnoreCase(TARGET_STRING)) {
                                System.out.println("发现后门 Class: " + className + ", Line: " + currentLine);
                            }
                        }
                    }
                };
            }
        }, 0);
    }
}