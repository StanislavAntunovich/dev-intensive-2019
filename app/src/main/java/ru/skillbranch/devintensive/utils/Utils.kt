package ru.skillbranch.devintensive.utils

object Utils {

    fun parseFullName(fullName: String?): Pair<String?, String?> {
        if (fullName == null || fullName.isBlank() || fullName.isEmpty())
            return Pair(null, null)

        val parts: List<String> = fullName.split(" ")
        val firstName = parts.getOrNull(0)
        val lastName = parts.getOrNull(1)

        return Pair(firstName, lastName)
    }

    fun toInitials(firstName: String?, lastName: String?): String? {
        if ((firstName.isNullOrBlank() || firstName.isNullOrEmpty())
            && (lastName.isNullOrBlank() || lastName.isNullOrEmpty())
        )
            return null
        val sb = StringBuilder()

        firstName?.first()?.let {
            sb.append(it.toUpperCase())
        }

        lastName?.first()?.let {
            sb.append(it.toUpperCase())
        }
        return sb.toString()
    }

    fun transliteration(payload: String, divider: String = " "): String {
        val sb = StringBuilder()
        payload.forEach {
            if (it.toString() == " ")
                sb.append(divider)
            else {
                sb.append(
                    if (it.isLowerCase()) {
                        DICT[it.toString()] ?: it
                    }
                    else
                        DICT[it.toLowerCase().toString()]?.toUpperCase() ?: it
                )
            }
        }
        return sb.toString()
    }

}