<template>
  <div class="learning-center">
    <div class="sidebar">
      <h3>知识点目录</h3>
      <ul>
        <li v-for="item in catalog" :key="item.id" @click="selectKnowledge(item)" :class="{active: selectedKnowledge && selectedKnowledge.id === item.id}">
          {{ item.title }}
        </li>
      </ul>
    </div>
    <div class="main-content">
      <div class="knowledge-section card">
        <h2 class="knowledge-title">{{ selectedKnowledge ? selectedKnowledge.title : '知识点' }}</h2>
        <div v-if="loading" class="loading">加载中...</div>
        <div v-else-if="selectedKnowledge">
          <div class="knowledge-content" v-html="selectedKnowledge.content"></div>
        </div>
        <div v-else>
          <p class="text-muted">请选择左侧知识点</p>
        </div>
      </div>
      <div class="problem-list-section card">
        <h3>题目列表</h3>
        <div v-if="problems.length === 0" class="text-muted">暂无题目</div>
        <div v-else class="problem-card-list">
          <div v-for="prob in problems" :key="prob.id" class="problem-card">
            <div class="problem-title">{{ prob.title }}</div>
            <button class="go-btn" @click="$router.push(`/problem/${prob.id}`)">
              去做题
            </button>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
import axios from 'axios';
export default {
  name: 'LearnDetial',
  data() {
    return {
      catalog: [],
      selectedKnowledge: null,
      problems: [],
      loading: false,
      point: {}
    }
  },
  mounted() {
    this.fetchCatalog();
    const title = this.$route.query.id;
    if (title) {
      this.selectKnowledgeByTitle(title);
    }
  },
  watch: {
    '$route.query.id': {
      immediate: true,
      handler(newId) {
        if (newId) {
          this.selectKnowledgeByTitle(newId);
        }
      }
    }
  },
  methods: {
    async fetchCatalog() {
      this.loading = true;
      try {
        const res = await axios.get('/api/knowledge/catalog');
        this.catalog = res.data;
      } finally {
        this.loading = false;
      }
    },
    async selectKnowledge(item) {
      this.loading = true;
      try {
        const res = await axios.get(`/api/knowledge/${item.id}/detail`);
        this.selectedKnowledge = res.data.knowledge;
        this.problems = res.data.problems;
      } finally {
        this.loading = false;
      }
    },
    async selectKnowledgeByTitle(title) {
      if (!this.catalog.length) {
        await this.fetchCatalog();
      }
      let item = this.catalog.find(k => k.title === title);
      if (item) {
        await this.selectKnowledge(item);
      }
    },
    async fetchKnowledgePoint(title) {
      this.loading = true;
      try {
        const res = await axios.get(`/api/knowledge/point?title=${encodeURIComponent(title)}`);
        this.point = res.data;
        if (this.point.question) {
          const ids = this.point.question.split(',').map(q => q.trim());
          const probRes = await axios.get(`/api/problems?ids=${ids.join(',')}`);
          this.problems = probRes.data;
        }
      } finally {
        this.loading = false;
      }
    }
  }
}
</script>

<style scoped>
.learning-center {
  display: flex;
  justify-content: center;
  background: #f6f8fa;
  min-height: 100vh;
  padding: 32px 0;
}

.sidebar {
  width: 260px;
  background: #fff;
  border-radius: 14px;
  box-shadow: 0 2px 16px rgba(37,99,235,0.06);
  margin-right: 32px;
  padding: 28px 18px 18px 18px;
  min-height: 600px;
  display: flex;
  flex-direction: column;
  align-items: flex-start;
}

.sidebar h3 {
  font-size: 20px;
  font-weight: bold;
  margin-bottom: 18px;
  color: #2563eb;
}

.sidebar ul {
  list-style: none;
  padding: 0;
  width: 100%;
}

.sidebar li {
  padding: 12px 10px;
  border-radius: 8px;
  margin-bottom: 6px;
  cursor: pointer;
  transition: background 0.2s, color 0.2s;
  font-size: 16px;
  color: #22223b;
}

.sidebar li:hover {
  background: #e3eafe;
  color: #2563eb;
}

.sidebar li.active {
  background: linear-gradient(90deg, #2563eb 0%, #60a5fa 100%);
  color: #fff;
  font-weight: bold;
}

.main-content {
  flex: 1;
  max-width: 900px;
  width: 100%;
  display: flex;
  flex-direction: column;
  gap: 28px;
}

.card {
  background: #fff;
  border-radius: 14px;
  box-shadow: 0 2px 16px rgba(37,99,235,0.06);
  padding: 32px 36px;
  margin-bottom: 0;
}

.knowledge-section {
  margin-bottom: 0;
}

.knowledge-title {
  font-size: 24px;
  font-weight: bold;
  color: #2563eb;
  margin-bottom: 18px;
}

.knowledge-content {
  font-size: 16px;
  color: #22223b;
  line-height: 1.8;
  word-break: break-all;
  background: #f8fafd;
  border-radius: 8px;
  padding: 18px 20px;
  min-height: 120px;
  box-shadow: 0 1px 4px #e0e0e0;
}

.problem-list-section {
  margin-top: 0;
}

.problem-card-list {
  display: flex;
  flex-wrap: wrap;
  gap: 18px;
  margin-top: 12px;
}

.problem-card {
  background: #f8fafd;
  border-radius: 10px;
  box-shadow: 0 1px 6px #e0e0e0;
  padding: 18px 24px;
  min-width: 220px;
  display: flex;
  align-items: center;
  justify-content: space-between;
  transition: box-shadow 0.2s, background 0.2s;
}

.problem-card:hover {
  box-shadow: 0 6px 18px #d0d7e2;
  background: #e3eafe;
}

.problem-title {
  font-weight: bold;
  color: #2563eb;
  font-size: 16px;
  flex: 1;
  margin-right: 12px;
  text-align: left;
}

.go-btn {
  background: linear-gradient(90deg, #2563eb 0%, #60a5fa 100%);
  color: #fff;
  border: none;
  border-radius: 20px;
  padding: 6px 18px;
  font-size: 14px;
  font-weight: 500;
  cursor: pointer;
  transition: background 0.2s;
  box-shadow: 0 2px 8px rgba(37,99,235,0.08);
}

.go-btn:hover {
  background: linear-gradient(90deg, #1746a0 0%, #2563eb 100%);
}

.text-muted {
  color: #6b7280;
  font-size: 15px;
  margin-top: 10px;
}

.loading {
  color: #2563eb;
  font-size: 16px;
  padding: 20px 0;
  text-align: center;
}
</style> 