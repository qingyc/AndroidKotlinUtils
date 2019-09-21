package com.qing.androidkotlinutils.core.ext

import kotlin.math.ceil
import kotlin.math.floor

/**
 *
 * 类说明: 数字扩展
 *
 * @author qing
 * @time 2019-09-03 18:58
 */


fun Double.ceilToInt(): Int = ceil(this).toInt()

fun Float.ceilToInt(): Int = ceil(this).toInt()

fun Double.floorToInt(): Int = floor(this).toInt()

fun Float.floorToInt(): Int = floor(this).toInt()