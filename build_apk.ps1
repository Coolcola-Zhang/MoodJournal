# MoodCalendar APK 构建脚本
Write-Host "📱 MoodCalendar APK 构建脚本" -ForegroundColor Green
Write-Host "========================" -ForegroundColor Green

Set-Location "e:\MoodJournal"

if (-not (Test-Path "gradlew.bat")) {
    Write-Host "❌ 错误: 未找到 gradlew.bat 文件" -ForegroundColor Red
    Write-Host "请确保在项目根目录运行此脚本" -ForegroundColor Yellow
    exit 1
}

Write-Host "🔍 检查构建环境..." -ForegroundColor Yellow

if (-not (Test-Path "app\src\main\AndroidManifest.xml")) {
    Write-Host "❌ 错误: 项目结构不完整" -ForegroundColor Red
    Write-Host "请检查 AndroidManifest.xml 是否存在" -ForegroundColor Yellow
    exit 1
}

Write-Host "✅ 项目结构检查通过" -ForegroundColor Green

Write-Host ""
Write-Host "🔨 开始清理项目..." -ForegroundColor Yellow
.\gradlew.bat clean

if ($LASTEXITCODE -ne 0) {
    Write-Host "❌ 清理失败" -ForegroundColor Red
    exit 1
}

Write-Host "✅ 项目清理完成" -ForegroundColor Green

Write-Host ""
Write-Host "🔨 开始构建调试版 APK..." -ForegroundColor Yellow
.\gradlew.bat assembleDebug

if ($LASTEXITCODE -eq 0) {
    Write-Host ""
    Write-Host "✅ 调试版 APK 构建成功！" -ForegroundColor Green
    Write-Host "📁 APK 位置: app\build\outputs\apk\debug\" -ForegroundColor Cyan
    Write-Host "📱 文件: app-debug.apk" -ForegroundColor Cyan
    Write-Host ""
    Write-Host "💡 调试版 APK 可以直接安装测试" -ForegroundColor Yellow
} else {
    Write-Host "❌ 调试版 APK 构建失败" -ForegroundColor Red
    exit 1
}

Write-Host ""
Write-Host "🔨 开始构建发布版 APK..." -ForegroundColor Yellow
.\gradlew.bat assembleRelease

if ($LASTEXITCODE -eq 0) {
    Write-Host ""
    Write-Host "✅ 发布版 APK 构建成功！" -ForegroundColor Green
    Write-Host "📁 APK 位置: app\build\outputs\apk\release\" -ForegroundColor Cyan
    Write-Host "📱 文件: app-release.apk" -ForegroundColor Cyan
    Write-Host ""
    Write-Host "💡 发布版 APK 需要签名后才能安装" -ForegroundColor Yellow
} else {
    Write-Host "❌ 发布版 APK 构建失败" -ForegroundColor Red
    Write-Host "💡 这可能是正常的，因为缺少签名配置" -ForegroundColor Yellow
}

Write-Host ""
Write-Host "📊 构建完成总结:" -ForegroundColor Magenta
Write-Host "=================" -ForegroundColor Magenta

if (Test-Path "app\build\outputs\apk\debug\app-debug.apk") {
    $debugApk = Get-Item "app\build\outputs\apk\debug\app-debug.apk"
    Write-Host "📱 调试版 APK: $($debugApk.FullName) ($($debugApk.Length) 字节)" -ForegroundColor Cyan
}

if (Test-Path "app\build\outputs\apk\release\app-release.apk") {
    $releaseApk = Get-Item "app\build\outputs\apk\release\app-release.apk"
    Write-Host "📱 发布版 APK: $($releaseApk.FullName) ($($releaseApk.Length) 字节)" -ForegroundColor Cyan
}

Write-Host ""
Write-Host "💡 下一步操作:" -ForegroundColor Magenta
Write-Host "1. 使用调试版 APK 进行测试安装" -ForegroundColor Yellow
Write-Host "2. 配置签名信息以生成可发布的 APK" -ForegroundColor Yellow
Write-Host "3. 查看 APK_BUILD_GUIDE.md 获取详细指导" -ForegroundColor Yellow

Write-Host ""
Read-Host "按 Enter 键退出"