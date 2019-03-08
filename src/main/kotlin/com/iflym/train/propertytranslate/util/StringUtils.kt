package com.iflym.train.propertytranslate.util

import java.util.regex.Pattern

/**
 * 字符串工具类
 * Created by flym on 7/6/2017.
 *
 * @author flym
 */
object StringUtils {
    private val COLON_PATTERN = Pattern.compile("\\s*:\\s*")
    private val COMMA_PATTERN = Pattern.compile("\\s*,\\s*")
    private val DOT_PATTERN = Pattern.compile("\\s*\\.\\s*")
    private val EQ_PATTERN = Pattern.compile("\\s*=\\s*")
    private val AND_PATTERN = Pattern.compile("\\s*&\\s*")

    /** 根据并且符号进行分隔  */
    fun splitByAnd(source: String): Array<String> {
        return AND_PATTERN.split(source, -1)
    }

    /** 根据等号进行分隔  */
    fun splitByEq(source: String): Array<String> {
        return EQ_PATTERN.split(source, -1)
    }

    /** 根据点号进行分隔  */
    fun splitByDot(source: String): Array<String> {
        return DOT_PATTERN.split(source, -1)
    }

    /** 根据冒号进行分隔  */
    fun splitByColon(source: String): Array<String> {
        return COLON_PATTERN.split(source, -1)
    }

    /** 根据逗号进行分隔  */
    fun splitByComma(source: String): Array<String> {
        return COMMA_PATTERN.split(source, -1)
    }

    /** 将小写字符串转驼峰形式  */
    fun lowerUnderscore2Camel(str: String): String {
        val scs = str.toCharArray()
        val dcs = CharArray(scs.size)
        var k = 0

        var preUnderscore = false
        for(c in scs) {
            if(c == '_') {
                preUnderscore = true
                continue
            }

            if(preUnderscore) {
                dcs[k] = Character.toUpperCase(c)
                preUnderscore = false
            } else {
                dcs[k] = c
            }

            k++
        }

        return if(k == 0) {
            ""
        } else String(dcs, 0, k)

    }
}
