<template>
  <div class="function-entries">
    <div v-for="group in filteredGroups" :key="group.group" class="entry-group">
      <h4 class="group-title">{{ group.group }}</h4>
      <div class="entry-grid">
        <div
          v-for="entry in group.entries"
          :key="entry.key"
          class="entry-card"
          @click="navigateTo(entry.path)"
          :style="{ '--card-color': entry.color }"
        >
          <div class="icon-wrapper">
            <i :class="entry.icon"></i>
          </div>
          <div class="entry-content">
            <div class="entry-title">{{ entry.title }}</div>
            <div class="entry-desc">{{ entry.desc }}</div>
          </div>
          <div class="entry-arrow">
            <i class="fas fa-chevron-right"></i>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
import entryConfig from '@/views/profile/entryConfig'

export default {
  name: 'FunctionEntries',
  props: {
    userRole: {
      type: String,
      required: true
    }
  },
  computed: {
    filteredGroups() {
      // 只展示当前用户有权限的入口
      return entryConfig
        .map(group => ({
          group: group.group,
          entries: group.entries.filter(entry => 
            entry.roles.includes(this.userRole)
          )
        }))
        .filter(group => group.entries.length > 0)
    }
  },
  methods: {
    navigateTo(path) {
      this.$router.push(path)
    }
  }
}
</script>

<style scoped>
.function-entries {
  margin-top: 30px;
}

.entry-group {
  margin-bottom: 40px;
}

.group-title {
  font-size: 18px;
  font-weight: 600;
  color: #333;
  margin-bottom: 20px;
  padding-bottom: 8px;
  border-bottom: 2px solid #f0f0f0;
}

.entry-grid {
  display: grid;
  grid-template-columns: repeat(auto-fit, minmax(280px, 1fr));
  gap: 20px;
}

.entry-card {
  background: white;
  border-radius: 12px;
  padding: 24px;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.06);
  cursor: pointer;
  transition: all 0.3s ease;
  position: relative;
  overflow: hidden;
  display: flex;
  align-items: center;
  gap: 16px;
}

.entry-card::before {
  content: '';
  position: absolute;
  top: 0;
  left: 0;
  width: 4px;
  height: 100%;
  background: var(--card-color);
  transition: width 0.3s ease;
}

.entry-card:hover {
  transform: translateY(-4px);
  box-shadow: 0 8px 24px rgba(0, 0, 0, 0.12);
}

.entry-card:hover::before {
  width: 8px;
}

.icon-wrapper {
  width: 50px;
  height: 50px;
  border-radius: 12px;
  background: var(--card-color);
  display: flex;
  align-items: center;
  justify-content: center;
  flex-shrink: 0;
}

.icon-wrapper i {
  color: white;
  font-size: 20px;
}

.entry-content {
  flex: 1;
  min-width: 0;
}

.entry-title {
  font-size: 16px;
  font-weight: 600;
  color: #333;
  margin-bottom: 4px;
  line-height: 1.4;
}

.entry-desc {
  font-size: 13px;
  color: #666;
  line-height: 1.4;
}

.entry-arrow {
  color: #ccc;
  transition: color 0.3s ease;
}

.entry-card:hover .entry-arrow {
  color: var(--card-color);
}

/* 响应式设计 */
@media (max-width: 768px) {
  .entry-grid {
    grid-template-columns: 1fr;
  }
  
  .entry-card {
    padding: 20px;
  }
  
  .icon-wrapper {
    width: 40px;
    height: 40px;
  }
  
  .icon-wrapper i {
    font-size: 16px;
  }
  
  .entry-title {
    font-size: 15px;
  }
  
  .entry-desc {
    font-size: 12px;
  }
}
</style> 