<template>
  <div class="course-card card h-100 shadow-sm border-0 hover-shadow transition-all">
    <!-- 课程封面 -->
    <div class="position-relative cursor-pointer" @click="handleViewCourse">
      <img 
        :src="getImageUrl(course.coverImage || course.imageUrl || course.ImageURL)" 
        :alt="course.title"
        class="card-img-top"
        style="height: 200px; object-fit: cover;"
        @error="handleImageError"
      >
      
      <!-- 标签显示 -->
      <div class="position-absolute top-0 end-0 p-2">
        <span v-for="tag in (course.tags ? course.tags.split(',') : [])" :key="tag" class="badge bg-primary me-1">
          {{ tag }}
        </span>
      </div>
      
      <!-- 课程时长和课时信息 -->
      <div class="position-absolute bottom-0 start-0 end-0 bg-dark bg-opacity-75 text-white p-2">
        <div class="d-flex justify-content-between align-items-center small">
          <span>
            <i class="fa fa-play-circle me-1"></i>{{ course.views || '0' }}播放
          </span>
          <span>
            <i class="fa fa-calendar me-1"></i>{{ formatDate(course.publicationDate) }}
          </span>
        </div>
      </div>
    </div>
    
    <!-- 课程内容 -->
    <div class="card-body d-flex flex-column">
      <!-- 评分 -->
      <div class="d-flex align-items-center mb-3 justify-content-end">
        <div class="text-warning">
          <i class="fa fa-star" v-for="i in 5" :key="i"></i>
        </div>
        <span class="text-muted ms-2 fw-bold">{{ course.rating || '4.5' }}</span>
      </div>
      
      <!-- 课程标题 -->
      <h5 class="card-title fw-bold text-dark mb-2">{{ course.title }}</h5>
      
      <!-- 课程描述 -->
      <div class="mb-3">
        <span v-for="tag in getTags(course.tags)" :key="tag" class="badge bg-light text-dark me-1 mb-1">{{ tag }}</span>
      </div>
      
      <!-- 课程信息 -->
      <div class="mt-auto">
        <div class="d-flex justify-content-between align-items-center mb-2">
          <small class="text-muted">
            <i class="fa fa-clock me-1"></i>{{ course.duration || '2小时' }}
          </small>
          <small class="text-muted">
            <i class="fa fa-book me-1"></i>{{ course.lessons || '10' }}课时
          </small>
        </div>
        
        <!-- 难度和价格 -->
        <div class="d-flex justify-content-between align-items-center">
          <span :class="getDifficultyClass(course.difficulty)" class="badge">
            {{ getDifficultyText(course.difficulty) }}
          </span>
          <span class="fw-bold text-primary">
            {{ course.price > 0 ? '¥' + course.price : '免费' }}
          </span>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
export default {
  name: 'CourseCard',
  props: {
    course: {
      type: Object,
      required: true
    }
  },
  methods: {
    // 统一图片路径处理
    getImageUrl(url) {
      if (!url) return 'https://picsum.photos/600/300?random=1';
      if (typeof url === 'string' && url.startsWith('/course_images/')) {
        return 'http://localhost:8080' + url;
      }
      return url;
    },
    
    // 格式化日期
    formatDate(dateString) {
      if (!dateString) return '';
      const date = new Date(dateString);
      return date.toLocaleDateString('zh-CN');
    },
    
    // 获取难度样式类
    getDifficultyClass(difficulty) {
      const classes = {
        'beginner': 'bg-success',
        'intermediate': 'bg-warning',
        'advanced': 'bg-danger'
      };
      return classes[difficulty] || classes.beginner;
    },
    
    // 获取难度文本
    getDifficultyText(difficulty) {
      const texts = {
        'beginner': '入门',
        'intermediate': '中级',
        'advanced': '高级'
      };
      return texts[difficulty] || texts.beginner;
    },
    
    // 处理标签
    getTags(tags) {
      if (typeof tags === 'string') {
        return tags.split(',').slice(0, 3); // 最多显示3个标签
      }
      return [];
    },
    
    // 格式化数字
    formatNumber(num) {
      if (num >= 10000) {
        return (num / 10000).toFixed(1) + '万';
      }
      return num.toString();
    },
    
    // 处理图片加载错误
    handleImageError(event) {
      event.target.src = 'https://picsum.photos/600/300?random=1';
    },
    
    // 查看课程详情
    handleViewCourse() {
      this.$emit('view-course', this.course);
      // 跳转到视频详情页或外链
      window.open(this.course.url, '_blank');
    },
    
    // 开始学习
    handleStartLearning() {
      this.$emit('start-learning', this.course);
      if (this.course.price === 0 || this.course.price === null) {
        this.$router.push(`/course/${this.course.id}`);
      } else {
        // 这里可以跳转到支付页面或显示支付弹窗
        this.$emit('purchase-course', this.course);
      }
    }
  }
}
</script>

<style scoped>
.course-card {
  transition: all 0.3s ease;
}

.course-card:hover {
  transform: translateY(-5px);
  box-shadow: 0 10px 25px rgba(0,0,0,0.1);
}

.hover-shadow:hover {
  box-shadow: 0 0.5rem 1rem rgba(0, 0, 0, 0.15) !important;
}

.cursor-pointer {
  cursor: pointer;
}

.card-title {
  display: -webkit-box;
  -webkit-line-clamp: 2;
  -webkit-box-orient: vertical;
  overflow: hidden;
  min-height: 2.5rem;
}
</style>
