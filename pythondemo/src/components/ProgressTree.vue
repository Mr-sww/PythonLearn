<template>
  <div class="progress-tree">
    <!-- 进度概览 -->
    <div class="progress-overview bg-light rounded-3 p-4 mb-4">
      <div class="row">
        <div class="col-md-3 col-6 mb-3">
          <div class="text-center">
            <div class="h4 text-primary mb-1">{{ progress.overallProgress }}%</div>
            <div class="text-muted small">总体进度</div>
          </div>
        </div>
        <div class="col-md-3 col-6 mb-3">
          <div class="text-center">
            <div class="h4 text-success mb-1">{{ progress.completedLessons }}</div>
            <div class="text-muted small">已完成课时</div>
          </div>
        </div>
        <div class="col-md-3 col-6 mb-3">
          <div class="text-center">
            <div class="h4 text-info mb-1">{{ progress.totalLessons }}</div>
            <div class="text-muted small">总课时数</div>
          </div>
        </div>
        <div class="col-md-3 col-6 mb-3">
          <div class="text-center">
            <div class="h4 text-warning mb-1">{{ formatTime(progress.totalTimeSpent) }}</div>
            <div class="text-muted small">学习时长</div>
          </div>
        </div>
      </div>
      
      <!-- 总体进度条 -->
      <div class="mt-3">
        <div class="d-flex justify-content-between align-items-center mb-2">
          <span class="small text-muted">学习进度</span>
          <span class="small text-muted">{{ progress.overallProgress }}%</span>
        </div>
        <div class="progress" style="height: 8px;">
          <div 
            class="progress-bar bg-primary" 
            :style="{ width: progress.overallProgress + '%' }"
          ></div>
        </div>
      </div>
    </div>

    <!-- 章节列表 -->
    <div class="chapters-list">
      <div 
        v-for="(chapter, chapterIndex) in chapters" 
        :key="chapter.chapterId"
        class="chapter-item mb-4"
      >
        <!-- 章节标题 -->
        <div 
          class="chapter-header d-flex align-items-center p-3 bg-white rounded-3 shadow-sm cursor-pointer"
          @click="toggleChapter(chapterIndex)"
        >
          <div class="flex-grow-1">
            <div class="d-flex align-items-center">
              <i 
                :class="['fa me-2', chapter.expanded ? 'fa-chevron-down' : 'fa-chevron-right']"
              ></i>
              <h6 class="mb-0 fw-bold">{{ chapter.title }}</h6>
              <span class="badge bg-primary ms-2">{{ chapter.lessons.length }}课时</span>
            </div>
            <div class="text-muted small mt-1">{{ chapter.description }}</div>
          </div>
          <div class="text-end">
            <div class="text-success fw-bold">{{ chapter.progress }}%</div>
            <div class="text-muted small">{{ chapter.completedCount }}/{{ chapter.lessons.length }}</div>
          </div>
        </div>

        <!-- 章节进度条 -->
        <div class="chapter-progress px-3 pb-2">
          <div class="progress" style="height: 4px;">
            <div 
              class="progress-bar bg-success" 
              :style="{ width: chapter.progress + '%' }"
            ></div>
          </div>
        </div>

        <!-- 课时列表 -->
        <div v-if="chapter.expanded" class="lessons-list mt-2">
          <div 
            v-for="lesson in chapter.lessons" 
            :key="lesson.lessonId"
            class="lesson-item d-flex align-items-center p-3 bg-light rounded-3 mb-2 cursor-pointer"
            @click="selectLesson(lesson)"
          >
            <!-- 课时状态图标 -->
            <div class="lesson-status me-3">
              <i 
                v-if="lesson.completed"
                class="fa fa-check-circle text-success"
              ></i>
              <i 
                v-else-if="lesson.inProgress"
                class="fa fa-play-circle text-primary"
              ></i>
              <i 
                v-else
                class="fa fa-circle text-muted"
              ></i>
            </div>

            <!-- 课时信息 -->
            <div class="flex-grow-1">
              <div class="d-flex align-items-center">
                <span class="fw-medium">{{ lesson.title }}</span>
                <span v-if="lesson.isFree" class="badge bg-success ms-2">免费</span>
                <span v-if="lesson.isNew" class="badge bg-danger ms-2">新</span>
              </div>
              <div class="text-muted small mt-1">
                <i class="fa fa-clock me-1"></i>
                {{ formatDuration(lesson.duration) }}
                <span v-if="lesson.progress > 0" class="ms-2">
                  已学习 {{ lesson.progress }}%
                </span>
              </div>
            </div>

            <!-- 课时操作 -->
            <div class="lesson-actions">
              <button 
                v-if="lesson.completed"
                @click.stop="reviewLesson(lesson)"
                class="btn btn-sm btn-outline-primary"
              >
                <i class="fa fa-refresh me-1"></i>复习
              </button>
              <button 
                v-else
                @click.stop="startLesson(lesson)"
                class="btn btn-sm btn-primary"
              >
                <i class="fa fa-play me-1"></i>学习
              </button>
            </div>
          </div>
        </div>
      </div>
    </div>

    <!-- 学习统计 -->
    <div class="learning-stats bg-white rounded-3 p-4 mt-4">
      <h6 class="fw-bold mb-3">学习统计</h6>
      <div class="row">
        <div class="col-md-4 mb-3">
          <div class="d-flex align-items-center">
            <div class="stats-icon bg-primary rounded-circle me-3">
              <i class="fa fa-calendar text-white"></i>
            </div>
            <div>
              <div class="fw-bold">{{ stats.studyDays }}</div>
              <div class="text-muted small">学习天数</div>
            </div>
          </div>
        </div>
        <div class="col-md-4 mb-3">
          <div class="d-flex align-items-center">
            <div class="stats-icon bg-success rounded-circle me-3">
              <i class="fa fa-trophy text-white"></i>
            </div>
            <div>
              <div class="fw-bold">{{ stats.achievements }}</div>
              <div class="text-muted small">获得成就</div>
            </div>
          </div>
        </div>
        <div class="col-md-4 mb-3">
          <div class="d-flex align-items-center">
            <div class="stats-icon bg-warning rounded-circle me-3">
              <i class="fa fa-fire text-white"></i>
            </div>
            <div>
              <div class="fw-bold">{{ stats.streak }}</div>
              <div class="text-muted small">连续学习</div>
            </div>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
export default {
  name: 'ProgressTree',
  props: {
    courseId: {
      type: Number,
      required: true
    },
    userId: {
      type: Number,
      required: true
    }
  },
  data() {
    return {
      chapters: [],
      progress: {
        overallProgress: 0,
        completedLessons: 0,
        totalLessons: 0,
        totalTimeSpent: 0
      },
      stats: {
        studyDays: 0,
        achievements: 0,
        streak: 0
      },
      loading: false
    }
  },
  mounted() {
    this.loadProgressData()
  },
  methods: {
    // 加载进度数据
    async loadProgressData() {
      this.loading = true
      try {
        // 这里应该调用后端API获取真实数据
        // const response = await axios.get(`/api/progress/user/${this.userId}/course/${this.courseId}`)
        // this.progress = response.data
        
        // 使用模拟数据
        this.loadMockData()
      } catch (error) {
        console.error('加载进度数据失败:', error)
        this.loadMockData()
      } finally {
        this.loading = false
      }
    },

    // 加载模拟数据
    loadMockData() {
      this.chapters = [
        {
          chapterId: 1,
          title: '第一章：Python基础入门',
          description: '学习Python的基本语法和数据类型',
          progress: 80,
          completedCount: 4,
          totalCount: 5,
          expanded: true,
          lessons: [
            {
              lessonId: 1,
              title: '1.1 Python简介与环境搭建',
              duration: 1800,
              progress: 100,
              completed: true,
              inProgress: false,
              isFree: true,
              isNew: false
            },
            {
              lessonId: 2,
              title: '1.2 变量与数据类型',
              duration: 2400,
              progress: 100,
              completed: true,
              inProgress: false,
              isFree: false,
              isNew: false
            },
            {
              lessonId: 3,
              title: '1.3 运算符与表达式',
              duration: 2100,
              progress: 60,
              completed: false,
              inProgress: true,
              isFree: false,
              isNew: false
            },
            {
              lessonId: 4,
              title: '1.4 条件语句',
              duration: 2700,
              progress: 0,
              completed: false,
              inProgress: false,
              isFree: false,
              isNew: false
            },
            {
              lessonId: 5,
              title: '1.5 循环语句',
              duration: 3000,
              progress: 0,
              completed: false,
              inProgress: false,
              isFree: false,
              isNew: true
            }
          ]
        },
        {
          chapterId: 2,
          title: '第二章：函数与模块',
          description: '掌握函数定义和模块使用',
          progress: 0,
          completedCount: 0,
          totalCount: 4,
          expanded: false,
          lessons: [
            {
              lessonId: 6,
              title: '2.1 函数定义与调用',
              duration: 2400,
              progress: 0,
              completed: false,
              inProgress: false,
              isFree: false,
              isNew: false
            },
            {
              lessonId: 7,
              title: '2.2 参数传递',
              duration: 2100,
              progress: 0,
              completed: false,
              inProgress: false,
              isFree: false,
              isNew: false
            },
            {
              lessonId: 8,
              title: '2.3 返回值与作用域',
              duration: 1800,
              progress: 0,
              completed: false,
              inProgress: false,
              isFree: false,
              isNew: false
            },
            {
              lessonId: 9,
              title: '2.4 模块与包',
              duration: 2700,
              progress: 0,
              completed: false,
              inProgress: false,
              isFree: false,
              isNew: false
            }
          ]
        }
      ]

      // 计算总体进度
      this.calculateOverallProgress()
      
      // 设置统计数据
      this.stats = {
        studyDays: 12,
        achievements: 5,
        streak: 7
      }
    },

    // 计算总体进度
    calculateOverallProgress() {
      let totalLessons = 0
      let completedLessons = 0
      let totalTimeSpent = 0

      this.chapters.forEach(chapter => {
        chapter.lessons.forEach(lesson => {
          totalLessons++
          if (lesson.completed) {
            completedLessons++
          }
          totalTimeSpent += Math.floor(lesson.duration * lesson.progress / 100)
        })
      })

      this.progress = {
        overallProgress: totalLessons > 0 ? Math.round((completedLessons / totalLessons) * 100) : 0,
        completedLessons,
        totalLessons,
        totalTimeSpent
      }
    },

    // 切换章节展开状态
    toggleChapter(chapterIndex) {
      this.chapters[chapterIndex].expanded = !this.chapters[chapterIndex].expanded
    },

    // 选择课时
    selectLesson(lesson) {
      this.$emit('lesson-selected', lesson)
    },

    // 开始学习课时
    startLesson(lesson) {
      this.$emit('start-lesson', lesson)
    },

    // 复习课时
    reviewLesson(lesson) {
      this.$emit('review-lesson', lesson)
    },

    // 格式化时长
    formatDuration(seconds) {
      const hours = Math.floor(seconds / 3600)
      const minutes = Math.floor((seconds % 3600) / 60)
      
      if (hours > 0) {
        return `${hours}小时${minutes}分钟`
      }
      return `${minutes}分钟`
    },

    // 格式化时间
    formatTime(seconds) {
      const hours = Math.floor(seconds / 3600)
      const minutes = Math.floor((seconds % 3600) / 60)
      
      if (hours > 0) {
        return `${hours}h ${minutes}m`
      }
      return `${minutes}m`
    },

    // 更新课时进度
    updateLessonProgress(lessonId, progress) {
      for (const chapter of this.chapters) {
        for (const lesson of chapter.lessons) {
          if (lesson.lessonId === lessonId) {
            lesson.progress = progress
            lesson.completed = progress >= 100
            lesson.inProgress = progress > 0 && progress < 100
            
            // 重新计算章节进度
            this.calculateChapterProgress(chapter)
            this.calculateOverallProgress()
            break
          }
        }
      }
    },

    // 计算章节进度
    calculateChapterProgress(chapter) {
      const totalLessons = chapter.lessons.length
      const completedCount = chapter.lessons.filter(lesson => lesson.completed).length
      chapter.completedCount = completedCount
      chapter.progress = totalLessons > 0 ? Math.round((completedCount / totalLessons) * 100) : 0
    }
  }
}
</script>

<style scoped>
.cursor-pointer {
  cursor: pointer;
}

.chapter-header:hover {
  background-color: #f8f9fa !important;
}

.lesson-item:hover {
  background-color: #e9ecef !important;
}

.stats-icon {
  width: 40px;
  height: 40px;
  display: flex;
  align-items: center;
  justify-content: center;
}

.lesson-status {
  width: 24px;
  text-align: center;
}

.lesson-actions {
  min-width: 80px;
}

.progress {
  border-radius: 10px;
}

.progress-bar {
  border-radius: 10px;
}

@media (max-width: 768px) {
  .chapter-header {
    flex-direction: column;
    align-items: flex-start !important;
  }
  
  .text-end {
    text-align: left !important;
    margin-top: 10px;
  }
  
  .lesson-item {
    flex-direction: column;
    align-items: flex-start !important;
  }
  
  .lesson-actions {
    margin-top: 10px;
    align-self: flex-end;
  }
}
</style>
