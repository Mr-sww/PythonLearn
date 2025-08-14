<template>
  <div>
    <!-- 只在非AuthPage路由时显示导航栏 -->
    <GlobalNavbar v-if="!isAuthPage" />
    <router-view />
  </div>
</template>

<script>
import GlobalNavbar from './components/GlobalNavbar.vue'

export default {
  name: 'App',
  components: { GlobalNavbar },
  computed: {
    isAuthPage() {
      return this.$route.path === '/auth'
    }
  }
}
</script>

<style>
body {
  font-family: 'Inter', 'system-ui', 'sans-serif';
  background: #F8FAFC;
  color: #1E293B;
}
</style>
