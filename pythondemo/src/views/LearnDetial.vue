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
      <div class="knowledge-section">
        <h3>知识点</h3>
        <div v-if="loading" class="loading">加载中...</div>
        <div v-else-if="selectedKnowledge">
          <h4>{{ selectedKnowledge.title }}</h4>
          <div v-html="selectedKnowledge.content"></div>
        </div>
        <div v-else>
          <p>请选择左侧知识点</p>
        </div>
      </div>
      <div class="problem-list-section">
        <h3>题目列表</h3>
        <div v-for="prob in problems" :key="prob.Id">
          <button @click="$router.push(`/problem/${prob.Id}`)">去做题：{{ prob.Title }}</button>
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
  min-height: 600px;
}
.sidebar {
  width: 250px;
  border: 1px solid #ccc;
  margin-right: 20px;
  padding: 20px;
  background: #fafafa;
  border-radius: 8px;
}
.sidebar ul {
  list-style: none;
  padding: 0;
}
.sidebar li {
  padding: 12px 0;
  cursor: pointer;
  border-bottom: 1px solid #eee;
  transition: background 0.2s, color 0.2s;
}
.sidebar li:hover {
  background: #f0f7ff;
  color: #1976d2;
}
.sidebar li.active {
  color: #fff;
  background: #1976d2;
  font-weight: bold;
}
.main-content {
  flex: 1;
  display: flex;
  flex-direction: column;
  border: 1px solid #ccc;
  background: #fff;
  border-radius: 8px;
}
.knowledge-section {
  flex: 2;
  padding: 24px;
  border-bottom: 1px solid #eee;
  background: #f9f9fb;
  border-radius: 8px 8px 0 0;
  margin-bottom: 0;
  box-shadow: 0 2px 8px #f0f1f2;
  min-height: 180px;
}
.loading {
  color: #1976d2;
  font-size: 16px;
  padding: 20px 0;
}
.problem-list-section {
  flex: 1;
  padding: 24px;
  background: #f6fafd;
  border-radius: 0 0 8px 8px;
}
.problem-list-section ul {
  padding: 0;
  margin: 0;
  list-style: none;
}
.problem-list-section li {
  background: #fff;
  margin-bottom: 12px;
  padding: 16px;
  border-radius: 6px;
  box-shadow: 0 1px 4px #e0e0e0;
  transition: box-shadow 0.2s, background 0.2s;
}
.problem-list-section li:hover {
  box-shadow: 0 4px 12px #d0d7e2;
  background: #f5faff;
}
</style> 