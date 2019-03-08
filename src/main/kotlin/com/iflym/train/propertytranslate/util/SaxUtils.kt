package com.iflym.train.propertytranslate.util

import javax.xml.parsers.SAXParserFactory

/**
 * created at 2018-09-05
 *
 * @author flym
 */
object SaxUtils {

    private val SAX_FACTORY: SAXParserFactory

    init {
        val sax = SAXParserFactory.newInstance()
        sax.isNamespaceAware = false
        sax.isValidating = false
        SAX_FACTORY = sax
    }

    fun notValidFactory(): SAXParserFactory = SAX_FACTORY
}
