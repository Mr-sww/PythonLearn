<template>
  <div class="course-video-page">
    <div class="container-fluid">
      <!-- 页面标题 -->
      <div class="row mb-4">
        <div class="col-12">
          <nav aria-label="breadcrumb">
            <ol class="breadcrumb">
              <li class="breadcrumb-item">
                <router-link to="/courses">课程中心</router-link>
              </li>
              <li class="breadcrumb-item">
                <router-link :to="`/courses/${courseId}`">{{ courseTitle }}</router-link>
              </li>
              <li class="breadcrumb-item active" aria-current="page">{{ videoTitle }}</li>
            </ol>
          </nav>
          <h2 class="fw-bold text-dark">
            <i class="fa fa-play-circle me-2"></i>{{ videoTitle }}
          </h2>
        </div>
      </div>

      <div class="row">
        <!-- 视频播放区域 -->
        <div class="col-lg-8">
          <div class="card border-0 shadow-sm">
            <div class="card-body p-0">
              <VideoPlayer
                :video-id="videoId"
                :video-title="videoTitle"
                :video-url="videoUrl"
                :poster="videoPoster"
                :total-duration="videoDuration"
                @video-completed="onVideoCompleted"
              />
            </div>
          </div>

          <!-- 视频描述 -->
          <div class="card border-0 shadow-sm mt-4">
            <div class="card-header bg-white">
              <h5 class="mb-0">
                <i class="fa fa-info-circle me-2"></i>视频描述
              </h5>
            </div>
            <div class="card-body">
              <p class="text-muted">{{ videoDescription || '暂无描述' }}</p>
            </div>
          </div>
        </div>

        <!-- 侧边栏 -->
        <div class="col-lg-4">
          <!-- 课程信息 -->
          <div class="card border-0 shadow-sm mb-4">
            <div class="card-header bg-white">
              <h5 class="mb-0">
                <i class="fa fa-book me-2"></i>课程信息
              </h5>
            </div>
            <div class="card-body">
              <div class="course-info">
                <h6 class="fw-bold">{{ courseTitle }}</h6>
                <p class="text-muted small mb-3">{{ courseDescription }}</p>
                <div class="course-stats">
                  <div class="row text-center">
                    <div class="col-4">
                      <div class="stat-item">
                        <div class="stat-number">{{ courseStats.totalVideos || 0 }}</div>
                        <div class="stat-label">视频数量</div>
                      </div>
                    </div>
                    <div class="col-4">
                      <div class="stat-item">
                        <div class="stat-number">{{ courseStats.completedVideos || 0 }}</div>
                        <div class="stat-label">已完成</div>
                      </div>
                    </div>
                    <div class="col-4">
                      <div class="stat-item">
                        <div class="stat-number">{{ courseStats.totalDuration || 0 }}分钟</div>
                        <div class="stat-label">总时长</div>
                      </div>
                    </div>
                  </div>
                </div>
              </div>
            </div>
          </div>

          <!-- 视频列表 -->
          <div class="card border-0 shadow-sm">
            <div class="card-header bg-white">
              <h5 class="mb-0">
                <i class="fa fa-list me-2"></i>视频列表
              </h5>
            </div>
            <div class="card-body p-0">
              <div class="video-list">
                <div
                  v-for="video in videoList"
                  :key="video.id"
                  class="video-item"
                  :class="{ active: video.id == videoId }"
                  @click="selectVideo(video)"
                >
                  <div class="video-thumbnail">
                    <img :src="video.thumbnail || '/default-video-thumb.jpg'" :alt="video.title">
                    <div class="video-duration">{{ formatDuration(video.duration) }}</div>
                    <div v-if="video.status === 'completed'" class="video-completed">
                      <i class="fa fa-check-circle"></i>
                    </div>
                  </div>
                  <div class="video-info">
                    <h6 class="video-title">{{ video.title }}</h6>
                    <div class="video-meta">
                      <span class="video-status" :class="getStatusClass(video.status)">
                        {{ getStatusText(video.status) }}
                      </span>
                    </div>
                  </div>
                </div>
              </div>
            </div>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
import VideoPlayer from '@/components/VideoPlayer.vue'

export default {
  name: 'CourseVideo',
  components: {
    VideoPlayer
  },
  data() {
    return {
      courseId: null,
      videoId: null,
      courseTitle: '课程标题',
      videoTitle: '视频标题',
      videoUrl: '',
      videoPoster: '',
      videoDuration: 0,
      videoDescription: '',
      courseDescription: '',
      courseStats: {
        totalVideos: 0,
        completedVideos: 0,
        totalDuration: 0
      },
      videoList: []
    }
  },
  mounted() {
    this.initializeData()
  },
  methods: {
    async initializeData() {
      // 从路由参数获取课程ID和视频ID
      this.courseId = this.$route.params.courseId
      this.videoId = this.$route.params.videoId
      
      // 加载课程和视频数据
      await this.loadCourseData()
      await this.loadVideoData()
      await this.loadVideoList()
    },

    async loadCourseData() {
      try {
        // 这里应该调用课程API获取课程信息
        // const courseData = await courseService.getCourse(this.courseId)
        // this.courseTitle = courseData.title
        // this.courseDescription = courseData.description
        
        // 临时数据
        this.courseTitle = 'Python基础教程'
        this.courseDescription = '从零开始学习Python编程语言，掌握基础语法和常用库的使用。'
      } catch (error) {
        console.error('加载课程数据失败:', error)
      }
    },

    async loadVideoData() {
      try {
        // 这里应该调用视频API获取视频信息
        // const videoData = await videoService.getVideo(this.videoId)
        // this.videoTitle = videoData.title
        // this.videoUrl = videoData.url
        // this.videoPoster = videoData.poster
        // this.videoDuration = videoData.duration
        // this.videoDescription = videoData.description
        
        // 临时数据
        this.videoTitle = 'Python基础语法'
        this.videoUrl = 'https://sample-videos.com/zip/10/mp4/SampleVideo_1280x720_1mb.mp4'
        this.videoPoster = '/default-video-poster.jpg'
        this.videoDuration = 1800 // 30分钟
        this.videoDescription = '学习Python的基本语法，包括变量、数据类型、控制结构等。'
      } catch (error) {
        console.error('加载视频数据失败:', error)
      }
    },

    async loadVideoList() {
      try {
        // 这里应该调用API获取课程的视频列表
        // this.videoList = await courseService.getCourseVideos(this.courseId)
        
        // 临时数据
        this.videoList = [
          {
            id: 1,
            title: 'Python基础语法',
            duration: 1800,
            thumbnail: '/video-thumb-1.jpg',
            status: 'completed'
          },
          {
            id: 2,
            title: 'Python数据类型',
            duration: 2400,
            thumbnail: '/video-thumb-2.jpg',
            status: 'in_progress'
          },
          {
            id: 3,
            title: 'Python控制结构',
            duration: 2100,
            thumbnail: '/video-thumb-3.jpg',
            status: 'not_started'
          }
        ]
        
        // 计算课程统计信息
        this.calculateCourseStats()
      } catch (error) {
        console.error('加载视频列表失败:', error)
      }
    },

    calculateCourseStats() {
      this.courseStats = {
        totalVideos: this.videoList.length,
        completedVideos: this.videoList.filter(v => v.status === 'completed').length,
        totalDuration: Math.round(this.videoList.reduce((sum, v) => sum + v.duration, 0) / 60)
      }
    },

    selectVideo(video) {
      if (video.id !== this.videoId) {
        this.$router.push(`/courses/${this.courseId}/videos/${video.id}`)
      }
    },

    onVideoCompleted(data) {
      console.log('视频观看完成:', data)
      // 更新视频列表中的状态
      const video = this.videoList.find(v => v.id == this.videoId)
      if (video) {
        video.status = 'completed'
        this.calculateCourseStats()
      }
      
      // 显示完成提示
      this.$toast?.success?.('恭喜！视频观看完成') || alert('恭喜！视频观看完成')
    },

    formatDuration(seconds) {
      if (!seconds || isNaN(seconds)) return '00:00'
      
      const hours = Math.floor(seconds / 3600)
      const minutes = Math.floor((seconds % 3600) / 60)
      const secs = Math.floor(seconds % 60)
      
      if (hours > 0) {
        return `${hours}:${minutes.toString().padStart(2, '0')}:${secs.toString().padStart(2, '0')}`
      } else {
        return `${minutes}:${secs.toString().padStart(2, '0')}`
      }
    },

    getStatusClass(status) {
      switch (status) {
        case 'completed': return 'status-completed'
        case 'in_progress': return 'status-progress'
        default: return 'status-not-started'
      }
    },

    getStatusText(status) {
      switch (status) {
        case 'completed': return '已完成'
        case 'in_progress': return '学习中'
        default: return '未开始'
      }
    }
  }
}
</script>

<style scoped>
.course-video-page {
  padding: 20px 0;
}

.breadcrumb {
  background: transparent;
  padding: 0;
  margin-bottom: 16px;
}

.breadcrumb-item a {
  color: #007bff;
  text-decoration: none;
}

.breadcrumb-item.active {
  color: #6c757d;
}

.course-info .stat-item {
  padding: 8px 0;
}

.stat-number {
  font-size: 18px;
  font-weight: bold;
  color: #007bff;
}

.stat-label {
  font-size: 12px;
  color: #6c757d;
  margin-top: 4px;
}

.video-list {
  max-height: 500px;
  overflow-y: auto;
}

.video-item {
  display: flex;
  padding: 12px;
  border-bottom: 1px solid #f0f0f0;
  cursor: pointer;
  transition: background-color 0.2s;
}

.video-item:hover {
  background-color: #f8f9fa;
}

.video-item.active {
  background-color: #e3f2fd;
  border-left: 3px solid #007bff;
}

.video-thumbnail {
  position: relative;
  width: 80px;
  height: 60px;
  margin-right: 12px;
  flex-shrink: 0;
}

.video-thumbnail img {
  width: 100%;
  height: 100%;
  object-fit: cover;
  border-radius: 4px;
}

.video-duration {
  position: absolute;
  bottom: 4px;
  right: 4px;
  background: rgba(0, 0, 0, 0.7);
  color: white;
  font-size: 10px;
  padding: 2px 4px;
  border-radius: 2px;
}

.video-completed {
  position: absolute;
  top: 4px;
  right: 4px;
  color: #28a745;
  font-size: 16px;
}

.video-info {
  flex: 1;
  min-width: 0;
}

.video-title {
  font-size: 14px;
  font-weight: 500;
  margin-bottom: 4px;
  color: #333;
  overflow: hidden;
  text-overflow: ellipsis;
  white-space: nowrap;
}

.video-meta {
  font-size: 12px;
}

.video-status {
  padding: 2px 6px;
  border-radius: 10px;
  font-size: 10px;
}

.status-completed {
  background-color: #d4edda;
  color: #155724;
}

.status-progress {
  background-color: #fff3cd;
  color: #856404;
}

.status-not-started {
  background-color: #f8f9fa;
  color: #6c757d;
}

/* 响应式设计 */
@media (max-width: 992px) {
  .col-lg-4 {
    margin-top: 20px;
  }
}

@media (max-width: 768px) {
  .course-video-page {
    padding: 10px 0;
  }
  
  .video-item {
    padding: 8px;
  }
  
  .video-thumbnail {
    width: 60px;
    height: 45px;
  }
}
</style>
