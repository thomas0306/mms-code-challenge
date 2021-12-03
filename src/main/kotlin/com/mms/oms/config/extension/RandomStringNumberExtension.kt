package com.mms.oms.config.extension

import kotlin.random.Random

private val CHAR_POOL: List<Char> = ('0'..'9').toList()

fun String.randomNumberSuffix(length: Int) = this.plus(
    (1..length)
        .map { Random.nextInt(0, CHAR_POOL.size) }
        .map { CHAR_POOL.toList()[it] }
        .joinToString("")
)
