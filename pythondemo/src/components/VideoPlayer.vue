<template>
  <div class="video-player-container">
    <!-- 视频播放器 -->
    <div class="video-wrapper">
      <video
        ref="videoElement"
        :src="videoUrl"
        :poster="poster"
        class="video-player"
        controls
        preload="metadata"
        @play="onVideoPlay"
        @pause="onVideoPause"
        @timeupdate="onTimeUpdate"
        @ended="onVideoEnd"
        @loadedmetadata="onLoadedMetadata"
      >
        您的浏览器不支持视频播放
      </video>
    </div>

    <!-- 视频信息 -->
    <div class="video-info">
      <h3 class="video-title">{{ videoTitle }}</h3>
      <div class="video-meta">
        <span class="duration">
          <i class="fa fa-clock me-1"></i>
          {{ formatDuration(currentTime) }} / {{ formatDuration(localDuration) }}
        </span>
        <span class="progress">
          <i class="fa fa-percentage me-1"></i>
          {{ Math.round(progress) }}%
        </span>
      </div>
    </div>

    <!-- 学习记录状态 -->
    <div class="learning-status">
      <div v-if="!isWatchingStarted" class="text-center">
        <button @click="startWatching" class="btn btn-primary" :disabled="loading">
          <i class="fa fa-play me-2"></i>
          {{ loading ? '记录中...' : '开始观看' }}
        </button>
      </div>
      <div v-else-if="!isWatchingCompleted" class="text-center">
        <div class="alert alert-info">
          <i class="fa fa-eye me-2"></i>
          正在记录观看进度...
        </div>
        <button @click="completeWatching" class="btn btn-success mt-2" :disabled="loading">
          <i class="fa fa-check me-2"></i>
          {{ loading ? '记录中...' : '完成观看' }}
        </button>
      </div>
      <div v-else class="text-center">
        <div class="alert alert-success">
          <i class="fa fa-check-circle me-2"></i>
          观看完成！
        </div>
      </div>
    </div>
  </div>
</template>

<script>
import { learningRecordService } from '@/services/learningRecordService.js';

export default {
  name: 'VideoPlayer',
  props: {
    videoId: {
      type: [Number, String],
      required: true
    },
    videoTitle: {
      type: String,
      required: true
    },
    videoUrl: {
      type: String,
      required: true
    },
    poster: {
      type: String,
      default: ''
    },
    totalDuration: {
      type: Number,
      default: 0
    }
  },
  data() {
    return {
      currentTime: 0,
      progress: 0,
      loading: false,
      isWatchingStarted: false,
      isWatchingCompleted: false,
      watchStartTime: null,
      lastProgressUpdate: 0,
      progressUpdateInterval: 30, // 每30秒更新一次进度
      localDuration: 0 // 本地变量存储视频总时长
    };
  },
  mounted() {
    this.checkWatchingStatus();
  },
  methods: {
    // 检查观看状态
    async checkWatchingStatus() {
      try {
        const records = await learningRecordService.getVideoRecords(10);
        const currentRecord = records.find(r => r.videoId == this.videoId);
        
        if (currentRecord) {
          this.isWatchingStarted = true;
          this.isWatchingCompleted = currentRecord.status === 'completed';
          this.watchStartTime = new Date(currentRecord.startTime);
        }
      } catch (error) {
        console.error('检查观看状态失败:', error);
      }
    },

    // 视频开始播放
    async onVideoPlay() {
      if (!this.isWatchingStarted) {
        await this.startWatching();
      }
    },

    // 视频暂停
    onVideoPause() {
      // 可以在这里记录暂停状态
      console.log('视频暂停');
    },

    // 视频时间更新
    onTimeUpdate() {
      const video = this.$refs.videoElement;
      if (video) {
        this.currentTime = video.currentTime;
        this.progress = (this.currentTime / this.localDuration) * 100;
        
        // 定期更新观看进度（避免过于频繁的API调用）
        if (this.currentTime - this.lastProgressUpdate >= this.progressUpdateInterval) {
          this.updateWatchingProgress();
          this.lastProgressUpdate = this.currentTime;
        }
      }
    },

    // 视频结束
    async onVideoEnd() {
      await this.completeWatching();
    },

    // 视频元数据加载完成
    onLoadedMetadata() {
      const video = this.$refs.videoElement;
      if (video) {
        // 使用本地变量而不是直接修改prop
        this.localDuration = video.duration;
      }
    },

    // 开始观看
    async startWatching() {
      this.loading = true;
      try {
        await learningRecordService.startVideoWatch(
          this.videoId,
          this.videoTitle,
          this.videoUrl,
          this.localDuration
        );
        
        this.isWatchingStarted = true;
        this.watchStartTime = new Date();
        console.log('开始观看记录已创建');
        
        this.$toast?.success?.('观看记录已开始') || alert('观看记录已开始');
      } catch (error) {
        console.error('创建观看记录失败:', error);
        this.$toast?.error?.('创建观看记录失败') || alert('创建观看记录失败');
      } finally {
        this.loading = false;
      }
    },

    // 更新观看进度
    async updateWatchingProgress() {
      if (!this.isWatchingStarted || this.isWatchingCompleted) return;
      
      try {
        await learningRecordService.updateVideoProgress(
          this.videoId,
          this.currentTime,
          this.progress
        );
        console.log('观看进度已更新:', this.progress.toFixed(1) + '%');
      } catch (error) {
        console.error('更新观看进度失败:', error);
      }
    },

    // 完成观看
    async completeWatching() {
      if (!this.isWatchingStarted || this.isWatchingCompleted) return;
      
      this.loading = true;
      try {
        // 计算观看时长
        const watchTime = this.watchStartTime ? Math.floor((new Date() - this.watchStartTime) / 1000) : 0;
        
        // 更新观看进度为100%
        await learningRecordService.updateVideoProgress(
          this.videoId,
          watchTime,
          100.0
        );
        
        // 完成观看
        await learningRecordService.completeVideoWatch(this.videoId);
        
        this.isWatchingCompleted = true;
        console.log('观看完成记录已更新');
        
        this.$toast?.success?.('观看完成！') || alert('观看完成！');
        
        // 触发完成事件
        this.$emit('video-completed', {
          videoId: this.videoId,
          watchTime: watchTime,
          progress: 100
        });
      } catch (error) {
        console.error('更新观看记录失败:', error);
        this.$toast?.error?.('更新观看记录失败') || alert('更新观看记录失败');
      } finally {
        this.loading = false;
      }
    },

    // 格式化时长
    formatDuration(seconds) {
      if (!seconds || isNaN(seconds)) return '00:00';
      
      const hours = Math.floor(seconds / 3600);
      const minutes = Math.floor((seconds % 3600) / 60);
      const secs = Math.floor(seconds % 60);
      
      if (hours > 0) {
        return `${hours.toString().padStart(2, '0')}:${minutes.toString().padStart(2, '0')}:${secs.toString().padStart(2, '0')}`;
      } else {
        return `${minutes.toString().padStart(2, '0')}:${secs.toString().padStart(2, '0')}`;
      }
    }
  }
};
</script>

<style scoped>
.video-player-container {
  max-width: 100%;
  margin: 0 auto;
}

.video-wrapper {
  position: relative;
  width: 100%;
  background: #000;
  border-radius: 8px;
  overflow: hidden;
  margin-bottom: 20px;
}

.video-player {
  width: 100%;
  height: auto;
  display: block;
}

.video-info {
  margin-bottom: 20px;
}

.video-title {
  font-size: 24px;
  font-weight: 600;
  color: #333;
  margin-bottom: 12px;
}

.video-meta {
  display: flex;
  gap: 20px;
  color: #666;
  font-size: 14px;
}

.video-meta span {
  display: flex;
  align-items: center;
}

.learning-status {
  background: #f8f9fa;
  border-radius: 8px;
  padding: 20px;
  text-align: center;
}

.learning-status .btn {
  padding: 10px 20px;
  font-weight: 500;
  border-radius: 8px;
}

.learning-status .alert {
  margin-bottom: 0;
  border: none;
  border-radius: 8px;
}

/* 响应式设计 */
@media (max-width: 768px) {
  .video-title {
    font-size: 20px;
  }
  
  .video-meta {
    flex-direction: column;
    gap: 8px;
  }
  
  .learning-status {
    padding: 16px;
  }
}
</style>









