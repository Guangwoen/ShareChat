<template>
  <div id="mycard">
    <el-menu default-active="1-4-1" class="el-menu-vertical-demo" @open="handleOpen" @close="handleClose" :collapse="isCollapse">
      <el-submenu index="1">
        <template slot="title">
          <img style="height: 25px;margin-right: 30px" :src="msgImg" alt="???">
          <span slot="title">消息列表</span>
        </template>
        <el-menu-item-group>
          <el-menu-item @click="getChat(false)">我的聊天</el-menu-item>
        </el-menu-item-group>
        <el-menu-item-group>
          <el-menu-item @click="getChat(true)">ShareChat</el-menu-item>
        </el-menu-item-group>
      </el-submenu>
      <el-menu-item index="2">
        <i class="el-icon-user-solid" @click="drawer = true"/>
        <span slot="title">联系人</span>
      </el-menu-item>
      <el-menu-item index="3">
        <i class="el-icon-male" @click="dialogVisible = true"></i>
        <span slot="title">感兴趣的人</span>
      </el-menu-item>
      <el-menu-item index="4">
        <i class="el-icon-setting"></i>
        <span slot="title">设置</span>
      </el-menu-item>
      <el-menu-item index="5">
        <i v-show="!isCollapse" class="el-icon-s-fold" @click="isCollapse=true"></i>
        <i v-show="isCollapse" class="el-icon-s-unfold" @click="isCollapse=false"></i>
      </el-menu-item>
    </el-menu>
    <el-drawer title="好友列表" :visible.sync="drawer" :direction="direction" >
      <FriendList :drawer="drawer" @close="drawer=false"/>
    </el-drawer>
    <el-drawer title="消息列表" :visible.sync="msgVisible" :direction="msgdir" style="width: 65%">
      <ChatList @close="msgVisible=false"></ChatList>
    </el-drawer>
    <el-dialog :visible.sync="dialogVisible" width="30%" >
      <h2 align="center">可能感兴趣的人</h2>
      <MayLike></MayLike>
    </el-dialog>

  </div>
</template>

<script>
import FriendList from "@/components/friend/FriendList";
import MayLike from "@/components/friend/MayLike";
import ChatList from "@/components/chat/ChatList";
import axios from "axios";
export default {
  name: "MyCard",
  components: {ChatList, FriendList,MayLike},
  data() {
    return {
      msgImg:require('@/assets/img/img.png'),
      num:0,
      timer:null,
      msgVisible:false,
      dialogVisible: false,
      drawer: false,
      direction: 'rtl',
      msgdir:'ltr',
      isCollapse: true
    };
  },
  destroyed() {
    //离开页面是销毁
    clearInterval(this.timer);
    this.timer = null;
  },
  created() {
    this.timer = window.setInterval(() => {
      setTimeout(this.getProjectList(), 0);
    }, 10000);
  },
  /*mounted() {
    this.timer = window.setInterval(() => {
      setTimeout(() => {
        // this.flag=!this.flag
        alert("hello")
      },0)
    },3000)
  },*/
  methods: {
    stop() {
      clearInterval(this.timer);
      this.timer = null;
    },
    // 请求是否有新消息
    getProjectList() {
      let _this=this
      let userId=this.$store.state.info.userId
      //函数内的this指向函数本身，因此要用_this指向this
      axios.get('#',{
        params:{
          userId: userId
        }}).then(function(res){
        if (res.data.flag){
          _this.msgImg=require('@/assets/img/img2.png')
        }
      }).catch(function (error){
        console.log(error)
      })
    },
    getChat(flag){
      this.msgImg=require('@/assets/img/img.png')
      this.msgVisible=true
    },
    handleOpen(key, keyPath) {
      console.log(key, keyPath);
    },
    handleClose(key, keyPath) {
      console.log(key, keyPath);
    }
  }
}

</script>

<style scoped>
.item {
  margin-top: 20px;
  margin-right: 0px;
}
.el-menu-vertical-demo:not(.el-menu--collapse) {
  width: 200px;
  min-height: 400px;

}

</style>
