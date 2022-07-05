# ZIMKit-Android

## 概述

本文介绍如何快速跑通示例源码，体验基础的 ZIMKit 集成方案。

## 准备环境

在运行示例源码前，请确保开发环境满足以下要求：

- Android Studio 3.6 或以上版本。
- Android SDK 28、Android SDK Platform-Tools 30。
- Android 5.0 或以上版本的 Android 设备或模拟器（推荐使用真机），如果是真机，请开启“允许调试”选项。
- Android 设备、开发电脑已经连接到 Internet。

## 前提条件

已在 [ZEGO 控制台](https://console.zego.im/) 创建项目，并申请有效的 AppID 和 ServerSecret，详情请参考 [控制台 - 项目管理](http://doc.oa.zego.im/zh#12107) 中的“项目信息”。

## 示例源码目录结构

```
├── README.md                // 说明文件
├── ZIMKit
│   ├── ZIMKitCommon         // 公共库
│   ├── ZIMKitConversation   // 会话组件
│   ├── ZIMKitFull           // 依赖了全部组件
│   ├── ZIMKitGroup          // 群组组件
│   ├── ZIMKitMessages       // 消息组件
│   ├── app                  // demo模块
│   ├── build.gradle         // 全局配置
│   ├── gradle
│   ├── gradle.properties
│   ├── gradlew
│   ├── gradlew.bat
│   ├── local.properties
│   └── settings.gradle      // 模块配置
└── key.jks                  // 编译release 所用的签名
```

## 运行示例源码

1. 替换 AppConfig 中的 APP_ID 和 ServerSecret 字段。
2. 单击 “Sync” 按钮，同步 Gradle 和所需的相关依赖。
3. 电脑连接好 Android 手机，单击“运行”按钮，即可运行体验。

## 体验 ZIMKit 功能

1. 在登录页面输入 UserID，单击“登录”，登录成功后，即可进入会话列表页面。
2. 首次进入需要先创建会话，可创建单聊会话或者群聊会话或者加入其他群聊。
3. 会话创建成功后，进入消息页面，即可发送消息。