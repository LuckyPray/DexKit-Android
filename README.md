<div align="center">
    <h1> DexKit-Android </h1>

[![license](https://img.shields.io/github/license/LuckyPray/DexKit-Android.svg)](https://www.gnu.org/licenses/lgpl-3.0.html)
[![](https://jitpack.io/v/LuckyPray/DexKit-Android.svg)](https://jitpack.io/#LuckyPray/DexKit-Android)

[README](https://github.com/LuckyPray/DexKit-Android/blob/master/README.md)|[中文文档](https://github.com/LuckyPray/DexKit-Android/blob/master/README_zh.md)

</div>

A high performance dex deobfuscator library(NDK).

> **Warning**: The current project has been refactored, `1.1.0` and earlier APIs are deprecated. Please refer to the latest documentation for use.

## API introduction

These two APIs can meet most of your usage scenarios:

- **`DexKit::BatchFindClassesUsedStrings`**
- **`DexKit::BatchFindMethodsUsedStrings`**

> **Note**: In all cases you should avoid searching for keywords that contain duplicate content, eg: {"key_word", "word"}, as this will cause tags to be overwritten, resulting in inaccurate search results.
> If there is such a need, open the advanced search mode as much as possible, and use the string to match the content exactly, for example, modify it to this: {"^key_word$", "^word$"}

And there are many other APIs:

- `DexKit::FindMethodBeInvoked`: find caller for specified method.
- `DexKit::FindMethodInvoking`: find the called method
- `DexKit::FindFieldBeUsed`: find method getting specified field, access types(put/get) can be limited by setting `be_used_flags`
- `DexKit::FindMethodUsedString`: find method used utf8 string
- `DexKit::FindMethod`: find method by multiple conditions
- `DexKit::FindSubClasses`: find all direct subclasses of the specified class
- `DexKit::FindMethodOpPrefixSeq`:  find all method used opcode prefix sequence

For more detailed instructions, please refer to [dex_kit.h](https://github.com/LuckyPray/DexKit/blob/master/include/dex_kit.h).

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

JNI used example :
- [dexkit.cpp](https://github.com/LuckyPray/XAutoDaily/blob/master/app/src/main/cpp/dexkit.cpp)
- [DexKitHelper.kt](https://github.com/LuckyPray/XAutoDaily/blob/master/app/src/main/java/me/teble/xposed/autodaily/dexkit/DexKitHelper.kt)

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
