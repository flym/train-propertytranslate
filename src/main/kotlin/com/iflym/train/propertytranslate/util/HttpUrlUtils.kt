package com.iflym.train.propertytranslate.util

import io.iflym.core.util.ExceptionUtils

/**
 * 针对http url的工具类处理
 * created at 2018-08-14
 *
 * @author flym
 */
object HttpUrlUtils {
    private const val URL_PATH_SEPARATOR = "/"

    /** 将两部url拼接起来  */
    fun joinUrl(prePart: String, secondPart: String): String {
        val preEnds = prePart.endsWith(URL_PATH_SEPARATOR)
        val secondStarts = secondPart.startsWith(URL_PATH_SEPARATOR)

        if(preEnds && secondStarts) {
            return prePart.substring(0, prePart.length - 1) + secondPart
        }

        return if(preEnds || secondStarts) {
            prePart + secondPart
        } else prePart + URL_PATH_SEPARATOR + secondPart

    }

    fun buildAhref(linkUrl: String, title: String): String {
        return ExceptionUtils.doFunRethrowE({ "<a href=\"$linkUrl\" target=\"_blank\">$title</a>" })
    }
}
