package com.kotlin.mathness.util

import android.animation.TypeEvaluator

/**
 * Inspired from [android.animation.FloatArrayEvaluator]
 *
 * This evaluator can be used to perform type interpolation between `double[]` values.
 * Each index into the array is treated as a separate value to interpolate. For example,
 * evaluating `{100, 200}` and `{300, 400}` will interpolate the value at
 * the first index between 100 and 300 and the value at the second index value between 200 and 400.
 */
class DoubleArrayEvaluator : TypeEvaluator<DoubleArray> {
    private lateinit var mArray: DoubleArray

    /**
     * Create a DoubleArrayEvaluator that does not reuse the animated value. Care must be taken
     * when using this option because on every evaluation a new `double[]` will be
     * allocated.
     *
     * @see .DoubleArrayEvaluator
     */
    constructor()

    /**
     * Create a DoubleArrayEvaluator that reuses `reuseArray` for every evaluate() call.
     * Caution must be taken to ensure that the value returned from
     * [android.animation.ValueAnimator.getAnimatedValue] is not cached, modified, or
     * used across threads. The value will be modified on each `evaluate()` call.
     *
     * @param reuseArray The array to modify and return from `evaluate`.
     */
    constructor(reuseArray: DoubleArray) {
        mArray = reuseArray
    }

    /**
     * Interpolates the value at each index by the fraction. If
     * [.DoubleArrayEvaluator] was used to construct this object,
     * `reuseArray` will be returned, otherwise a new `double[]`
     * will be returned.
     *
     * @param fraction   The fraction from the starting to the ending values
     * @param startValue The start value.
     * @param endValue   The end value.
     * @return A `double[]` where each element is an interpolation between
     * the same index in startValue and endValue.
     */
    override fun evaluate(
        fraction: Float,
        startValue: DoubleArray,
        endValue: DoubleArray
    ): DoubleArray {
        var array: DoubleArray
        if (this::mArray.isInitialized) {
            array = mArray
        }
        else {
            array = DoubleArray(startValue.size)
        }

        for (i in array.indices) {
            val start = startValue[i]
            val end = endValue[i]
            array[i] = start + (fraction * (end - start))
        }
        return array
    }
}