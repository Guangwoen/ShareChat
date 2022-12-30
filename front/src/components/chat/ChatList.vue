<template>
<div>
  <el-table :data="friendLists" style="width: 100%" stripe>
    <el-table-column width="50">
      <template scope="scope">
        <el-avatar id="avatar" size="large" :src="friendLists[scope.$index].avatar"/>
      </template>
    </el-table-column>

    <el-table-column>
      <template scope="scope">
        <div style="width: 200px">
          <span style="font-weight: bold">{{scope.row.username}}</span>
          <span style="margin: 18px;">{{scope.row.time}}</span>
        </div>
        <div style="color: #8c939d; overflow: hidden;white-space: nowrap;text-overflow:ellipsis;">{{friendLists[scope.$index].msg}}</div>
      </template>
    </el-table-column>
    <el-table-column>
      <template scope="scope">
        <el-badge :is-dot="friendLists[scope.$index].unread" class="item">
          <el-button size="mini" @click="checkMessage(scope.$index)">查看</el-button>
        </el-badge>
      </template>
    </el-table-column>
  </el-table>
</div>
</template>

<script>
import Bus from "@/assets/Bus";
import axios from "axios";
import moment from 'moment'

export default {
  name: "ChatList",
  data(){
    return{
      //朋友id，用户名和最后一条聊天记录
      friendLists:[],
    }
  },
  methods:{
    //查看信息
    checkMessage(index){
      this.friendLists[index].unread=0
      let fri={
        "name":this.friendLists[index].username,
        "id":this.friendLists[index].userId,
        "avatar":"https://cube.elemecdn.com/0/88/03b0d39583f48206768a7534e55bcpng.png"
      }
      console.log(this.friendLists[index])
      console.log(fri)
      Bus.$emit('sendmsg',fri)
      this.$emit('close')
    }
  },
  computed:{
  },
  created() {
    //获取消息列表
    let _this=this
    let userId=this.$store.state.info.userId
    axios.get(
        "http://127.0.0.1:8888/api/message/list",
        {params:{
                userId: userId
      }}).then(function (res){
      _this.friendLists=res.data.data
      let now=moment.utc().local().format('YYYY-MM-DD')//当前时间
      for (let i = 0; i < _this.friendLists.length; i++) {
        let fri=_this.friendLists[i]
        if(fri.time===null){
          fri.time=""
          break;
        }
        let date=moment.utc(fri.time).local().format('YYYY-MM-DD')
        let time=moment.utc(fri.time).local().format('HH:mm')
        if(date<now)//今天之前
          fri.time=date
        else//今天
          fri.time=time
      }
    }).catch(function (error){
      console.log(error)
    })
    //处理时间字段
    /*let demo="2022-06-14T09:36:41.180127"
    let now=moment.utc().local().format('HH:mm:ss')//当前时间
    alert(_this.friendLists.length)
    this.friendLists.forEach(function (fri){
      alert("hello")
      let date=moment.utc(fri.time).local().format('YYYY-MM-DD')
      let time=moment.utc(fri.time).local().format('HH:mm:ss')
      if(date<now)//今天之前
        fri.time=date
      else//今天
        fri.time=time
    })
    for (let i = 0; i < this.friendLists.length; i++) {
      let fri=this.friendLists[i]
      alert("hello")
      let date=moment.utc(fri.time).local().format('YYYY-MM-DD')
      let time=moment.utc(fri.time).local().format('HH:mm:ss')
      if(date<now)//今天之前
        fri.time=date
      else//今天
        fri.time=time
    }*/
  }
}
</script>

<style scoped>
.item {
  margin-top: 10px;
  margin-right: 40px;
}
</style>
