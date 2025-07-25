const { defineConfig } = require('@vue/cli-service')
module.exports = defineConfig({
  transpileDependencies: true,
  devServer: {
    host: '0.0.0.0', // 新增这一行，支持局域网访问
    port: 8081,
    proxy: {
      '/api': {
        target: 'http://localhost:8080', // Spring Boot后端端口
        changeOrigin: true
      },
      '/course_images': {
        target: 'http://localhost:8080', // 静态图片资源代理到后端
        changeOrigin: true
      }
    }
  }
})
