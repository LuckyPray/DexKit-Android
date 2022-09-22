package com.github.LuckyPray

import java.io.Closeable
import java.net.URL

class DexKitHelper private constructor(dexpath: String): Closeable {

    companion object {
        const val FLAG_GETTING = 1
        const val FLAG_SETTING = 2
        const val FLAG_USING = FLAG_GETTING or FLAG_SETTING

        fun create(dexpath: String): DexKitHelper? {
            val helper = DexKitHelper(dexpath)
            return if (helper.valid()) helper else null
        }

        fun create(loader: ClassLoader): DexKitHelper? {
            val url = loader.javaClass.getDeclaredMethod("findResource", String::class.java)
                .invoke(loader, "AndroidManifest.xml")
            if (url is URL) {
                url.path.substring(5, url.path.length - 26).let {
                    return DexKitHelper(it)
                }
            }
            return null
        }
    }

    /**
     * 使用完成后切记记得调用 [release]，否则内存不会释放
     */
    private var token: Long = 0

    init {
        token = initDexKit(dexpath)
    }

    fun release() {
        release(token)
    }

    fun valid(): Boolean {
        return token != 0L
    }

    fun batchFindClassesUsedStrings(
        map: Map<String, Set<String>>,
        advancedMatch: Boolean = true,
        dexPriority: IntArray? = intArrayOf(),
    ): Map<String, Array<String>> {
        return batchFindClassesUsedStrings(token, map, advancedMatch, dexPriority)
    }

    fun batchFindMethodsUsedStrings(
        map: Map<String, Set<String>>,
        advancedMatch: Boolean = true,
        dexPriority: IntArray? = intArrayOf(),
    ): Map<String, Array<String>> {
        return batchFindMethodsUsedStrings(token, map, advancedMatch, dexPriority)
    }

    @JvmName("batchFindClassesUsedStrings2")
    fun batchFindClassesUsedStrings(
        map: Map<String, Array<String>>,
        advancedMatch: Boolean = true,
        dexPriority: IntArray? = intArrayOf(),
    ): Map<String, Array<String>> {
        return convertSignature(batchFindClassesUsedStrings(token, toSet(map), advancedMatch, dexPriority))
    }

    @JvmName("batchFindMethodsUsedStrings2")
    fun batchFindMethodsUsedStrings(
        map: Map<String, Array<String>>,
        advancedMatch: Boolean = true,
        dexPriority: IntArray? = intArrayOf(),
    ): Map<String, Array<String>> {
        return batchFindMethodsUsedStrings(token, toSet(map), advancedMatch, dexPriority)
    }

    fun toSet(map: Map<String, Array<String>>): Map<String, Set<String>> {
        val result = mutableMapOf<String, Set<String>>()
        for (entry in map) {
            result[entry.key] = entry.value.toSet()
        }
        return result
    }

    fun convertSignature(map: Map<String, Array<String>>) : Map<String, Array<String>> {
        val result = mutableMapOf<String, Array<String>>()
        for (entry in map) {
            result[entry.key] = entry.value.map { it.jniSignatureToJava() }.toTypedArray()
        }
        return result
    }

    fun findMethodBeInvoked(
        methodDescriptor: String,
        methodDeclareClass: String,
        methodName: String,
        methodReturnType: String,
        methodParamTypes: Array<String>? = null,
        callerMethodDeclareClass: String,
        callerMethodName: String,
        callerMethodReturnType: String,
        callerMethodParamTypes: Array<String>? = null,
        dexPriority: IntArray? = intArrayOf(),
    ): Array<String> {
        return findMethodBeInvoked(
            token,
            methodDescriptor,
            methodDeclareClass,
            methodName,
            methodReturnType,
            methodParamTypes,
            callerMethodDeclareClass,
            callerMethodName,
            callerMethodReturnType,
            callerMethodParamTypes,
            dexPriority
        )
    }

    fun findMethodInvoking(
        methodDescriptor: String,
        methodDeclareClass: String,
        methodName: String,
        methodReturnType: String,
        methodParamTypes: Array<String>? = null,
        beCalledMethodDeclareClass: String,
        beCalledMethodName: String,
        beCalledMethodReturnType: String,
        beCalledMethodParamTypes: Array<String>? = null,
        dexPriority: IntArray? = intArrayOf(),
    ): Map<String, Array<String>> {
        return findMethodInvoking(
            token,
            methodDescriptor,
            methodDeclareClass,
            methodName,
            methodReturnType,
            methodParamTypes,
            beCalledMethodDeclareClass,
            beCalledMethodName,
            beCalledMethodReturnType,
            beCalledMethodParamTypes,
            dexPriority
        )
    }

    fun findMethodUsedField(
        fieldDescriptor: String,
        fieldDeclareClass: String,
        fieldName: String,
        fieldType: String,
        usedFlags: Int,
        callerMethodDeclareClass: String,
        callerMethodName: String,
        callerMethodReturnType: String,
        callerMethodParamTypes: Array<String>? = null,
        dexPriority: IntArray? = intArrayOf(),
    ): Array<String> {
        return findMethodUsedField(
            token,
            fieldDescriptor,
            fieldDeclareClass,
            fieldName,
            fieldType,
            usedFlags,
            callerMethodDeclareClass,
            callerMethodName,
            callerMethodReturnType,
            callerMethodParamTypes,
            dexPriority
        )
    }

    fun findMethodUsedString(
        usedString: String,
        advancedMatch: Boolean = true,
        methodDeclareClass: String,
        methodName: String,
        methodReturnType: String,
        methodParamTypes: Array<String>? = null,
        dexPriority: IntArray? = intArrayOf(),
    ): Array<String> {
        return findMethodUsedString(
            token,
            usedString,
            advancedMatch,
            methodDeclareClass,
            methodName,
            methodReturnType,
            methodParamTypes,
            dexPriority
        )
    }

    fun findMethod(
        methodDeclareClass: String,
        methodName: String,
        methodReturnType: String,
        methodParamTypes: Array<String>? = null,
        dexPriority: IntArray? = intArrayOf(),
    ): Array<String> {
        return findMethod(
            token,
            methodDeclareClass,
            methodName,
            methodReturnType,
            methodParamTypes,
            dexPriority
        )
    }

    fun findSubClasses(
        parentClass: String,
        dexPriority: IntArray? = intArrayOf(),
    ): Array<String> {
        return findSubClasses(token, parentClass, dexPriority).map { it.jniSignatureToJava() }.toTypedArray()
    }

    fun findMethodOpPrefixSeq(
        opPrefixSeq: IntArray,
        methodDeclareClass: String,
        methodName: String,
        methodReturnType: String,
        methodParamTypes: Array<String>? = null,
        dexPriority: IntArray? = intArrayOf(),
    ): Array<String> {
        return findMethodOpPrefixSeq(
            token,
            opPrefixSeq,
            methodDeclareClass,
            methodName,
            methodReturnType,
            methodParamTypes,
            dexPriority
        )
    }

    private external fun initDexKit(dexpath: String): Long

    private external fun release(token: Long)

    private external fun batchFindClassesUsedStrings(
        token: Long,
        map: Map<String, Set<String>>,
        advancedMatch: Boolean,
        dexPriority: IntArray?,
    ): Map<String, Array<String>>

    private external fun batchFindMethodsUsedStrings(
        token: Long,
        map: Map<String, Set<String>>,
        advancedMatch: Boolean,
        dexPriority: IntArray?,
    ): Map<String, Array<String>>

    private external fun findMethodBeInvoked(
        token: Long,
        methodDescriptor: String,
        methodDeclareClass: String,
        methodName: String,
        methodReturnType: String,
        methodParamTypes: Array<String>?,
        callerMethodDeclareClass: String,
        callerMethodName: String,
        callerMethodReturnType: String,
        callerMethodParamTypes: Array<String>?,
        dexPriority: IntArray?,
    ): Array<String>
    
    private external fun findMethodInvoking(
        token: Long,
        methodDescriptor: String,
        methodDeclareClass: String,
        methodName: String,
        methodReturnType: String,
        methodParamTypes: Array<String>?,
        beCalledMethodDeclareClass: String,
        beCalledMethodName: String,
        beCalledMethodReturnType: String,
        beCalledMethodParamTypes: Array<String>?,
        dexPriority: IntArray?,
    ): Map<String, Array<String>>
    
    private external fun findMethodUsedField(
        token: Long,
        fieldDescriptor: String,
        fieldDeclareClass: String,
        fieldName: String,
        fieldType: String,
        usedFlags: Int,
        callerMethodDeclareClass: String,
        callerMethodName: String,
        callerMethodReturnType: String,
        callerMethodParamTypes: Array<String>?,
        dexPriority: IntArray?,
    ): Array<String>

    private external fun findMethodUsedString(
        token: Long,
        usedString: String,
        advancedMatch: Boolean,
        methodDeclareClass: String,
        methodName: String,
        methodReturnType: String,
        methodParamTypes: Array<String>?,
        dexPriority: IntArray?,
    ): Array<String>

    private external fun findMethod(
        token: Long,
        methodDeclareClass: String,
        methodName: String,
        methodReturnType: String,
        methodParamTypes: Array<String>?,
        dexPriority: IntArray?,
    ): Array<String>

    private external fun findSubClasses(
        token: Long,
        parentClass: String,
        dexPriority: IntArray?,
    ): Array<String>

    private external fun findMethodOpPrefixSeq(
        token: Long,
        opPrefixSeq: IntArray,
        methodDeclareClass: String,
        methodName: String,
        methodReturnType: String,
        methodParamTypes: Array<String>?,
        dexPriority: IntArray?,
    ): Array<String>

    override fun close() {
        release()
    }

    fun String.jniSignatureToJava(): String {
        return this.replace('/', '.').substring(1, this.length - 1)
    }
}