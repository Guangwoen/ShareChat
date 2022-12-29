<template>
  <div>
    <el-table :data="friendLists" style="width: 100%" stripe>
      <el-table-column>
        <el-avatar id="avatar" size="large" :src="friendLists[this.index].avatar"/>
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
      <UserInfo :UserInfo="friendLists[this.index]"></UserInfo>
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
      friendLists: []
    }
  },
  methods:{
    checkInfo(index){
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
        "avatar":"https://cube.elemecdn.com/0/88/03b0d39583f48206768a7534e55bcpng.png"
      }
      this.$emit('close')
      Bus.$emit('sendmsg',fri)
      //Bus.$emit('sendmsg',this.friendLists[index])
    }
  },
  created() {
    //获取朋友列表
    let _this=this
    axios.post("http://127.0.0.1:8888/api/friend/friends",{
      name:_this.$store.state.info.userId
    }).then(res=>{
      console.log(res.data.data)
      _this.friendLists=res.data.data
      for (let i = 0; i < _this.friendLists.length; i++) {
        _this.friendLists[i].avatar="https://cube.elemecdn.com/0/88/03b0d39583f48206768a7534e55bcpng.png"
      }
      }).catch(function (error){
        console.log(error)
      })
    // id（邮箱）,用户名, 学校/公司, 年龄, 性别，头像, 地址（省/市）,个性签名（个人描述）, 待定:（朋友圈）
  }
}
</script>

<style scoped>

</style>
