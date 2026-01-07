# Material 3 (Material You) 升级说明

## 概述

本项目已成功升级到 Google Material 3 设计系统（也称为 Material You），提供了更现代化、更具个性化的用户界面体验。

## Material 3 的主要特性

### 1. 动态配色系统
- **自适应颜色**：Material 3 使用基于用户壁纸的动态配色方案
- **完整的色彩角色**：Primary、Secondary、Tertiary 及其容器颜色
- **深色模式支持**：完整的日间/夜间主题切换

### 2. 更新的组件样式
- **圆角设计**：更柔和的16dp圆角卡片
- **无阴影设计**：使用描边代替阴影，更加扁平化
- **Material Button**：支持 Filled、Tonal、Outlined 等多种样式
- **Material Card**：支持描边、圆角、背景色等丰富配置

### 3. 改进的排版系统
- **Material Typography**：使用 textAppearanceHeadlineMedium 等语义化样式
- **更好的可读性**：优化的字体大小和行高

## 已更新的文件

### 1. 颜色资源 (colors.xml)
```xml
<!-- Material 3 完整配色方案 -->
- md_theme_light_primary
- md_theme_light_onPrimary
- md_theme_light_primaryContainer
- md_theme_light_onPrimaryContainer
- md_theme_light_secondary
- md_theme_light_tertiary
- md_theme_light_error
- md_theme_light_background
- md_theme_light_surface
- md_theme_light_surfaceVariant
- md_theme_light_outline
... (以及对应的深色主题颜色)
```

### 2. 主题配置
- **values/themes.xml**: Material 3 浅色主题
- **values-night/themes.xml**: Material 3 深色主题
- 父主题从 `Theme.MaterialComponents` 更新为 `Theme.Material3.DayNight`

### 3. 布局文件

#### activity_main.xml
- 使用 `MaterialTextView` 替代 `TextView`
- 使用 `MaterialCardView` 包裹图片预览
- 使用 `MaterialButton` 替代普通 `Button`
- 使用 `MaterialRadioButton` 替代普通 `RadioButton`
- 使用 `CircularProgressIndicator` 替代 `ProgressBar`
- 添加图标和更好的视觉层次

#### activity_result.xml
- 使用 Material 3 组件
- 优化卡片样式和间距
- 添加图标到按钮

### 4. 代码更新
- **ResultActivity.kt**: 更新文本颜色使用 Material 主题颜色

## Material 3 组件使用示例

### 1. MaterialButton 样式

```xml
<!-- Filled Button (默认) -->
<com.google.android.material.button.MaterialButton
    style="@style/Widget.Material3.Button"
    android:text="开始识别" />

<!-- Tonal Button (柔和填充) -->
<com.google.android.material.button.MaterialButton
    style="@style/Widget.Material3.Button.TonalButton"
    android:text="选择图片" />

<!-- Outlined Button (描边) -->
<com.google.android.material.button.MaterialButton
    style="@style/Widget.Material3.Button.OutlinedButton"
    android:text="取消" />

<!-- Text Button (文本) -->
<com.google.android.material.button.MaterialButton
    style="@style/Widget.Material3.Button.TextButton"
    android:text="跳过" />
```

### 2. MaterialCardView 样式

```xml
<!-- 描边卡片 (推荐) -->
<com.google.android.material.card.MaterialCardView
    app:cardElevation="0dp"
    app:strokeWidth="1dp"
    app:strokeColor="?attr/colorOutline"
    app:cardCornerRadius="16dp">
    <!-- 内容 -->
</com.google.android.material.card.MaterialCardView>

<!-- 填充卡片 -->
<com.google.android.material.card.MaterialCardView
    app:cardElevation="0dp"
    app:cardBackgroundColor="?attr/colorSurfaceVariant"
    app:cardCornerRadius="12dp">
    <!-- 内容 -->
</com.google.android.material.card.MaterialCardView>
```

### 3. 文本样式

```xml
<!-- 标题 -->
<com.google.android.material.textview.MaterialTextView
    android:textAppearance="?attr/textAppearanceHeadlineMedium"
    android:text="标题" />

<!-- 正文 -->
<com.google.android.material.textview.MaterialTextView
    android:textAppearance="?attr/textAppearanceBodyLarge"
    android:text="正文内容" />

<!-- 标签 -->
<com.google.android.material.textview.MaterialTextView
    android:textAppearance="?attr/textAppearanceLabelMedium"
    android:text="标签" />
```

## 视觉改进对比

### 主界面改进
- ✅ 标题使用 Material 3 排版系统
- ✅ 图片预览使用圆角卡片包裹
- ✅ 按钮添加图标，使用 Tonal 和 Filled 样式
- ✅ 单选按钮组使用卡片背景突出显示
- ✅ 进度指示器使用 Material 3 样式

### 结果页面改进
- ✅ 使用描边卡片展示结果
- ✅ 优化文本颜色和大小
- ✅ 按钮添加图标
- ✅ 更好的间距和布局

## 颜色系统说明

### Primary (主色)
- **用途**：主要操作按钮、重要元素
- **浅色主题**：#6750A4 (紫色)
- **深色主题**：#D0BCFF (浅紫色)

### Secondary (次要色)
- **用途**：次要操作、辅助元素
- **浅色主题**：#625B71 (灰紫色)
- **深色主题**：#CCC2DC (浅灰紫色)

### Tertiary (第三色)
- **用途**：强调、对比元素
- **浅色主题**：#7D5260 (玫瑰色)
- **深色主题**：#EFB8C8 (浅玫瑰色)

### Surface (表面色)
- **用途**：卡片、对话框背景
- **SurfaceVariant**：用于次要表面

### Outline (轮廓色)
- **用途**：边框、分隔线

## 自定义颜色方案

如果想使用自己的品牌颜色，可以使用 [Material Theme Builder](https://m3.material.io/theme-builder) 生成完整的配色方案：

1. 访问 https://m3.material.io/theme-builder
2. 选择你的品牌主色
3. 导出 Android (Compose/Views) 配色方案
4. 替换 `colors.xml` 中的颜色值

## 兼容性说明

- **最低 SDK**: 24 (Android 7.0)
- **Material 库版本**: 1.13.0
- **完全向后兼容**：在不支持动态配色的设备上使用预定义颜色

## 参考资源

- [Material Design 3 官方文档](https://m3.material.io/)
- [Material Components for Android](https://github.com/material-components/material-components-android)
- [Material Theme Builder](https://m3.material.io/theme-builder)
- [Material Design 颜色系统](https://m3.material.io/styles/color/overview)

## 后续优化建议

1. **添加动态配色支持** (Android 12+)
   ```kotlin
   if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
       DynamicColors.applyToActivitiesIfAvailable(application)
   }
   ```

2. **使用 Motion 动画**
   - 添加共享元素转场动画
   - 使用 Material Motion 过渡效果

3. **优化无障碍支持**
   - 确保颜色对比度符合 WCAG 标准
   - 添加内容描述

4. **响应式布局**
   - 支持平板和折叠屏
   - 使用 Material 3 的自适应布局组件
