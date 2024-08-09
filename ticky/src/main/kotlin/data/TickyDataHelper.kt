package data

import java.io.File

fun createDirIfNotExists(path: String) {
    val directory = File(path)

    if (!directory.exists()) {
        if (directory.mkdirs()) {
            println("Directory created: ${directory.path}")
        } else {
            println("Failed to create directory: ${directory.path}")
        }
    }
}

fun writeToFile(path: String, filename: String, data: String) {
    createDirIfNotExists(path)
    val file = File("${path}/${filename}")

    if (!file.exists()) {
        file.createNewFile()
    }

    file.appendText("$data\n")
}

fun writeToFile(path: String, filename: String, data: List<String>) {
    createDirIfNotExists(path)
    val file = File("${path}/${filename}")

    if (!file.exists()) {
        file.createNewFile()
    }

    data.forEach { line ->
        file.appendText(line)
        file.appendText(System.lineSeparator())
    }
}

fun readFromFile(path: String, filename: String): List<String> {
    val file = File("${path}/${filename}")

    return when {
        file.exists() -> file.readLines()
        else -> listOf<String>()
    }
}

fun deleteFile(path: String, filename: String) {
    val file = File("${path}/${filename}")
    if (file.exists()) file.delete()
}

fun getCsvFileNamesInDir(path: String): List<File>? {
    val directory = File(path)

    if (directory.exists() && directory.isDirectory) {
        val csvFiles = directory.listFiles { file -> file.isFile && file.extension == "csv" }
        return when {
            csvFiles.isNullOrEmpty() -> null
            else -> csvFiles.asList()
        }
    } else {
        println("Directory does not exist or is not a directory.")
        return null
    }
}