<template>
  <div class="course-card bg-white rounded-xl shadow-md overflow-hidden hover:shadow-lg transition-all duration-300 transform hover:-translate-y-1">
    <!-- 课程封面 -->
    <div class="relative cursor-pointer" @click="handleViewCourse">
      <img 
        :src="getImageUrl(course.imageUrl || course.ImageURL)" 
        :alt="course.title"
        class="w-full h-48 object-cover"
        @error="handleImageError"
      >
      
      <!-- 标签显示 -->
      <div class="absolute top-3 right-3 flex flex-wrap gap-1">
        <span v-for="tag in (course.tags ? course.tags.split(',') : [])" :key="tag" class="bg-blue-500 text-white text-xs font-bold px-2 py-1 rounded mr-1">
          {{ tag }}
        </span>
      </div>
      
      <!-- 课程时长和课时信息 -->
      <div class="absolute bottom-0 left-0 right-0 bg-black bg-opacity-75 text-white p-2">
        <div class="flex justify-between items-center text-sm">
          <span>
            <i class="fa fa-play-circle mr-1"></i>{{ course.playCount || '0' }}播放
          </span>
          <span>
            <i class="fa fa-calendar mr-1"></i>{{ course.publishDate || '' }}
          </span>
        </div>
      </div>
    </div>
    
    <!-- 课程内容 -->
    <div class="p-5">
      <!-- 讲师信息 -->
      <div class="flex items-center mb-3">
        <img 
          :src="course.authorAvatar || `https://picsum.photos/100/100?random=${course.id || Math.random()}`" 
          :alt="course.author"
          class="w-8 h-8 rounded-full mr-2 border"
          @error="handleAvatarError"
        >
        <span class="text-sm text-gray-600">{{ course.author || '未知讲师' }}</span>
        
        <!-- 评分 -->
        <div class="ml-auto flex items-center">
          <div class="flex text-yellow-400 text-sm">
            <i class="fa fa-star" v-for="i in 5" :key="i"></i>
          </div>
          <span class="text-sm text-gray-500 ml-1">{{ course.rating || '4.5' }}</span>
        </div>
      </div>
      
      <!-- 课程标题 -->
      <h3 class="text-lg font-bold text-gray-800 mb-2 line-clamp-2">{{ course.title }}</h3>
      
      <!-- 课程描述 -->
      <p class="text-gray-600 text-sm mb-4 line-clamp-2">{{ course.content || course.description || '暂无描述' }}</p>
      
      <!-- 底部信息 -->
      <div class="mt-auto">
        <div class="flex justify-between items-center mb-3">
          <div class="flex items-center text-sm text-gray-500">
            <i class="fa fa-folder mr-1"></i>
            <span>{{ (course.tags ? course.tags.split(',')[0] : '标签') }}</span>
          </div>
          <div class="text-right">
            <div class="text-lg font-bold text-blue-600" v-if="course.price === 0 || course.price === null">
              免费
            </div>
            <div class="text-lg font-bold text-red-600" v-else>
              ¥{{ course.price }}
            </div>
          </div>
        </div>
        
        <!-- 操作按钮已删除 -->
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
    
    // 获取难度样式类
    getDifficultyClass(difficulty) {
      const classes = {
        'beginner': 'bg-green-500 text-white',
        'intermediate': 'bg-yellow-500 text-white',
        'advanced': 'bg-red-500 text-white'
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
    
    // 处理头像加载错误
    handleAvatarError(event) {
      event.target.src = 'https://picsum.photos/100/100?random=1';
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
.line-clamp-2 {
  display: -webkit-box;
  -webkit-line-clamp: 2;
  -webkit-box-orient: vertical;
  overflow: hidden;
}

.course-card {
  transition: all 0.3s ease;
}

.course-card:hover {
  transform: translateY(-4px);
  box-shadow: 0 10px 25px rgba(0, 0, 0, 0.1);
}
</style>
