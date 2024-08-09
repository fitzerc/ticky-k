import kotlinx.datetime.*
import kotlin.time.Duration

val tz = TimeZone.currentSystemDefault()

data class RunningTimeEntry (
    val project: String,
    val task: String,
    val tag: String,
    val startedAt: LocalDateTime,
) {
    companion object {
        fun create(project: String, task: String, tag: String) = RunningTimeEntry(
            project,
            task,
            tag,
            Clock.System.now().toLocalDateTime(tz)
        )

        fun fromCsv(csvData: String): RunningTimeEntry {
            val data = csvData.split(',')

            return RunningTimeEntry(
                data[0],
                data[1],
                data[2],
                LocalDateTime.parse(data[3])
            )
        }
    }
}

data class TimeEntry (
    val project: String,
    val task: String,
    val tag: String,
    val startedAt: LocalDateTime,
    val endedAt: LocalDateTime,
    val elapsed: Duration
)

fun TimeEntry.toCsv(): String {
    val tz = TimeZone.currentSystemDefault()
    val formattedDelta = elapsed.toComponents { hours, minutes, seconds, _ ->
        String.format("%02d:%02d:%02d", hours, minutes, seconds)
    }

    return "${project},${task},${tag},${startedAt},${endedAt},${formattedDelta}"
}

fun RunningTimeEntry.toTimeEntry(): TimeEntry {
    val endedAt = Clock.System.now().toLocalDateTime(tz)
    val delta = endedAt.toInstant(tz) - startedAt.toInstant(tz)

    return TimeEntry(
        project,
        task,
        tag,
        startedAt,
        endedAt,
        delta
    )
}

fun RunningTimeEntry.toCsv(): String = "${project},${task},${tag},${startedAt}"