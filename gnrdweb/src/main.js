import {createApp} from 'vue'
import App from './App.vue'
import ElementPlus from 'element-plus'
import router from './router.js'
import 'element-plus/dist/index.css'

const app = createApp(App).use(router);
app.use(ElementPlus, { size: 'small', zIndex: 3000 });
app.mount('#app');
