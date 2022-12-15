import Vue from 'vue'
import Vuex from 'vuex'

Vue.use(Vuex)

export default new Vuex.Store({
  state: {
    joinRoom:[],
    leaveRoom:{},
    // id（邮箱）,用户名, 学校/公司, 年龄, 性别，头像, 地址（省/市）,个性签名（个人描述）, 待定:（朋友圈）
    info:{
      userId:"",
      username:"tyrion",
      password:""
    },
    curFriend:{},
    friends:[]
  },
  mutations: {
    setJoinRoom(state,payload){
      state.joinRoom.push(payload)
    },
    setLeaveRoom(state,payload){
      state.leaveRoom.username = payload.username
    },
    SetInfo(state,userInfo){
      state.info.userId=userInfo.userId
      state.info.password=userInfo.password

      //获得好友列表
      state.friends=[{
        userId:"146895172@qq.com",
        username:"tyrion",
        organization:"华东师范大学",
        age:12,
        gender:"male",
        avatar:"https://cube.elemecdn.com/0/88/03b0d39583f48206768a7534e55bcpng.png",
        description:"乐",
        address: '上海市普陀区'
      }, {
        userId:"144895172@qq.com",
        username:"mbt",
        organization:"华东师范大学",
        age:12,
        gender:"male",
        avatar:"https://cube.elemecdn.com/0/88/03b0d39583f48206768a7534e55bcpng.png",
        description:"乐",
        address: '上海市普陀区'
      }, {
        userId:"144695172@qq.com",
        username:"cgy",
        organization:"华东师范大学",
        age:12,
        gender:"male",
        avatar:"https://cube.elemecdn.com/0/88/03b0d39583f48206768a7534e55bcpng.png",
        description:"乐",
        address: '上海市普陀区'
      }, {
        userId:"144685172@qq.com",
        username:"lsw",
        organization:"华东师范大学",
        age:12,
        gender:"male",
        avatar:"https://cube.elemecdn.com/0/88/03b0d39583f48206768a7534e55bcpng.png",
        description:"乐",
        address: '上海市普陀区'
      }]
    },
    SetCurFir(state,curFriInfo){
      state.curFriend=curFriInfo

    }
},
  actions: {
    setUserInfo(context, userInfo){
      context.commit('SetInfo',userInfo)
    },
    setCurFri(context,curFri){
      context.commit('SetCurFir',curFri)
    }
  },
  modules: {
  }
})
