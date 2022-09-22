#include <dex_kit.h>
#include "DexKitJniHelper.h"

extern "C"
JNIEXPORT jlong JNICALL
Java_com_github_LuckyPray_DexKitHelper_initDexKit(JNIEnv *env, jobject thiz, jstring dexpath) {
    if (!dexpath) {
        return 0;
    }

    auto cdexpath = env->GetStringUTFChars(dexpath, nullptr);
    auto dexkit = new dexkit::DexKit(cdexpath);
    env->ReleaseStringUTFChars(dexpath, cdexpath);
    return (jlong) dexkit;
}

extern "C"
JNIEXPORT void JNICALL
Java_com_github_LuckyPray_DexKitHelper_release(JNIEnv *env, jobject thiz, jlong token) {
    ReleaseDexKitInstance(env, token);
}

extern "C"
JNIEXPORT jobject JNICALL
Java_com_github_LuckyPray_DexKitHelper_batchFindClassesUsedStrings(JNIEnv *env,
                                                                               jobject thiz,
                                                                               jlong token,
                                                                               jobject map,
                                                                               jboolean advanced_match,
                                                                               jintArray dex_priority) {
    return BatchFindClassesUsedStrings(env, token, map, advanced_match, dex_priority);
}

extern "C"
JNIEXPORT jobject JNICALL
Java_com_github_LuckyPray_DexKitHelper_batchFindMethodsUsedStrings(JNIEnv *env,
                                                                               jobject thiz,
                                                                               jlong token,
                                                                               jobject map,
                                                                               jboolean advanced_match,
                                                                               jintArray dex_priority) {
    return BatchFindMethodsUsedStrings(env, token, map, advanced_match, dex_priority);
}

extern "C"
JNIEXPORT jobjectArray JNICALL
Java_com_github_LuckyPray_DexKitHelper_findMethodBeInvoked(JNIEnv *env, jobject thiz,
                                                                       jlong token,
                                                                       jstring method_descriptor,
                                                                       jstring method_declare_class,
                                                                       jstring method_declare_name,
                                                                       jstring method_return_type,
                                                                       jobjectArray method_param_types,
                                                                       jstring caller_method_declare_class,
                                                                       jstring caller_method_declare_name,
                                                                       jstring caller_method_return_type,
                                                                       jobjectArray caller_method_param_types,
                                                                       jintArray dex_priority) {
    return FindMethodBeInvoked(env, token, method_descriptor, method_declare_class,
                               method_declare_name, method_return_type, method_param_types,
                               caller_method_declare_class, caller_method_declare_name,
                               caller_method_return_type, caller_method_param_types, dex_priority);
}

extern "C"
JNIEXPORT jobject JNICALL
Java_com_github_LuckyPray_DexKitHelper_findMethodInvoking(JNIEnv *env, jobject thiz,
                                                                      jlong token,
                                                                      jstring method_descriptor,
                                                                      jstring method_declare_class,
                                                                      jstring method_declare_name,
                                                                      jstring method_return_type,
                                                                      jobjectArray method_param_types,
                                                                      jstring be_called_method_declare_class,
                                                                      jstring be_called_method_declare_name,
                                                                      jstring be_called_method_return_type,
                                                                      jobjectArray be_called_method_param_types,
                                                                      jintArray dex_priority) {
    return FindMethodInvoking(env, token, method_descriptor, method_declare_class,
                              method_declare_name, method_return_type, method_param_types,
                              be_called_method_declare_class, be_called_method_declare_name,
                              be_called_method_return_type, be_called_method_param_types,
                              dex_priority);
}

extern "C"
JNIEXPORT jobjectArray JNICALL
Java_com_github_LuckyPray_DexKitHelper_findMethodUsedField(JNIEnv *env, jobject thiz,
                                                                       jlong token,
                                                                       jstring field_descriptor,
                                                                       jstring field_declare_class,
                                                                       jstring field_name,
                                                                       jstring field_type,
                                                                       jint used_flags,
                                                                       jstring caller_method_declare_class,
                                                                       jstring caller_method_name,
                                                                       jstring caller_method_return_type,
                                                                       jobjectArray caller_method_param_types,
                                                                       jintArray dex_priority) {
    return FindMethodUsedField(env, token, field_descriptor, field_declare_class, field_name,
                               field_type, used_flags, caller_method_declare_class,
                               caller_method_name, caller_method_return_type,
                               caller_method_param_types, dex_priority);
}

extern "C"
JNIEXPORT jobjectArray JNICALL
Java_com_github_LuckyPray_DexKitHelper_findMethodUsedString(JNIEnv *env, jobject thiz,
                                                                        jlong token,
                                                                        jstring used_string,
                                                                        jboolean advanced_match,
                                                                        jstring method_declare_class,
                                                                        jstring method_name,
                                                                        jstring method_return_type,
                                                                        jobjectArray method_param_types,
                                                                        jintArray dex_priority) {
    return FindMethodUsedString(env, token, used_string, advanced_match, method_declare_class,
                                method_name, method_return_type, method_param_types, dex_priority);
}

extern "C"
JNIEXPORT jobjectArray JNICALL
Java_com_github_LuckyPray_DexKitHelper_findMethod(JNIEnv *env, jobject thiz,
                                                              jlong token,
                                                              jstring method_declare_class,
                                                              jstring method_name,
                                                              jstring method_return_type,
                                                              jobjectArray method_param_types,
                                                              jintArray dex_priority) {
    return FindMethod(env, token, method_declare_class, method_name, method_return_type,
                      method_param_types, dex_priority);
}

extern "C"
JNIEXPORT jobjectArray JNICALL
Java_com_github_LuckyPray_DexKitHelper_findSubClasses(JNIEnv *env, jobject thiz,
                                                                  jlong token, jstring parent_class,
                                                                  jintArray dex_priority) {
    return FindSubClasses(env, token, parent_class, dex_priority);
}

extern "C"
JNIEXPORT jobjectArray JNICALL
Java_com_github_LuckyPray_DexKitHelper_findMethodOpPrefixSeq(JNIEnv *env, jobject thiz,
                                                                         jlong token,
                                                                         jintArray op_prefix_seq,
                                                                         jstring method_declare_class,
                                                                         jstring method_name,
                                                                         jstring method_return_type,
                                                                         jobjectArray method_param_types,
                                                                         jintArray dex_priority) {
    return FindMethodOpPrefixSeq(env, token, op_prefix_seq, method_declare_class, method_name,
                                 method_return_type, method_param_types, dex_priority);
}