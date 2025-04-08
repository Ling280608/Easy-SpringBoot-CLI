package com.ling.cli.utilTest

import org.junit.jupiter.api.Test

class KotlinTest {

    @Test
    fun funTest() {
        val value_1 = collection2StringWithDefault(listOf("A", "B", "C"))
        val value_2 = collection2StringWithDefault(listOf("A", "B", "C"), ";")
        val value_3 = collection2StringWithDefault(listOf("A", "B", "C"), prefix = "#", postfix = "#")
        println(value_3)
    }

    private fun <T> collection2StringWithDefault(
        collection: Collection<T>,
        separator: String = ",",
        prefix: String = "{",
        postfix: String = "}"
    ): String {
        val resule = StringBuilder(prefix)
        for ((index, element) in collection.withIndex()) {
            if (index > 0)
                resule.append(separator)
            resule.append(element)
        }
        resule.append(postfix)
        return resule.toString()
    }

    @Test
    fun testStart() {
//        test3("abed")
        test4()
    }

    fun test4(){
        for (i in 1..10) {
            println(i);
        }
    }

    fun test3(mValue: Any): Boolean {

        return if (mValue is String) {
            println("字符串类型，值为${mValue}")
            true
        }else if (mValue is Int) {
            println("整数类型，值为${mValue}")
            false
        }else {
            println("其他类型,值为${mValue}")
            false
        }
    }

    @Test
    fun test2() {
        var a = 1
        var b = 2
        var num = if (a > b) a else b;
        println(num)
    }

    @Test
    fun test1() {
        println("test1")
    }
}