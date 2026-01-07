# 身份证识别应用 - 快速开始指南

## 前置要求

1. Android Studio (推荐最新版本)
2. JDK 11或更高版本
3. Android SDK (API 24+)
4. 腾讯云账号和OCR服务密钥

## 配置步骤

### 1. 获取腾讯云密钥

1. 访问腾讯云控制台：https://console.cloud.tencent.com/
2. 开通OCR服务：https://console.cloud.tencent.com/ocr
3. 获取API密钥：https://console.cloud.tencent.com/cam/capi
   - 记录您的 `SecretId`
   - 记录您的 `SecretKey`

### 2. 配置项目密钥

打开文件：`app/src/main/java/com/project/demo/utils/TencentOCRService.kt`

找到以下代码并替换为您的密钥：

```kotlin
// 请替换为您自己的密钥信息
private const val SECRET_ID = "YOUR_SECRET_ID"      // 替换为您的SecretId
private const val SECRET_KEY = "YOUR_SECRET_KEY"    // 替换为您的SecretKey
```

**重要提示**：
- 请妥善保管您的密钥，不要泄露给他人
- 不要将包含真实密钥的代码提交到公共代码仓库
- 生产环境建议通过服务器中转API调用，避免密钥暴露

### 3. 构建和运行

#### 使用Android Studio

1. 打开Android Studio
2. 选择 `File` -> `Open`
3. 选择项目目录
4. 等待Gradle同步完成
5. 连接Android设备或启动模拟器
6. 点击运行按钮 (绿色三角形)

#### 使用命令行

```bash
# 构建Debug版本
./gradlew assembleDebug

# 安装到设备
adb install app/build/outputs/apk/debug/app-debug.apk

# 或者直接运行
./gradlew installDebug
```

## 使用说明

### 基本操作流程

1. **启动应用**
   - 打开"身份证证照识别"应用

2. **选择图片**
   - 点击"选择身份证图片"按钮
   - 首次使用会请求存储权限，请允许
   - 从相册中选择身份证照片

3. **选择正反面**
   - 正面：包含姓名、性别、民族、出生日期、住址、身份证号
   - 反面：包含签发机关、有效期限

4. **开始识别**
   - 点击"开始识别"按钮
   - 等待识别完成（通常需要2-5秒）

5. **查看结果**
   - 自动跳转到结果页面
   - 显示识别出的所有信息
   - 点击"返回"按钮返回主界面

### 注意事项

1. **图片要求**
   - 支持JPG、PNG等常见格式
   - 建议图片清晰，光线充足
   - 身份证信息完整可见
   - 避免反光、模糊、遮挡

2. **网络要求**
   - 需要连接互联网
   - 建议使用WiFi或4G/5G网络
   - 识别一次约消耗50-200KB流量

3. **权限说明**
   - 存储权限：用于读取相册图片
   - 网络权限：用于调用腾讯云API

## 常见问题

### Q1: 提示"识别失败"怎么办？

**可能原因**：
- 未配置正确的密钥
- 网络连接问题
- 图片格式不支持
- 腾讯云账户余额不足

**解决方法**：
1. 检查密钥配置是否正确
2. 确认网络连接正常
3. 尝试更换图片
4. 查看腾讯云控制台余额

### Q2: 识别结果不准确？

**建议**：
- 使用清晰的图片
- 确保光线充足
- 避免身份证反光
- 确保身份证信息完整

### Q3: 如何查看详细错误信息？

使用Android Studio的Logcat查看日志：
```
标签：TencentOCRService, HttpUtil
```

### Q4: 应用闪退怎么办？

1. 检查是否授予了必要的权限
2. 查看Logcat错误日志
3. 确认Android版本兼容性（需要Android 7.0+）

## 项目结构说明

```
demo/
├── app/
│   ├── src/
│   │   ├── main/
│   │   │   ├── java/com/project/demo/
│   │   │   │   ├── activity/          # Activity类
│   │   │   │   │   ├── MainActivity.kt
│   │   │   │   │   └── ResultActivity.kt
│   │   │   │   └── utils/             # 工具类
│   │   │   │       ├── TencentCloudSignature.kt  # V3签名
│   │   │   │       ├── HttpUtil.kt               # HTTP请求
│   │   │   │       ├── TencentOCRService.kt      # OCR服务
│   │   │   │       └── ImageUtil.kt              # 图片处理
│   │   │   ├── res/
│   │   │   │   ├── layout/            # 布局文件
│   │   │   │   │   ├── activity_main.xml
│   │   │   │   │   └── activity_result.xml
│   │   │   │   └── values/            # 资源文件
│   │   │   └── AndroidManifest.xml    # 清单文件
│   │   └── build.gradle.kts           # 构建配置
│   └── ...
├── README.md                          # 项目文档
└── SETUP.md                          # 本文件
```

## 技术支持

如有问题，请参考：
- 腾讯云OCR文档：https://cloud.tencent.com/document/product/866
- Android开发文档：https://developer.android.com/

## 更新日志

### v1.0.0 (2024-01-03)
- 初始版本发布
- 支持身份证正反面识别
- 实现腾讯云V3签名算法
- 使用HttpURLConnection进行网络请求
