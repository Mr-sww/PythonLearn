import { createApp } from 'vue';
import App from './App.vue';
import router from './router';
import 'bootstrap/dist/css/bootstrap.min.css';
import 'bootstrap/dist/js/bootstrap.bundle.min.js';
import axios from 'axios';

// 配置axios默认设置
axios.defaults.baseURL = 'http://localhost:8080';
axios.defaults.withCredentials = true;

const app = createApp(App);
app.use(router);
app.config.globalProperties.$axios = axios;
app.mount('#app');