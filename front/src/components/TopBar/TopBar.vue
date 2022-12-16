<template>
  <div id="topbar">
    <el-col span="20">
      <h>聚信</h>
    </el-col>
    <el-breadcrumb separator="">
      <el-breadcrumb-item>
          <el-tooltip content="通知" placement="bottom" effect="light">
            <el-button class="button" icon="el-icon-message-solid" circle/>
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
      <el-table :data="wantedUser" style="width: 100%" stripe>
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
            <el-button size="mini" @click="addFriend(scope.$index)">添加好友</el-button>
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
export default {
  name: "TopBar",
  components: {ChangeInfo, UserInfo},
  data(){
    return{
      wantedVisible:false,
      wantedUser:[],
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
    addFriend(index){

    },
    search(){
      //传输用户ID或用户名
      this.searchText=""
      this.wantedUser=[{
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
      this.searchVisible=false
      this.wantedVisible=true
    },
    parentVisible(){
      this.formVisible=false
    },
    changeInfo(){
      this.dialogVisible=false
      this.formVisible=true
    },
    //从后端获取用户信息
    showUserInfo(){
      this.dialogVisible = true
      //初始化用户信息
      this.linkNode = {
        userId: this.$store.state.info.userId,
        username: "御坂美琴",
        organization: "常盘台中学",
        age: 12,
        gender: "female",
        avatar: "https://cube.elemecdn.com/0/88/03b0d39583f48206768a7534e55bcpng.png",
        description: "乐",
        address: '上海市普陀区'
      };
      // axios.get('https://localhost:8081/'+this.linkNode.userId).then(function (res){
      //   this.linkNode=res.data
      // }).catch(function (error){
      //   console.log(error)
      // })
    }
  },
  computed:{

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
  margin-top: 0px;
  margin-right: 0px;
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
