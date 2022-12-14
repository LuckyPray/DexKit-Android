<div align="center">
    <h1> DexKit-Android </h1>

[![license](https://img.shields.io/github/license/LuckyPray/DexKit-Android.svg)](https://www.gnu.org/licenses/lgpl-3.0.html)
[![](https://jitpack.io/v/LuckyPray/DexKit-Android.svg)](https://jitpack.io/#LuckyPray/DexKit-Android)

[README](https://github.com/LuckyPray/DexKit-Android/blob/master/README.md)|[中文文档](https://github.com/LuckyPray/DexKit-Android/blob/master/README_zh.md)

</div>

# 本仓库更新终止, 后续请使用 [DexKit](https://github.com/LuckyPray/DexKit), 它使用起来更便捷。

一个高性能的 dex 反混淆工具（NDK版本）。

> **Warning**: 当前项目已经进行重构，`1.1.0`及以下的API全被弃用，请参考最新的文档进行使用。

## API说明

这两个 API 可以满足你大部分的使用场景：

- **`DexKit::BatchFindClassesUsedStrings`**
- **`DexKit::BatchFindMethodsUsedStrings`**

> **Note**：无论什么情况都应当避免搜索关键词包含重复内容， 例如：{"key_word", "word"}，因为这样会导致标记被覆盖，从而导致搜索结果不准确。
> 如果真的有这样的需求，尽可能打开高级搜索模式，同时使用字符串完全匹配内容，例如修改成这样：{"^key_word$", "^word$"}

以及其他 API：

- `DexKit::FindMethodBeInvoked`: 查找指定方法的调用者
- `DexKit::FindMethodInvoking`: 查找指定方法调用的方法
- `DexKit::FindMethodUsedField`: 查找指定的属性被什么方法调用，可通过参数 `used_flags` 限制访问类型(put/get)
- `DexKit::FindMethodUsedString`: 查找指定字符串的调用者
- `DexKit::FindMethod`: 多条件查找方法
- `DexKit::FindSubClasses`: 查找直系子类
- `DexKit::FindMethodOpPrefixSeq`: 查找满足特定op前缀序列的方法(使用`0x00`-`0xff`)

更详细的API说明请参考 [dex_kit.h](https://github.com/LuckyPray/DexKit/blob/master/include/dex_kit.h).

## 集成

Gradle:

`implementation: com.github.LuckyPray:DexKit-Android:<version>`

这个库使用了 [prefab](https://google.github.io/prefab/)，你需要在 gradle (Android Gradle Plugin 4.1+ 版本以上才支持)中开启此特性：

```
android {
    buildFeatures {
        prefab true
    }
}
```

**注意**：DexKit-Android 使用 [prefab package schema v2](https://github.com/google/prefab/releases/tag/v2.0.0)，
它是从 [Android Gradle Plugin 7.1.0](https://developer.android.com/studio/releases/gradle-plugin?buildsystem=cmake#7-1-0) 开始作为默认配置的。
如果你使用的是 Android Gradle Plugin 7.1.0 之前的版本，请在 `gradle.properties` 中加入以下配置：

```
android.prefabVersion=2.0.0
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

> 注意：此头文件从 `1.1.0` 版本开始加入

同时，我们提供了 [DexKitJniHelper.h](https://github.com/LuckyPray/DexKit/blob/master/include/DexKitJniHelper.h)
用于java与c++之间复杂对象的转换，例如：`HashMap<String, HashSet<String>>` -> `std::map<std::string, std::set<std::string>>`。

JNI 使用示例：
- [dexkit.cpp](https://github.com/LuckyPray/XAutoDaily/blob/master/app/src/main/cpp/dexkit.cpp)
- [DexKitHelper.kt](https://github.com/LuckyPray/XAutoDaily/blob/master/app/src/main/java/me/teble/xposed/autodaily/dexkit/DexKitHelper.kt)

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
