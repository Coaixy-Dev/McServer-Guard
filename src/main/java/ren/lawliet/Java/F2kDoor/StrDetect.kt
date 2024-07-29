package ren.lawliet.Java.F2kDoor

import org.objectweb.asm.*
import java.io.IOException

/**
 * @author Coaixy
 * @createTime 2024-07-15
 * @packageName ren.lawliet.Java.F2kDoor
 */
object StrDetect {
    private const val TARGET_STRING = "setop"
    private const val BUFFER_SIZE = TARGET_STRING.length

    @Throws(IOException::class)
    fun detectString(classInfo: ClassFinder.ClassInfo): detectStatus {
        val classReader = ClassReader(classInfo.classInputStream)
        val buffer = StringBuilder()

        val classNameList = ArrayList<String>()
        val lineList = ArrayList<Int>()
        val detectFlag = arrayOf(false)

        classReader.accept(object : ClassVisitor(Opcodes.ASM9) {
            private var currentLine = -1

            override fun visitMethod(
                access: Int,
                name: String,
                descriptor: String,
                signature: String,
                exceptions: Array<String>
            ): MethodVisitor {
                return object : MethodVisitor(Opcodes.ASM9) {
                    override fun visitLineNumber(line: Int, start: Label) {
                        super.visitLineNumber(line, start)
                        currentLine = line
                    }

                    override fun visitLdcInsn(value: Any) {
                        if (value is String) {
                            buffer.append(value)
                            if (buffer.length > BUFFER_SIZE) {
                                buffer.delete(0, buffer.length - BUFFER_SIZE)
                            }
                            if (buffer.toString().equals(TARGET_STRING, ignoreCase = true)) {
                                detectFlag[0] = true
                                classNameList.add(classInfo.className)
                                lineList.add(currentLine)
                            }
                        }
                    }
                }
            }
        }, 0)
        if (detectFlag[0]) {
            // Return
            return detectStatus(true, classInfo.jarFileName, classNameList, lineList)
        }
        return detectStatus(false, null, null, null)
    }

    @JvmRecord
    data class detectStatus(
        val status: Boolean, val jarFileName: String?, val className: ArrayList<String>?,
        val line: ArrayList<Int>?
    ) {
        override fun toString(): String {
            return "detectStatus{" +
                    "status=" + status +
                    ", className=" + className +
                    ", line=" + line +
                    '}' + "\n"
        }
    }
}
