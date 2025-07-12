<template>
  <div>
    <!-- 首次访问欢迎模态框 -->
    <div class="fixed inset-0 z-50 flex items-center justify-center bg-dark bg-opacity-50" v-if="showWelcomeModal && !isLoggedIn">
        <div class="bg-white rounded-2xl shadow-2xl w-full max-w-md overflow-hidden">
            <div class="p-8 text-center">
                <div class="w-16 h-16 bg-primary/10 rounded-full flex items-center justify-center mx-auto mb-6">
                    <i class="fa fa-code text-primary text-2xl"></i>
                </div>
                <h3 class="text-[clamp(1.5rem,3vw,2rem)] font-bold text-dark mb-3">欢迎来到 Python 学习平台</h3>
                <p class="text-muted mb-8">注册账号并登录，开启您的 Python 学习之旅</p>

                <div class="space-y-3">
                    <button @click="openRegisterModal" class="w-full py-3 px-6 bg-primary text-white rounded-lg font-medium transition-all hover:bg-primary/90 focus:outline-none focus:ring-2 focus:ring-primary/50">
                        立即注册
                    </button>
                    <button @click="openLoginModal" class="w-full py-3 px-6 border border-primary text-primary rounded-lg font-medium transition-all hover:bg-primary/5 focus:outline-none focus:ring-2 focus:ring-primary/50">
                        已有账号，登录
                    </button>
                    <button @click="closeWelcomeModal" class="w-full py-3 px-6 text-muted font-medium transition-all hover:text-dark focus:outline-none">
                        暂不登录，浏览
                    </button>
                </div>
            </div>
        </div>
    </div>

    <!-- 登录模态框 -->
    <div class="fixed inset-0 z-50 flex items-center justify-center bg-dark bg-opacity-50" v-if="showLoginModal && !isLoggedIn">
        <div class="bg-white rounded-2xl shadow-2xl w-full max-w-md overflow-hidden">
            <div class="p-8">
                <div class="flex justify-between items-center mb-6">
                    <h3 class="text-2xl font-bold text-dark">用户登录</h3>
                    <button @click="closeLoginModal" class="text-muted hover:text-dark">
                        <i class="fa fa-times"></i>
                    </button>
                </div>

                <form @submit.prevent="handleLogin">
                    <div class="space-y-4">
                        <div>
                            <label for="username" class="block text-sm font-medium text-muted mb-1">用户名</label>
                            <input type="text" id="username" v-model="loginForm.username" class="w-full px-4 py-3 rounded-lg border border-gray-300 focus:outline-none focus:ring-2 focus:ring-primary/50 focus:border-primary transition-all" placeholder="请输入您的用户名">
                        </div>

                        <div>
                            <label for="password" class="block text-sm font-medium text-muted mb-1">密码</label>
                            <input type="password" id="password" v-model="loginForm.password" class="w-full px-4 py-3 rounded-lg border border-gray-300 focus:outline-none focus:ring-2 focus:ring-primary/50 focus:border-primary transition-all" placeholder="请输入密码">
                        </div>

                        <div class="flex items-center justify-between">
                            <div class="flex items-center">
                                <input type="checkbox" id="remember" class="h-4 w-4 text-primary focus:ring-primary border-gray-300 rounded">
                                <label for="remember" class="ml-2 block text-sm text-muted">记住我</label>
                            </div>
                            <a href="#" class="text-sm font-medium text-primary hover:text-primary/80">忘记密码?</a>
                        </div>

                        <button type="submit" class="w-full py-3 px-6 bg-primary text-white rounded-lg font-medium transition-all hover:bg-primary/90 focus:outline-none focus:ring-2 focus:ring-primary/50">
                            登录
                        </button>

                        <div class="relative">
                            <div class="absolute inset-0 flex items-center">
                                <div class="w-full border-t border-gray-300"></div>
                            </div>
                            <div class="relative flex justify-center text-xs uppercase">
                                <span class="px-2 bg-white text-muted">其他登录方式</span>
                            </div>
                        </div>

                        <div class="grid grid-cols-2 gap-3">
                            <button class="py-2 px-4 border border-gray-300 rounded-lg flex items-center justify-center hover:bg-gray-50 transition-all">
                                <i class="fa fa-github text-2xl"></i>
                            </button>
                            <button class="py-2 px-4 border border-gray-300 rounded-lg flex items-center justify-center hover:bg-gray-50 transition-all">
                                <i class="fa fa-google text-2xl"></i>
                            </button>
                        </div>
                    </div>
                </form>

                <div class="mt-6 text-center">
                    <span class="text-muted">还没有账号?</span>
                    <button @click="openRegisterModal" class="ml-2 text-primary font-medium hover:text-primary/80">立即注册</button>
                </div>
            </div>
        </div>
    </div>

    <!-- 注册模态框 -->
    <div class="fixed inset-0 z-50 flex items-center justify-center bg-dark bg-opacity-50" v-if="showRegisterModal && !isLoggedIn">
        <div class="bg-white rounded-2xl shadow-2xl w-full max-w-md overflow-hidden">
            <div class="p-8">
                <div class="flex justify-between items-center mb-6">
                    <h3 class="text-2xl font-bold text-dark">注册账号</h3>
                    <button @click="closeRegisterModal" class="text-muted hover:text-dark">
                        <i class="fa fa-times"></i>
                    </button>
                </div>

                <form @submit.prevent="handleRegister">
                    <div class="space-y-4">
                        <div>
                            <label for="reg-nickname" class="block text-sm font-medium text-muted mb-1">昵称</label>
                            <input type="text" id="reg-nickname" v-model="registerForm.nickname" class="w-full px-4 py-3 rounded-lg border border-gray-300 focus:outline-none focus:ring-2 focus:ring-primary/50 focus:border-primary transition-all" placeholder="请设置您的昵称">
                        </div>

                        <div>
                            <label for="reg-gender" class="block text-sm font-medium text-muted mb-1">性别</label>
                            <select id="reg-gender" v-model="registerForm.gender" class="w-full px-4 py-3 rounded-lg border border-gray-300 focus:outline-none focus:ring-2 focus:ring-primary/50 focus:border-primary transition-all">
                                <option value="">请选择性别</option>
                                <option value="男">男</option>
                                <option value="女">女</option>
                                <option value="保密">保密</option>
                            </select>
                        </div>

                        <div>
                            <label for="reg-username" class="block text-sm font-medium text-muted mb-1">用户名</label>
                            <input type="text" id="reg-username" v-model="registerForm.username" class="w-full px-4 py-3 rounded-lg border border-gray-300 focus:outline-none focus:ring-2 focus:ring-primary/50 focus:border-primary transition-all" placeholder="请设置用户名">
                        </div>

                        <div>
                            <label for="reg-email" class="block text-sm font-medium text-muted mb-1">邮箱</label>
                            <input type="email" id="reg-email" v-model="registerForm.email" class="w-full px-4 py-3 rounded-lg border border-gray-300 focus:outline-none focus:ring-2 focus:ring-primary/50 focus:border-primary transition-all" placeholder="请输入您的邮箱">
                        </div>

                        <div>
                            <label for="reg-phone" class="block text-sm font-medium text-muted mb-1">手机号</label>
                            <input type="tel" id="reg-phone" v-model="registerForm.phone" class="w-full px-4 py-3 rounded-lg border border-gray-300 focus:outline-none focus:ring-2 focus:ring-primary/50 focus:border-primary transition-all" placeholder="请输入您的手机号">
                        </div>

                        <div>
                            <label for="reg-password" class="block text-sm font-medium text-muted mb-1">密码</label>
                            <input type="password" id="reg-password" v-model="registerForm.password" class="w-full px-4 py-3 rounded-lg border border-gray-300 focus:outline-none focus:ring-2 focus:ring-primary/50 focus:border-primary transition-all" placeholder="请设置密码">
                        </div>

                        <div>
                            <label for="reg-confirm-password" class="block text-sm font-medium text-muted mb-1">确认密码</label>
                            <input type="password" id="reg-confirm-password" v-model="registerForm.confirmPassword" class="w-full px-4 py-3 rounded-lg border border-gray-300 focus:outline-none focus:ring-2 focus:ring-primary/50 focus:border-primary transition-all" placeholder="请再次输入密码">
                        </div>

                        <div class="flex items-center">
                            <input type="checkbox" id="terms" class="h-4 w-4 text-primary focus:ring-primary border-gray-300 rounded">
                            <label for="terms" class="ml-2 block text-sm text-muted">我已阅读并同意<a href="#" class="text-primary hover:text-primary/80">服务条款</a>和<a href="#" class="text-primary hover:text-primary/80">隐私政策</a></label>
                        </div>

                        <button type="submit" class="w-full py-3 px-6 bg-primary text-white rounded-lg font-medium transition-all hover:bg-primary/90 focus:outline-none focus:ring-2 focus:ring-primary/50">
                            注册
                        </button>
                    </div>
                </form>

                <div class="mt-6 text-center">
                    <span class="text-muted">已有账号?</span>
                    <button @click="openLoginModal" class="ml-2 text-primary font-medium hover:text-primary/80">立即登录</button>
                </div>
            </div>
        </div>
    </div>

    <!-- 专业选择模态框 -->
    <div class="fixed inset-0 z-50 flex items-center justify-center bg-dark bg-opacity-50" v-if="showMajorModal">
        <div class="bg-white rounded-2xl shadow-2xl w-full max-w-md overflow-hidden">
            <div class="p-8">
                <div class="flex justify-between items-center mb-6">
                    <h3 class="text-2xl font-bold text-dark">选择您的专业大类</h3>
                    <button @click="closeMajorModal" class="text-muted hover:text-dark">
                        <i class="fa fa-times"></i>
                    </button>
                </div>

                <div class="grid grid-cols-2 gap-4 mb-6">
                    <label class="border border-gray-300 rounded-lg p-4 cursor-pointer hover:border-primary transition-all flex items-center mb-2">
                        <input type="radio" :value="1" v-model="user.groupType">
                        <span class="font-medium">计算机类</span>
                    </label>
                    <label class="border border-gray-300 rounded-lg p-4 cursor-pointer hover:border-primary transition-all flex items-center mb-2">
                        <input type="radio" v-model="user.groupType" :value="2" class="mr-2" />
                        <span class="font-medium">工设类</span>
                    </label>
                    <label class="border border-gray-300 rounded-lg p-4 cursor-pointer hover:border-primary transition-all flex items-center mb-2">
                        <input type="radio" v-model="user.groupType" :value="3" class="mr-2" />
                        <span class="font-medium">艺术类</span>
                    </label>
                    <label class="border border-gray-300 rounded-lg p-4 cursor-pointer hover:border-primary transition-all flex items-center mb-2">
                        <input type="radio" v-model="user.groupType" :value="4" class="mr-2" />
                        <span class="font-medium">医学类</span>
                    </label>
                    <label class="border border-gray-300 rounded-lg p-4 cursor-pointer hover:border-primary transition-all flex items-center mb-2">
                        <input type="radio" v-model="user.groupType" :value="5" class="mr-2" />
                        <span class="font-medium">文科类</span>
                    </label>
                    <label class="border border-gray-300 rounded-lg p-4 cursor-pointer hover:border-primary transition-all flex items-center mb-2">
                        <input type="radio" v-model="user.groupType" :value="6" class="mr-2" />
                        <span class="font-medium">体育类</span>
                    </label>
                    <label class="border border-gray-300 rounded-lg p-4 cursor-pointer hover:border-primary transition-all flex items-center mb-2">
                        <input type="radio" v-model="user.groupType" :value="7" class="mr-2" />
                        <span class="font-medium">其他</span>
                    </label>
                </div>

                <button @click="completeRegistration" class="w-full py-3 px-6 bg-primary text-white rounded-lg font-medium transition-all hover:bg-primary/90 focus:outline-none focus:ring-2 focus:ring-primary/50" :disabled="!user.groupType">
                    完成注册
                </button>
            </div>
        </div>
    </div>

    <!-- 学习方向选择模态框 -->
    <div class="fixed inset-0 z-50 flex items-center justify-center bg-dark bg-opacity-50" v-if="showLearningDirectionModal">
        <div class="bg-white rounded-2xl shadow-2xl w-full max-w-md overflow-hidden">
            <div class="p-8">
                <div class="flex justify-between items-center mb-6">
                    <h3 class="text-2xl font-bold text-dark">选择您感兴趣的学习方向</h3>
                    <button @click="closeLearningDirectionModal" class="text-muted hover:text-dark">
                        <i class="fa fa-times"></i>
                    </button>
                </div>

                <div class="grid grid-cols-2 gap-4 mb-6">
                    <div class="border border-gray-300 rounded-lg p-4 cursor-pointer hover:border-primary transition-all" @click="toggleLearningDirection(1)">
                        <div class="flex items-center mb-2">
                            <div class="w-5 h-5 rounded-full border border-gray-300 flex items-center justify-center mr-2">
                                <div class="w-3 h-3 rounded-full bg-primary" v-if="user.intestTypes && user.intestTypes.includes(1)"></div>
                            </div>
                            <span class="font-medium">Web开发</span>
                        </div>
                        <p class="text-sm text-muted">Django, Flask, 前端框架</p>
                    </div>

                    <div class="border border-gray-300 rounded-lg p-4 cursor-pointer hover:border-primary transition-all" @click="toggleLearningDirection(2)">
                        <div class="flex items-center mb-2">
                            <div class="w-5 h-5 rounded-full border border-gray-300 flex items-center justify-center mr-2">
                                <div class="w-3 h-3 rounded-full bg-primary" v-if="user.intestTypes && user.intestTypes.includes(2)"></div>
                            </div>
                            <span class="font-medium">数据分析</span>
                        </div>
                        <p class="text-sm text-muted">Pandas, Numpy, Matplotlib</p>
                    </div>

                    <div class="border border-gray-300 rounded-lg p-4 cursor-pointer hover:border-primary transition-all" @click="toggleLearningDirection(3)">
                        <div class="flex items-center mb-2">
                            <div class="w-5 h-5 rounded-full border border-gray-300 flex items-center justify-center mr-2">
                                <div class="w-3 h-3 rounded-full bg-primary" v-if="user.intestTypes && user.intestTypes.includes(3)"></div>
                            </div>
                            <span class="font-medium">机器学习</span>
                        </div>
                        <p class="text-sm text-muted">Scikit-learn, TensorFlow</p>
                    </div>

                    <div class="border border-gray-300 rounded-lg p-4 cursor-pointer hover:border-primary transition-all" @click="toggleLearningDirection(4)">
                        <div class="flex items-center mb-2">
                            <div class="w-5 h-5 rounded-full border border-gray-300 flex items-center justify-center mr-2">
                                <div class="w-3 h-3 rounded-full bg-primary" v-if="user.intestTypes && user.intestTypes.includes(4)"></div>
                            </div>
                            <span class="font-medium">自动化脚本</span>
                        </div>
                        <p class="text-sm text-muted">文件处理, 网页抓取</p>
                    </div>

                    <div class="border border-gray-300 rounded-lg p-4 cursor-pointer hover:border-primary transition-all" @click="toggleLearningDirection(5)">
                        <div class="flex items-center mb-2">
                            <div class="w-5 h-5 rounded-full border border-gray-300 flex items-center justify-center mr-2">
                                <div class="w-3 h-3 rounded-full bg-primary" v-if="user.intestTypes && user.intestTypes.includes(5)"></div>
                            </div>
                            <span class="font-medium">DevOps</span>
                        </div>
                        <p class="text-sm text-muted">CI/CD, Docker, 云服务</p>
                    </div>

                    <div class="border border-gray-300 rounded-lg p-4 cursor-pointer hover:border-primary transition-all" @click="toggleLearningDirection(6)">
                        <div class="flex items-center mb-2">
                            <div class="w-5 h-5 rounded-full border border-gray-300 flex items-center justify-center mr-2">
                                <div class="w-3 h-3 rounded-full bg-primary" v-if="user.intestTypes && user.intestTypes.includes(6)"></div>
                            </div>
                            <span class="font-medium">游戏开发</span>
                        </div>
                        <p class="text-sm text-muted">Pygame, 2D/3D游戏</p>
                    </div>
                </div>

                <button @click="saveLearningDirections" class="w-full py-3 px-6 bg-primary text-white rounded-lg font-medium transition-all hover:bg-primary/90 focus:outline-none focus:ring-2 focus:ring-primary/50" :disabled="!user.intestTypes || !user.intestTypes.length">
                    保存学习方向
                </button>
            </div>
        </div>
    </div>

    <!-- 主内容区 -->
    <main class="container mx-auto px-4 py-8">
        <!-- 首页轮播和推荐课程 -->
        <section id="home" class="mb-16">
            <!-- 轮播图 -->
            <div class="relative rounded-2xl overflow-hidden shadow-lg mb-12">
                <!-- 轮播图容器 -->
                <div class="relative h-[400px] overflow-hidden">
                    <!-- 轮播图渐变背景 -->
                    <div class="absolute inset-0 bg-gradient-to-r from-primary/80 to-secondary/80 z-10"></div>

                    <!-- 轮播图滑动区域 -->
                    <div class="carousel-slide-container absolute inset-0 flex transition-transform duration-500 ease-in-out" :style="{ transform: `translateX(${-currentSlide * 100}%)` }">
                        <!-- 轮播图1 -->
                        <div class="w-full flex-shrink-0 relative">
                            <img src="https://picsum.photos/1200/400?random=1" alt="Python学习" class="w-full h-full object-cover">
                            <div class="absolute inset-0 z-20 flex flex-col justify-center px-8 md:px-16">
                                <h1 class="text-[clamp(2rem,5vw,3.5rem)] font-bold text-white text-shadow mb-4">Python学习平台</h1>
                                <p class="text-[clamp(1rem,2vw,1.25rem)] text-white/90 max-w-2xl mb-8">从零基础到专业水平，全面系统地学习Python编程，开启你的编程之旅</p>
                                <div class="flex flex-wrap gap-4">
                                    <button @click="goToCourseList" class="px-6 py-3 bg-white text-primary rounded-lg font-medium hover:bg-gray-100 transition-all shadow-lg">
                                        浏览课程
                                    </button>
                                    <button @click="openRegisterModal" class="px-6 py-3 bg-transparent border-2 border-white text-white rounded-lg font-medium hover:bg-white/10 transition-all">
                                        立即注册
                                    </button>
                                </div>
                            </div>
                        </div>

                        <!-- 轮播图2 -->
                        <div class="w-full flex-shrink-0 relative">
                            <img src="https://picsum.photos/1200/400?random=2" alt="Python Web开发" class="w-full h-full object-cover">
                            <div class="absolute inset-0 z-20 flex flex-col justify-center px-8 md:px-16">
                                <h1 class="text-[clamp(2rem,5vw,3.5rem)] font-bold text-white text-shadow mb-4">Python Web开发</h1>
                                <p class="text-[clamp(1rem,2vw,1.25rem)] text-white/90 max-w-2xl mb-8">学习Django和Flask框架，开发专业级Web应用，成为全栈开发工程师</p>
                                <div class="flex flex-wrap gap-4">
                                    <button @click="goToCourseList" class="px-6 py-3 bg-white text-primary rounded-lg font-medium hover:bg-gray-100 transition-all shadow-lg">
                                        开始学习
                                    </button>
                                </div>
                            </div>
                        </div>

                        <!-- 轮播图3 -->
                        <div class="w-full flex-shrink-0 relative">
                            <img src="https://picsum.photos/1200/400?random=3" alt="Python数据分析" class="w-full h-full object-cover">
                            <div class="absolute inset-0 z-20 flex flex-col justify-center px-8 md:px-16">
                                <h1 class="text-[clamp(2rem,5vw,3.5rem)] font-bold text-white text-shadow mb-4">Python数据分析</h1>
                                <p class="text-[clamp(1rem,2vw,1.25rem)] text-white/90 max-w-2xl mb-8">掌握Pandas、Numpy、Matplotlib等库，成为专业数据分析师，洞察数据价值</p>
                                <div class="flex flex-wrap gap-4">
                                    <button @click="goToCourseList" class="px-6 py-3 bg-white text-primary rounded-lg font-medium hover:bg-gray-100 transition-all shadow-lg">
                                        探索课程
                                    </button>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>

                <!-- 轮播图控制按钮 -->
                <button @click="prevSlide" class="absolute left-4 top-1/2 -translate-y-1/2 z-30 w-10 h-10 bg-white/20 backdrop-blur-sm rounded-full flex items-center justify-center text-white hover:bg-white/40 transition-all">
                    <i class="fa fa-angle-left text-xl"></i>
                </button>
                <button @click="nextSlide" class="absolute right-4 top-1/2 -translate-y-1/2 z-30 w-10 h-10 bg-white/20 backdrop-blur-sm rounded-full flex items-center justify-center text-white hover:bg-white/40 transition-all">
                    <i class="fa fa-angle-right text-xl"></i>
                </button>

                <!-- 轮播图指示器 -->
                <div class="absolute bottom-4 left-0 right-0 z-30 flex justify-center space-x-2">
                    <button
                            @click="goToSlide(0)"
                            :class="['carousel-indicator w-3 h-3 rounded-full transition-all', currentSlide === 0 ? 'bg-white opacity-100 w-6' : 'bg-white opacity-50']"
                    ></button>
                    <button
                            @click="goToSlide(1)"
                            :class="['carousel-indicator w-3 h-3 rounded-full transition-all', currentSlide === 1 ? 'bg-white opacity-100 w-6' : 'bg-white opacity-50']"
                    ></button>
                    <button
                            @click="goToSlide(2)"
                            :class="['carousel-indicator w-3 h-3 rounded-full transition-all', currentSlide === 2 ? 'bg-white opacity-100 w-6' : 'bg-white opacity-50']"
                    ></button>
                </div>
            </div>

            <!-- 推荐课程 -->
            <div>
                <div class="flex justify-between items-center mb-8">
                    <h2 class="text-[clamp(1.5rem,3vw,2rem)] font-bold text-dark">为你推荐</h2>
                    <a href="/courses" class="btn btn-link text-primary p-0" style="font-size:1rem;">查看更多</a>
                </div>
                <div class="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-6">
                    <div v-for="course in recommendCourses.slice(0, 3)" :key="course.id || course.ID" class="bg-white rounded-xl shadow-md overflow-hidden card-hover">
                        <div class="relative">
                            <a :href="course.url || course.URL" target="_blank">
                                <img :src="course.imageUrl || course.ImageURL" :alt="course.title || course.Title" class="w-full h-48 object-cover">
                            </a>
                            <div class="absolute top-3 right-3 bg-accent text-white text-xs font-bold px-2 py-1 rounded">
                                推荐
                            </div>
                        </div>
                        <div class="p-5">
                            <div class="flex items-center mb-3">
                                <img src="https://picsum.photos/100/100?random=101" alt="讲师头像" class="w-8 h-8 rounded-full mr-2">
                                <span class="text-sm text-muted">{{ course.author || '平台推荐' }}</span>
                            </div>
                            <h3 class="text-xl font-bold text-dark mb-2">{{ course.title || course.Title }}</h3>
                            <p class="text-muted text-sm mb-4">{{ course.description || course.Tags }}</p>
                            <div class="flex justify-between items-center">
                                <div class="flex items-center">
                                    <i class="fa fa-users text-muted mr-1"></i>
                                    <span class="text-sm text-muted">{{ course.playCount || course.PlayCount }}人学习</span>
                                </div>
                                <a :href="course.url || course.URL" target="_blank" class="px-4 py-2 bg-primary text-white rounded-lg font-medium hover:bg-primary/90 transition-colors">开始学习</a>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </section>

        <!-- 合并后的课程中心大模块 -->
        <!-- <section id="courses" class="mb-16"> ... </section> -->

        <!-- 学习模块 -->
        <section id="learning" class="mb-16">
            <div class="d-flex justify-content-between align-items-center mb-4">
                <h2 class="fw-bold text-dark" style="font-size:2rem;">我的学习</h2>
                <router-link to="/learning" class="btn btn-link text-primary p-0" style="font-size:1rem;">学习中心</router-link>
            </div>
            <div class="row g-4" style="display:flex;align-items:stretch;">
                <!-- 学习进度树 -->
                <div class="col-lg-7" style="display:flex;flex-direction:column;">
                    <div class="bg-white rounded-3 shadow-sm border p-4 mb-3" style="flex:1 1 auto;min-height:420px;">
                        <h3 class="fw-bold text-dark mb-4" style="font-size:1.3rem;">学习进度树</h3>
                        <div class="space-y-4">
                            <div v-for="(node, idx) in knowledgeTree" :key="idx" class="p-4 border border-gray-200 rounded-lg mb-3">
                                <div class="d-flex align-items-center justify-content-between mb-2">
                                    <div class="d-flex align-items-center">
                                        <div :class="statusIconClass(node.status)" class="rounded-circle d-flex align-items-center justify-content-center text-white me-3" style="width:28px;height:28px;">
                                            <i :class="statusIcon(node.status)"></i>
                                        </div>
                                        <h4 class="fw-medium text-dark mb-0">{{ node.title }}</h4>
                                    </div>
                                    <span :class="badgeClass(node.status)">{{ node.status }}</span>
                                </div>
                                <!-- 内嵌滚动框 -->
                                <div class="progress-list-scroll ms-5">
                                    <div v-for="(child, cidx) in node.children" :key="cidx" class="d-flex align-items-center mb-2">
                                        <div :class="statusIconClass(child.status)" class="rounded-circle d-flex align-items-center justify-content-center text-white me-3" style="width:20px;height:20px;font-size:0.9rem;">
                                            <i :class="statusIcon(child.status)"></i>
                                        </div>
                                        <span :class="child.status==='未解锁' ? 'text-muted' : 'text-dark'">
                                            <span
                                              v-if="child.status!=='未解锁'"
                                              class="knowledge-btn"
                                              @click="goToKnowledgeDetail(child.title)"
                                              tabindex="0"
                                            >{{ child.title }}</span>
                                            <span v-else>{{ child.title }}</span>
                                        </span>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
                <!-- 学习统计 -->
                <div class="col-lg-5" style="display:flex;flex-direction:column;">
                    <div class="bg-white rounded-3 shadow-sm border p-4 mb-3" style="flex:1 1 auto;min-height:420px;">
                        <h3 class="fw-bold text-dark mb-4" style="font-size:1.3rem;">学习统计</h3>
                        <div class="mb-3">
                            <div class="d-flex justify-content-between mb-1"><span class="text-muted">总学习时长</span><span class="fw-medium">18.5小时</span></div>
                            <div class="d-flex justify-content-between mb-1"><span class="text-muted">完成课程</span><span class="fw-medium">3门</span></div>
                            <div class="d-flex justify-content-between mb-1"><span class="text-muted">练习题目</span><span class="fw-medium">24题</span></div>
                            <div class="d-flex justify-content-between"><span class="text-muted">连续学习</span><span class="fw-medium">7天</span></div>
                        </div>
                        <div>
                            <h4 class="fw-medium text-dark mb-2" style="font-size:1.1rem;">学习时间分布</h4>
                            <div style="height:220px;">
                                <canvas id="learningChart"></canvas>
                            </div>
                        </div>
                    </div>
                    <!-- 新增内容卡片：学习成就和最近学习 -->
                    <div class="bg-white rounded-3 shadow-sm border p-4 mt-3">
                        <h4 class="fw-bold text-dark mb-3" style="font-size:1.1rem;">学习成就</h4>
                        <div class="row g-2 mb-2">
                            <div class="col-6">
                                <div class="text-center p-2 border rounded">
                                    <i class="fa fa-trophy text-warning fa-lg mb-1"></i>
                                    <div class="fw-medium">连续学习7天</div>
                                    <div class="text-muted small">坚持不懈</div>
                                </div>
                            </div>
                            <div class="col-6">
                                <div class="text-center p-2 border rounded">
                                    <i class="fa fa-star text-warning fa-lg mb-1"></i>
                                    <div class="fw-medium">完成3门课程</div>
                                    <div class="text-muted small">学习达人</div>
                                </div>
                            </div>
                        </div>
                        <h4 class="fw-bold text-dark mb-3 mt-4" style="font-size:1.1rem;">最近学习</h4>
                        <ul class="list-unstyled mb-0">
                            <li class="mb-2 d-flex align-items-center">
                                <i class="fa fa-book text-primary me-2"></i>
                                <span class="text-dark">Python3 基础语法</span>
                                <span class="badge bg-success ms-auto">已完成</span>
                            </li>
                            <li class="mb-2 d-flex align-items-center">
                                <i class="fa fa-book text-primary me-2"></i>
                                <span class="text-dark">Python3 字典</span>
                                <span class="badge bg-warning ms-auto">进行中</span>
                            </li>
                        </ul>
                    </div>
                </div>
            </div>
        </section>

        <!-- 实践模块 -->
        <section id="practice" class="mb-16">
            <div class="d-flex justify-content-between align-items-center mb-4">
                <h2 class="fw-bold text-dark" style="font-size:2rem;">实践练习</h2>
                <router-link to="/practice" class="btn btn-link text-primary p-0" style="font-size:1rem;">练习中心</router-link>
            </div>
            <div class="row g-4">
                <!-- 练习进度（无进度条，仅题数） -->
                <div class="col-lg-4">
                    <div class="bg-white rounded-3 shadow-sm border p-4 mb-3">
                        <h3 class="fw-bold text-dark mb-4" style="font-size:1.3rem;">练习进度</h3>
                        <ul class="list-unstyled mb-4">
                            <li class="d-flex justify-content-between align-items-center py-2 border-bottom">
                                <span class="fw-medium text-dark">基础语法</span>
                                <span class="badge bg-primary rounded-pill px-3 py-2">15题</span>
                            </li>
                            <li class="d-flex justify-content-between align-items-center py-2 border-bottom">
                                <span class="fw-medium text-dark">数据结构</span>
                                <span class="badge bg-info rounded-pill px-3 py-2">8题</span>
                            </li>
                            <li class="d-flex justify-content-between align-items-center py-2 border-bottom">
                                <span class="fw-medium text-dark">算法基础</span>
                                <span class="badge bg-warning text-dark rounded-pill px-3 py-2">3题</span>
                            </li>
                            <li class="d-flex justify-content-between align-items-center py-2">
                                <span class="fw-medium text-dark">Web开发</span>
                                <span class="badge bg-secondary rounded-pill px-3 py-2">0题</span>
                            </li>
                        </ul>
                        <button class="w-100 btn btn-primary btn-lg rounded-pill mt-3">继续练习</button>
                    </div>
                </div>
                <!-- 推荐练习题 -->
                <div class="col-lg-8">
                    <div class="bg-white rounded-3 shadow-sm border p-4 mb-3">
                        <h3 class="fw-bold text-dark mb-4" style="font-size:1.3rem;">推荐练习题</h3>
                        <div class="row g-3">
                            <div class="col-md-6" v-for="(ex, idx) in practiceExercises" :key="idx">
                                <div class="card border-0 shadow-sm rounded-3 h-100">
                                    <div class="card-body">
                                        <div class="d-flex justify-content-between align-items-start mb-2">
                                            <div>
                                                <h5 class="fw-bold text-dark mb-1" style="font-size:1.1rem;">{{ ex.title }}</h5>
                                                <p class="text-muted mb-2" style="font-size:0.98rem;">{{ ex.desc }}</p>
                                            </div>
                                            <span :class="['badge rounded-pill px-3 py-2', ex.level==='简单'?'bg-success':'bg-warning text-dark']">{{ ex.level }}</span>
                                        </div>
                                        <div class="d-flex justify-content-between align-items-center">
                                            <span class="text-muted" style="font-size:0.95rem;">{{ ex.status }}</span>
                                            <button class="btn btn-outline-primary btn-sm rounded-pill px-3">{{ ex.btn }}</button>
                                        </div>
                                    </div>
                                </div>
                            </div>
                        </div>
                        <div class="text-center mt-4">
                            <a href="#" class="btn btn-link text-primary p-0" style="font-size:1rem;">查看更多练习题</a>
                        </div>
                    </div>
                </div>
            </div>
        </section>

        <!-- AI问答模块 -->
        <section id="ai" class="mb-16">
            <div class="flex justify-between items-center mb-8">
                <h2 class="text-[clamp(1.5rem,3vw,2rem)] font-bold text-dark">AI问答</h2>
                <router-link to="/ai" class="text-primary font-medium hover:underline">查看历史</router-link>
            </div>

            <div class="grid grid-cols-1 lg:grid-cols-4 gap-6">
                <!-- 快捷问题 -->
                <div class="lg:col-span-1">
                    <div class="bg-white rounded-xl shadow-md p-6">
                        <h3 class="text-xl font-bold text-dark mb-4">常见问题</h3>

                        <div class="space-y-3">
                            <button @click="sendQuickQuestion('如何安装Python')" class="w-full text-left px-4 py-2 bg-gray-50 hover:bg-gray-100 rounded-lg text-dark transition-colors">
                                如何安装Python
                            </button>
                            <button @click="sendQuickQuestion('Python基础语法')" class="w-full text-left px-4 py-2 bg-gray-50 hover:bg-gray-100 rounded-lg text-dark transition-colors">
                                Python基础语法
                            </button>
                            <button @click="sendQuickQuestion('Python和JavaScript的区别')" class="w-full text-left px-4 py-2 bg-gray-50 hover:bg-gray-100 rounded-lg text-dark transition-colors">
                                Python和JavaScript的区别
                            </button>
                            <button @click="sendQuickQuestion('如何使用Django')" class="w-full text-left px-4 py-2 bg-gray-50 hover:bg-gray-100 rounded-lg text-dark transition-colors">
                                如何使用Django
                            </button>
                            <button @click="sendQuickQuestion('Python数据分析库')" class="w-full text-left px-4 py-2 bg-gray-50 hover:bg-gray-100 rounded-lg text-dark transition-colors">
                                Python数据分析库
                            </button>
                            <button @click="sendQuickQuestion('Python爬虫教程')" class="w-full text-left px-4 py-2 bg-gray-50 hover:bg-gray-100 rounded-lg text-dark transition-colors">
                                Python爬虫教程
                            </button>
                        </div>
                    </div>
                </div>

                <!-- 聊天区域 -->
                <div class="lg:col-span-3">
                    <div class="bg-white rounded-3 shadow-sm border h-[420px] flex flex-col">
                        <!-- 聊天历史 -->
                        <div class="flex-1 p-3 overflow-y-auto" ref="chatContainer">
                            <div class="space-y-3">
                                <div v-for="(msg, idx) in chatMessages" :key="idx">
                                    <div v-if="msg.type === 'ai'" class="d-flex align-items-start">
                                        <div class="rounded-circle bg-primary d-flex align-items-center justify-content-center text-white me-2 mt-1" style="width:32px;height:32px;font-size:1.1rem;"><i class="fa fa-robot"></i></div>
                                        <div class="bg-light border border-primary/10 rounded-4 rounded-start-0 px-4 py-2" style="max-width:60%;min-width:80px;word-break:break-all;">
                                            <span v-if="msg.loading" class="text-muted" style="font-size:1rem;">
                                                <i class="fa fa-spinner fa-spin me-2"></i>{{ msg.content }}
                                            </span>
                                            <span v-else class="text-dark" style="font-size:1rem;">{{ msg.content }}</span>
                                        </div>
                                    </div>
                                    <div v-else class="d-flex align-items-start justify-content-end">
                                        <div class="bg-primary text-white rounded-4 rounded-end-0 px-4 py-2 me-2" style="max-width:60%;min-width:80px;word-break:break-all;">
                                            <span style="font-size:1rem;">{{ msg.content }}</span>
                                        </div>
                                        <div class="rounded-circle bg-secondary d-flex align-items-center justify-content-center text-dark ms-2 mt-1" style="width:32px;height:32px;font-size:1.1rem;"><i class="fa fa-user"></i></div>
                                    </div>
                                </div>
                            </div>
                        </div>
                        <!-- 输入区域 -->
                        <div class="p-3 border-top bg-light">
                            <div class="d-flex align-items-center">
                                <input type="text" v-model="userMessage" @keyup.enter="sendMessage" placeholder="输入您的问题..." class="form-control rounded-pill me-2" style="font-size:1rem;" />
                                <button @click="sendMessage" class="btn btn-primary rounded-pill px-4">发送</button>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </section>
    </main>

    <!-- 页脚 -->
    <footer class="bg-dark text-white py-12">
        <div class="container mx-auto px-4">
            <div class="grid grid-cols-1 md:grid-cols-4 gap-8">
                <!-- 关于我们 -->
                <div>
                    <div class="flex items-center mb-4">
                        <div class="bg-primary text-white p-2 rounded-lg mr-2">
                            <i class="fa fa-code text-xl"></i>
                        </div>
                        <span class="text-xl font-bold">Python学习平台</span>
                    </div>
                    <p class="text-gray-400 mb-4">
                        专注于Python编程学习的专业平台，帮助您从入门到精通
                        专注于Python编程学习的专业平台，帮助您从入门到精通</p>
                    <div class="flex space-x-4">
                        <a href="#" class="text-gray-400 hover:text-white transition-colors">
                            <i class="fa fa-weibo text-xl"></i>
                        </a>
                        <a href="#" class="text-gray-400 hover:text-white transition-cols">
                            <i class="fa fa-wechat text-xl"></i>
                        </a>
                        <a href="#" class="text-gray-400 hover:text-white transition-colors">
                            <i class="fa fa-github text-xl"></i>
                        </a>
                        <a href="#" class="text-gray-400 hover:text-white transition-colors">
                            <i class="fa fa-twitter text-xl"></i>
                        </a>
                    </div>
                </div>

                <!-- 快速链接 -->
                <div>
                    <h4 class="text-lg font-bold mb-4">快速链接</h4>
                    <ul class="space-y-2">
                        <li><a href="#" class="text-gray-400 hover:text-white transition-colors">首页</a></li>
                        <li><a href="#learning" class="text-gray-400 hover:text-white transition-colors">学习中心</a></li>
                        <li><a href="#practice" class="text-gray-400 hover:text-white transition-colors">实践练习</a></li>
                        <li><a href="#ai" class="text-gray-400 hover:text-white transition-colors">AI问答</a></li>
                        <li><a href="#" class="text-gray-400 hover:text-white transition-colors">关于我们</a></li>
                    </ul>
                </div>

                <!-- 课程分类 -->
                <div>
                    <h4 class="text-lg font-bold mb-4">课程分类</h4>
                    <ul class="space-y-2">
                        <li><a href="#" class="text-gray-400 hover:text-white transition-colors">Python基础</a></li>
                        <li><a href="#" class="text-gray-400 hover:text-white transition-colors">Web开发</a></li>
                        <li><a href="#" class="text-gray-400 hover:text-white transition-colors">数据分析</a></li>
                        <li><a href="#" class="text-gray-400 hover:text-white transition-colors">机器学习</a></li>
                        <li><a href="#" class="text-gray-400 hover:text-white transition-colors">自动化脚本</a></li>
                    </ul>
                </div>

                <!-- 联系我们 -->
                <div>
                    <h4 class="text-lg font-bold mb-4">联系我们</h4>
                    <ul class="space-y-3">
                        <li class="flex items-start">
                            <i class="fa fa-map-marker text-primary mt-1 mr-3"></i>
                            <span class="text-gray-400">北京市海淀区中关村科技园区</span>
                        </li>
                        <li class="flex items-center">
                            <i class="fa fa-envelope text-primary mr-3"></i>
                            <span class="text-gray-400">contact@pythonlearning.com</span>
                        </li>
                        <li class="flex items-center">
                            <i class="fa fa-phone text-primary mr-3"></i>
                            <span class="text-gray-400">400-123-4567</span>
                        </li>
                    </ul>
                </div>
            </div>

            <div class="border-t border-gray-800 mt-10 pt-6 text-center text-gray-500 text-sm">
                <p>© 2025 Python学习平台 版权所有 | 京ICP备12345678号</p>
            </div>
        </div>
    </footer>
</div>
</template>
<script>
import Chart from 'chart.js/auto';
import axios from 'axios';
import { EventBus } from '../eventBus';

axios.defaults.withCredentials = true;

export default {
    name: 'HomePage',
    data() {
        return {
            // 专业和兴趣映射表
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
            },

            // 模态框状态
            showWelcomeModal: true,
            showLoginModal: false,
            showRegisterModal: false,
            showMajorModal: false,
            showLearningDirectionModal: false,

            // 用户状态
            isLoggedIn: false,
            user: {
                avatar: null,
                nickname: '',
                userId: null,
                groupType: null,  // 改为数字
                intestTypes: [] // 改为数组
            },

            // 表单数据
            loginForm: {
                username: '',
                password: ''
            },
            registerForm: {
                nickname: '',
                gender: '',
                username: '',
                email: '',
                phone: '',
                password: '',
                confirmPassword: '',
                intestTypes: []
            },

            // 导航菜单
            showUserMenu: false,
            showMobileMenu: false,

            // 活跃区域跟踪
            activeSection: 'home',

            // AI聊天
            userMessage: '',
            chatMessages: [],

            // 学习统计图表
            learningChart: null,

            // 轮播图
            currentSlide: 0,
            slideInterval: null,
            slides: [
                {
                    image: "https://picsum.photos/1200/400?random=1",
                    title: "Python学习平台",
                    description: "从零基础到专业水平，全面系统地学习Python编程，开启你的编程之旅",
                    buttonText: "浏览课程"
                },
                {
                    image: "https://picsum.photos/1200/400?random=2",
                    title: "Python Web开发",
                    description: "学习Django和Flask框架，开发专业级Web应用，成为全栈开发工程师",
                    buttonText: "开始学习"
                },
                {
                    image: "https://picsum.photos/1200/400?random=3",
                    title: "Python数据分析",
                    description: "掌握Pandas、Numpy、Matplotlib等库，成为专业数据分析师，洞察数据价值",
                    buttonText: "探索课程"
                }
            ],
            navItems: [
                { id: 'home', label: '首页', href: '#' },
                { id: 'learning', label: '学习', href: '#learning' },
                { id: 'practice', label: '实践', href: '#practice' },
                { id: 'ai', label: 'AI问答', href: '#ai' }
            ],
            navLinks: {},
            navIndicatorStyle: null,
            practiceExercises: [
                { title: '计算斐波那契数列', desc: '编写一个Python函数，计算并返回斐波那契数列的前n项。', level: '简单', status: '已完成', btn: '查看解答' },
                { title: '字符串反转', desc: '编写一个Python函数，接受字符串并返回反转形式。', level: '简单', status: '已完成', btn: '查看解答' },
                { title: '列表去重', desc: '编写函数去除列表重复元素，保持顺序。', level: '中等', status: '进行中', btn: '继续解答' },
                { title: '字典排序', desc: '根据值对字典进行排序，并返回排序后的字典。', level: '中等', status: '未开始', btn: '开始解答' }
            ],
            defaultAvatar: 'https://picsum.photos/200/200',
            recommendCourses: [],
            allCourses: [],
            userStats: {},
            learningProgress: [],
            practiceProgress: [],
            recommendProblems: [],
            aiFaq: [],
            myLearning: [],
            knowledgeTree: []
        }
    },
    computed: {
        // 获取专业的中文名称
        groupTypeName() {
            return this.user.groupType ? this.groupTypeMap[this.user.groupType] : '';
        },
        // 获取兴趣的中文名称
        intestTypeName() {
            return this.user.intestTypes.length ? this.intestTypeMap[this.user.intestTypes[0]] : '';
        }
    },
    mounted() {
        this.fetchRecommendCourses();
        this.fetchAllCourses();
        this.fetchUserStats();
        this.fetchLearningProgress();
        this.fetchPracticeProgress();
        this.fetchRecommendProblems();
        this.fetchAiFaq();
        axios.get('http://localhost:8080/api/user/me')
            .then(res => {
                this.isLoggedIn = true;
                this.user = res.data;
                let rawIntestTypes = res.data.intestTypes || res.data.intest_types;
                console.log('[me] 后端兴趣原始值:', rawIntestTypes, 'user:', this.user);
                this.user.intestTypes = this.parseIntestTypes(rawIntestTypes);
                console.log('[me] 最终 user.intestTypes:', this.user.intestTypes);
                localStorage.setItem('userId', this.user.userId);
            })
            .catch(() => {
                this.isLoggedIn = false;
                this.user = {
                    avatar: null,
                    nickname: '',
                    userId: null,
                    groupType: null,
                    intestTypes: []
                };
                localStorage.removeItem('userId');
            });
        this.initLearningChart();
        this.scrollToBottom();
        this.startSlideShow();
        window.addEventListener('scroll', this.handleScroll);
        this.handleScroll();
        this.$nextTick(() => {
            this.updateNavIndicator();
        });
        if (localStorage.getItem('justRegistered') === 'true') {
            this.openLearningDirectionModal();
            localStorage.removeItem('justRegistered');
        }
        let userId = this.user && this.user.userId;
        if (!userId) {
            userId = localStorage.getItem('userId');
        }
        if (!userId || userId === 'null') {
            return;
        }
        // 拉取用户信息并判断兴趣弹窗
        axios.get(`http://localhost:8080/api/user/${userId}`).then(res => {
            this.user = res.data;
            let rawIntestTypes = res.data.intestTypes || res.data.intest_types;
            console.log('[byId] 后端兴趣原始值:', rawIntestTypes, 'user:', this.user);
            this.user.intestTypes = this.parseIntestTypes(rawIntestTypes);
            console.log('[byId] 最终 user.intestTypes:', this.user.intestTypes);
            if (!this.user.intestTypes || this.user.intestTypes.length === 0) {
                this.openLearningDirectionModal();
            }
        });
        EventBus.on('open-login-modal', this.openLoginModal);
        EventBus.on('open-register-modal', this.openRegisterModal);
        this.fetchKnowledgeTree();
    },
    beforeUnmount() {
        // 停止轮播图自动播放
        this.stopSlideShow();

        // 移除滚动事件监听
        window.removeEventListener('scroll', this.handleScroll);
        EventBus.off('open-login-modal', this.openLoginModal);
        EventBus.off('open-register-modal', this.openRegisterModal);
    },
    methods: {
        parseIntestTypes(intestTypes) {
            if (Array.isArray(intestTypes)) return intestTypes;
            if (typeof intestTypes === 'string' && intestTypes.trim() && intestTypes !== 'null') {
                // 兼容逗号、空格、全角逗号
                return intestTypes.split(/[,，\s]+/).filter(x => x).map(Number);
            }
            return [];
        },
        // 模态框操作方法
        openLoginModal() {
            this.showLoginModal = true;
            this.showWelcomeModal = false;
            this.showRegisterModal = false;
        },
        closeLoginModal() {
            this.showLoginModal = false;
        },
        openRegisterModal() {
            this.showRegisterModal = true;
            this.showWelcomeModal = false;
            this.showLoginModal = false;
        },
        closeRegisterModal() {
            this.showRegisterModal = false;
        },
        closeWelcomeModal() {
            this.showWelcomeModal = false;
        },
        openMajorModal() {
            this.showMajorModal = true;
            this.showRegisterModal = false;
        },
        closeMajorModal() {
            this.showMajorModal = false;
        },
        openLearningDirectionModal() {
            console.log('打开兴趣弹窗');
            this.showLearningDirectionModal = true;
        },
        closeLearningDirectionModal() {
            this.showLearningDirectionModal = false;
        },

        // 用户登录注册方法
        handleLogin() {
            if (this.loginForm.username && this.loginForm.password) {
                axios.post('http://localhost:8080/api/user/login', {
                    account: this.loginForm.username,
                    password: this.loginForm.password
                }).then(res => {
                    const user = res.data.data || res.data;
                    let rawIntestTypes = user.intestTypes || user.intest_types;
                    console.log('[login] 后端兴趣原始值:', rawIntestTypes, 'user:', user);
                    if (user && (user.userId || user.account)) {
                        this.isLoggedIn = true;
                        this.user = user;
                        this.user.userId = user.userId || user.user_id;
                        this.user.intestTypes = this.parseIntestTypes(rawIntestTypes);
                        console.log('[login] 最终 user.intestTypes:', this.user.intestTypes);
                        this.closeLoginModal();
                        if (!this.user.groupType) {
                            this.openMajorModal();
                        } else if (!this.user.intestTypes || this.user.intestTypes.length === 0) {
                            this.openLearningDirectionModal();
                        }
                        localStorage.setItem('userId', this.user.userId);
                    } else {
                        alert('登录失败');
                    }
                }).catch(err => {
                    alert(typeof err.response?.data === 'string' ? err.response.data : JSON.stringify(err.response?.data) || '登录失败');
                });
            } else {
                alert('请填写完整的登录信息');
            }
        },
        handleRegister() {
            if (this.registerForm.nickname && this.registerForm.username && this.registerForm.password) {
                if (this.registerForm.password !== this.registerForm.confirmPassword) {
                    alert('两次输入的密码不一致');
                    return;
                }
                const intestTypesStr = Array.isArray(this.registerForm.intestTypes) ? this.registerForm.intestTypes.join(',') : '';
                axios.post('http://localhost:8080/api/user/register', {
                    nickname: this.registerForm.nickname,
                    account: this.registerForm.username,
                    phone: this.registerForm.phone,
                    password: this.registerForm.password,
                    groupType: this.user.groupType,
                    intestTypes: intestTypesStr,
                    email: this.registerForm.email
                }).then(res => {
                    this.user = res.data;
                    this.user.userId = res.data.userId || res.data.user_id;
                    let rawIntestTypes = res.data.intestTypes || res.data.intest_types;
                    console.log('[register] 后端兴趣原始值:', rawIntestTypes, 'user:', this.user);
                    this.isLoggedIn = true;
                    this.user.intestTypes = this.parseIntestTypes(rawIntestTypes);
                    console.log('[register] 最终 user.intestTypes:', this.user.intestTypes);
                    this.openMajorModal();
                    alert('注册成功');
                    this.closeRegisterModal();
                    localStorage.setItem('justRegistered', 'true');
                    axios.post('http://localhost:8080/api/user/login', {
                        account: this.registerForm.username,
                        password: this.registerForm.password
                    }).then(res => {
                        this.isLoggedIn = true;
                        this.user = res.data;
                        this.user.userId = res.data.userId || res.data.user_id;
                        let rawIntestTypes = res.data.intestTypes || res.data.intest_types;
                        this.user.intestTypes = this.parseIntestTypes(rawIntestTypes);
                        console.log('[register->login] 最终 user.intestTypes:', this.user.intestTypes);
                        this.openMajorModal();
                    });
                }).catch(() => {
                    alert('注册失败');
                });
            } else {
                alert('请填写完整的注册信息');
            }
        },
        selectMajor(major) {
            this.user.groupType = major;
        },
        completeRegistration() {
            if (this.user.groupType) {
                axios.patch(`http://localhost:8080/api/user/${this.user.userId}/groupType`, {
                    groupType: this.user.groupType
                }).then(() => {
                    console.log('关闭专业弹窗，打开兴趣弹窗');
                    this.closeMajorModal();
                    this.openLearningDirectionModal();
                }).catch(() => {
                    alert('保存专业失败');
                });
            }
            console.log('completeRegistration user:', this.user);
            console.log('userId:', this.user.userId);
        },
        toggleLearningDirection(direction) {
            const idx = this.user.intestTypes.indexOf(direction);
            if (idx === -1) {
                this.user.intestTypes.push(direction);
            } else {
                this.user.intestTypes.splice(idx, 1);
            }
        },
        saveLearningDirections() {
            if (this.user.intestTypes.length > 0) {
                const intestTypesStr = this.user.intestTypes.join(',');
                axios.patch(`http://localhost:8080/api/user/${this.user.userId}/intestTypes`, {
                    intestTypes: intestTypesStr
                }).then(() => {
                    localStorage.setItem('hasLearningDirections', 'true');
                    this.closeLearningDirectionModal();
                    this.isLoggedIn = true;
                }).catch(() => {
                    alert('保存兴趣失败');
                });
            }
        },

        // 导航菜单方法
        toggleUserMenu() {
            this.showUserMenu = !this.showUserMenu;
        },
        toggleMobileMenu() {
            this.showMobileMenu = !this.showMobileMenu;
        },
        logout() {
            // 调用后端注销接口，清除 session/cookie
            axios.post('http://localhost:8080/api/user/logout', {}, { withCredentials: true }).finally(() => {
                this.isLoggedIn = false;
                this.user = {
                    avatar: null,
                    nickname: '',
                    userId: null,
                    groupType: null,
                    intestTypes: []
                };
                this.showUserMenu = false;
                localStorage.removeItem('userId');
                localStorage.removeItem('hasLearningDirections');
                window.location.reload(); // 强制刷新页面，彻底清空状态
            });
        },
        goToProfile() {
            this.$router.push('/profile');
            this.showUserMenu = false;
        },

        // 滚动和导航方法
        scrollToSection(section) {
            this.activeSection = section;
            const element = document.getElementById(section);
            if (element) {
                element.scrollIntoView({ behavior: 'smooth' });
            }
            this.showMobileMenu = false;
            this.$nextTick(() => {
                this.updateNavIndicator();
            });
        },
        handleScroll() {
            const sections = ['home', 'learning', 'practice', 'ai'];
            const scrollPosition = window.scrollY + 100;
            for (const section of sections) {
                const element = document.getElementById(section);
                if (element) {
                    const offsetTop = element.offsetTop;
                    const offsetHeight = element.offsetHeight;
                    if (scrollPosition >= offsetTop && scrollPosition < offsetTop + offsetHeight) {
                        this.activeSection = section;
                        this.$nextTick(() => {
                            this.updateNavIndicator();
                        });
                        break;
                    }
                }
            }
        },
        updateNavIndicator() {
            // 只保留蓝色小线，动画平滑，蓝线正好对齐文字底部
            const navBar = this.$refs.navBar;
            const activeId = this.activeSection;
            const activeLink = this.navLinks[activeId];
            if (navBar && activeLink) {
                const navRect = navBar.getBoundingClientRect();
                const linkRect = activeLink.getBoundingClientRect();
                const left = linkRect.left - navRect.left;
                const width = linkRect.width;
                this.navIndicatorStyle = {
                    position: 'absolute',
                    left: left + 'px',
                    bottom: '0',
                    width: width + 'px',
                    height: '2px',
                    background: '#2563eb',
                    borderRadius: '2px',
                    transition: 'left 0.3s cubic-bezier(.4,0,.2,1), width 0.3s cubic-bezier(.4,0,.2,1)'
                };
            }
        },

        // 轮播图方法
        nextSlide() {
            this.currentSlide = (this.currentSlide + 1) % this.slides.length;
        },
        prevSlide() {
            this.currentSlide = this.currentSlide === 0 ? this.slides.length - 1 : this.currentSlide - 1;
        },
        goToSlide(index) {
            this.currentSlide = index;
        },
        startSlideShow() {
            this.slideInterval = setInterval(() => {
                this.nextSlide();
            }, 5000);
        },
        stopSlideShow() {
            if (this.slideInterval) {
                clearInterval(this.slideInterval);
            }
        },

        // 课程相关方法
        goToCourseList() {
            console.log('跳转到课程列表');
        },
        goToCourseDetail(courseId) {
            console.log('跳转到课程详情:', courseId);
        },

        // AI聊天方法
        sendQuickQuestion(question) {
            this.userMessage = question;
            this.sendMessage();
        },
        async sendMessage() {
            if (this.userMessage.trim()) {
                const question = this.userMessage.trim();
                
                // 添加用户消息
                this.chatMessages.push({
                    type: 'user',
                    content: question,
                    timestamp: new Date()
                });
                
                // 添加AI加载消息
                const loadingMsg = {
                    type: 'ai',
                    content: '正在思考中...',
                    timestamp: new Date(),
                    loading: true
                };
                this.chatMessages.push(loadingMsg);
                
                this.userMessage = '';
                this.scrollToBottom();
                
                try {
                    // 调用真实的AI API
                    const res = await axios.post("/api/ai/ask", { 
                        question,
                        history: "" // 主页面的AI聊天不保存历史
                    });
                    
                    if (res.data.success) {
                        // 移除加载消息，添加AI回复
                        const loadingIndex = this.chatMessages.findIndex(msg => msg.loading);
                        if (loadingIndex !== -1) {
                            this.chatMessages.splice(loadingIndex, 1);
                        }
                        
                        this.chatMessages.push({
                            type: 'ai',
                            content: res.data.answer,
                            timestamp: new Date()
                        });
                    } else {
                        throw new Error(res.data.error || 'AI服务响应错误');
                    }
                } catch (e) {
                    console.error('AI请求失败:', e);
                    
                    // 移除加载消息，添加错误消息
                    const loadingIndex = this.chatMessages.findIndex(msg => msg.loading);
                    if (loadingIndex !== -1) {
                        this.chatMessages.splice(loadingIndex, 1);
                    }
                    
                    this.chatMessages.push({
                        type: 'ai',
                        content: `抱歉，AI服务暂时不可用。错误信息：${e.message}`,
                        timestamp: new Date()
                    });
                }
                
                this.scrollToBottom();
            }
        },
        scrollToBottom() {
            this.$nextTick(() => {
                const chatContainer = this.$refs.chatContainer;
                if (chatContainer) {
                    chatContainer.scrollTop = chatContainer.scrollHeight;
                }
            });
        },

        // 图表初始化方法
        initLearningChart() {
            this.$nextTick(() => {
                const ctx = document.getElementById('learningChart');
                if (ctx) {
                    // 创建渐变色
                    const gradient = ctx.getContext('2d').createLinearGradient(0, 0, 0, 300);
                    gradient.addColorStop(0, 'rgba(59,130,246,0.7)');
                    gradient.addColorStop(1, 'rgba(59,130,246,0.1)');
                    this.learningChart = new Chart(ctx, {
                        type: 'bar',
                        data: {
                            labels: ['周一', '周二', '周三', '周四', '周五', '周六', '周日'],
                            datasets: [{
                                label: '学习时长(小时)',
                                data: [2, 3, 1.5, 4, 2.5, 3.5, 2],
                                backgroundColor: gradient,
                                borderRadius: 12,
                                borderSkipped: false,
                                maxBarThickness: 40
                            }]
                        },
                        options: {
                            responsive: true,
                            maintainAspectRatio: false,
                            plugins: {
                                legend: {
                                    display: false
                                },
                                tooltip: {
                                    backgroundColor: '#2563eb',
                                    titleColor: '#fff',
                                    bodyColor: '#fff',
                                    borderColor: '#fff',
                                    borderWidth: 1
                                }
                            },
                            scales: {
                                y: {
                                    beginAtZero: true,
                                    grid: {
                                        color: 'rgba(37,99,235,0.08)'
                                    },
                                    ticks: {
                                        color: '#64748b',
                                        font: { size: 14 }
                                    }
                                },
                                x: {
                                    grid: {
                                        display: false
                                    },
                                    ticks: {
                                        color: '#64748b',
                                        font: { size: 14 }
                                    }
                                }
                            }
                        }
                    });
                }
            });
        },
        getAvatarUrl(url) {
            // 只要url为null/undefined/空字符串，直接返回默认头像
            if (!url || typeof url !== 'string') {
                return this.defaultAvatar || 'https://picsum.photos/200/200';
            }
            if (url.startsWith('/avatar/')) {
                return 'http://localhost:8080' + url;
            }
            return url;
        },
        async fetchRecommendCourses() {
            const res = await axios.get('/api/courses/recommend');
            this.recommendCourses = res.data;
        },
        async fetchAllCourses() {
            const res = await axios.get('/api/courses');
            this.allCourses = res.data;
        },
        async fetchUserStats() {
            const res = await axios.get('/api/user/statistics');
            this.userStats = res.data;
        },
        async fetchLearningProgress() {
            const res = await axios.get('/api/user/learning-progress');
            this.learningProgress = res.data;
        },
        async fetchPracticeProgress() {
            const res = await axios.get('/api/practice/progress');
            this.practiceProgress = res.data;
        },
        async fetchRecommendProblems() {
            const res = await axios.get('/api/practice/recommend');
            this.recommendProblems = res.data;
        },
        async fetchAiFaq() {
            const res = await axios.get('/api/ai/faq');
            this.aiFaq = res.data;
        },
        async fetchMyLearning() {
            const res = await axios.get('/api/knowledge/catalog');
            this.myLearning = res.data;
        },
        fetchKnowledgeTree() {
            this.$axios.get('/api/knowledge/points').then(res => {
                const points = res.data;
                // 分组
                const group = {
                    'Python基础': [],
                    'Python进阶': [],
                    'Web开发': []
                };
                points.forEach(p => {
                    if (p.stage && p.stage.startsWith('1.')) {
                        group['Python基础'].push({ title: p.title, status: '已完成' });
                    } else if (p.stage && p.stage.startsWith('2.')) {
                        group['Python进阶'].push({ title: p.title, status: '进行中' });
                    } else if (p.stage && p.stage.startsWith('3.')) {
                        group['Web开发'].push({ title: p.title, status: '未解锁' });
                    }
                });
                this.knowledgeTree = [
                    { title: 'Python基础', status: '已完成', children: group['Python基础'] },
                    { title: 'Python进阶', status: '进行中', children: group['Python进阶'] },
                    { title: 'Web开发', status: '未解锁', children: group['Web开发'] }
                ];
            }).catch(() => {
                // mock数据
                this.knowledgeTree = [
                  {
                    title: 'Python基础',
                    status: '已完成',
                    children: [
                      { title: '变量与数据类型', status: '已完成' },
                      { title: '条件语句与循环', status: '已完成' },
                      { title: '函数与模块', status: '已完成' }
                    ]
                  },
                  {
                    title: 'Python进阶',
                    status: '进行中',
                    children: [
                      { title: '异常处理', status: '已完成' },
                      { title: '文件操作', status: '进行中' },
                      { title: '装饰器', status: '未解锁' }
                    ]
                  },
                  {
                    title: 'Web开发',
                    status: '未解锁',
                    children: [
                      { title: 'Django框架', status: '未解锁' },
                      { title: 'Flask框架', status: '未解锁' }
                    ]
                  }
                ];
            });
        },
        badgeClass(status) {
            if(status==='已完成') return 'badge bg-success';
            if(status==='进行中') return 'badge bg-warning text-white';
            return 'badge bg-secondary';
        },
        statusIcon(status) {
            if(status==='已完成') return 'fa fa-check';
            if(status==='进行中') return 'fa fa-spinner';
            return 'fa fa-lock';
        },
        statusIconClass(status) {
            if(status==='已完成') return 'bg-success';
            if(status==='进行中') return 'bg-warning';
            return 'bg-secondary';
        },
        goToKnowledgeDetail(title) {
            this.$router.push({ path: '/learn-detail', query: { id: title } });
        }
    }
}
</script>

<style type="text/tailwindcss">
@layer utilities {
    .content-auto {
        content-visibility: auto;
    }
    .text-shadow {
        text-shadow: 0 2px 4px rgba(0,0,0,0.1);
    }
    .card-hover {
        transition: all 0.3s ease;
    }
    .card-hover:hover {
        transform: translateY(-5px);
        box-shadow: 0 10px 25px -5px rgba(0, 0, 0, 0.1), 0 10px 10px -5px rgba(0, 0, 0, 0.04);
    }
    .carousel-slide {
        transition: transform 0.5s ease-in-out;
    }
    .carousel-indicator {
        transition: all 0.3s ease;
    }
    .carousel-indicator.active {
        background-color: white;
        width: 1.5rem;
    }
    .nav-indicator {
        transition: transform 0.3s cubic-bezier(.4,0,.2,1), width 0.3s cubic-bezier(.4,0,.2,1);
        transform-origin: left;
        z-index: 2;
        height: 2px !important;
    }
    .nav-link-no-underline {
        text-decoration: none !important;
    }
    .nav-link-inline {
        display: inline-block;
        padding-bottom: 0 !important;
        margin-bottom: 0 !important;
    }
}

/* 首页顶部导航栏蓝色渐变主题 */
.header-nav {
  background: linear-gradient(90deg, #2563eb 0%, #60a5fa 100%);
  box-shadow: 0 2px 8px rgba(37,99,235,0.08);
}
.logo {
  font-size: 1.5rem;
  font-weight: bold;
  color: #fff;
  letter-spacing: 2px;
  display: flex;
  align-items: center;
}
.logo i {
  margin-right: 8px;
  color: #ffe066;
}
.nav-link {
  color: #fff !important;
  font-weight: 500;
  font-size: 1.1rem;
  padding: 0.5rem 1.5rem;
  border-radius: 8px;
  transition: background 0.2s, color 0.2s;
  text-decoration: none;
  display: inline-block;
}
.nav-link:hover,
.nav-link.router-link-exact-active {
  background: rgba(255,255,255,0.18);
  color: #ffe066 !important;
  text-decoration: none;
}
@media (max-width: 768px) {
  .header-nav .container {
    flex-direction: column;
    height: auto;
    padding: 1rem 0;
  }
  .logo {
    margin-bottom: 0.5rem;
  }
  .header-nav nav {
    flex-wrap: wrap;
    justify-content: center;
  }
  .nav-link {
    margin-bottom: 0.5rem;
  }
}
.nav-indicator-white {
  position: absolute;
  height: 3px;
  border-radius: 2px;
  background: #fff;
  bottom: 0;
  transition: left 0.3s cubic-bezier(.4,0,.2,1), width 0.3s cubic-bezier(.4,0,.2,1);
  z-index: 10;
}
.knowledge-btn {
  display: inline-block;
  background: #f3f6fd;
  color: #2563eb;
  border-radius: 16px;
  padding: 4px 16px;
  margin: 2px 0;
  font-weight: 500;
  cursor: pointer;
  transition: background 0.18s, color 0.18s, box-shadow 0.18s;
  box-shadow: 0 1px 2px rgba(37,99,235,0.04);
  outline: none;
  border: none;
  text-decoration: none;
}
.knowledge-btn:hover, .knowledge-btn:focus {
  background: #2563eb;
  color: #fff;
  box-shadow: 0 2px 8px rgba(37,99,235,0.13);
  text-decoration: none;
}
.progress-list-scroll {
  background: #f8fafc;
  border-radius: 14px;
  box-shadow: 0 1px 4px rgba(37,99,235,0.04);
  padding: 12px 18px;
  max-height: 220px;
  overflow-y: auto;
  margin-bottom: 0;
}
.progress-list-scroll::-webkit-scrollbar {
  width: 6px;
  background: #f3f6fd;
  border-radius: 8px;
}
.progress-list-scroll::-webkit-scrollbar-thumb {
  background: #dbeafe;
  border-radius: 8px;
}
</style>
