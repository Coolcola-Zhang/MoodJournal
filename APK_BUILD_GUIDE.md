# 📱 MoodCalendar APK 打包指南

本文档将指导您如何将 MoodCalendar 项目打包成 APK 文件。

## 🚀 快速开始

### 方法一：使用 Android Studio（推荐）

1. **打开项目**
   - 用 Android Studio 打开 `e:\MoodJournal` 文件夹
   - 等待 Gradle 同步完成

2. **配置签名**
   - 点击 `Build` → `Generate Signed Bundle / APK`
   - 选择 `APK`
   - 创建新的密钥库（KeyStore）或使用现有

3. **构建 APK**
   - 选择 `Build Variants` 为 `release`
   - 点击 `Finish`
   - APK 将生成在 `app/build/outputs/apk/release/` 目录

### 方法二：使用命令行

```bash
# 导航到项目目录
cd e:\MoodJournal

# 生成发布版 APK
.\gradlew.bat assembleRelease

# 或者生成调试版 APK
.\gradlew.bat assembleDebug
```

## 🔧 详细配置

### 1. 签名配置

创建 `app/keystore.properties` 文件（不要上传到GitHub）：

```properties
storePassword=your_store_password
keyPassword=your_key_password
keyAlias=key0
storeFile=keystore.jks
```

创建密钥库：
```bash
keytool -genkey -v -keystore keystore.jks -keyalg RSA -keysize 2048 -validity 10000 -alias key0
```

### 2. 更新构建配置

在 `app/build.gradle.kts` 中添加签名配置：

```kotlin
android {
    // ... 现有配置
    
    signingConfigs {
        create("release") {
            storeFile = file("keystore.jks")
            storePassword = properties["storePassword"] as String
            keyAlias = properties["keyAlias"] as String
            keyPassword = properties["keyPassword"] as String
        }
    }
    
    buildTypes {
        release {
            isMinifyEnabled = true
            isShrinkResources = true
            signingConfig = signingConfigs.getByName("release")
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
}
```

## 📦 APK 构建脚本

我为您创建了自动构建脚本：

### Windows 批处理脚本

创建 `build_apk.bat`：

```batch
@echo off
echo 📱 MoodCalendar APK 构建脚本
echo ================================

cd /d "e:\MoodJournal"

if not exist "keystore.properties" (
    echo ❌ 错误: 未找到 keystore.properties 文件
    echo 请先创建签名配置文件
    pause
    exit /b 1
)

echo 🔨 开始构建发布版 APK...
.\gradlew.bat clean assembleRelease

if %errorlevel% equ 0 (
    echo ✅ APK 构建成功！
    echo 📁 APK 位置: app\build\outputs\apk\release\
    echo 📱 文件: app-release.apk
) else (
    echo ❌ APK 构建失败
)

pause
```

### PowerShell 脚本

创建 `build_apk.ps1`：

```powershell
# MoodCalendar APK 构建脚本
Write-Host "📱 MoodCalendar APK 构建脚本" -ForegroundColor Green
Write-Host "========================" -ForegroundColor Green

Set-Location "e:\MoodJournal"

if (-not (Test-Path "keystore.properties")) {
    Write-Host "❌ 错误: 未找到 keystore.properties 文件" -ForegroundColor Red
    Write-Host "请先创建签名配置文件" -ForegroundColor Yellow
    exit 1
}

Write-Host "🔨 开始构建发布版 APK..." -ForegroundColor Yellow
.\gradlew.bat clean assembleRelease

if ($LASTEXITCODE -eq 0) {
    Write-Host "✅ APK 构建成功！" -ForegroundColor Green
    Write-Host "📁 APK 位置: app\build\outputs\apk\release\" -ForegroundColor Cyan
    Write-Host "📱 文件: app-release.apk" -ForegroundColor Cyan
} else {
    Write-Host "❌ APK 构建失败" -ForegroundColor Red
}
```

## 🔄 优化构建配置

### 启用代码压缩和资源优化

在 `app/build.gradle.kts` 中优化发布构建：

```kotlin
android {
    buildTypes {
        release {
            isMinifyEnabled = true
            isShrinkResources = true
            isDebuggable = false
            
            // 优化配置
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
        
        debug {
            applicationIdSuffix = ".debug"
            isDebuggable = true
        }
    }
}
```

### 添加构建变体

```kotlin
android {
    flavorDimensions += listOf("version")
    productFlavors {
        create("free") {
            dimension = "version"
            applicationIdSuffix = ".free"
            versionNameSuffix = "-free"
        }
        create("pro") {
            dimension = "version"
            applicationIdSuffix = ".pro"
            versionNameSuffix = "-pro"
        }
    }
}
```

## 📊 APK 分析工具

构建完成后，您可以使用以下工具分析 APK：

### Android Studio APK Analyzer
1. 打开 Android Studio
2. 点击 `Build` → `Analyze APK`
3. 选择生成的 APK 文件
4. 查看文件大小、资源使用等

### 命令行工具
```bash
# 查看 APK 信息
aapt dump badging app-release.apk

# 查看文件大小详情
unzip -l app-release.apk | sort -nr -k 4
```

## 🚨 常见问题解决

### 问题1：构建失败 - 缺少依赖
**解决方案：**
```bash
# 清理并重新同步
.\gradlew.bat clean
# 重新同步项目
```

### 问题2：签名错误
**解决方案：**
- 检查 `keystore.properties` 文件路径
- 确认密钥库密码正确
- 重新生成密钥库

### 问题3：APK 文件过大
**解决方案：**
- 启用代码压缩：`isMinifyEnabled = true`
- 启用资源优化：`isShrinkResources = true`
- 移除未使用的资源

### 问题4：安装失败
**解决方案：**
- 检查 Android 版本兼容性
- 确认已启用"未知来源"安装
- 卸载旧版本再安装新版本

## 📱 安装指南

### 在 Android 设备上安装

1. **传输 APK 文件**
   - 通过 USB 连接设备
   - 将 APK 文件复制到设备存储
   - 或通过电子邮件、云存储分享

2. **安装 APK**
   - 在文件管理器中找到 APK 文件
   - 点击安装
   - 允许"未知来源"安装（如需要）
   - 完成安装

3. **首次运行**
   - 打开 MoodCalendar 应用
   - 授予必要的权限
   - 开始使用心情日记功能

### 在模拟器上安装

```bash
# 安装到连接的设备
adb install app-release.apk

# 安装到特定设备
adb -s <device_id> install app-release.apk

# 重新安装（覆盖）
adb install -r app-release.apk
```

## 🎯 构建优化建议

### 减小 APK 大小
1. **启用 ProGuard/R8**
2. **移除未使用的资源**
3. **使用 WebP 格式图片**
4. **启用资源压缩**

### 提高构建速度
1. **启用增量构建**
2. **使用构建缓存**
3. **配置 Gradle 守护进程**
4. **使用并行构建**

## 📞 技术支持

如果遇到构建问题：
1. 查看 Android Studio 的 Build 输出
2. 检查 Gradle 日志文件
3. 搜索相关错误信息
4. 在 GitHub Issues 中提问

## 🎉 完成！

成功构建 APK 后，您将获得：
- `app-release.apk` - 发布版本
- `app-debug.apk` - 调试版本（如配置）

现在您可以安装和测试 MoodCalendar 应用了！🚀