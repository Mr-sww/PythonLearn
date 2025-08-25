<template>
  <div>
    <h2 class="title">系统日志</h2>
    <div class="log placeholder">这里显示系统日志（占位）。</div>
  </div>
</template>

<script>
export default { name: 'AdminSystemLogs' }
</script>

<style scoped>
.title{ font-weight:700; margin-bottom:12px; }
.log{ background:#fff; border-radius:10px; padding:16px; box-shadow:0 2px 8px rgba(0,0,0,.06) }
.placeholder{ color:#64748b }
</style>


