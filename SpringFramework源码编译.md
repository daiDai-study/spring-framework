# SpringFramework源码编译

在编译 SpringFramework 源码时，注意事项较多，掌握之后相对来说还是比较简单的。

## 0. 基础环境

+ JDK：1.8 及以上即可（[官方文档](https://github.com/spring-projects/spring-framework/wiki/Build-from-Source)要求 **JDK 8 update 262 or later**），笔者编译时的版本时 JDK 8 update 251
+ IDEA：这个不确定，笔者使用的是 IDEA 2019.2.4 
+ Gradle：有的文档说官方文档要求 Gradle 需要低于 Gradle 6 的版本（没有查证），可以根据 [spring framework 源码](https://github.com/spring-projects/spring-framework) 
`gradle/wrapper/gradle-wrapper.properties` 中指定的版本去下载，笔者使用的是 **gradle-5.6.4-bin.zip**

## 1. spring framework 源码下载

1. 直接进入 [spring framework 源码 github 官方仓库](https://github.com/spring-projects/spring-framework) 中，选定版本（5.0.x 或 5.1.x 或 5.2.x）后，下载在本地
2. 将 spring framework 源码 github 官方仓库 `fork` 到自己的 github 仓库中，然后 `git clone` 到本地，本地可以通过 `git checkout -b localBranchName origin/remoteBranchName` 拉取远程分支到本地并直接切换

## 2. gradle 配置修改

> gradle 与 maven 类似，都需要从远程仓库中拉取依赖到本地仓库，才能运行，而在编译 spring framework 源码中，笔者个人觉得 **依赖的下载** 是 *劝退部分人没有完成源码编译* 的最大问题，而解决该问题的主要方案同 maven 类似，就是 **添加 maven仓库镜像** 和 **修改某些配置**

### 2.1 settings.gradle 添加 阿里云 maven 仓库镜像

`settings.gradle` 在 spring framework 根目录下，修改如下

修改前：
```gradle
pluginManagement {
	repositories {
		gradlePluginPortal()
		maven { url 'https://repo.spring.io/plugins-release' }
	}
}
```

修改后：
```gradle
pluginManagement {
	repositories {
		maven { url 'https://maven.aliyun.com/repository/gradle-plugin' }
		maven { url 'https://maven.aliyun.com/repository/spring-plugin'}
		maven { url "https://maven.aliyun.com/repository/public" }
		maven { url "https://maven.aliyun.com/repository/central" }
		gradlePluginPortal()
		maven { url 'https://repo.spring.io/plugins-release' }
	}
}
```

注意：不要删除原有的 maven 仓库地址，可能有的依赖（或插件）只在原有的 maven 仓库地址才有

### 2.2 build.gradle 添加 阿里云 maven 仓库镜像

`build.gradle` 也在 spring framework 根目录下，修改如下

修改前：
```gradle
repositories {
    mavenCentral()
    maven { url "https://repo.spring.io/libs-spring-framework-build" }
}
```

修改后：
```gradle
repositories {
    maven { url "https://maven.aliyun.com/repository/apache-snapshots" }
    maven { url "https://maven.aliyun.com/repository/spring" }
    maven { url "https://maven.aliyun.com/repository/google" }
    maven { url 'https://maven.aliyun.com/repository/central'}
    maven { url "https://maven.aliyun.com/repository/public" }
    mavenCentral()
    maven { url "https://repo.spring.io/libs-spring-framework-build" }
}
```

注意：不要删除原有的 maven 仓库地址，可能有的依赖（或插件）只在原有的 maven 仓库地址才有

### 2.3 gradle.properties 修改配置

修改前：
```gradle
version=5.2.9.BUILD-SNAPSHOT
org.gradle.jvmargs=-Xmx1536M
org.gradle.caching=true
org.gradle.parallel=true
kotlin.stdlib.default.dependency=false
```

修改后：
```gradle
version=5.2.9.BUILD-SNAPSHOT
## 设置此参数主要是编译下载包会占用大量的内存，可能会内存溢出
org.gradle.jvmargs=-Xmx2048M
## 开启 Gradle 缓存
org.gradle.caching=true
## 开启并行编译
org.gradle.parallel=true
## 启用新的孵化模式
org.gradle.configureondemand=true
## 开启守护进程 通过开启守护进程，下一次构建的时候，将会连接这个守护进程进行构建，而不是重新fork一个gradle构建进程
org.gradle.daemon=true
```

做完这些修改后，后续编译过程会相当简单许多

## 3. 源码命令行编译测试

在 spring framework 根目录下执行两条编译测试

```linux
./gradlew :spring-oxm:compileTestJava // 官方建议
```

```linux
./gradlew :spring-core:compileTestJava
```

在这个过程，就是下载项目中需要各个的 **插件（plugin）** 和 **依赖（dependency）**，这个过程可能会失败。
个人经验：重试 *gradelew 命令* 直到成功！！！

注意：默认情况（ *spring-framework\gradle\wrapper\gradle-wrapper.properties* 文件未做修改）下，各种依赖和插件会下载到 *用户主目录/.gradle/caches/modules-2/files-2.1* 文件夹（目录）中

## 4. 导入到 IDEA

IDEA --> Open --> 选定项目根目录或选定项目根目录下的 build.gradle 文件 --> OK
IDEA --> File --> New --> Project from Existed Sources... --> 选定项目根目录或选定项目根目录下的 build.gradle 文件 --> OK

如果在 IDEA --> Settings --> Build Tools --> Gradle 配置中，*use gradle from* 选项，可以指定本地下载的 gradle 应用程序，后续各种依赖和插件会下载到 *gradle应用程序根目录/caches/modules-2/files-2.1* 文件夹（目录）中

所以导入的时候，需要重新下载依赖和插件（比步骤3更多）

## 5. 其它

笔者在 build 整个项目时，是直接成功的，并没有针对 aspectj 报错，如有此方面的问题，可参考[听说你还没学Spring就被源码编译劝退了？30+张图带你玩转Spring编译](https://juejin.im/post/6847902215592640526)

参考文档：
+ [听说你还没学Spring就被源码编译劝退了？30+张图带你玩转Spring编译](https://juejin.im/post/6847902215592640526)
+ [想读Spring源码？先从这篇「 极简教程」开始](https://zhuanlan.zhihu.com/p/128673649)
+ [IDEA+Gradle构建Spring5源码阅读环境教程(优化构建速度)](https://zhuanlan.zhihu.com/p/149641082)
