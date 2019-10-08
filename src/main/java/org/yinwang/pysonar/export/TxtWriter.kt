package org.yinwang.pysonar.export

import org.yinwang.pysonar.Analyzer
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
            out.write(it.node.toString() + '|' + it.type.toString())
            out.newLine()
        }
    }
}