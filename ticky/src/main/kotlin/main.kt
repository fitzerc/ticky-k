import data.*
import kotlinx.datetime.*

fun main(args: Array<String>) {
    val action = if (args.isEmpty()) "info" else args[0]

    val localAppData = System.getenv("LOCALAPPDATA")
    val pathToTickyData = localAppData + "/ticky"

    when (action.lowercase()) {
        "start" -> {
            val runningTe = promptForTimeEntry()
            writeToFile(pathToTickyData, ".ticky", runningTe.toCsv())
        }
        "stop" -> {
            val lines = readFromFile(pathToTickyData, ".ticky")

            if (lines.isEmpty()) {
                println("no timer running")
            }

            val runningTe = RunningTimeEntry.fromCsv(lines[0])
            val timeEntry = runningTe.toTimeEntry()

            writeToFile(pathToTickyData, getTodaysFileName(), timeEntry.toCsv())
            deleteFile(pathToTickyData, ".ticky")
        }
        //TODO: implement this
        "consolidate" -> {
            val fileNames = getCsvFileNamesInDir(pathToTickyData)
            val allLines = mutableListOf<String>()

            when {
                fileNames.isNullOrEmpty() -> println("no files to consolidate")
                else -> {
                    fileNames.forEach { file ->
                        allLines.addAll(readFromFile(pathToTickyData, file.name))
                    }

                    writeToFile(pathToTickyData, "consolidated.csv", allLines)
                    println("files consolidated")
                }
            }

            println("consolidate")
        }
        "info" -> {
            val lines = readFromFile(pathToTickyData, ".ticky")

            if (lines.isEmpty()) {
                println("no timer running")
            } else {
                val runningTe = RunningTimeEntry.fromCsv(lines[0])
                println(runningTe)
            }
        }
        else -> println("info")
    }
}

fun promptForTimeEntry(): RunningTimeEntry {
    println("Project: ")
    val project = readln()
    println("Task: ")
    val task = readln()

    println("Tag: ")
    val tag = readln()

    return RunningTimeEntry.create(project, task, tag)
}

fun getTodaysFileName(): String {
    val now = Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault())
    return "ticky-${now.year}-${now.month}-${now.dayOfMonth}.csv"
}