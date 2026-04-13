@echo off
echo 📱 MoodCalendar APK 构建脚本
echo ================================

cd /d "e:\MoodJournal"

if not exist "gradlew.bat" (
    echo ❌ 错误: 未找到 gradlew.bat 文件
    echo 请确保在项目根目录运行此脚本
    pause
    exit /b 1
)

echo 🔍 检查构建环境...

if not exist "app\src\main\AndroidManifest.xml" (
    echo ❌ 错误: 项目结构不完整
    echo 请检查 AndroidManifest.xml 是否存在
    pause
    exit /b 1
)

echo ✅ 项目结构检查通过

echo.
echo 🔨 开始清理项目...
.\gradlew.bat clean

if %errorlevel% neq 0 (
    echo ❌ 清理失败
    pause
    exit /b 1
)

echo ✅ 项目清理完成

echo.
echo 🔨 开始构建调试版 APK...
.\gradlew.bat assembleDebug

if %errorlevel% equ 0 (
    echo.
    echo ✅ 调试版 APK 构建成功！
    echo 📁 APK 位置: app\build\outputs\apk\debug\
    echo 📱 文件: app-debug.apk
    echo.
    echo 💡 调试版 APK 可以直接安装测试
) else (
    echo ❌ 调试版 APK 构建失败
    pause
    exit /b 1
)

echo.
echo 🔨 开始构建发布版 APK...
.\gradlew.bat assembleRelease

if %errorlevel% equ 0 (
    echo.
    echo ✅ 发布版 APK 构建成功！
    echo 📁 APK 位置: app\build\outputs\apk\release\
    echo 📱 文件: app-release.apk
    echo.
    echo 💡 发布版 APK 需要签名后才能安装
) else (
    echo ❌ 发布版 APK 构建失败
    echo 💡 这可能是正常的，因为缺少签名配置
)

echo.
echo 📊 构建完成总结:
echo =================
if exist "app\build\outputs\apk\debug\app-debug.apk" (
    for %%I in ("app\build\outputs\apk\debug\app-debug.apk") do (
        echo 📱 调试版 APK: %%~fI (%%~zI 字节)
    )
)

if exist "app\build\outputs\apk\release\app-release.apk" (
    for %%I in ("app\build\outputs\apk\release\app-release.apk") do (
        echo 📱 发布版 APK: %%~fI (%%~zI 字节)
    )
)

echo.
echo 💡 下一步操作:
echo 1. 使用调试版 APK 进行测试安装
echo 2. 配置签名信息以生成可发布的 APK
echo 3. 查看 APK_BUILD_GUIDE.md 获取详细指导

echo.
pause