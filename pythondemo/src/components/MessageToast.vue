<template>
  <div v-if="visible" class="message-toast" :class="type">
    <div class="message-content">
      <i :class="iconClass"></i>
      <span>{{ message }}</span>
    </div>
    <button class="message-close" @click="close">
      <i class="fas fa-times"></i>
    </button>
  </div>
</template>

<script>
export default {
  name: 'MessageToast',
  data() {
    return {
      visible: false,
      message: '',
      type: 'info',
      timer: null
    }
  },
  computed: {
    iconClass() {
      const iconMap = {
        success: 'fas fa-check-circle',
        error: 'fas fa-exclamation-circle',
        warning: 'fas fa-exclamation-triangle',
        info: 'fas fa-info-circle'
      }
      return iconMap[this.type] || iconMap.info
    }
  },
  methods: {
    show(message, type = 'info', duration = 3000) {
      this.message = message
      this.type = type
      this.visible = true
      
      if (this.timer) {
        clearTimeout(this.timer)
      }
      
      if (duration > 0) {
        this.timer = setTimeout(() => {
          this.close()
        }, duration)
      }
    },
    
    close() {
      this.visible = false
      if (this.timer) {
        clearTimeout(this.timer)
        this.timer = null
      }
    }
  }
}
</script>

<style scoped>
.message-toast {
  position: fixed;
  top: 20px;
  right: 20px;
  z-index: 9999;
  display: flex;
  align-items: center;
  gap: 0.5rem;
  padding: 1rem 1.5rem;
  border-radius: 8px;
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.15);
  animation: slideIn 0.3s ease-out;
  max-width: 400px;
}

.message-toast.success {
  background: #d4edda;
  color: #155724;
  border: 1px solid #c3e6cb;
}

.message-toast.error {
  background: #f8d7da;
  color: #721c24;
  border: 1px solid #f5c6cb;
}

.message-toast.warning {
  background: #fff3cd;
  color: #856404;
  border: 1px solid #ffeaa7;
}

.message-toast.info {
  background: #d1ecf1;
  color: #0c5460;
  border: 1px solid #bee5eb;
}

.message-content {
  display: flex;
  align-items: center;
  gap: 0.5rem;
  flex: 1;
}

.message-close {
  background: none;
  border: none;
  cursor: pointer;
  opacity: 0.7;
  transition: opacity 0.2s;
}

.message-close:hover {
  opacity: 1;
}

@keyframes slideIn {
  from {
    transform: translateX(100%);
    opacity: 0;
  }
  to {
    transform: translateX(0);
    opacity: 1;
  }
}

@media (max-width: 768px) {
  .message-toast {
    top: 10px;
    right: 10px;
    left: 10px;
    max-width: none;
  }
}
</style>
