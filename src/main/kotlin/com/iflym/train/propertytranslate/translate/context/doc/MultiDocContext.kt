package com.iflym.train.propertytranslate.translate.context.doc

import com.iflym.train.propertytranslate.translate.context.TransformContext
import io.iflym.core.util.ListUtils
import java.util.*

/**
 * created at 2018-09-11
 *
 * @author flym
 */
class MultiDocContext : TransformContext {
    lateinit var docList: List<DocContext>

    fun setDocListWithStrArray(docKeys: Array<String>) {
        val list = docKeys.map { DocContext.build(it) }
        docList = list
    }
}
