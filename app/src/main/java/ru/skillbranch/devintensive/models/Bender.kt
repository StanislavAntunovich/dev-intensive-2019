package ru.skillbranch.devintensive.models

class Bender(
    var status: Status = Status.NORMAL,
    var question: Question = Question.NAME
) {

    fun askQuestion(): String = when (question) {
        Question.NAME -> Question.NAME.question
        Question.PROFESSION -> Question.PROFESSION.question
        Question.BDAY -> Question.BDAY.question
        Question.MATERIAL -> Question.MATERIAL.question
        Question.SERIAL -> Question.SERIAL.question
        Question.IDLE -> Question.IDLE.question
    }

    fun listenAnswer(answer: String): Pair<String, Triple<Int, Int, Int>> {
        val validation = question.validateAnswer(answer)
        if (!validation.isBlank())
            return "$validation\n${question.question}" to status.color

        return if (question.answers.contains(answer)) {
            question = question.nextQuestion()
            "Отлично - ты справился\n" + if (question.question == Question.IDLE.question) {
                "На этом все, вопросов больше нет"
            } else {
                question.question to status.color
            } to status.color
        } else {
            status = status.nextStatus()
            "Это не правильный ответ! " + if (status == Status.NORMAL) {
                reset()
                "Давай все по новой\n${question.question}"
            } else {
                question.question
            } to status.color
        }
    }


    private fun reset() {
        question = Question.NAME
    }

    enum class Status(val color: Triple<Int, Int, Int>) {
        NORMAL(Triple(255, 255, 255)),
        WARNING(Triple(255, 120, 0)),
        DANGER(Triple(255, 60, 60)),
        CRITICAL(Triple(255, 0, 0));

        fun nextStatus(): Status =
            if (this.ordinal < values().lastIndex)
                values()[this.ordinal + 1]
            else
                values()[0]
    }

    enum class Question(val question: String, val answers: List<String>) {
        NAME("Как меня зовут?", listOf("Бендер", "bender")) {
            override fun validateAnswer(answer: String): String = if (answer.first().isUpperCase())
                ""
            else
                "Имя должно начинаться с заглавной буквы"


            override fun nextQuestion(): Question = PROFESSION
        },

        PROFESSION("Назови мою профессию?", listOf("сгибальщик", "bender")) {
            override fun validateAnswer(answer: String): String = if (answer.first().isLowerCase())
                ""
            else
                "Профессия должна начинаться со строчной буквы"

            override fun nextQuestion(): Question = MATERIAL
        },

        MATERIAL("Из чего я сделан?", listOf("металл", "дерево", "metal", "iron", "wood")) {
            override fun validateAnswer(answer: String): String {
                return if (answer.contains(Regex("\\d"))) "Материал не должен содержать цифр"
                else ""
            }

            override fun nextQuestion(): Question = BDAY
        },

        BDAY("Когда меня создали?", listOf("2993")) {
            override fun validateAnswer(answer: String): String = if (answer.contains(Regex("\\D")))
                "Год моего рождения должен содержать только цифры"
            else
                ""

            override fun nextQuestion(): Question = SERIAL
        },
        SERIAL("Мой серийный номер?", listOf("2716057")) {
            override fun validateAnswer(answer: String): String = if (
                answer.contains(Regex("\\D"))
                && answer.length != 7
            ) "Серийный номер содержит только цифры, и их 7"
            else
                ""

            override fun nextQuestion(): Question = IDLE
        },
        IDLE("На этом все, вопросов больше нет", listOf("")) {
            override fun validateAnswer(answer: String): String = ""

            override fun nextQuestion(): Question = IDLE
        };

        abstract fun nextQuestion(): Question
        abstract fun validateAnswer(answer: String): String
    }
}