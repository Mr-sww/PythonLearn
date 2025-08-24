<template>
  <div class="container mt-4">
    <div class="row">
      <div class="col-12">
        <div class="card">
          <div class="card-header">
            <h3>视频点击测试页面</h3>
          </div>
          <div class="card-body">
            <div class="row">
              <div class="col-md-6">
                <h5>测试知识点</h5>
                <div class="list-group">
                  <div 
                    v-for="item in testKnowledgeItems" 
                    :key="item.id"
                    class="list-group-item list-group-item-action"
                    @click="testVideoClick(item)"
                  >
                    <div class="d-flex justify-content-between align-items-center">
                      <div>
                        <h6 class="mb-1">{{ item.title }}</h6>
                        <small class="text-muted">
                          类型: {{ item.contentType || 'text' }} | 
                          URL: {{ item.url || '无' }}
                        </small>
                      </div>
                      <span class="badge" :class="getBadgeClass(item.contentType)">
                        {{ item.contentType === 'video' ? '视频' : '文字' }}
                      </span>
                    </div>
                  </div>
                </div>
              </div>
              
              <div class="col-md-6">
                <h5>测试结果</h5>
                <div class="alert" :class="resultClass">
                  <h6>{{ resultTitle }}</h6>
                  <p>{{ resultMessage }}</p>
                  <pre v-if="resultData" class="mt-2">{{ JSON.stringify(resultData, null, 2) }}</pre>
                </div>
                
                <div class="mt-3">
                  <h6>用户信息</h6>
                  <div class="card">
                    <div class="card-body">
                      <p><strong>用户ID:</strong> {{ userInfo.userId || '未登录' }}</p>
                      <p><strong>用户名:</strong> {{ userInfo.nickname || '未知' }}</p>
                      <p><strong>登录状态:</strong> {{ userInfo.userId ? '已登录' : '未登录' }}</p>
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
import { videoClickService } from '@/services/videoClickService.js';

export default {
  name: 'TestVideoClick',
  data() {
    return {
      testKnowledgeItems: [
        {
          id: 1,
          title: 'Python基础语法视频教程',
          url: 'https://www.youtube.com/watch?v=example1',
          contentType: 'video'
        },
        {
          id: 2,
          title: 'Python数据类型详解',
          url: 'https://www.bilibili.com/video/example2',
          contentType: 'video'
        },
        {
          id: 3,
          title: 'Python控制流语句',
          url: 'https://docs.python.org/3/tutorial/',
          contentType: 'text'
        },
        {
          id: 4,
          title: 'Python函数定义',
          url: null,
          contentType: 'text'
        },
        {
          id: 5,
          title: 'Python面向对象编程.mp4',
          url: 'https://example.com/video.mp4',
          contentType: 'video'
        }
      ],
      resultTitle: '等待测试',
      resultMessage: '点击左侧知识点进行测试',
      resultClass: 'alert-info',
      resultData: null,
      userInfo: {}
    };
  },
  mounted() {
    this.checkUserInfo();
  },
  methods: {
    checkUserInfo() {
      const user = JSON.parse(localStorage.getItem('user') || 'null');
      this.userInfo = user || {};
    },
    
    getBadgeClass(contentType) {
      return contentType === 'video' ? 'bg-danger' : 'bg-primary';
    },
    
    async testVideoClick(item) {
      this.resultTitle = '测试中...';
      this.resultMessage = `正在测试知识点: ${item.title}`;
      this.resultClass = 'alert-warning';
      this.resultData = null;
      
      try {
        console.log('开始测试视频点击:', item);
        
        // 检查是否为视频内容
        const isVideo = videoClickService.isVideoContent(item.url, item.contentType);
        console.log('是否为视频内容:', isVideo);
        
        if (isVideo) {
          // 记录视频点击
          const result = await videoClickService.recordVideoClick(
            item.id,
            item.title,
            item.url,
            'video'
          );
          
          this.resultTitle = '测试成功';
          this.resultMessage = `视频点击记录成功！知识点: ${item.title}`;
          this.resultClass = 'alert-success';
          this.resultData = result;
          
          console.log('视频点击记录结果:', result);
        } else {
          this.resultTitle = '测试完成';
          this.resultMessage = `该知识点不是视频内容，无需记录点击。知识点: ${item.title}`;
          this.resultClass = 'alert-info';
          this.resultData = { isVideo: false, reason: '非视频内容' };
        }
        
      } catch (error) {
        this.resultTitle = '测试失败';
        this.resultMessage = `测试失败: ${error.message}`;
        this.resultClass = 'alert-danger';
        this.resultData = { error: error.message };
        
        console.error('视频点击测试失败:', error);
      }
    }
  }
};
</script>

<style scoped>
.list-group-item {
  cursor: pointer;
  transition: background-color 0.2s;
}

.list-group-item:hover {
  background-color: #f8f9fa;
}

.badge {
  font-size: 0.8rem;
}

pre {
  background-color: #f8f9fa;
  padding: 10px;
  border-radius: 4px;
  font-size: 0.9rem;
  max-height: 200px;
  overflow-y: auto;
}
</style>
