package com.iflym.train.propertytranslate.translate.context

import io.iflym.core.util.CloneUtils

/**
 * created at 2018-09-05
 *
 * @author flym
 */
class GlobalTransformContext : Cloneable {
    /**
     * 相应的数据集是否参与处理
     * 即在整个过程中，是否仍处理数据集的结构变化
     * 在一定程度上可以优化相应的流程,避免多次处理,如对于循环式处理这种情况，后面的转换就不再需要处理了
     */
    var dataSchemaParted: Boolean = false

    fun changeDataSchemaParted(parted: Boolean): GlobalTransformContext {
        val value = CloneUtils.clone(this)
        value.dataSchemaParted = parted

        return value
    }
}
