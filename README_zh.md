<div align="center">
    <h1> DexKit-Android </h1>

[![license](https://img.shields.io/github/license/LuckyPray/DexKit-Android.svg)](https://www.gnu.org/licenses/lgpl-3.0.html)
[![](https://jitpack.io/v/LuckyPray/DexKit-Android.svg)](https://jitpack.io/#LuckyPray/DexKit-Android)

[README](https://github.com/LuckyPray/DexKit-Android/blob/master/README.md)|[中文文档](https://github.com/LuckyPray/DexKit-Android/blob/master/README_zh.md)

</div>

一个高性能的 dex 反混淆工具（NDK版本）。

## API说明

这两个 API 可以满足你大部分的使用场景：

- **`DexKit::LocationClasses`**
- **`DexKit::LocationMethods`**

以及其他 API：

- `DexKit::FindMethodInvoked`: 查找指定方法的调用者(invoke-kind类别的opcode)
- `DexKit::FindMethodUsedString`: 查找指定字符串的调用者(`const-string`、`const-string/jumbo`)
- `DexKit::FindMethod`: 多条件查找方法
- `DexKit::FindSubClasses`: 查找直系子类
- `DexKit::FindMethodOpPrefixSeq`: 查找满足特定op前缀序列的方法(使用`0x00`-`0xff`)

## 集成

Gradle:

`implementation: io.github.LuckyPray:DexKit-Android:<version>`

这个库使用了 [prefab](https://google.github.io/prefab/)，你需要在 gradle (Android Gradle Plugin 4.1+ 版本以上才支持)中开启此特性：

```
android {
    buildFeatures {
        prefab true
    }
}
```

## 使用

### CMake

你可以直接在 `CMakeLists.txt` 中使用 `find_package` 来使用 DexKit:

```cmake
add_library(mylib SHARED main.cpp)

# 添加如下两行，注意必须添加 libz，如果你有其他依赖可以放在后面
find_package(dexkit REQUIRED CONFIG)
target_link_libraries(mylib dexkit::dex_kit_static z)
```

> 注意：此头文件从 `1.1.0` 开始加入
同时，我们提供了 [DexKitJniHelper.h](https://github.com/LuckyPray/DexKit/blob/master/include/DexKitJniHelper.h)
用于java与c++之间复杂对象的转换，例如：`HashMap<String, HashSet<String>>` -> `std::map<std::string, std::set<std::string>>`。

dexkit.cpp
```c++
#include<DexKitJniHelper.h>

extern "C"
JNIEXPORT jlong JNICALL
Java_me_xxx_dexkit_DexKitHelper_initDexKit(JNIEnv *env, jobject thiz,
                                           jstring apkPath) {
    const char *cStr = env->GetStringUTFChars(apkPath, nullptr);
    std::string filePathStr(cStr);
    auto dexkit = new dexkit::DexKit(hostApkPath);
    env->ReleaseStringUTFChars(apkPath, cStr);
    return (jlong) dexkit;
}

extern "C"
JNIEXPORT void JNICALL
Java_me_xxx_dexkit_DexKitHelper_release(JNIEnv *env, jobject thiz, jlong token) {
    ReleaseDexKitInstance(env, token);
}

extern "C"
JNIEXPORT jobject JNICALL
Java_me_xxx_dexkit_DexKitHelper_batchFindClassUsedString(JNIEnv *env,
                                                         jobject thiz,
                                                         jlong token,
                                                         jobject map,
                                                         jboolean advanced_match) {
    // 该方法定义于 DexKitJniHelper.h
    // 获取更多辅助方法请参阅源码：https://github.com/LuckyPray/DexKit/blob/master/include/DexKitJniHelper.h
    return LocationClasses(env, token, map, advanced_match);
}
```

DexKitHelper.kt
```kotlin
class DexKitHelper(
    classLoader: ClassLoader
) {
    
    private var token: Long = 0

    init {
        token = initDexKit(classLoader)
    }

    private external fun initDexKit(apkPath: String): Long

    /**
     * 释放c++分配的空间
     */
    private external fun release(token: Long)

    private external fun batchFindClassUsedString(
        token: Long,
        map: Map<String, Set<String>>,
        advancedMatch: Boolean = false,
    ): Map<String, Array<String>>
    
    // 省略... omit...
}
```

## 使用示例

- [main.cpp](https://github.com/LuckyPray/DexKit/blob/master/main.cpp)
- [qq-example.cpp](https://github.com/LuckyPray/DexKit/blob/master/qq-example.cpp)

## 基准测试

qq-example.cpp 在MacPro M1环境下对 `qq-8.9.3.apk` 执行结果如下所示:
```text
findClass count: 47
findMethod count: 29
used time: 207 ms
```

## License

slicer目录下内容是从 [AOSP](https://cs.android.com/android/platform/superproject/+/master:frameworks/base/startop/view_compiler) 拷贝的.

修改部分归 LuckyPray 所有。如果您想在开源项目中使用，请将其子模块化。
