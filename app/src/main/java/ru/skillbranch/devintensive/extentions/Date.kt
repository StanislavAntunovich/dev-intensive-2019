package ru.skillbranch.devintensive.extentions

import java.text.SimpleDateFormat
import java.util.*
import kotlin.math.abs

const val SECOND = 1000L
const val MINUTE = 60 * SECOND
const val HOUR = 60 * MINUTE
const val DAY = 24 * HOUR

val mins = Triple("минуту", "минуты", "минут")
val hours = Triple("час", "часа", "часов")
val days = Triple("день", "дня", "дней")

fun Date.format(pattern: String = "HH:mm:ss dd:MM:yy"): String {
    val dateFormat = SimpleDateFormat(pattern, Locale("ru"))
    return dateFormat.format(this)
}

fun Date.add(value: Int, units: TimeUnits = TimeUnits.SECOND): Date {

    this.time += when (units) {
        TimeUnits.SECOND -> value * SECOND
        TimeUnits.MINUTE -> value * MINUTE
        TimeUnits.HOUR -> value * HOUR
        TimeUnits.DAY -> value * DAY
    }
    return this
}

fun Date.humanizeDiff(date: Date = Date()): String {
    val delta = (this.time - date.time)
    val isFuture = delta > 0
    return when (abs(delta)) {
        in 0..(1 * SECOND) -> "только что"
        in (2 * SECOND)..(45 * SECOND) -> if (isFuture) "через секунду" else "несколько секунд назад"
        in (46 * SECOND)..(75 * SECOND) -> "${if (isFuture) "через " else ""}минуту${if (!isFuture) " назад" else ""}"

        in (75 * SECOND)..(45 * MINUTE) -> parsTimeDiff(delta, TimeUnits.MINUTE)

        in (45 * MINUTE)..(75 * MINUTE) -> "${if (isFuture) "через " else ""}час${if (isFuture) "" else " назад"}"

        in (75 * MINUTE)..(22 * HOUR) -> parsTimeDiff(delta, TimeUnits.HOUR)

        in (22 * HOUR)..(26 * HOUR) -> "${if (isFuture) "через " else ""}день${if (isFuture) "" else " назад"}"

        in (26 * HOUR)..(360 * DAY) -> parsTimeDiff(delta, TimeUnits.DAY)

        else -> if (isFuture) "более чем через год" else "более года назад"
    }
}

private fun parsTimeDiff(delta: Long, units: TimeUnits): String {
    val isFuture = delta > 0


    var string: Triple<String, String, String> = Triple("", "", "")

    val time = abs(delta) / when (units) {
        TimeUnits.SECOND -> SECOND
        TimeUnits.MINUTE -> {
            string = mins
            MINUTE
        }
        TimeUnits.HOUR -> {
            string = hours
            HOUR
        }
        TimeUnits.DAY -> {
            string = days
            DAY
        }
    }

    val res = when {
        ((time % 10) in 2..4 && (time / 10 != 1L)) -> string.second
        ((time % 10) == 1L) && (time / 10 != 1L) -> string.first
        else -> string.third
    }

    return if (isFuture) {
        "через $time $res"
    } else {
        "$time $res назад"
    }

}


enum class TimeUnits {
    SECOND,
    MINUTE,
    HOUR,
    DAY
}