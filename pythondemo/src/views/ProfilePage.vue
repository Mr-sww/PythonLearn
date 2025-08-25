<template>
  <!-- 移除顶部导航栏，已由全局组件统一管理 -->
  <div class="min-h-screen bg-gray-50 py-8">
    <div class="container mx-auto px-4">
      <div class="grid grid-cols-1 lg:grid-cols-3 gap-8">
        
        <!-- 左侧：个人信息和编辑 -->
        <div class="lg:col-span-1">
          <!-- 左侧竖向菜单（按角色显示） -->
          <div class="bg-white rounded-3xl shadow-2xl p-4 mt-6 sticky top-6">
            <nav class="flex flex-col gap-2">
              <!-- 通用 -->
              <button class="menu-item w-full text-left" :class="{ active: activeTab==='profile' }" @click="activeTab='profile'">
                <i class="fa fa-user"></i>
                <span>我的资料</span>
              </button>
              <button class="menu-item w-full text-left" :class="{ active: activeTab==='learning' }" @click="activeTab='learning'">
                <i class="fa fa-book"></i>
                <span>学习记录</span>
              </button>
              <button class="menu-item w-full text-left" :class="{ active: activeTab==='practice' }" @click="activeTab='practice'">
                <i class="fa fa-chart-line"></i>
                <span>练习统计</span>
              </button>

              <!-- 学生 -->
              <button v-if="isStudent" class="menu-item w-full text-left" :class="{ active: activeTab==='myCourses' }" @click="activeTab='myCourses'">
                <i class="fa fa-graduation-cap"></i>
                <span>我的课程</span>
              </button>
              <button v-if="isStudent" class="menu-item w-full text-left" :class="{ active: activeTab==='studentClasses' }" @click="activeTab='studentClasses'">
                <i class="fa fa-users"></i>
                <span>我的班级</span>
              </button>

              <!-- 教师 -->
              <button v-if="isTeacher" class="menu-item w-full text-left" :class="{ active: activeTab==='teacherClasses' }" @click="activeTab='teacherClasses'">
                <i class="fa fa-users"></i>
                <span>我的班级</span>
              </button>
              <button v-if="isTeacher" class="menu-item w-full text-left" :class="{ active: activeTab==='teacherRequests' }" @click="activeTab='teacherRequests'">
                <i class="fa fa-chalkboard-teacher"></i>
                <span>课程申请</span>
              </button>

              <!-- 管理员入口移至头像下拉菜单，这里不再展示 -->
            </nav>
          </div>
        </div>

        <!-- 右侧：功能区域 -->
        <div class="lg:col-span-2">
          <!-- 我的资料（仅在“我的资料”标签下显示） -->
          <div v-if="activeTab==='profile'" class="bg-white rounded-3xl shadow-2xl p-8 mb-6">
            <!-- 头像和基本信息 -->
            <div class="text-center mb-6">
              <div class="relative mb-4 group inline-block">
                <img :src="avatarPreview || (user.avatar ? getAvatarUrl(user.avatar) : defaultAvatar)" alt="头像" class="w-32 h-32 rounded-full border-4 border-primary shadow-lg object-cover transition-transform hover:scale-105" />
                <label class="absolute inset-0 flex items-center justify-center bg-black bg-opacity-40 rounded-full opacity-0 group-hover:opacity-100 transition-opacity cursor-pointer">
                  <i class="fa fa-camera text-white text-2xl"></i>
                  <input type="file" accept="image/png,image/jpeg" class="hidden" @change="onAvatarChange" />
                </label>
                <div v-if="avatarLoading" class="absolute inset-0 flex items-center justify-center bg-white/60 rounded-full">
                  <span class="loader"></span>
                </div>
              </div>
              <h2 class="text-2xl font-bold text-dark mb-1">{{ user.nickname || '未设置昵称' }}</h2>
              <div class="text-muted text-base">用户名：{{ user.account }}</div>
            </div>

            <!-- 个人信息详情 -->
            <div class="space-y-4 mb-6">
              <div class="flex items-center"><i class="fa fa-envelope text-primary mr-3"></i><span class="text-dark">邮箱：</span><span class="text-muted ml-1">{{ user.email || '未设置' }}</span></div>
              <div class="flex items-center"><i class="fa fa-phone text-primary mr-3"></i><span class="text-dark">手机号：</span><span class="text-muted ml-1">{{ user.phone || '未设置' }}</span></div>
              <div class="flex items-center"><i class="fa fa-graduation-cap text-primary mr-3"></i><span class="text-dark">专业大类：</span><span class="text-muted ml-1">{{ majorName }}</span></div>
              <div class="flex items-center"><i class="fa fa-user-tag text-primary mr-3"></i><span class="text-dark">用户角色：</span><span class="text-muted ml-1">{{ userRole === 'student' ? '学生' : userRole === 'teacher' ? '教师' : userRole === 'admin' ? '管理员' : '未知' }}</span></div>
              <div class="flex items-start"><i class="fa fa-star text-primary mr-3 mt-1"></i><span class="text-dark">兴趣方向：</span>
                <div class="flex flex-wrap gap-2 ml-1">
                  <template v-if="user.intestTypes && user.intestTypes.length">
                    <span v-for="i in user.intestTypes" :key="i" class="inline-block bg-gradient-to-r from-blue-400 to-blue-600 text-white px-3 py-1 rounded-full text-sm shadow-sm">{{ intestTypeMap[i] || i }}</span>
                  </template>
                  <span v-else class="text-muted">未设置</span>
                </div>
              </div>
            </div>

            <!-- 编辑资料按钮 -->
            <button class="w-full px-6 py-3 bg-primary text-white rounded-xl font-bold shadow-lg hover:bg-primary/90 transition-all text-lg flex items-center justify-center gap-2" @click="editProfile">
              <i class="fa fa-edit"></i> 编辑资料
            </button>
          </div>

          <!-- 内容分区：练习记录完整模块 -->
          <div v-if="activeTab==='practice'" class="section-title"><i class="fa fa-chart-pie mr-2"></i>练习记录</div>
          <div v-if="activeTab==='practice'" class="mb-6">
            <PracticeRecords />
          </div>

          <!-- 内容分区：学习记录完整模块 -->
          <div v-if="activeTab==='learning'" class="section-title"><i class="fa fa-book mr-2"></i>学习记录</div>
          <div v-if="activeTab==='learning'" class="mb-6">
            <LearningRecords />
          </div>

          <!-- 学生：我的课程、我的班级 内联展示 -->
          <div v-if="activeTab==='myCourses'" class="section-title"><i class="fa fa-graduation-cap mr-2"></i>我的课程</div>
          <div v-if="activeTab==='myCourses'" class="mb-6">
            <MyCourses />
          </div>

          <div v-if="activeTab==='studentClasses'" class="section-title"><i class="fa fa-users mr-2"></i>我的班级</div>
          <div v-if="activeTab==='studentClasses'" class="mb-6">
            <StudentMyClasses />
          </div>

          <!-- 教师：我的班级 / 课程申请 内联展示 -->
          <div v-if="activeTab==='teacherClasses'" class="section-title"><i class="fa fa-users mr-2"></i>教师 - 我的班级</div>
          <div v-if="activeTab==='teacherClasses'" class="mb-6">
            <TeacherClasses />
          </div>

          <div v-if="activeTab==='teacherRequests'" class="section-title"><i class="fa fa-chalkboard-teacher mr-2"></i>教师 - 课程申请</div>
          <div v-if="activeTab==='teacherRequests'" class="mb-6">
            <TeacherCourseRequests />
          </div>

          <!-- 管理员：课程审核 内联展示 -->
          <div v-if="activeTab==='adminApprovals'" class="section-title"><i class="fa fa-clipboard-check mr-2"></i>课程审核</div>
          <div v-if="activeTab==='adminApprovals'" class="mb-6">
            <AdminCourseApprovals />
          </div>

          <!-- 快捷功能卡片 -->
          <div class="grid grid-cols-1 md:grid-cols-2 gap-6">
            <!-- 学生功能：查看已选课程 -->
            <div v-if="isStudent" @click="goToMyCourses" class="bg-white rounded-3xl shadow-2xl p-6 hover:shadow-2xl transition-all cursor-pointer transform hover:scale-105 border-2 border-transparent hover:border-green-200">
              <div class="flex items-center justify-between mb-4">
                <h3 class="text-xl font-bold text-dark">
                  <i class="fa fa-graduation-cap text-green-500 me-2"></i>我的已选课程
                </h3>
                <i class="fa fa-arrow-right text-green-500 text-xl"></i>
              </div>
              <div class="text-center">
                <i class="fa fa-book text-4xl text-green-400 mb-3"></i>
                <div class="text-sm text-gray-600">查看您已选择的所有课程</div>
              </div>
              <div class="text-center mt-4 text-green-500 text-sm font-medium">
                点击进入 →
              </div>
            </div>
            
            <!-- 教师功能：查看创建的课程 -->
            <div v-if="isTeacher" @click="goToTeacherCourses" class="bg-white rounded-3xl shadow-2xl p-6 hover:shadow-2xl transition-all cursor-pointer transform hover:scale-105 border-2 border-transparent hover:border-blue-200">
              <div class="flex items-center justify-between mb-4">
                <h3 class="text-xl font-bold text-dark">
                  <i class="fa fa-chalkboard-teacher text-blue-500 me-2"></i>我创建的课程
                </h3>
                <i class="fa fa-arrow-right text-blue-500 text-xl"></i>
              </div>
              <div class="text-center">
                <i class="fa fa-plus-circle text-4xl text-blue-400 mb-3"></i>
                <div class="text-sm text-gray-600">管理您创建的所有课程</div>
              </div>
              <div class="text-center mt-4 text-blue-500 text-sm font-medium">
                点击进入 →
              </div>
            </div>
            
            <!-- 管理员功能：进入后台管理系统 -->
            <div v-if="isAdmin" @click="goToAdminPanel" class="bg-white rounded-3xl shadow-2xl p-6 hover:shadow-2xl transition-all cursor-pointer transform hover:scale-105 border-2 border-transparent hover:border-red-200">
              <div class="flex items-center justify-between mb-4">
                <h3 class="text-xl font-bold text-dark">
                  <i class="fa fa-cog text-red-500 me-2"></i>后台管理系统
                </h3>
                <i class="fa fa-arrow-right text-red-500 text-xl"></i>
              </div>
              <div class="text-center">
                <i class="fa fa-shield-alt text-4xl text-red-400 mb-3"></i>
                <div class="text-sm text-gray-600">进入系统管理后台</div>
              </div>
              <div class="text-center mt-4 text-red-500 text-sm font-medium">
                点击进入 →
              </div>
            </div>
            
            <router-link to="/favorites" class="bg-white rounded-3xl shadow-2xl p-6 hover:shadow-2xl transition-all cursor-pointer transform hover:scale-105 border-2 border-transparent hover:border-red-200">
              <div class="flex items-center justify-between mb-4">
                <h3 class="text-xl font-bold text-dark">
                  <i class="fa fa-heart text-red-500 me-2"></i>我的收藏课程
                </h3>
                <i class="fa fa-arrow-right text-red-500 text-xl"></i>
              </div>
              <div class="text-center">
                <i class="fa fa-heart text-4xl text-red-400 mb-3"></i>
                <div class="text-sm text-gray-600">查看您收藏的所有课程</div>
              </div>
              <div class="text-center mt-4 text-red-500 text-sm font-medium">
                点击进入 →
              </div>
            </router-link>
            
            <router-link to="/favorite-problems" class="bg-white rounded-3xl shadow-2xl p-6 hover:shadow-2xl transition-all cursor-pointer transform hover:scale-105 border-2 border-transparent hover:border-blue-200">
              <div class="flex items-center justify-between mb-4">
                <h3 class="text-xl font-bold text-dark">
                  <i class="fa fa-star text-blue-500 me-2"></i>我收藏的题目
                </h3>
                <i class="fa fa-arrow-right text-blue-500 text-xl"></i>
              </div>
              <div class="text-center">
                <i class="fa fa-star text-4xl text-blue-400 mb-3"></i>
                <div class="text-sm text-gray-600">查看您收藏的所有题目</div>
              </div>
              <div class="text-center mt-4 text-blue-500 text-sm font-medium">
                点击进入 →
              </div>
            </router-link>
          </div>
        </div>
      </div>
    </div>

    <!-- 编辑资料弹窗 -->
    <div v-if="editMode" class="fixed inset-0 z-50 flex items-center justify-center bg-white/40 backdrop-blur-sm">
      <div class="bg-white rounded-2xl shadow-2xl p-8 w-full max-w-md relative">
        <button class="absolute top-4 right-4 text-muted hover:text-dark text-xl" @click="editMode=false"><i class="fa fa-times"></i></button>
        <h3 class="text-xl font-bold text-dark mb-6 text-center">编辑个人资料</h3>
        <form @submit.prevent="saveProfile" class="space-y-5">
          <div class="relative flex flex-col items-center mb-4 group">
            <img :src="avatarPreview || (user.avatar ? getAvatarUrl(user.avatar) : defaultAvatar)" alt="头像" class="w-24 h-24 rounded-full border-4 border-primary shadow-lg object-cover mx-auto transition-transform hover:scale-105" />
            <label class="absolute inset-0 flex items-center justify-center bg-black bg-opacity-40 rounded-full opacity-0 group-hover:opacity-100 transition-opacity cursor-pointer">
              <i class="fa fa-camera text-white text-xl"></i>
              <input type="file" accept="image/png,image/jpeg" class="hidden" @change="onAvatarChange" />
            </label>
            <div v-if="avatarLoading" class="absolute inset-0 flex items-center justify-center bg-white/60 rounded-full">
              <span class="loader"></span>
            </div>
          </div>
          <div>
            <label class="block text-muted mb-1">昵称</label>
            <input v-model="editForm.nickname" type="text" class="w-full px-4 py-3 rounded-lg border border-gray-300 focus:outline-none focus:ring-2 focus:ring-primary/50 focus:border-primary transition-all" placeholder="请输入昵称">
          </div>
          <div>
            <label class="block text-muted mb-1">邮箱</label>
            <input v-model="editForm.email" type="email" class="w-full px-4 py-3 rounded-lg border border-gray-300 focus:outline-none focus:ring-2 focus:ring-primary/50 focus:border-primary transition-all" placeholder="请输入邮箱">
          </div>
          <div>
            <label class="block text-muted mb-1">手机号</label>
            <input v-model="editForm.phone" type="tel" class="w-full px-4 py-3 rounded-lg border border-gray-300 focus:outline-none focus:ring-2 focus:ring-primary/50 focus:border-primary transition-all" placeholder="请输入手机号">
          </div>
          <div>
            <label class="block text-muted mb-1">专业大类</label>
            <select v-model="editForm.groupType" class="w-full px-4 py-3 rounded-lg border border-gray-300 focus:outline-none focus:ring-2 focus:ring-primary/50 focus:border-primary transition-all">
              <option value="">请选择</option>
              <option v-for="(name, key) in intestTypeMap" :key="key" :value="key">{{ name }}</option>
            </select>
          </div>
          <div>
            <label class="block text-muted mb-1">兴趣方向</label>
            <div class="flex flex-wrap gap-2">
              <label v-for="(name, key) in intestTypeMap" :key="key" class="flex items-center cursor-pointer">
                <input type="checkbox" v-model="editForm.intestTypes" :value="Number(key)" class="mr-2 rounded border-gray-300 focus:ring-primary">
                <span class="inline-block bg-gradient-to-r from-blue-400 to-blue-600 text-white px-3 py-1 rounded-full text-sm">{{ name }}</span>
              </label>
            </div>
          </div>
          <button type="submit" class="w-full py-3 bg-primary text-white rounded-xl font-bold text-lg shadow-lg hover:bg-primary/90 transition-all mt-2">保存</button>
        </form>
      </div>
    </div>
  </div>
</template>

<script>
import axios from 'axios';
import { getRoleByGroupType, getMajorName, isStudent, isTeacher, isAdmin } from '@/utils/auth';
import MyCourses from '@/views/MyCourses.vue'
import StudentMyClasses from '@/views/student/MyClasses.vue'
import TeacherClasses from '@/views/teacher/TeacherClasses.vue'
import TeacherCourseRequests from '@/views/teacher/TeacherCourseRequests.vue'
import AdminCourseApprovals from '@/views/admin/AdminCourseApprovals.vue'
import PracticeRecords from '@/views/PracticeRecords.vue'
import LearningRecords from '@/views/LearningRecords.vue'

export default {
  components: { MyCourses, StudentMyClasses, TeacherClasses, TeacherCourseRequests, AdminCourseApprovals, PracticeRecords, LearningRecords },
  data() {
    return {
      activeTab: 'profile',
      user: {
        avatar: null,
        nickname: '',
        account: '',
        email: '',
        userId: null,
        groupType: null
      },
      editMode: false,
      editForm: {
        avatar: '',
        avatarPreview: '',
        nickname: '',
        email: '',
        password: '',
        confirmPassword: '',
        groupType: null,
        intestTypes: []
      },
      avatarPreview: '',
      avatarLoading: false,
      defaultAvatar: 'https://picsum.photos/200/200',
      groupTypeMap: {
        1: '计算机类',
        2: '工设类',
        3: '艺术类',
        4: '医学类',
        5: '文科类',
        6: '体育类',
        7: '教师',
        8: '管理员'
      },
      intestTypeMap: {
        1: 'Web开发',
        2: '数据分析',
        3: '机器学习',
        4: '自动化脚本',
        5: 'DevOps',
        6: '游戏开发'
      },
      practiceStats: {
        totalSubmissions: 0,
        accuracy: 0,
        continuousDays: 0,
        passedProblems: 0
      },
      learningStats: {
        totalCourses: 0,
        completedLessons: 0
      }
    }
  },
  computed: {
    // 计算用户角色
    userRole() {
      return getRoleByGroupType(this.user.groupType)
    },
    
    // 角色判断
    isStudent() {
      return isStudent()
    },
    
    isTeacher() {
      return isTeacher()
    },
    
    isAdmin() {
      return isAdmin()
    },
    
    // 获取专业名称
    majorName() {
      return getMajorName(this.user.groupType)
    }
  },
  mounted() {
    // 从新的用户信息中获取userId
    const user = JSON.parse(localStorage.getItem('user') || 'null')
    let userId = user ? (user.userId || user.user_id) : null
    
    if (!userId) {
      alert('请先登录！');
      this.$router.push('/auth');
      return;
    }
    axios.get(`http://localhost:8080/api/user/${userId}`).then(res => {
      this.user = res.data;
      let rawIntestTypes = res.data.intestTypes || res.data.intest_types;
      console.log('[profile] 后端兴趣原始值:', rawIntestTypes, 'user:', this.user);
      this.user.intestTypes = this.parseIntestTypes(rawIntestTypes);
      console.log('[profile] 最终 user.intestTypes:', this.user.intestTypes);
      
      // 加载练习统计信息
      this.loadPracticeStats(userId);
      
      // 加载学习统计信息
      this.loadLearningStats(userId);

      // 根据角色选择默认 Tab（支持通过路由 ?tab=xxx 覆盖）
      this.applyDefaultTab();
    });
  },
  methods: {
    applyDefaultTab() {
      const tabFromQuery = this.$route?.query?.tab;
      if (tabFromQuery && typeof tabFromQuery === 'string') {
        this.activeTab = tabFromQuery;
        return;
      }
      const role = getRoleByGroupType(this.user.groupType);
      if (role === 'admin') {
        this.activeTab = 'adminApprovals';
      } else if (role === 'teacher') {
        this.activeTab = 'teacherClasses';
      } else {
        this.activeTab = 'myCourses';
      }
    },
    getAvatarUrl(url) {
      if (!url || typeof url !== 'string') {
        return this.defaultAvatar;
      }
      if (url.startsWith('/avatar/')) {
        return 'http://localhost:8080' + url;
      }
      return url;
    },
    editProfile() {
      // 处理 intestTypes，确保是数组
      let intestTypesArray = [];
      if (this.user.intestTypes) {
        if (typeof this.user.intestTypes === 'string') {
          intestTypesArray = this.user.intestTypes
            ? this.user.intestTypes.split(',').filter(x => x).map(Number)
            : [];
        } else if (Array.isArray(this.user.intestTypes)) {
          intestTypesArray = this.user.intestTypes;
        }
      }

      this.editForm = {
        avatar: this.user.avatar,
        avatarPreview: '',
        nickname: this.user.nickname || '',
        email: this.user.email || '',
        password: '',
        confirmPassword: '',
        groupType: this.user.groupType,
        intestTypes: intestTypesArray
      };
      this.editMode = true;
    },
    closeEdit() {
      this.editMode = false;
      this.editForm.avatarPreview = '';
    },
    triggerAvatarInput() {
      this.$refs.avatarInput.click();
    },
    onAvatarChange(e) {
      const file = e.target.files[0];
      if (!file) return;
      if (!['image/jpeg', 'image/png'].includes(file.type)) {
        alert('只支持jpg/png格式');
        return;
      }
      if (file.size > 2 * 1024 * 1024) {
        alert('图片不能超过2MB');
        return;
      }
      const reader = new FileReader();
      reader.onload = (ev) => {
        this.avatarPreview = ev.target.result;
      };
      reader.readAsDataURL(file);
      const formData = new FormData();
      formData.append('avatar', file);
      this.avatarLoading = true;
      axios.post(`http://localhost:8080/api/user/${this.user.userId}/avatar`, formData)
        .then(res => {
          this.user.avatar = res.data.avatar;
          this.avatarPreview = '';
          this.avatarLoading = false;
        })
        .catch(() => {
          alert('上传失败');
          this.avatarLoading = false;
        });
    },
    saveProfile() {
      // 密码确认验证
      if (this.editForm.password && this.editForm.password !== this.editForm.confirmPassword) {
        alert('两次输入的密码不一致');
        return;
      }

      const userId = this.user.userId;
      
      // 确保 intestTypes 是数组再转字符串
      let intestTypesStr = '';
      if (Array.isArray(this.editForm.intestTypes)) {
        intestTypesStr = this.editForm.intestTypes.join(',');
      } else if (typeof this.editForm.intestTypes === 'string') {
        intestTypesStr = this.editForm.intestTypes;
      }

      const updateData = {
        nickname: this.editForm.nickname,
        email: this.editForm.email,
        avatar: this.editForm.avatar || this.user.avatar,
        groupType: this.editForm.groupType,
        intestTypes: intestTypesStr
      };

      // 只有当密码不为空时才更新密码
      if (this.editForm.password) {
        updateData.password = this.editForm.password;
      }

      axios.put(`http://localhost:8080/api/user/${userId}`, updateData).then(() => {
        this.editMode = false;
        this.user.nickname = this.editForm.nickname;
        this.user.email = this.editForm.email;
        if (this.editForm.avatar) {
          this.user.avatar = this.editForm.avatar;
        }
        this.user.groupType = this.editForm.groupType;
        this.user.intestTypes = intestTypesStr;
        alert('保存成功');
      }).catch(err => {
        alert('保存失败：' + (err.response?.data || '未知错误'));
      });
    },
    parseIntestTypes(intestTypes) {
        if (Array.isArray(intestTypes)) return intestTypes;
        if (typeof intestTypes === 'string' && intestTypes.trim() && intestTypes !== 'null') {
            // 兼容逗号、空格、全角逗号
            return intestTypes.split(/[,，\s]+/).filter(x => x).map(Number);
        }
        return [];
    },
    loadPracticeStats(userId) {
      // 加载练习统计信息
      axios.get(`http://localhost:8080/api/user-problem-record/statistics?userId=${userId}`).then(res => {
        if (res.data.success && res.data.data) {
          const stats = res.data.data;
          this.practiceStats = {
            totalSubmissions: stats.totalSubmissions || 0,
            accuracy: Math.round((stats.accuracy || 0) * 100),
            continuousDays: stats.continuousDays || 0,
            passedProblems: stats.passedProblems || 0
          };
        }
      }).catch(err => {
        console.error('加载练习统计失败:', err);
      });
    },
    loadLearningStats(userId) {
      // 加载学习统计信息
      axios.get(`http://localhost:8080/api/user/learning-statistics?userId=${userId}`).then(res => {
        if (res.data.success && res.data.data) {
          const stats = res.data.data;
          this.learningStats = {
            totalCourses: stats.totalCourses || 0,
            completedLessons: stats.completedLessons || 0
          };
        }
      }).catch(err => {
        console.error('加载学习统计失败:', err);
        // 如果API不存在，设置默认值
        this.learningStats = {
          totalCourses: 0,
          completedLessons: 0
        };
      });
    },
    
    // 跳转方法
    goToMyCourses() {
      this.$router.push('/my-courses');
    },
    
    goToTeacherCourses() {
      this.$router.push('/teacher-courses');
    },
    
    goToAdminPanel() {
      this.$router.push('/admin');
    }
  }
}
</script>

<style scoped>
.bg-primary {
  background: linear-gradient(90deg, #2563eb 0%, #60a5fa 100%);
}
.text-primary {
  color: #2563eb;
}
.text-dark {
  color: #22223b;
}
.text-muted {
  color: #6b7280;
}
.shadow-2xl {
  box-shadow: 0 10px 40px 0 rgba(37,99,235,0.10), 0 2px 4px 0 rgba(0,0,0,0.04);
}
.loader {
  border: 3px solid #f3f3f3;
  border-top: 3px solid #2563eb;
  border-radius: 50%;
  width: 32px;
  height: 32px;
  animation: spin 1s linear infinite;
}
@keyframes spin {
  0% { transform: rotate(0deg); }
  100% { transform: rotate(360deg); }
}

/* 新增的样式 */
.btn {
  display: inline-flex;
  align-items: center;
  justify-content: center;
  padding: 0.5rem 1rem;
  font-size: 0.875rem;
  font-weight: 500;
  text-decoration: none;
  border-radius: 9999px;
  transition: all 0.2s;
  cursor: pointer;
}

.btn-primary {
  background: linear-gradient(90deg, #2563eb 0%, #60a5fa 100%);
  color: white;
  border: none;
}

.btn-primary:hover {
  background: linear-gradient(90deg, #1d4ed8 0%, #3b82f6 100%);
  transform: translateY(-1px);
  box-shadow: 0 4px 12px rgba(37,99,235,0.3);
}

/* 左侧菜单样式美化 */
.menu-item {
  display: flex;
  align-items: center;
  gap: 8px;
  padding: 10px 12px;
  border-radius: 10px;
  color: #1f2937;
  transition: all .2s;
}
.menu-item:hover { background: #f3f4f6; }
.menu-item.active { background: #eef2ff; color: #2563eb; box-shadow: inset 0 0 0 1px #c7d2fe; }

/* 内容区分标题 */
.section-title {
  font-size: 16px;
  font-weight: 700;
  color: #1f2937;
  margin: 12px 0 8px 4px;
}

/* 响应式设计 */
@media (max-width: 1024px) {
  .grid-cols-1.lg\:grid-cols-3 {
    grid-template-columns: 1fr;
  }
  
  .lg\:col-span-2 {
    grid-column: span 1;
  }
}

@media (max-width: 768px) {
  .container {
    padding-left: 1rem;
    padding-right: 1rem;
  }
  
  .grid-cols-2.md\:grid-cols-4 {
    grid-template-columns: repeat(2, 1fr);
  }
  
  .p-8 {
    padding: 1.5rem;
  }
  
  .p-6 {
    padding: 1rem;
  }
}
</style>