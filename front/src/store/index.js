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
    curFriend:{}
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
