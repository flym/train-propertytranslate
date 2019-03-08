package com.iflym.train.propertytranslate.translate

import com.iflym.train.propertytranslate.translate.context.GlobalTransformContext
import com.iflym.train.propertytranslate.translate.context.TransformContext
import com.iflym.train.propertytranslate.translate.enums.TransformerResultType
import com.iflym.train.propertytranslate.vo.DataSchemaVO

/**
 * 默认实现相应的执行信息
 * created at 2018-08-31
 *
 * @author flym
 */
class DefaultTransformerInvocation : TransformerInvocation {
    private var transformerInstanceList: List<TransformerInstance<*, *>>? = null

    private lateinit var input: Any
    private lateinit var inputSchema: DataSchemaVO
    var globalContext = GlobalTransformContext()

    //---------------------------- 逻辑相关 start ------------------------------//

    private var maxStep: Int = 0
    private var currentStep: Int = 0

    private lateinit var currentOutput: Any
    private lateinit var currentOutputSchema: DataSchemaVO
    private var currentResultType = TransformerResultType.MAP

    //---------------------------- 逻辑相关 end ------------------------------//

    fun bindCurrentOutputSchema(currentOutputSchema: DataSchemaVO) {
        this.currentOutputSchema = currentOutputSchema
    }

    fun bindCurrentOutput(currentOutput: Any) {
        this.currentOutput = currentOutput
    }

    fun bindCurrentResultType(currentResultType: TransformerResultType) {
        this.currentResultType = currentResultType
    }

    override fun init() {
        //默认输出结果为当前输入
        currentOutput = input
        currentOutputSchema = inputSchema

        maxStep = transformerInstanceList!!.size
    }

    @Suppress("UNCHECKED_CAST")
    override fun proceed() {
        if(currentStep >= maxStep) {
            return
        }

        val transformerVO = transformerInstanceList!![currentStep++]
        val transformer = transformerVO.createTransformer() as Transformer<TransformContext, Any>
        val context = transformerVO.createContext()

        transformer.transformChained(currentOutput, currentOutputSchema, context, this, this.globalContext)
    }

    override fun resultType(): TransformerResultType {
        return currentResultType
    }

    override fun result(): Any? {
        return currentOutput
    }

    override fun resultSchema(): DataSchemaVO? {
        return currentOutputSchema
    }

    override fun clone(): Any {
        val objCloneMethod = (java.lang.Object::class.java).getDeclaredMethod("clone")
        objCloneMethod.isAccessible = true
        return com.iflym.train.propertytranslate.util.MethodUtils.invokeSpecial(objCloneMethod, this)
    }

    companion object {

        fun build(transformerInstanceList: List<TransformerInstance<*, *>>, input: Any, inputSchema: DataSchemaVO): DefaultTransformerInvocation {
            val value = DefaultTransformerInvocation()
            value.transformerInstanceList = transformerInstanceList
            value.input = input
            value.inputSchema = inputSchema

            return value
        }
    }
}
