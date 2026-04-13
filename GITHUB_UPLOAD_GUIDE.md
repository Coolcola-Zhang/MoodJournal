# 📤 GitHub 上传指南

本文档将指导您如何将 MoodCalendar 项目上传到 GitHub。

## 🚀 快速开始（推荐）

### 方法一：使用 GitHub Desktop（最简单）

1. **下载 GitHub Desktop**
   - 访问 [GitHub Desktop 官网](https://desktop.github.com/)
   - 下载并安装

2. **登录 GitHub 账户**
   - 打开 GitHub Desktop
   - 登录您的 GitHub 账户

3. **创建新仓库**
   - 点击 "File" → "New repository"
   - 填写仓库信息：
     - Name: `MoodCalendar`
     - Description: `一个基于 Android Jetpack Compose 的现代化心情日记应用`
     - Local path: `e:\MoodJournal`
     - 选择 "Initialize this repository with a README"

4. **发布到 GitHub**
   - 点击 "Publish repository"
   - 选择公开或私有
   - 确认发布

### 方法二：使用命令行

1. **初始化 Git 仓库**
```bash
cd e:\MoodJournal
git init
git add .
git commit -m "初始提交: 完整的 MoodCalendar 心情日记应用"
```

2. **连接到 GitHub**
```bash
git remote add origin https://github.com/你的用户名/MoodCalendar.git
git branch -M main
git push -u origin main
```

## 📋 上传前的检查清单

### ✅ 确保文件完整
- [x] 所有源代码文件存在
- [x] README.md 文档完整
- [x] .gitignore 配置正确
- [x] LICENSE 许可证文件

### ✅ 项目结构验证
```
MoodCalendar/
├── README.md
├── LICENSE
├── .gitignore
├── app/
├── build.gradle.kts
├── settings.gradle.kts
├── gradle.properties
└── gradlew.bat
```

## 🔧 详细步骤

### 步骤 1：准备 GitHub 账户

1. 如果没有 GitHub 账户，请先注册：https://github.com/signup
2. 验证邮箱地址

### 步骤 2：创建新仓库

1. 登录 GitHub
2. 点击右上角 "+" → "New repository"
3. 填写仓库信息：
   - **Owner**: 您的用户名
   - **Repository name**: `MoodCalendar`
   - **Description**: `一个基于 Android Jetpack Compose 的现代化心情日记应用`
   - **Public/Private**: 建议选择 Public
   - **Initialize this repository with**: 不要勾选任何选项（因为本地已有文件）

### 步骤 3：上传项目

#### 使用 GitHub Desktop（推荐）

1. **打开 GitHub Desktop**
2. **添加本地仓库**
   - 点击 "File" → "Add Local Repository"
   - 选择 `e:\MoodJournal` 文件夹
   - 点击 "Add Repository"

3. **提交更改**
   - 在左侧看到所有文件变更
   - 填写提交信息：`初始提交: 完整的 MoodCalendar 心情日记应用`
   - 点击 "Commit to main"

4. **发布到 GitHub**
   - 点击 "Publish repository"
   - 确认仓库信息
   - 点击 "Publish Repository"

#### 使用命令行

1. **打开命令提示符或 PowerShell**
2. **导航到项目目录**
```bash
cd e:\MoodJournal
```

3. **初始化 Git**
```bash
git init
git add .
git commit -m "初始提交: 完整的 MoodCalendar 心情日记应用"
```

4. **连接到远程仓库**
```bash
git remote add origin https://github.com/你的用户名/MoodCalendar.git
git branch -M main
git push -u origin main
```

### 步骤 4：验证上传

1. 访问您的 GitHub 仓库：`https://github.com/你的用户名/MoodCalendar`
2. 确认所有文件都已上传
3. 检查 README.md 显示正常

## 🎯 上传后的操作

### 1. 设置仓库信息
- 添加项目描述
- 设置主题标签
- 添加项目网站（如果有）

### 2. 配置 GitHub Pages（可选）
如果您想展示 Web 演示版：
1. 进入仓库 Settings
2. 找到 Pages 选项
3. 选择 main 分支
4. 保存设置

### 3. 邀请协作者（可选）
1. 进入仓库 Settings
2. 选择 Collaborators
3. 添加协作者用户名

## 🔍 常见问题

### Q: 上传时遇到认证错误？
A: 需要配置 Git 认证：
```bash
git config --global user.name "你的用户名"
git config --global user.email "你的邮箱"
```

### Q: 文件太大上传失败？
A: 检查 .gitignore 文件，确保没有上传构建文件

### Q: 如何更新项目？
A: 使用以下命令：
```bash
git add .
git commit -m "更新描述"
git push
```

## 📞 获取帮助

如果遇到问题：
1. 查看 [GitHub 官方文档](https://docs.github.com/)
2. 搜索相关错误信息
3. 在 GitHub 社区提问

## 🎉 完成！

成功上传后，您的项目将可以在以下地址访问：
`https://github.com/你的用户名/MoodCalendar`

祝您上传顺利！🚀