# 路由守卫实现说明

## 概述

实现了全局路由守卫机制，确保用户在访问需要认证的页面时已经登录，未登录用户会自动跳转到登录页面。

## 实现原理

### 1. 路由元信息配置

为每个路由添加了 `meta.requiresAuth` 属性：

```javascript
const routes = [
  { path: '/', component: HomePage, meta: { requiresAuth: true } },
  { path: '/auth', component: AuthPage, meta: { requiresAuth: false } },
  { path: '/courses', component: CourseCenter, meta: { requiresAuth: true } },
  // ... 其他需要认证的路由
]
```

### 2. 全局路由守卫

在 `router/index.js` 中实现了 `beforeEach` 守卫：

```javascript
router.beforeEach((to, from, next) => {
  // 检查路由是否需要认证
  if (to.meta.requiresAuth) {
    // 检查用户是否已登录
    const userId = localStorage.getItem('userId')
    if (!userId || userId === 'null') {
      // 未登录，跳转到登录页面
      next('/auth')
    } else {
      // 已登录，允许访问
      next()
    }
  } else {
    // 不需要认证的路由（如登录页面），直接允许访问
    next()
  }
})
```

## 工作流程

### 1. 用户访问页面
- 用户输入URL或点击链接访问任何页面
- 路由守卫被触发

### 2. 检查认证需求
- 检查目标路由的 `meta.requiresAuth` 属性
- 如果为 `true`，需要认证
- 如果为 `false`，不需要认证

### 3. 验证登录状态
- 检查 `localStorage` 中的 `userId`
- 如果存在且不为 `null`，认为已登录
- 如果不存在或为 `null`，认为未登录

### 4. 路由决策
- **已登录 + 需要认证**：允许访问目标页面
- **未登录 + 需要认证**：跳转到 `/auth` 登录页面
- **不需要认证**：直接允许访问（如登录页面）

## 优势

### 1. 自动化
- 无需在每个页面手动检查登录状态
- 统一的认证逻辑，减少代码重复

### 2. 安全性
- 确保所有需要认证的页面都受到保护
- 防止未登录用户访问敏感功能

### 3. 用户体验
- 未登录用户自动跳转到登录页面
- 登录成功后自动跳转到原目标页面

### 4. 维护性
- 集中管理认证逻辑
- 易于修改和扩展

## 配置说明

### 需要认证的路由
```javascript
{ path: '/', component: HomePage, meta: { requiresAuth: true } }
{ path: '/courses', component: CourseCenter, meta: { requiresAuth: true } }
{ path: '/learning', component: LearningCenter, meta: { requiresAuth: true } }
{ path: '/practice', component: PracticeCenter, meta: { requiresAuth: true } }
{ path: '/profile', component: ProfilePage, meta: { requiresAuth: true } }
{ path: '/ai', component: AIChatPage, meta: { requiresAuth: true } }
// ... 所有功能页面
```

### 不需要认证的路由
```javascript
{ path: '/auth', component: AuthPage, meta: { requiresAuth: false } }
```

## 登录状态管理

### 登录成功
```javascript
// 在AuthPage.vue中
localStorage.setItem('userId', this.user.userId)
EventBus.emit('user-logged-in')
this.$router.push('/')
```

### 退出登录
```javascript
// 在GlobalNavbar.vue中
localStorage.removeItem('userId')
// 路由守卫会自动跳转到登录页面
```

## 注意事项

1. **localStorage依赖**：登录状态依赖于localStorage中的userId
2. **EventBus事件**：登录成功后发送事件通知其他组件
3. **路由跳转**：登录成功后使用router.push跳转到目标页面
4. **错误处理**：确保在localStorage操作失败时有适当的错误处理

## 扩展建议

1. **Token认证**：可以扩展为使用JWT token进行认证
2. **权限控制**：可以添加基于角色的权限控制
3. **记住登录**：可以实现"记住我"功能
4. **自动刷新**：可以实现token自动刷新机制
5. **登录重定向**：可以记住用户原本要访问的页面，登录后跳转回去

