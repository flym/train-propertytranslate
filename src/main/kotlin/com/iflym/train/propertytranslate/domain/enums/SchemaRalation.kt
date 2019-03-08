package com.iflym.train.propertytranslate.domain.enums

/**
 * 描述分类之间的关系信息
 *
 *
 * 主要是子分类和父分类的关系
 *
 * @author flym
 */
enum class SchemaRalation(
        @Suppress("unused")
        private val desc: String
) {

    /** 关系-一对一  */
    ONE_TO_ONE("一对一"),

    /** 关系-多对一  */
    MANY_TO_ONE("多对一"),

    //
    ;
}
