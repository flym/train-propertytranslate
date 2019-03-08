package com.iflym.train.propertytranslate.vo

import com.google.common.primitives.Ints
import com.iflym.train.propertytranslate.domain.enums.DataPropertyDataType
import io.iflym.core.util.CloneUtils
import java.util.*

/**
 * created at 2018-09-03
 *
 * @author flym
 */
class DataPropertyVO : Cloneable, Comparable<DataPropertyVO> {
    /** 当前层的惟一名  */
    var name: String? = null

    /** 当前标题  */
    var title: String? = null

    /** 数据类型  */
    var dataType: DataPropertyDataType? = null


    /** 序号(在显示时使用)  */
    var order: Int = 0

    override fun compareTo(other: DataPropertyVO): Int {
        return Ints.compare(this.order, other.order)
    }

    fun changeName(newName: String): DataPropertyVO {
        if (Objects.equals(this.name, newName)) {
            return this
        }

        val value = CloneUtils.clone(this)
        value.name = newName

        return value
    }

    fun changeDataType(dataType: DataPropertyDataType): DataPropertyVO {
        if (this.dataType === dataType) {
            return this
        }

        val value = CloneUtils.clone(this)
        value.dataType = dataType

        return value
    }

    companion object {

        const val ELEMENT_NAME_PROPERTY = "property"

        const val QNAME_NAME = "name"
        const val QNAME_TITLE = "title"
        const val QNAME_DATA_TYPE = "dataType"

        fun build(name: String, title: String, dataType: DataPropertyDataType): DataPropertyVO {
            val value = DataPropertyVO()
            value.name = name
            value.title = title
            value.dataType = dataType

            return value
        }
    }
}
