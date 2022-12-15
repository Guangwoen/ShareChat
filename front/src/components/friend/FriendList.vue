<template>
  <div>
    <el-table :data="friendLists" style="width: 100%" stripe>
      <el-table-column>
        <el-avatar id="avatar" size="large" :src="friendLists[this.index].avatar"/>
      </el-table-column>

      <el-table-column>
        <template scope="scope">
          <el-button slot="reference" type="text" @click="checkInfo(scope.$index)">{{scope.row.username}}</el-button>
        </template>
      </el-table-column>

      <el-table-column>
        <template scope="scope">
          <el-button size="mini" @click="sendMessage(scope.$index)">发消息</el-button>
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
    sendMessage(index){
      this.$store.state.curFriend=this.friendLists[index]
      this.$emit('close')
      Bus.$emit('sendmsg',this.friendLists[index])
    }
  },
  created() {
    //获取朋友列表
    /*axios.post('#',{
        params:{
          userId:this.$store.state.info.userId
        }
      }).then(data=>{
          this.friendLists=data.data
      }).catch(function (error){
        console.log(error)
      })*/
    // id（邮箱）,用户名, 学校/公司, 年龄, 性别，头像, 地址（省/市）,个性签名（个人描述）, 待定:（朋友圈）
    this.friendLists=[{
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
  }
}
</script>

<style scoped>

</style>
