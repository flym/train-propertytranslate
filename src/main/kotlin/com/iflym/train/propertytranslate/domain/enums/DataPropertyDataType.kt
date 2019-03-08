package com.iflym.train.propertytranslate.domain.enums

/**
 * 描述属性的数据层对象类型
 *
 * @author flym
 */
enum class DataPropertyDataType(
        @Suppress("unused")
        private val desc: String
) {
    /** 对象类型-整数  */
    LONG("整数"),

    /** 对象类型-浮点数  */
    DOUBLE("浮点数"),

    /** 对象类型-字符串  */
    STRING("字符串"),

    /** 对象类型-布尔  */
    BOOL("布尔"),

    /** 对象类型-日期  */
    DATE("日期"),

    /** 对象类型-时间  */
    TIME("时间"),

    /** 对象类型-日期时间  */
    DATETIME("日期时间"),

    /** 对象类型-对象  */
    OBJECT("对象"),

    /** 对象类型-整数集合  */
    LIST_LONG("整数集合"),

    /** 对象类型-浮点数集合  */
    LIST_DOUBLE("浮点数集合"),

    /** 对象类型-字符串集合  */
    LIST_STRING("字符串集合"),

    /** 对象类型-布尔集合  */
    LIST_BOOL("布尔集合"),

    /** 对象类型-日期集合  */
    LIST_DATE("日期集合"),

    /** 对象类型-时间集合  */
    LIST_TIME("时间集合"),

    /** 对象类型-日期时间集合  */
    LIST_DATETIME("日期时间集合"),

    /** 对象类型-对象集合  */
    LIST_OBJECT("对象集合");


    val list: Boolean
        get() = this == LIST_LONG || this == LIST_DOUBLE || this == LIST_STRING || this == LIST_BOOL ||
                this == LIST_DATE || this == LIST_TIME || this == LIST_DATETIME || this == LIST_OBJECT

    /** 将当前类型转换为字符串类型  */
    fun changeToStringIfSuitable(): DataPropertyDataType {
        return if(this == OBJECT || this == LIST_OBJECT) {
            this
        } else STRING
    }

    /** 将当前类型转换值的字符串类型，如果当前为集合，则不改变集合语义  */
    fun changeToValueStringIfSuitable(): DataPropertyDataType {
        return when(this) {
            OBJECT, LIST_OBJECT -> this
            else -> if(this.list) LIST_STRING else STRING
        }
    }

    companion object {

        /** 判断对象是否是集合  */
        fun isList(obj: Any): Boolean {
            return obj is List<*>
        }

        /** 判断对象是否是对象(map类型)  */
        fun isObject(obj: Any): Boolean {
            return obj is Map<*, *>
        }

        /**
         * 将对象转换为string形式(如果可以)
         * 如果不能转换，则转换原格式数据，因此返回结果可能并不一定是字符串
         */
        fun toStringIfSuitable(obj: Any?): Any? {
            return when(obj) {
                is List<*> -> obj.asSequence().map { toStringIfSuitable(it) }.joinToString(",")
            //如果对象是map，则表示此对象是复合对象，应该进一步转换，因此这里不作处理
                is Map<*, *> -> obj
                else -> obj?.toString()
            }
        }
    }
}
