<template>
  <div class="pagination">
    <button :disabled="currentPage === 1" @click="$emit('change', currentPage - 1)">上一页</button>
    <span>第 {{ currentPage }} / {{ totalPages }} 页</span>
    <button :disabled="currentPage === totalPages" @click="$emit('change', currentPage + 1)">下一页</button>
  </div>
</template>
<script>
export default {
  name: 'BasePagination',
  props: {
    total: { type: Number, required: true },
    pageSize: { type: Number, required: true },
    currentPage: { type: Number, required: true }
  },
  computed: {
    totalPages() {
      return Math.max(1, Math.ceil(this.total / this.pageSize));
    }
  }
}
</script>
<style scoped>
.pagination {
  display: flex;
  align-items: center;
  gap: 10px;
  margin-top: 16px;
}
</style> 