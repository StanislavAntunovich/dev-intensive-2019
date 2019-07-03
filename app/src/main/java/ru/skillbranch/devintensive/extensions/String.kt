package ru.skillbranch.devintensive.extensions

fun String.truncate(length: Int = 16): String =
    if (this.trimEnd().length >= length)
        "${this.substring(0, length + 1).trimEnd()}..."
    else
        this.trimEnd()

fun String.stripHtml(): String {
    return replace(Regex("<.*?>"), "")
        .replace(Regex("[\\s]{2,}"), " ")
}
