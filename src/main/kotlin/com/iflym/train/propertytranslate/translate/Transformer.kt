package com.iflym.train.propertytranslate.translate


import com.iflym.train.propertytranslate.translate.context.GlobalTransformContext
import com.iflym.train.propertytranslate.translate.context.TransformContext
import com.iflym.train.propertytranslate.vo.DataSchemaVO
import io.iflym.core.tuple.Tuple2

/**
 * 转换器接口。
 * 实现注：转换器一般直接操作原数据，以避免使用额外的空间以及额外的复杂度，这样也方便程序实现
 * 即一般情况下，I,R的类型是一致的，一般的实现即类似以下的操作
 * `
 * I input = xxxx
 * input.setXXX(...);
 * return input;
` *
 * 如果外部应用需要保存原数据，则请自行进行copy或clone处理
 *
 * @author flym
 */
interface Transformer<in C : TransformContext, I> {

    /**
     * 进行数据转换
     *
     * @param input                  当前要转换的数据对象
     * @param context                表示当前转换器要进行访问的上下文信息
     * @param globalTransformContext 全局上下文
     * @param inputSchema  此对象对应的入参结构
     * @param invocation 当前转换执行器
     */
    fun transformChained(input: I, inputSchema: DataSchemaVO, context: C, invocation: DefaultTransformerInvocation, globalTransformContext: GlobalTransformContext) {
        val tuple2 = transform(input, inputSchema, context, globalTransformContext)

        invocation.bindCurrentOutput(tuple2.t1)
        invocation.bindCurrentOutputSchema(tuple2.t2)

        invocation.proceed()
    }

    /**
     * 进行数据转换
     *
     * @param input         当前要转换的数据对象
     * @param inputSchema   数据对象相应的属性结构
     * @param context       表示当前转换器要进行访问的上下文信息
     * @param globalContext 全局上下文
     * @return 根据转换规则转换后的数据对象集合
     */
    fun transform(input: I, inputSchema: DataSchemaVO, context: C, globalContext: GlobalTransformContext): Tuple2<Any, DataSchemaVO>
}
