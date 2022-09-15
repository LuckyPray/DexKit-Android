<div align="center">
    <h1> DexKit-Android </h1>

[![license](https://img.shields.io/github/license/LuckyPray/DexKit-Android.svg)](https://www.gnu.org/licenses/lgpl-3.0.html)
[![](https://jitpack.io/v/LuckyPray/DexKit-Android.svg)](https://jitpack.io/#LuckyPray/DexKit-Android)

[README](https://github.com/LuckyPray/DexKit-Android/blob/master/README.md)|[中文文档](https://github.com/LuckyPray/DexKit-Android/blob/master/README_zh.md)

</div>

A high performance dex deobfuscator library(NDK).

## API introduction

These two APIs can meet most of your usage scenarios:

- **`DexKit::LocationClasses`**
- **`DexKit::LocationMethods`**

And there are many other APIs:

- `DexKit::FindMethodInvoked`: Find caller for specified method.
- `DexKit::FindMethodUsedString`
- `DexKit::FindMethod`: Find method with various conditions
- `DexKit::FindSubClasses`: Find sub class of specified class
- `DexKit::FindMethodOpPrefixSeq`: Find method with op prefix

## Integration

Gradle:

`implementation: io.github.LuckyPray:DexKit-Android:<version>`

This library uses [prefab](https://google.github.io/prefab/), you should enable it in gradle (Android Gradle Plugin 4.1+):

```
android {
    buildFeatures {
        prefab true
    }
}
```

## Usage

### CMake

You can use `find_package` in `CMakeLists.txt`:

```cmake
add_library(mylib SHARED main.cpp)

# Add two lines below
find_package(dexkit REQUIRED CONFIG)
target_link_libraries(mylib dexkit::dex_kit_static z)
```

> Note: This header file was added since `1.1.0`
At the same time, we also provide [DexKitJniHelper.h](https://github.com/LuckyPray/DexKit/blob/master/include/DexKitJniHelper.h) 
for the conversion of complex objects between java and c++. For example: `HashMap<String, HashSet<String>>` -> `std::map<std::string, std::set<std::string>>`

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
    // this function is declared in DexKitJniHelper.h
    // For more help methods, please check the source code: https://github.com/LuckyPray/DexKit/blob/master/include/DexKitJniHelper.h
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
     * free space allocated by c++
     */
    private external fun release(token: Long)

    private external fun batchFindClassUsedString(
        token: Long,
        map: Map<String, Set<String>>,
        advancedMatch: Boolean = false,
    ): Map<String, Array<String>>
    
    //  omit...
}
```

## Example

- [main.cpp](https://github.com/LuckyPray/DexKit/blob/master/main.cpp)
- [qq-example.cpp](https://github.com/LuckyPray/DexKit/blob/master/qq-example.cpp)

## Benchmark

qq-example.cpp in MacPro M1 to deobfuscate `qq-8.9.3.apk`, the result is:

```txt
findClass count: 47
findMethod count: 29
used time: 207 ms
```

## License

The slicer directory is partially copied from [AOSP](https://cs.android.com/android/platform/superproject/+/master:frameworks/base/startop/view_compiler).

Modified parts are owed by LuckyPray Developers. If you would like to use it in an open source project, please submodule it.