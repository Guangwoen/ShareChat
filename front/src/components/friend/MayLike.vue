<template>
  <div>
    <el-table :data="peopleList" stripe style="width: 100%">
      <el-table-column>
        <el-avatar id="avatar" size="large" :src="peopleList[this.index].avatar"/>
      </el-table-column>
      <el-table-column>
        <template scope="scope">
          <el-button slot="reference" type="text" @click="checkInfo(scope.$index)">{{scope.row.name}}</el-button>
        </template>
      </el-table-column>
      <el-table-column>
        <template scope="scope">
          <el-button v-show="peopleList[scope.$index].flag" disabled size="mini">已关注</el-button>
          <el-button v-show="!peopleList[scope.$index].flag" size="mini" @click="makeFriends(scope.$index)">关注</el-button>
        </template>
      </el-table-column>
    </el-table>
    <el-dialog :visible.sync="dialogVisible" width="30%" append-to-body>
      <!--      传递用户信息至该组件-->
      <UserInfo :UserInfo="peopleList[this.index]"></UserInfo>
      <span slot="footer" class="dialog-footer">
        <el-button @click="dialogVisible = false">取消</el-button>
        <el-button type="primary" @click="dialogVisible = false">确定</el-button>
      </span>
    </el-dialog>
  </div>

</template>

<script>
import axios from "axios";

export default {
  name: "MayLike",
  data(){
    return{
      index:0,
      dialogVisible: false,
      visible: false,
      peopleList: []
    }
  },
  methods:{
    checkInfo(index){
      this.dialogVisible = true
      this.index=index
    },
    makeFriends(index){
      let _this=this
      this.$http.get("http://127.0.0.1:8888/api/friend/new",{
        params:{
          userId:_this.$store.state.info.userId,
          friendId:_this.peopleList[index].userId
        }
      })
      this.peopleList[index].flag=true
    }
  },
  created() {
    let _this=this
    //获取朋友列表
    this.$http.get("http://127.0.0.1:8888/api/friend/recommand",{
        params:{
          userId:_this.$store.state.info.userId
        }
      }).then(res=>{
          _this.peopleList=res.data.data
          _this.peopleList.forEach(function (person){
            person.flag=false
          })
      }).catch(function (error){
        console.log(error)
      })
    // id（邮箱）,用户名, 学校/公司, 年龄, 性别，头像, 地址（省/市）,个性签名（个人描述）, 待定:（朋友圈）
  }
}
</script>

<style scoped>

</style>
