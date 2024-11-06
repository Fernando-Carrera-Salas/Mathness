package com.kotlin.mathness.util

import android.annotation.SuppressLint
import android.content.Context
import android.os.Handler
import android.os.Looper
import android.view.MotionEvent
import android.view.View
import androidx.activity.ComponentActivity.MODE_PRIVATE
import com.kotlin.mathness.util.Constants.DIVIDE
import com.kotlin.mathness.util.Constants.EASY
import com.kotlin.mathness.util.Constants.HARD
import com.kotlin.mathness.util.Constants.MINUS
import com.kotlin.mathness.util.Constants.MULTIPLY
import com.kotlin.mathness.util.Constants.PLUS
import com.kotlin.mathness.util.Constants.PREFERENCES


@Suppress("LocalVariableName")
object Util
{
    // Generates a game based on the provided difficulty level and returns a list of game steps.
    fun generateGame(difficulty: String): List<String> {
        val numberOfNumbers: Int
        val targetLength: Int
        val listOfNumbers: List<Int>
        val steps = mutableListOf<String>()
        var validGame = false
        // Set parameters based on difficulty level
        when (difficulty) {
            EASY -> {
                numberOfNumbers = 3
                targetLength = 2
                listOfNumbers = listOf(1,1,2,2,3,3,4,4,5,5,6,6,7,7,8,8,9,9,10)
            }
            HARD -> {
                numberOfNumbers = 7
                targetLength = 4
                listOfNumbers = listOf(1,1,1,1,1,2,2,2,2,2,3,3,3,3,3,4,4,4,4,4,5,5,5,5,5,6,6,6,6,6,7,7,7,7,7,8,8,8,8,8,9,9,9,9,9,10,10,10,25,25,50,50,75,75,100)
            }
            else -> { // Default to medium difficulty
                numberOfNumbers = 5
                targetLength = 3
                listOfNumbers = listOf(1,1,2,2,3,3,4,4,5,5,6,6,7,7,8,8,9,9,10,10,25,50,75,100)
            }
        }
        // Loop until a valid game is generated
        while (!validGame) {
            val numbers = listOfNumbers.shuffled().take(numberOfNumbers)
            steps.clear()
            val availableNumbers = numbers.toMutableList()
            val results = mutableListOf<Int>()
            val operations = mutableListOf(1,2,2,2,3,3,3,3,3,4,4,4,4,4,4,4,4,4,4)
            while (availableNumbers.size > 1) {
                val (num1, num2) = availableNumbers.shuffled().take(2)
                when (operations.shuffled().take(1).first()) {
                    1 -> { // Addition
                        if ((num1+num2).toString().length<=4) {
                            val result = num1 + num2
                            steps.add("$num1 + $num2 = $result")
                            availableNumbers.remove(num1)
                            availableNumbers.remove(num2)
                            availableNumbers.add(result)
                            results.add(result)
                        }
                    }
                    2 -> { // Subtraction
                        if (num1>num2) {
                            val result = num1 - num2
                            steps.add("$num1 - $num2 = $result")
                            availableNumbers.remove(num1)
                            availableNumbers.remove(num2)
                            availableNumbers.add(result)
                            results.add(result)
                        }
                    }
                    3 -> { // Multiplication
                        if (num1 != 1 && num2 != 1 && (num1*num2).toString().length<=4) {
                            val result = num1 * num2
                            steps.add("$num1 x $num2 = $result")
                            availableNumbers.remove(num1)
                            availableNumbers.remove(num2)
                            availableNumbers.add(result)
                            results.add(result)
                        }
                    }
                    4 -> { // Division
                        if (num1 % num2 == 0 && num2 != 1) {
                            val result = num1 / num2
                            steps.add("$num1 ÷ $num2 = $result")
                            availableNumbers.remove(num1)
                            availableNumbers.remove(num2)
                            availableNumbers.add(result)
                            results.add(result)
                        }
                    }
                }
            }
            // Validate if the game meets target conditions
            validGame = availableNumbers.first()>0&&availableNumbers.first().toString().length==targetLength&&availableNumbers.first()%10!=0
            if (validGame) {
                // Additional validation for short solutions based on difficulty
                when (difficulty) {
                    EASY -> {
                        val combinations = combinations(numbers,2)
                        for (i in combinations.indices) {
                            val short_solution = combinations[i]
                            if (testGame(short_solution,availableNumbers.first()).second>0) {
                                validGame = false
                                break
                            }
                        }
                    }
                    HARD -> {
                        val combinations = combinations(numbers,4)
                        for (i in combinations.indices) {
                            val short_solution = combinations[i]
                            if (testGame(short_solution,availableNumbers.first()).second>0) {
                                validGame = false
                                break
                            }
                        }
                    }
                    else -> {
                        val combinations = combinations(numbers,3)
                        for (i in combinations.indices) {
                            val short_solution = combinations[i]
                            if (testGame(short_solution,availableNumbers.first()).second>0) {
                                validGame = false
                                break
                            }
                        }
                    }
                }
            }
            if (validGame) {
                steps.add(0,";")
                for (i in numbers.indices)
                    steps.add(0,numbers[i].toString())
                steps.add(0,availableNumbers.first().toString())
            }
        }
        return steps
    }

    // Tests if a given combination of numbers can reach a target using various operations
    private fun testGame(numbers: List<Int>, target: Int): Pair<List<List<String>>, Int> {
        val allSolutions = mutableListOf<List<String>>()
        var solutionCount = 0
        // Recursive function to solve combinations of numbers to reach the target
        fun trySolve(currentNumbers: List<Int>, currentSteps: List<String>) {
            if (currentNumbers.isEmpty()) return
            if (currentNumbers.size == 1 && currentNumbers.first() == target) {
                allSolutions.add(currentSteps)
                solutionCount++
                return
            }
            for (i in currentNumbers.indices) {
                for (j in i + 1 until currentNumbers.size) {
                    val num1 = currentNumbers[i]
                    val num2 = currentNumbers[j]
                    val remaining = currentNumbers.toMutableList().apply {
                        removeAt(j)
                        removeAt(i)
                    }
                    // Add new calculations for possible solutions
                    trySolve(remaining + (num1 + num2), currentSteps + "$num1 + $num2 = ${num1 + num2}")
                    if (num1 > num2) trySolve(remaining + (num1 - num2), currentSteps + "$num1 - $num2 = ${num1 - num2}")
                    if (num2 > num1) trySolve(remaining + (num2 - num1), currentSteps + "$num2 - $num1 = ${num2 - num1}")
                    if (num1 != 1 && num2 != 1) trySolve(remaining + (num1 * num2), currentSteps + "$num1 * $num2 = ${num1 * num2}")
                    if (num2 != 0 && num1 % num2 == 0 && num2 != 1) trySolve(remaining + (num1 / num2), currentSteps + "$num1 / $num2 = ${num1 / num2}")
                    if (num1 != 0 && num2 % num1 == 0 && num1 != 1) trySolve(remaining + (num2 / num1), currentSteps + "$num2 / $num1 = ${num2 / num1}")
                }
            }
        }
        trySolve(numbers, emptyList())
        return Pair(allSolutions, solutionCount)
    }

    // Generates all combinations of a specific length `k` from the provided list
    private fun <T> combinations(list: List<T>, k: Int): List<List<T>> {
        if (k == 0) return listOf(emptyList())
        if (list.isEmpty()) return emptyList()
        val result = mutableListOf<List<T>>()
        for (i in list.indices) {
            val remaining = list.subList(i + 1, list.size)
            val currentCombinations = combinations(remaining, k - 1)

            for (combo in currentCombinations) {
                result.add(listOf(list[i]) + combo)
            }
        }
        return result
    }

    // Sets a custom long-click listener on a view with a specific delay
    fun View.setOnCustomLongClickListener(listener: () -> Unit) {
        setOnTouchListener(object : View.OnTouchListener {
            private val longClickDuration = 300L
            private val handler = Handler(Looper.getMainLooper())
            @SuppressLint("ClickableViewAccessibility")
            override fun onTouch(v: View?, event: MotionEvent?): Boolean {
                if (event?.action == MotionEvent.ACTION_DOWN) {
                    handler.postDelayed({ listener.invoke() }, longClickDuration)
                } else if (event?.action == MotionEvent.ACTION_UP) {
                    handler.removeCallbacksAndMessages(null)
                }
                return false
            }
        })
    }

    // Calculates the nth Fibonacci number using dynamic programming
    fun fib(n: Int): Int {
        val f = IntArray(n + 1)
        var i: Int
        f[0] = 0
        if (n > 0) {
            f[1] = 1
            i = 2
            while (i <= n) {
                f[i] = f[i - 1] + f[i - 2]
                i++
            }
        }
        return f[n]
    }

    fun isOperator(text: String): Boolean {
        return text in listOf(PLUS, MINUS, MULTIPLY, DIVIDE)
    }

    fun putPrefInt(c: Context, key: String, value: Int) {
        c.getSharedPreferences(PREFERENCES, MODE_PRIVATE).edit().putInt(key,value).apply()
    }

    fun getPrefInt(c: Context, key: String, default: Int = 0) = c.getSharedPreferences(PREFERENCES, MODE_PRIVATE).getInt(key, default)
}