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
        <el-button size="mini" @click="checkMessage(scope.$index)">查看</el-button>
      </template>
    </el-table-column>
  </el-table>
</div>
</template>

<script>
import Bus from "@/assets/Bus";

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
      Bus.$emit('sendmsg',this.friendLists[index])
      this.$emit('close')
    }
  },
  computed:{
  },
  created() {
    this.friendLists=[
      {
        username:"tyrion",
        userId:"146895172@qq.com",
        avatar:"https://cube.elemecdn.com/0/88/03b0d39583f48206768a7534e55bcpng.png",
        msg:"你好",//最后一条消息
        time:new Date()
      },
      {
        username:"mbt",
        userId:"144895172@qq.com",
        avatar:"https://cube.elemecdn.com/0/88/03b0d39583f48206768a7534e55bcpng.png",
        msg:"可以交个朋友吗",
        time:new Date()
      },
      {
        username:"cgy",
        userId:"144695172@qq.com",
        avatar:"https://cube.elemecdn.com/0/88/03b0d39583f48206768a7534e55bcpng.png",
        msg:"期待明天的见面",
        time:new Date()
      },
      {
        username:"lsw",
        userId:"144685172@qq.com",
        avatar:"https://cube.elemecdn.com/0/88/03b0d39583f48206768a7534e55bcpng.png",
        msg:"可以交个朋友吗",
        time:new Date()
      }
    ]
    this.friendLists.sort(function (fri1,fri2){
      // return fri1.time-fri2.time
      return fri1.time.getTime()-fri2.time.getTime()
    })
    let now=new Date().getFullYear()+":"+new Date().getMonth()+":"+new Date().getDate()
    for (let i = 0; i < this.friendLists.length; i++) {
      const fri=this.friendLists[i]
      let date=fri.time.getFullYear()+":"+fri.time.getMonth()+":"+fri.time.getDate()
      if (date<now){
        const month=fri.time.getMonth+1
        this.friendLists[i].time=fri.time.getFullYear()+"-"+fri.time.getMonth()+"-"+fri.time.getDate()
      }
      else {
        this.friendLists[i].time=fri.time.getHours()+":"+fri.time.getMinutes()+":"+fri.time.getSeconds()
      }
    }
  }
}
</script>

<style scoped>

</style>
