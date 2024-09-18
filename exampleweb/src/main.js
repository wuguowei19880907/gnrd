import {createApp} from 'vue'
import App from './App.vue'
import ElementPlus from 'element-plus'
import 'element-plus/dist/index.css'

const app = createApp(App);
const urlParams = new URLSearchParams(window.location.search);
const token_header = 'df-auth-id';
const token_ = urlParams.get(token_header);
sessionStorage.setItem(token_header, token_);
app.use(ElementPlus, { size: 'small', zIndex: 3000 });
app.mount('#app');
