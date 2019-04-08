import Vue from 'vue'
import App from './App.vue'
import router from './router'
import axios from 'axios';
import ElementUI from 'element-ui';
import 'element-ui/lib/theme-chalk/index.css'; // 默认主题
// import '../static/css/theme-green/index.css';       // 浅绿色主题
import './assets/css/icon.css';
import './components/common/directives';
import "babel-polyfill";
import http from './components/common/http';

Vue.config.productionTip = false
Vue.use(ElementUI, {
    size: 'small'
});
Vue.prototype.$axios = axios;

Vue.prototype.$http = http;

//使用钩子函数对路由进行权限跳转
router.beforeEach((to, from, next) => {

    if(to.path == '/login' ){
        next();
    }else{
        let user = localStorage.getItem('user');
        if (user) {
            next();
        } else {
            let userData = JSON.parse(user);
            console.log(userData);
            next('/login');
        }
    }
})


new Vue({
    router,
    render: h => h(App)
}).$mount('#app')