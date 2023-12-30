package com.mejorappt.equipoa.util

import android.content.Context
import com.mejorappt.equipoa.R
import com.mejorappt.equipoa.model.Advice
import com.mejorappt.equipoa.model.Question
import java.io.ByteArrayOutputStream
import java.io.IOException
import java.io.InputStream

object CSV {
    private fun readFile(file: InputStream): Array<String> {
        val byteArray = ByteArrayOutputStream()
        try {
            var i = file.read()
            while (i != -1) {
                byteArray.write(i)
                i = file.read()
            }
            file.close()
        } catch (io: IOException) {
            io.printStackTrace()
        }
        return byteArray.toString().split("\n".toRegex()).dropLastWhile { it.isEmpty() }
            .toTypedArray()
    }

    private fun getCSVLine(line: String): Array<String> = line.split(";".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()

    private fun toQuestion(line: Array<String>): Question = Question(line[0].toInt(), line[1], line[2].toInt(), line[3].trim().toInt())

    private fun toAdvice(line: Array<String>): Advice = Advice(line[0].toInt(), line[1], line[2])

    fun getQuestions(context: Context): List<Question> {
        val questions = readFile(context.resources.openRawResource(R.raw.questions))
        val questionList = ArrayList<Question>()

        questions.forEach {
            val line = getCSVLine(it)
            questionList.add(toQuestion(line))
        }

        return questionList
    }

    fun getAdvices(context: Context): List<Advice> {
        val questions = readFile(context.resources.openRawResource(R.raw.advices))
        val adviceList = ArrayList<Advice>()

        questions.forEach {
            val line = getCSVLine(it)
            adviceList.add(toAdvice(line))
        }

        return adviceList
    }
}