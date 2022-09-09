DexKit
--

[README](https://github.com/LuckyPray/DexKit-Android/blob/master/README.md)|[中文文档](https://github.com/LuckyPray/DexKit-Android/blob/master/README_zh.md)

一个高性能的 dex 反混淆工具。

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

```
# 假设你的 library 名字为 mylib
add_library(mylib SHARED main.cpp)

# 添加如下两行，注意必须添加 libz，如果你有其他依赖可以放在后面
find_package(dexkit REQUIRED CONFIG)
target_link_libraries(mylib dexkit::dex_kit_static z)
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
