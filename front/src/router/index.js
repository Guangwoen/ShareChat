// eslint-disable-next-line no-unused-vars
import VueRouter from "vue-router";
import LogIn from '../views/login/LogIn'
import ChatRoom from "../views/ChatRoom";
import shareChat from "@/views/shareChat/shareChat";
import ChatDemo from "../views/ChatDemo";
import SignUp from "@/views/SignUp/SignUp";
import changeInfo from "@/components/UserInfo/ChangeInfo";
export default new VueRouter({
    routes: [
        {
          path:'/',
            // redirect:'/ChatDemo'
          redirect:'/login'
        },
        {
            name:'login',
            path: '/login',
            component: LogIn,
        },
        {
            name:'shareChat',
            path:'/shareChat',
            component: shareChat
        },
        {
            name:'ChatDemo',
            path:'/ChatDemo',
            component: ChatDemo
        },
        {
            name:'SignUp',
            path:'/SignUp',
            component:SignUp
        },
        {
            name:'changeInfo',
            path:'/changeInfo',
            component:changeInfo
        }
    ]
})
