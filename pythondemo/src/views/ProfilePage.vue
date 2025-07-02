<template>
  <!-- 移除顶部导航栏，已由全局组件统一管理 -->
  <div class="flex justify-center items-center min-h-screen bg-gray-50">
    <div class="bg-white rounded-3xl shadow-2xl p-10 w-full max-w-lg flex flex-col items-center">
      <div class="relative mb-6 group">
        <img :src="avatarPreview || (user.avatar ? getAvatarUrl(user.avatar) : defaultAvatar)" alt="头像" class="w-32 h-32 rounded-full border-4 border-primary shadow-lg object-cover mx-auto transition-transform hover:scale-105" />
        <label class="absolute inset-0 flex items-center justify-center bg-black bg-opacity-40 rounded-full opacity-0 group-hover:opacity-100 transition-opacity cursor-pointer">
          <i class="fa fa-camera text-white text-2xl"></i>
          <input type="file" accept="image/png,image/jpeg" class="hidden" @change="onAvatarChange" />
        </label>
        <div v-if="avatarLoading" class="absolute inset-0 flex items-center justify-center bg-white/60 rounded-full">
          <span class="loader"></span>
        </div>
      </div>
      <h2 class="text-2xl font-bold text-dark mb-1">{{ user.nickname || '未设置昵称' }}</h2>
      <div class="text-muted text-base mb-6">用户名：{{ user.account }}</div>
      <div class="w-full space-y-4 mb-6">
        <div class="flex items-center"><i class="fa fa-envelope text-primary mr-3"></i><span class="text-dark">邮箱：</span><span class="text-muted ml-1">{{ user.email || '未设置' }}</span></div>
        <div class="flex items-center"><i class="fa fa-phone text-primary mr-3"></i><span class="text-dark">手机号：</span><span class="text-muted ml-1">{{ user.phone || '未设置' }}</span></div>
        <div class="flex items-center"><i class="fa fa-graduation-cap text-primary mr-3"></i><span class="text-dark">专业大类：</span><span class="text-muted ml-1">{{ groupTypeMap[user.groupType] || '未设置' }}</span></div>
        <div class="flex items-start"><i class="fa fa-star text-primary mr-3 mt-1"></i><span class="text-dark">兴趣方向：</span>
          <div class="flex flex-wrap gap-2 ml-1">
            <template v-if="user.intestTypes && user.intestTypes.length">
              <span v-for="i in user.intestTypes" :key="i" class="inline-block bg-gradient-to-r from-blue-400 to-blue-600 text-white px-3 py-1 rounded-full text-sm shadow-sm">{{ intestTypeMap[i] || i }}</span>
            </template>
            <span v-else class="text-muted">未设置</span>
          </div>
        </div>
      </div>
      
      <!-- 功能按钮区域 -->
      <div class="w-full space-y-3 mb-6">
        <button class="w-full px-8 py-3 bg-primary text-white rounded-xl font-bold shadow-lg hover:bg-primary/90 transition-all text-lg flex items-center justify-center gap-2" @click="editProfile">
          <i class="fa fa-edit"></i> 编辑资料
        </button>
        <router-link to="/favorites" class="w-full px-8 py-3 bg-gradient-to-r from-red-500 to-pink-500 text-white rounded-xl font-bold shadow-lg hover:from-red-600 hover:to-pink-600 transition-all text-lg flex items-center justify-center gap-2">
          <i class="fa fa-heart"></i> 我的收藏课程
        </router-link>
        <router-link to="/favorite-problems" class="w-full px-8 py-3 bg-gradient-to-r from-blue-500 to-blue-400 text-white rounded-xl font-bold shadow-lg hover:from-blue-600 hover:to-blue-500 transition-all text-lg flex items-center justify-center gap-2">
          <i class="fa fa-star"></i> 我收藏的题目
        </router-link>
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
                <option v-for="(name, key) in groupTypeMap" :key="key" :value="key">{{ name }}</option>
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
  </div>
</template>

<script>
import axios from 'axios';
export default {
  data() {
    return {
      user: {
        avatar: null,
        nickname: '',
        account: '',
        email: '',
        userId: null
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
        7: '其他'
      },
      intestTypeMap: {
        1: 'Web开发',
        2: '数据分析',
        3: '机器学习',
        4: '自动化脚本',
        5: 'DevOps',
        6: '游戏开发'
      }
    }
  },
  mounted() {
    let userId = this.user && this.user.userId;
    if (!userId) {
      userId = localStorage.getItem('userId');
    }
    if (!userId || userId === 'null') {
      alert('请先登录！');
      this.$router.push('/login');
      return;
    }
    axios.get(`http://localhost:8080/api/user/${userId}`).then(res => {
      this.user = res.data;
      let rawIntestTypes = res.data.intestTypes || res.data.intest_types;
      console.log('[profile] 后端兴趣原始值:', rawIntestTypes, 'user:', this.user);
      this.user.intestTypes = this.parseIntestTypes(rawIntestTypes);
      console.log('[profile] 最终 user.intestTypes:', this.user.intestTypes);
    });
  },
  methods: {
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
.profile-header-nav {
  background: linear-gradient(90deg, #2563eb 0%, #60a5fa 100%);
  box-shadow: 0 2px 8px rgba(37,99,235,0.08);
}
.profile-logo {
  font-size: 1.5rem;
  font-weight: bold;
  color: #fff;
  letter-spacing: 2px;
  display: flex;
  align-items: center;
}
.profile-logo i {
  margin-right: 8px;
  color: #ffe066;
}
.profile-nav-link {
  color: #fff !important;
  font-weight: 500;
  font-size: 1.1rem;
  padding: 0.5rem 1.5rem;
  border-radius: 8px;
  transition: background 0.2s, color 0.2s;
  text-decoration: none;
  display: inline-block;
}
.profile-nav-link:hover,
.profile-nav-link.router-link-exact-active {
  background: rgba(255,255,255,0.18);
  color: #ffe066 !important;
  text-decoration: none;
}
@media (max-width: 768px) {
  .profile-header-nav .container {
    flex-direction: column;
    height: auto;
    padding: 1rem 0;
  }
  .profile-logo {
    margin-bottom: 0.5rem;
  }
  .profile-header-nav nav {
    flex-wrap: wrap;
    justify-content: center;
  }
  .profile-nav-link {
    margin-bottom: 0.5rem;
  }
}
</style>