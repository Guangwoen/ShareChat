import Vue from 'vue'
import App from './App.vue'
import VueRouter from 'vue-router'
import store from './store'
import router from './router'
//关闭生产提示
Vue.config.productionTip = false
/*导入ElementUI*/
import elementUI from 'element-ui';
/*导入ElementUI的CSS样式*/
import 'element-ui/lib/theme-chalk/index.css';
/*Vue使用ElementUI*/
Vue.use(elementUI);

import http from './http';  //此处问http文件的路径
Vue.prototype.$http = http;

//应用插件
Vue.use(VueRouter)
new Vue({
  el:"#app",
  render: h => h(App),
  router,
  store
})
