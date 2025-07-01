<template>
<div>
  <!-- 学习中心内容 -->
  <main class="container py-4">
    <!-- 页面标题 -->
    <div class="mb-4">
      <h2 class="fw-bold text-dark mb-2">我的学习中心</h2>
      <p class="text-muted mb-0">跟踪学习进度，管理学习计划</p>
    </div>

    <!-- 学习概览卡片 -->
    <div class="row g-4 mb-4">
      <div class="col-md-3">
        <div class="bg-white rounded-3 shadow-sm border p-4 text-center">
          <div class="bg-primary bg-opacity-10 rounded-circle d-inline-flex align-items-center justify-content-center mb-3" style="width:60px;height:60px;">
            <i class="fa fa-clock text-primary fa-2x"></i>
          </div>
          <h4 class="fw-bold text-dark mb-1">18.5小时</h4>
          <p class="text-muted mb-0">总学习时长</p>
        </div>
      </div>
      <div class="col-md-3">
        <div class="bg-white rounded-3 shadow-sm border p-4 text-center">
          <div class="bg-success bg-opacity-10 rounded-circle d-inline-flex align-items-center justify-content-center mb-3" style="width:60px;height:60px;">
            <i class="fa fa-check-circle text-success fa-2x"></i>
          </div>
          <h4 class="fw-bold text-dark mb-1">3门</h4>
          <p class="text-muted mb-0">完成课程</p>
        </div>
      </div>
      <div class="col-md-3">
        <div class="bg-white rounded-3 shadow-sm border p-4 text-center">
          <div class="bg-warning bg-opacity-10 rounded-circle d-inline-flex align-items-center justify-content-center mb-3" style="width:60px;height:60px;">
            <i class="fa fa-tasks text-warning fa-2x"></i>
          </div>
          <h4 class="fw-bold text-dark mb-1">24题</h4>
          <p class="text-muted mb-0">练习题目</p>
        </div>
      </div>
      <div class="col-md-3">
        <div class="bg-white rounded-3 shadow-sm border p-4 text-center">
          <div class="bg-info bg-opacity-10 rounded-circle d-inline-flex align-items-center justify-content-center mb-3" style="width:60px;height:60px;">
            <i class="fa fa-calendar text-info fa-2x"></i>
          </div>
          <h4 class="fw-bold text-dark mb-1">7天</h4>
          <p class="text-muted mb-0">连续学习</p>
        </div>
      </div>
    </div>

    <div class="row g-4">
      <!-- 学习进度树 -->
      <div class="col-lg-8">
        <div class="bg-white rounded-3 shadow-sm border p-4 mb-4">
          <div class="d-flex justify-content-between align-items-center mb-4">
            <h3 class="fw-bold text-dark mb-0">学习进度树</h3>
            <button class="btn btn-outline-primary btn-sm rounded-pill">查看完整进度</button>
          </div>
          
          <!-- 进度树内容 -->
          <div class="learning-tree">
            <!-- Python基础 -->
            <div class="learning-node completed mb-3">
              <div class="d-flex align-items-center justify-content-between p-3 border rounded-3">
                <div class="d-flex align-items-center">
                  <div class="rounded-circle bg-success d-flex align-items-center justify-content-center text-white me-3" style="width:40px;height:40px;">
                    <i class="fa fa-check"></i>
                  </div>
                  <div>
                    <h5 class="fw-bold text-dark mb-1">Python基础</h5>
                    <p class="text-muted mb-0 small">变量、数据类型、控制流、函数</p>
                  </div>
                </div>
                <div class="text-end">
                  <span class="badge bg-success">已完成</span>
                  <div class="text-muted small mt-1">100%</div>
                </div>
              </div>
              
              <!-- 子节点 -->
              <div class="ms-5 mt-2">
                <div class="d-flex align-items-center mb-2">
                  <div class="rounded-circle bg-success d-flex align-items-center justify-content-center text-white me-2" style="width:24px;height:24px;font-size:0.8rem;">
                    <i class="fa fa-check"></i>
                  </div>
                  <span class="text-dark">变量与数据类型</span>
                  <span class="badge bg-success ms-auto">已完成</span>
                </div>
                <div class="d-flex align-items-center mb-2">
                  <div class="rounded-circle bg-success d-flex align-items-center justify-content-center text-white me-2" style="width:24px;height:24px;font-size:0.8rem;">
                    <i class="fa fa-check"></i>
                  </div>
                  <span class="text-dark">条件语句与循环</span>
                  <span class="badge bg-success ms-auto">已完成</span>
                </div>
                <div class="d-flex align-items-center">
                  <div class="rounded-circle bg-success d-flex align-items-center justify-content-center text-white me-2" style="width:24px;height:24px;font-size:0.8rem;">
                    <i class="fa fa-check"></i>
                  </div>
                  <span class="text-dark">函数与模块</span>
                  <span class="badge bg-success ms-auto">已完成</span>
                </div>
              </div>
            </div>

            <!-- Python进阶 -->
            <div class="learning-node in-progress mb-3">
              <div class="d-flex align-items-center justify-content-between p-3 border rounded-3">
                <div class="d-flex align-items-center">
                  <div class="rounded-circle bg-warning d-flex align-items-center justify-content-center text-white me-3" style="width:40px;height:40px;">
                    <i class="fa fa-spinner"></i>
                  </div>
                  <div>
                    <h5 class="fw-bold text-dark mb-1">Python进阶</h5>
                    <p class="text-muted mb-0 small">异常处理、文件操作、装饰器</p>
                  </div>
                </div>
                <div class="text-end">
                  <span class="badge bg-warning text-dark">进行中</span>
                  <div class="text-muted small mt-1">65%</div>
                </div>
              </div>
              
              <!-- 子节点 -->
              <div class="ms-5 mt-2">
                <div class="d-flex align-items-center mb-2">
                  <div class="rounded-circle bg-success d-flex align-items-center justify-content-center text-white me-2" style="width:24px;height:24px;font-size:0.8rem;">
                    <i class="fa fa-check"></i>
                  </div>
                  <span class="text-dark">异常处理</span>
                  <span class="badge bg-success ms-auto">已完成</span>
                </div>
                <div class="d-flex align-items-center mb-2">
                  <div class="rounded-circle bg-warning d-flex align-items-center justify-content-center text-white me-2" style="width:24px;height:24px;font-size:0.8rem;">
                    <i class="fa fa-spinner"></i>
                  </div>
                  <span class="text-dark">文件操作</span>
                  <span class="badge bg-warning text-dark ms-auto">进行中</span>
                </div>
                <div class="d-flex align-items-center">
                  <div class="rounded-circle bg-secondary d-flex align-items-center justify-content-center text-white me-2" style="width:24px;height:24px;font-size:0.8rem;">
                    <i class="fa fa-lock"></i>
                  </div>
                  <span class="text-muted">装饰器</span>
                  <span class="badge bg-secondary ms-auto">未解锁</span>
                </div>
              </div>
            </div>

            <!-- Web开发 -->
            <div class="learning-node locked">
              <div class="d-flex align-items-center justify-content-between p-3 border rounded-3">
                <div class="d-flex align-items-center">
                  <div class="rounded-circle bg-secondary d-flex align-items-center justify-content-center text-white me-3" style="width:40px;height:40px;">
                    <i class="fa fa-lock"></i>
                  </div>
                  <div>
                    <h5 class="fw-bold text-dark mb-1">Web开发</h5>
                    <p class="text-muted mb-0 small">Django、Flask、前端集成</p>
                  </div>
                </div>
                <div class="text-end">
                  <span class="badge bg-secondary">未解锁</span>
                  <div class="text-muted small mt-1">0%</div>
                </div>
              </div>
            </div>
          </div>
        </div>

        <!-- 最近学习记录 -->
        <div class="bg-white rounded-3 shadow-sm border p-4">
          <h3 class="fw-bold text-dark mb-4">最近学习记录</h3>
          <div class="table-responsive">
            <table class="table table-hover">
              <thead>
                <tr>
                  <th>课程名称</th>
                  <th>学习时长</th>
                  <th>进度</th>
                  <th>最后学习</th>
                  <th>操作</th>
                </tr>
              </thead>
              <tbody>
                <tr>
                  <td>
                    <div class="d-flex align-items-center">
                      <img src="https://picsum.photos/60/40?random=1" class="rounded me-2" width="60" height="40" alt="课程图片">
                      <div>
                        <div class="fw-medium">Python基础入门</div>
                        <div class="text-muted small">第3章：函数与模块</div>
                      </div>
                    </div>
                  </td>
                  <td>45分钟</td>
                  <td>
                    <div class="progress" style="height:6px;">
                      <div class="progress-bar bg-success" style="width:100%"></div>
                    </div>
                    <small class="text-muted">100%</small>
                  </td>
                  <td>2小时前</td>
                  <td>
                    <button class="btn btn-outline-primary btn-sm">继续学习</button>
                  </td>
                </tr>
                <tr>
                  <td>
                    <div class="d-flex align-items-center">
                      <img src="https://picsum.photos/60/40?random=2" class="rounded me-2" width="60" height="40" alt="课程图片">
                      <div>
                        <div class="fw-medium">Python进阶实战</div>
                        <div class="text-muted small">第2章：文件操作</div>
                      </div>
                    </div>
                  </td>
                  <td>30分钟</td>
                  <td>
                    <div class="progress" style="height:6px;">
                      <div class="progress-bar bg-warning" style="width:65%"></div>
                    </div>
                    <small class="text-muted">65%</small>
                  </td>
                  <td>昨天</td>
                  <td>
                    <button class="btn btn-outline-primary btn-sm">继续学习</button>
                  </td>
                </tr>
              </tbody>
            </table>
          </div>
        </div>
      </div>

      <!-- 右侧统计和计划 -->
      <div class="col-lg-4">
        <!-- 学习统计图表 -->
        <div class="bg-white rounded-3 shadow-sm border p-4 mb-4">
          <h3 class="fw-bold text-dark mb-4">学习时间分布</h3>
          <div style="height:200px;" class="bg-light rounded-3 d-flex align-items-center justify-content-center text-muted">
            <div class="text-center">
              <i class="fa fa-chart-bar fa-3x mb-2"></i>
              <p class="mb-0">学习时间统计图表</p>
            </div>
          </div>
        </div>

        <!-- 学习计划 -->
        <div class="bg-white rounded-3 shadow-sm border p-4 mb-4">
          <div class="d-flex justify-content-between align-items-center mb-3">
            <h3 class="fw-bold text-dark mb-0">今日学习计划</h3>
            <button class="btn btn-outline-primary btn-sm rounded-pill">编辑</button>
          </div>
          <div class="space-y-2">
            <div class="d-flex align-items-center p-2 border rounded">
              <div class="rounded-circle bg-primary d-flex align-items-center justify-content-center text-white me-2" style="width:24px;height:24px;font-size:0.8rem;">
                <i class="fa fa-play"></i>
              </div>
              <div class="flex-grow-1">
                <div class="fw-medium">完成文件操作章节</div>
                <div class="text-muted small">预计30分钟</div>
              </div>
              <span class="badge bg-primary">进行中</span>
            </div>
            <div class="d-flex align-items-center p-2 border rounded">
              <div class="rounded-circle bg-secondary d-flex align-items-center justify-content-center text-white me-2" style="width:24px;height:24px;font-size:0.8rem;">
                <i class="fa fa-clock-o"></i>
              </div>
              <div class="flex-grow-1">
                <div class="fw-medium">练习题目5道</div>
                <div class="text-muted small">预计20分钟</div>
              </div>
              <span class="badge bg-secondary">待完成</span>
            </div>
          </div>
        </div>

        <!-- 学习成就 -->
        <div class="bg-white rounded-3 shadow-sm border p-4">
          <h3 class="fw-bold text-dark mb-4">学习成就</h3>
          <div class="row g-2">
            <div class="col-6">
              <div class="text-center p-2 border rounded">
                <i class="fa fa-trophy text-warning fa-2x mb-2"></i>
                <div class="fw-medium">连续学习7天</div>
                <div class="text-muted small">坚持不懈</div>
              </div>
            </div>
            <div class="col-6">
              <div class="text-center p-2 border rounded">
                <i class="fa fa-star text-warning fa-2x mb-2"></i>
                <div class="fw-medium">完成3门课程</div>
                <div class="text-muted small">学习达人</div>
              </div>
            </div>
            <div class="col-6">
              <div class="text-center p-2 border rounded">
                <i class="fa fa-clock text-info fa-2x mb-2"></i>
                <div class="fw-medium">学习18.5小时</div>
                <div class="text-muted small">时间管理</div>
              </div>
            </div>
            <div class="col-6">
              <div class="text-center p-2 border rounded">
                <i class="fa fa-check-circle text-success fa-2x mb-2"></i>
                <div class="fw-medium">完成24题</div>
                <div class="text-muted small">练习高手</div>
              </div>
            </div>
          </div>
        </div>
      </div>
    </div>
  </main>
</div>
</template>

<script>
export default {
  name: 'LearningCenter'
}
</script>

<style scoped>
.learning-tree {
  position: relative;
}

.learning-node {
  position: relative;
}

.learning-node:not(:last-child)::after {
  content: '';
  position: absolute;
  left: 20px;
  top: 60px;
  bottom: -20px;
  width: 2px;
  background: #e9ecef;
  z-index: 1;
}

.learning-node.completed::after {
  background: #28a745;
}
</style> 