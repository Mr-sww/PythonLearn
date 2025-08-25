<template>
  <div>
    <h2 class="title">概览</h2>
    <div class="grid">
      <div class="card" v-for="k in 4" :key="k">
        <div class="num">--</div>
        <div class="desc">统计项 {{ k }}</div>
      </div>
    </div>
  </div>
</template>

<script>
export default { name: 'AdminHome' }
</script>

<style scoped>
.title { font-size: 20px; font-weight: 700; margin-bottom: 12px; }
.grid { display: grid; grid-template-columns: repeat(auto-fit, minmax(220px, 1fr)); gap: 16px; }
.card { background: #fff; border-radius: 10px; padding: 16px; box-shadow: 0 2px 8px rgba(0,0,0,0.06); }
.num { font-size: 28px; font-weight: 800; }
.desc { color: #64748b; }
</style>


