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
      Bus.$emit('sendmsg',this.friendLists[index])
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
        console.log(res.data.data)
      _this.friendLists=res.data.data
    }).catch(function (error){
      console.log(error)
    })
    /*this.friendLists=[
      {
        username:"1",
        userId:"1",
        avatar:"https://cube.elemecdn.com/0/88/03b0d39583f48206768a7534e55bcpng.png",
        msg:"你好",//最后一条消息
        time:new Date(),
        unread:0
      },
      {
        username:"2",
        userId:"2",
        avatar:"https://cube.elemecdn.com/0/88/03b0d39583f48206768a7534e55bcpng.png",
        msg:"可以交个朋友吗",
        time:new Date(),
        unread:2
      },
      {
        username:"3",
        userId:"3",
        avatar:"https://cube.elemecdn.com/0/88/03b0d39583f48206768a7534e55bcpng.png",
        msg:"期待明天的见面",
        time:new Date(),
        unread:3
      },
      {
        username:"4",
        userId:"4",
        avatar:"https://cube.elemecdn.com/0/88/03b0d39583f48206768a7534e55bcpng.png",
        msg:"可以交个朋友吗",
        time:new Date(),
        unread:4
      }
    ]*/
    //传回的用户时间字段格式？
    //按时间对用户聊天进行排序
    /*this.friendLists.sort(function (fri1,fri2){
      // return fri1.time-fri2.time
      return fri1.time.getTime()-fri2.time.getTime()
    })*/

    //处理时间字段
    let demo="2022-06-14T09:36:41.180127"
    let now=moment.utc().local().format('HH:mm:ss')//当前时间
    // alert(now)
    this.friendLists.forEach(function (fri){
      let date=moment.utc(fri.time).local().format('YYYY-MM-DD')
      let time=moment.utc(fri.time).local().format('HH:mm:ss')
      if(date<now)//今天之前
        fri.time=date
      else//今天
        fri.time=time
    })
    /*for (let i = 0; i < this.friendLists.length; i++) {
      const fri=this.friendLists[i]
      let date=fri.time.getFullYear()+":"+fri.time.getMonth()+":"+fri.time.getDate()
      if (date<now){//早于今天，按年-月-日表示
        const month=fri.time.getMonth+1
        this.friendLists[i].time=fri.time.getFullYear()+"-"+fri.time.getMonth()+"-"+fri.time.getDate()
      }
      else {//今天，按时-分-秒表示
        this.friendLists[i].time=fri.time.getHours()+":"+fri.time.getMinutes()+":"+fri.time.getSeconds()
      }
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
