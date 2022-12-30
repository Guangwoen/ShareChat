<template>
  <div id="topbar">
    <el-col span="20">
      <h>聚信</h>
    </el-col>
    <el-breadcrumb separator="">
      <el-breadcrumb-item>
          <el-tooltip content="通知" placement="bottom" effect="light">
            <el-badge :hidden="!dotVisible" value="new" class="item">
              <el-button @click="checkReport" class="button" icon="el-icon-message-solid" circle/>
            </el-badge>
          </el-tooltip>
      </el-breadcrumb-item>
      <el-breadcrumb-item>
        <el-tooltip content="个人资料" placement="bottom" effect="light">
          <el-button class="button" @click="showUserInfo" id="info" icon="el-icon-edit" circle/>
        </el-tooltip>
      </el-breadcrumb-item>
      <el-breadcrumb-item>
        <el-tooltip content="查找用户" placement="bottom" effect="light">
          <el-button class="button" @click="searchVisible=true" id="search" icon="el-icon-search" circle></el-button>
        </el-tooltip>
      </el-breadcrumb-item>
      <el-breadcrumb-item>
        <el-avatar id="avatar" size="middle" :src="linkNode.avatar"/>
      </el-breadcrumb-item>
    </el-breadcrumb>
    <el-dialog :visible.sync="dialogVisible" width="30%" >
<!--      利用props传入用户信息-->
      <UserInfo :UserInfo="linkNode" v-show="change"></UserInfo>
      <span slot="footer" class="dialog-footer">
        <el-button type="primary" size="small" @click="changeInfo">修改资料</el-button>
      </span>
    </el-dialog>
<!--    修改信息表单-->
    <el-dialog :visible.sync="formVisible" width="30%">
      <ChangeInfo :UserInfo="linkNode" :formVisible="formVisible" @childVisible="parentVisible"/>
    </el-dialog>
<!--    查找用户-->
    <el-dialog :visible.sync="searchVisible" width="30%">
      <el-input placeholder="请输入ID或用户名" prefix-icon="el-icon-search" v-model="searchText"/>
      <el-button style="text-align: right" type="primary" @click="search">确定</el-button>
    </el-dialog>
<!--    找到的用户信息-->
    <el-dialog :visible.sync="wantedVisible" width="40%" append-to-body>
      <!--      传递用户信息至该组件-->
      <el-table :data="wantedUsers" style="width: 100%" stripe>
        <el-table-column>
          <template scope="scope">
            <p>{{scope.row.userId}}</p>
          </template>
        </el-table-column>
        <el-table-column style="width: auto">
          <template scope="scope">
            <el-button slot="reference" type="text">{{scope.row.username}}</el-button>
          </template>
        </el-table-column>
        <el-table-column style="width: auto">
          <template scope="scope">
            <el-button v-show="wantedUsers[scope.$index].flag" disabled size="mini">已关注</el-button>
            <el-button v-show="!wantedUsers[scope.$index].flag" size="mini" @click="addFriend(scope.$index)">关注</el-button>
          </template>
        </el-table-column>
      </el-table>
    </el-dialog>
<!--    share请求-->
    <el-dialog :visible.sync="shareVisible" width="40%" append-to-body>
      <!--      传递用户信息至该组件-->
      <el-table :data="shareRequests" style="width: 100%" stripe>
        <el-table-column>
          <template scope="scope">
            <el-avatar :src="scope.row.avatar"></el-avatar>
          </template>
        </el-table-column>
        <el-table-column style="width: auto">
          <template scope="scope">
            <el-text slot="reference" type="text">{{scope.row.name}}</el-text>
          </template>
        </el-table-column>
        <el-table-column style="width: auto">
          <template scope="scope">
            <el-button @click="agree(scope.$index)">agree</el-button>
          </template>
        </el-table-column>
      </el-table>
    </el-dialog>
  </div>
</template>

<script>
import UserInfo from "@/components/UserInfo/UserInfo";
import axios from "axios";
import ChangeInfo from "@/components/UserInfo/ChangeInfo";
import Bus from "@/assets/Bus";
export default {
  name: "TopBar",
  components: {ChangeInfo, UserInfo},
  data(){
    return{
      timer:null,
      shareRequests:[],
      shareVisible:false,
      dotVisible:false,
      wantedVisible:false,
      wantedUsers:[],
      searchText:"",
      searchVisible:false,
      formVisible:false,
      change:true,
      size: '',
      dialogVisible: false,
      // id（邮箱）,用户名, 学校/公司, 年龄, 性别，头像, 地址（省/市）,个性签名（个人描述）, 待定:（朋友圈）
      linkNode:{
        userId:"",//邮箱
        username:"",//用户名
        organization:"",//学校/公司
        age:"",//年龄
        gender:"",//性别
        avatar:"https://cube.elemecdn.com/0/88/03b0d39583f48206768a7534e55bcpng.png",//头像
        address:"",//地址（省/市）
        description:""//个性签名
      }
    }
  },
  methods: {
    agree(index){
      this.shareVisible=false
      //userId,name,avatar
      let fri={
        "name":this.shareRequests[index].name,
        "id":this.shareRequests[index].userId,
        "avatar":this.shareRequests[index].avatar,
      }
      Bus.$emit('shareChat',fri)
    },
    checkReport(){
      this.dotVisible=false
      let _this=this
      axios.get("http://127.0.0.1:8888/api/share/getAllReq",{
        params:{
          userId:_this.$store.state.info.userId,
        }
      }).then(function (res){
        let req={
          userId:res.data.data.userId,
          avatar:res.data.data.avatar,
          name:res.data.data.name
        }
        _this.shareRequests.push(req)
        console.log(req)
        console.log(_this.shareRequests)
      })
      this.shareVisible=true
    },
    addFriend(index){
      let _this=this
      axios.get("http://127.0.0.1:8888/api/friend/new",{
        params:{
          userId:_this.$store.state.info.userId,
          friendId:_this.wantedUsers[index].userId
        }
      })
      this.wantedUsers[index].flag=true
    },
    //查找用户
    search(){
      //传输用户ID或用户名
      let _this=this
      axios.get("http://127.0.0.1:8888/api/friend/match",{
        params:{
          userId:_this.$store.state.info.userId,
          info:_this.searchText
        }
      }).then(function (res){
        console.log(res.data)
        _this.wantedUsers=res.data.data
      })
      this.searchText=""
      this.searchVisible=false
      this.wantedVisible=true
    },
    parentVisible(){
      this.formVisible=false
    },
    childVisible() {
      this.formVisible = false
    },
    changeInfo(){
      this.dialogVisible=false
      this.formVisible=true
    },
    //从后端获取用户信息
    showUserInfo(){
      this.dialogVisible = true
      let _this=this
      //初始化用户信息
      this.$http.get("http://127.0.0.1:8888/api/user/showUserInfo",{
       params:{
         userId:_this.$store.state.info.userId
       }
      }).then(res=>{
            if(res.data.data.result){
              _this.linkNode=res.data.data
              _this.$store.state.userInfo=_this.linkNode
            }
            else{
              console.log("error")
            }
      })
    },
    ifReport(){
      let _this=this
      axios.get(
          "http://127.0.0.1:8888/api/share/isShareReq",
          {params:{
              userId:_this.$store.state.info.userId,
            }}).then(function (res){
          _this.dotVisible=res.data.data
      }).catch(function (error){
        console.log(error)
      })
    }
  },
  computed:{

  },
  created() {
    this.timer = window.setInterval(() => {
      setTimeout(this.ifReport(), 0);
    }, 5000);
    let _this=this
    //初始化用户信息
    this.$http.get("http://127.0.0.1:8888/api/user/showUserInfo",{
      params:{
        userId:_this.$store.state.info.userId
      }
    }).then(res=>{
      if(res.data.data.result){
        _this.linkNode=res.data.data
        _this.$store.state.userInfo=_this.linkNode
        console.log(_this.linkNode)
      }
      else{
        console.log("error")
      }
    })
  },
  destroyed() {
    //离开页面是销毁
    clearInterval(this.timer);
    this.timer = null;
  },
  mounted() {
    this.linkNode.userId=this.$store.state.info.userId
  }
}
</script>

<style scoped>
#topbar{
  top: 0
}
#username{
  right: 20px;
  top: 0px;
  font-size: 18px ;
  position: relative;
}
.item {
  margin-top: 2px;
  margin-right: 0px;
  margin-bottom: 20px;
}
#avatar{
  left:10px;
  right:0;
  bottom:0;
  position: relative;
  top:10px;
}
.button{
  bottom:0;
  position: relative;
  top:10px;
}
.transition-box {
  margin-bottom: 10px;
  width: 200px;
  height: 100px;
  border-radius: 4px;
  background-color: #409EFF;
  text-align: center;
  color: #fff;
  padding: 40px 20px;
  box-sizing: border-box;
  margin-right: 20px;}
</style>
