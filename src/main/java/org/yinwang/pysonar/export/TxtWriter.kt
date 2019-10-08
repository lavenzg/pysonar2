package org.yinwang.pysonar.export

import com.sun.org.apache.bcel.internal.generic.BIPUSH
import org.yinwang.pysonar.Analyzer
import org.yinwang.pysonar.Binding
import org.yinwang.pysonar.Options
import org.yinwang.pysonar.`$`
import java.io.File


fun main(args: Array<String>) {
    val options = Options(args)

    val argsList = options.args
    val fileOrDir = argsList[0]
    val txt_path = argsList[1]
    val f = File(fileOrDir)
    val rootDir = if (f.isFile) f.parentFile else f
    try {
        val rootPath = f.canonicalPath
    } catch (e: Exception) {
        `$`.die("File not found: $f")
    }


    val analyzer = Analyzer(options.optionsMap)
    try {
        analyzer.analyze(f.path)
    } finally {
        analyzer.finish()
    }
    File(txt_path).bufferedWriter().use { out ->
        analyzer.allBindings.forEach {
            if (it.isBuiltin || it.isURL || it.isSynthetic)
                return@forEach
            if (it.kind != Binding.Kind.PARAMETER && it.kind != Binding.Kind.SCOPE &&
                    it.kind != Binding.Kind.VARIABLE && it.kind != Binding.Kind.FUNCTION)
                return@forEach
            out.write(it.node.toString() + '|' + it.type.toString())
            out.newLine()
        }
    }
}