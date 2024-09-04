import {createRouter, createWebHistory} from "vue-router";
import Login from './components/Login.vue';
import CenterContent from './components/CenterContent.vue';
import Permissions from './components/Permissions.vue';
import Users from './components/Users.vue';
import Roles from './components/Roles.vue';


const routes = [
    {
        path: '/',
        redirect: '/login',
    },
    {
        path: '/login',
        name: 'Login',
        component: Login,
    },
    {
        path: '/index',
        name: 'CenterContent',
        component: CenterContent,
        children: [
            {path: 'users', component: Users},
            {path: 'permissions', component: Permissions},
            {path: 'roles', component: Roles},
        ]
    },
];

const router = createRouter({
    history: createWebHistory(),
    routes,
});

export default router;