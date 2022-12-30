<template>
  <div>
    <el-table :data="friendLists" style="width: 100%" stripe>
      <el-table-column>
        <template scope="scope">
          <el-avatar id="avatar" size="large" :src="friendLists[scope.$index].avatar"/>
        </template>
      </el-table-column>

      <el-table-column>
        <template scope="scope">
          <el-button slot="reference" type="text" @click="checkInfo(scope.$index)">{{scope.row.name}}</el-button>
        </template>
      </el-table-column>

      <el-table-column>
        <template scope="scope">
          <el-button size="mini" @click="sendMessage(scope.$index)">发消息</el-button>
        </template>
      </el-table-column>

      <el-table-column>
        <template scope="scope">
          <el-button size="mini" @click="deleteFri(scope.$index)">删除好友</el-button>
        </template>
      </el-table-column>
    </el-table>

    <el-dialog :visible.sync="dialogVisible" width="30%" append-to-body>
<!--      传递用户信息至该组件-->
      <UserInfo :UserInfo="curFri"></UserInfo>
    </el-dialog>
  </div>

</template>

<script>
import axios from "axios";
import UserInfo from "@/components/UserInfo/UserInfo";
import Bus from "@/assets/Bus";
export default {
  props:['drawer'],
  name: "FriendList",
  components: {UserInfo},
  data() {
    return {
      index:0,
      dialogVisible: false,
      visible: false,
      friendLists: [],
      curFri:{}
    }
  },
  methods:{
    checkInfo(index){
      let _this=this
      //初始化用户信息
      this.$http.get("http://127.0.0.1:8888/api/user/showUserInfo",{
        params:{
          userId:_this.friendLists[index].userId
        }
      }).then(res=>{
        if(res.data.data.result){
          _this.curFri=res.data.data
          console.log(_this.curFri)
        }
        else{
          console.log("error")
        }
      })
      this.dialogVisible = true
      this.index=index
    },
    deleteFri(index){
      let _this=this
      axios.delete("http://127.0.0.1:8888/api/friend/del",{
        params:{
          from:_this.$store.state.info.userId,
          to:_this.friendLists[index].userId
        }
      }).then(res=>{
        _this.friendLists.splice(index,1)
      })
      // _this.friendLists.splice(index,1)
      console.log(this.friendLists)
    },
    sendMessage(index){
      console.log(this.friendLists[index])
      let fri={
        "name":this.friendLists[index].name,
        "id":this.friendLists[index].userId,
        "avatar":this.friendLists[index].avatar,
      }
      this.$emit('close')
      Bus.$emit('sendmsg',fri)
      //Bus.$emit('sendmsg',this.friendLists[index])
    },
    initFriList(){
      let _this=this
      axios.post("http://127.0.0.1:8888/api/friend/friends",{
        name:_this.$store.state.info.userId
      }).then(res=>{
        _this.friendLists=res.data.data
        console.log(_this.friendLists)
      }).catch(function (error){
        console.log(error)
      })
    }
  },
  created() {
    //获取朋友列表
    this.initFriList()
  }
}
</script>

<style scoped>

</style>
