<template>
  <div>
    <el-row>
      <el-col :span="8">
        <el-card style="width: 400px; height: 100%; color: #333">
          <div style="padding-bottom: 10px; border-bottom: 1px solid #ccc">在线用户</div>
          <div style="padding: 10px 0" v-for="user in users" :key="user.name">
            <el-avatar v-show="user.id" id="avatar" size="small" :src="user.avatar" style="margin-top: 10px;margin-left: 10px"/>
            <span style="font-size: medium">{{ user.name }}</span>
            <i class="el-icon-chat-dot-round" style="margin-left: 10px; font-size: 16px; cursor: pointer"
               @click="selectFriend(user)"></i>
<!--            <span style="font-size: 12px;color: limegreen; margin-left: 5px" v-if="user.username === chatUser">chatting...</span>-->
          </div>
        </el-card>
        <el-divider></el-divider>
      </el-col>
      <el-col :span="10">
        <div style="width: 800px; margin: 0 auto; background-color: white;
                    border-radius: 5px; box-shadow: 0 0 10px #ccc">
          <div style="text-align: left; line-height: 50px; margin-left: auto">
            <el-avatar v-show="$store.state.curFriend.id" id="avatar" size="middle" :src="$store.state.curFriend.avatar" style="margin-top: 10px;margin-left: 10px"/>
            <span style="margin-bottom: 100px;margin-left: 15px; font-size: x-large">{{$store.state.curFriend.name}}</span>
          </div>
<!--          消息框-->
          <div id="interact" style="height: 350px; overflow:auto; border-top: 1px solid #ccc" v-html="content"></div>
          <div style="height: 25px; overflow:auto; border-top: 1px solid #ccc;">
            <div class="upDemo">
              <!--文件-->
              <el-upload
                  class="upload-demo"
                  action="https://jsonplaceholder.typicode.com/posts/"
                  :before-upload="beforeFileUpload"
                  :success="handleFileSuccess"
                  :show-file-list="false"
                  :auto-upload="false"
                  style="display: inline-block"
              >
                <i class="el-icon-folder-opened" style="margin-left: 10px"></i>
              </el-upload>
              <!--图片-->
              <el-upload
                  class="upload-demo"
                  action="https://jsonplaceholder.typicode.com/posts/"
                  :before-upload="beforeImgUpload"
                  :on-change="uploadImg"
                  :success="handleImgSuccess"
                  :show-file-list="false"
                  :auto-upload="false"
                  style="display: inline-block"
              >
                <i class="el-icon-picture-outline" style="margin-left: 10px"></i>
              </el-upload>
              <!--视频-->
              <el-upload
                  class="upload-demo"
                  action="https://jsonplaceholder.typicode.com/posts/"
                  :before-upload="beforeVideoUpload"
                  :success="handleVideoSuccess"
                  :show-file-list="false"
                  style="display: inline-block"
              >
                <i class="el-icon-video-camera" style="margin-left: 10px"></i>
              </el-upload>
            </div>
          </div>
          <div style="height: 200px">
            <textarea v-model="text" style="height: 100%; width: 100%; padding: 0; border: none; border-top: 1px solid #ccc;
             border-bottom: 1px solid #ccc; outline: none; font-size: 25px; resize:none" >
            </textarea>
            <div style="text-align: right; padding-right: 10px">
              <el-button type="primary" size="mini" @click="send">发送</el-button>
              <el-button type="success" size="mini" @click="shareChat">ShareChat</el-button>
            </div>
          </div>
        </div>
      </el-col>
    </el-row>

    <el-dialog title="ShareChat" :visible.sync="shareVisible" width="30%">
      <el-table :data="$store.state.friends" style="width: 100%" stripe>
        <el-table-column prop="avatar">
          <template scope="scope">
            <el-avatar id="avatar" size="large" :src="scope.row.avatar"/>
          </template>
        </el-table-column>
        <el-table-column>
          <template scope="scope">
            <el-button slot="reference" type="text" style="font-size: x-large">{{scope.row.username}}</el-button>
          </template>
        </el-table-column>
        <el-table-column>
          <template scope="scope">
            <el-button type="success" size="mini" @click="share(scope.$index)">share</el-button>
          </template>
        </el-table-column>
      </el-table>
    </el-dialog>

  </div>
</template>

<script>
import Bus from "@/assets/Bus";
// import request from "@/utils/request";

import MayLike from "@/components/friend/MayLike";
import axios from "axios";
let socket;//websocket

export default {

  name: "ChatDemo",
  components: {MayLike},
  data() {
    return {
      shareVisible:false,
      circleUrl: 'https://cube.elemecdn.com/3/7c/3ea6beec64369c2642b92c6726f1epng.png',
      user: {},
      isCollapse: false,
      users: [],//在线好友
      chatUser: {},
      text: "",
      messages: [],
      content: '',
      timer:null
    }
  },
  created() {
    this.getOnlineUsers()
    this.timer = window.setInterval(() => {
      setTimeout(this.getOnlineUsers(), 0);
    }, 2000);
  },
  destroyed() {
    //离开页面是销毁
    clearInterval(this.timer);
    this.timer = null;
  },
  mounted() {
    Bus.$on('sendmsg',user=>{
      this.selectFriend(user)
    })
  },
  methods: {
    getOnlineUsers(){
      let _this=this
      //获取在线用户
      this.$http.get("http://127.0.0.1:8888/api/user/getAllOnlineUsers",{
        params:{
          userId:_this.$store.state.info.userId
        }}).then(function (res){
        _this.users=res.data.data.onlineFriends;

        for (let i = 0; i < _this.users.length; i++) {
          _this.users[i].avatar="https://cube.elemecdn.com/0/88/03b0d39583f48206768a7534e55bcpng.png"
        }
        console.log(res.data.data)
      })
    },
    uploadImg(file){
      let isImg=""
      let ans=["image/png","image/jpg","image/jpeg"]
      ans.forEach(ans=>{
        if (file.raw.type===ans){
          isImg=true
        }
      })
      const isLt2M = file.raw.size / 1024 / 1024 < 100;
      if (!isImg) {
        this.$message.error("上传图片只能是 JPG/PNG/JPEG 格式");
        return false
      }
      if (!isLt2M) {
        this.$message.error("上传文件大小不能超过 100MB!");
        return false
      }
      //渲染图片到前端
      this.createImg(null,this.user,URL.createObjectURL(file.raw))
      //将图片发送至websocket
      let reader=new FileReader()
      //此处file格式？？？
      reader.readAsBinaryString(file.raw)
      reader.onload=function loaded(evt){
        socket.send(evt.target.result)
      }
    },
    //文件上传成功,接收文件路径
    handleFileSuccess(res,file){
      this.createFile(null,this.user,res.msg)
    },
    //文件上传前进行校验
    beforeFileUpload(file){
      const isLt2M = file.size / 1024 / 1024 < 100;
      if (!isLt2M) {
        this.$message.error("上传文件大小不能超过 100MB!");
      }
      return isLt2M;
    },
    handleImgSuccess(res,file){
      alert("img")
      //res.msg返回图片路径
      this.createImg(null,this.user,res.msg)
    },
    //文件上传前进行校验
    beforeImgUpload(file){
      let isImg=""
      let ans=["image/png","image/jpg","image/jpeg"]
      ans.forEach(ans=>{
        if (file.type===ans){
          isImg=true
        }
      })
      const isLt2M = file.size / 1024 / 1024 < 100;
      if (!isImg) {
        this.$message.error("上传头像图片只能是 JPG/PNG/JPEG 格式!");
      }
      if (!isLt2M) {
        this.$message.error("上传文件大小不能超过 100MB!");
      }
      return isLt2M&&isImg;
    },
    handleVideoSuccess(res,file){
      //res.msg返回视频路径
      this.createVideo(null,this.user,res.msg)
    },
    //文件上传前进行校验
    beforeVideoUpload(file){
      let isVideo=""
      let ans=["video/mp4"]
      ans.forEach(ans=>{
        if (file.type===ans){
          isVideo=true
        }
      })
      const isLt2M = file.size / 1024 / 1024 < 100;
      if (!isVideo) {
        this.$message.error("上传视频只能是 MP4 格式!");
      }
      if (!isLt2M) {
        this.$message.error("上传文件大小不能超过 100MB!");
      }
      let videoUrl="C:\\Users\\86152\\Desktop\\demo.mp4"
      return isLt2M&&isVideo;
    },
    //shareChat
    share(){

    },
    shareChat(){
      this.shareVisible=true
    },
    selectFriend(user){
      this.$store.state.curFriend = user
      this.content=''
      //获得消息记录，渲染聊天页面
      let _this=this
      axios.get(
          "http://127.0.0.1:8888/api/message/recent",
          {params:{
          from:_this.$store.state.info.userId,
          to:_this.$store.state.curFriend.id
        }}).then(function (res){
          console.log(res.data.data)
        _this.messages=res.data.data//接收消息记录
        _this.messages.forEach(function (msg){
          if (msg.userId===_this.$store.state.info.userId)//用户消息
            _this.createContent(null, true, msg.messages)
          else//朋友消息
            _this.createContent(true, null, msg.messages)
        })
      }).catch(function (error){
        console.log(error)
      })
      this.initSocket()//初始化websocket
    },
    send() {
      const time = new Date();
      console.log(this.$store.state.curFriend)
      if (this.$store.state.curFriend.id===undefined) {
        this.$message({type: 'warning', message: "请选择聊天对象"})
        return;
      }
      if (!this.text) {
        this.$message({type: 'warning', message: "请输入内容"})
      } else {
        if (typeof (WebSocket) == "undefined") {
          alert("您的浏览器不支持WebSocket")
          // console.log("您的浏览器不支持WebSocket");
        } else {
          // alert("您的浏览器支持WebSocket")
          console.log("您的浏览器支持WebSocket");
          // 组装待发送的消息 json
          // {"from": "zhang", "to": "admin", "text": "聊天文本"}
          console.log(socket)
          socket.send(this.text);  // 将组装好的json发送给服务端，由服务端进行转发

          // 构建消息内容，本人消息
          this.createContent(null, this.user, this.text)
          this.text = '';
        }
      }
    },
    createVideo(remoteUser, nowUser, texts){
      let html = "";
      // 当前用户消息
      if (nowUser) { // nowUser 表示是否显示当前用户发送的聊天消息，绿色气泡
        html = "<div class=\"el-row\" style=\"padding: 5px 0\">\n" +
            "  <div class=\"el-col el-col-22\" style=\"text-align: right; padding-right: 10px\">\n" +
            "    <div class=\"tip left\">" +
            " <video src=\""+texts+"\" controls='controls'>\n"
            + "</div>\n" +
            "  </div>\n" +
            "  <div class=\"el-col el-col-2\">\n" +
            "  <span class=\"el-avatar el-avatar--circle\" style=\"height: 40px; width: 40px; line-height: 40px;\">\n" +
            " <img src=\""+nowUser.avatar+"\" style=\"object-fit: none;\">\n" +
            "  </span>\n" +
            "  </div>\n" +
            "</div>";
      } else if (remoteUser) {   // remoteUser表示远程用户聊天消息，蓝色的气泡
        console.log("flag: " + texts)
        html = "<div class=\"el-row\" style=\"padding: 5px 0\">\n" +
            "  <div class=\"el-col el-col-2\" style=\"text-align: right\">\n" +
            "  <span class=\"el-avatar el-avatar--circle\" style=\"height: 40px; width: 40px; line-height: 40px;\">\n" +
            " <img src=\""+remoteUser.avatar+"\" style=\"object-fit: scale-down;\">\n" +
            "  </span>\n" +
            "  </div>\n" +
            "  <div class=\"el-col el-col-22\" style=\"text-align: left; padding-left: 10px\">\n" +
            "    <div class=\"tip right\">" +
            " <video src=\""+texts+"\" controls='controls'>\n"
            + "</div>\n" +
            "  </div>\n" +
            "</div>";
      }//渲染消息到页面
      console.log(html)
      this.content += html;
      this.$nextTick(() => {//将聊天框高度拉到最低
        let msg = document.getElementById('interact') // 获取对象
        msg.scrollTop = msg.scrollHeight // 滚动高度
      })
    },
    createFile(remoteUser, nowUser, texts){
      let html = "";
      // 当前用户消息
      if (nowUser) { // nowUser 表示是否显示当前用户发送的聊天消息，绿色气泡
        html = "<div class=\"el-row\" style=\"padding: 5px 0\">\n" +
            "  <div class=\"el-col el-col-22\" style=\"text-align: right; padding-right: 10px\">\n" +
            "    <div class=\"tip left\">" +
            " <a href=\""+texts+"\"/>\n"+texts+"</a>\n"
            + "</div>\n" +
            "  </div>\n" +
            "  <div class=\"el-col el-col-2\">\n" +
            "  <span class=\"el-avatar el-avatar--circle\" style=\"height: 40px; width: 40px; line-height: 40px;\">\n" +
            " <img src=\""+nowUser.avatar+"\" style=\"object-fit: cover;\">\n" +
            "  </span>\n" +
            "  </div>\n" +
            "</div>";
      } else if (remoteUser) {   // remoteUser表示远程用户聊天消息，蓝色的气泡
        console.log("flag: " + texts)
        html = "<div class=\"el-row\" style=\"padding: 5px 0\">\n" +
            "  <div class=\"el-col el-col-2\" style=\"text-align: right\">\n" +
            "  <span class=\"el-avatar el-avatar--circle\" style=\"height: 40px; width: 40px; line-height: 40px;\">\n" +
            " <img src=\""+remoteUser.avatar+"\" style=\"object-fit: cover;\">\n" +
            "  </span>\n" +
            "  </div>\n" +
            "  <div class=\"el-col el-col-22\" style=\"text-align: left; padding-left: 10px\">\n" +
            "    <div class=\"tip right\">" +
            " <a href=\""+texts+"\"/>\n"+texts+"</a>\n"
            + "</div>\n" +
            "  </div>\n" +
            "</div>";
      }//渲染消息到页面
      console.log(html)
      this.content += html;
      this.$nextTick(() => {//将聊天框高度拉到最低
        let msg = document.getElementById('interact') // 获取对象
        msg.scrollTop = msg.scrollHeight // 滚动高度
      })
    },
    createImg(remoteUser, nowUser, texts){
      let html = "";
      // 当前用户消息
      if (nowUser) { // nowUser 表示是否显示当前用户发送的聊天消息，绿色气泡
        html = "<div class=\"el-row\" style=\"padding: 5px 0\">\n" +
            "  <div class=\"el-col el-col-22\" style=\"text-align: right; padding-right: 10px\">\n" +
            "    <div class=\"tip left\">" +
            " <img src=\""+texts+"\">\n"
            + "</div>\n" +
            "  </div>\n" +
            "  <div class=\"el-col el-col-2\">\n" +
            "  <span class=\"el-avatar el-avatar--circle\" style=\"height: 40px; width: 40px; line-height: 40px;\">\n" +
            " <img src=\""+nowUser.avatar+"\" style=\"object-fit: cover;\">\n" +
            "  </span>\n" +
            "  </div>\n" +
            "</div>";
      } else if (remoteUser) {   // remoteUser表示远程用户聊天消息，蓝色的气泡
        console.log("flag: " + texts)
        html = "<div class=\"el-row\" style=\"padding: 5px 0\">\n" +
            "  <div class=\"el-col el-col-2\" style=\"text-align: right\">\n" +
            "  <span class=\"el-avatar el-avatar--circle\" style=\"height: 40px; width: 40px; line-height: 40px;\">\n" +
            " <img src=\""+remoteUser.avatar+"\" style=\"object-fit: cover;\">\n" +
            "  </span>\n" +
            "  </div>\n" +
            "  <div class=\"el-col el-col-22\" style=\"text-align: left; padding-left: 10px\">\n" +
            "    <div class=\"tip right\">" +
            " <img src=\""+texts+"\">\n"
            + "</div>\n" +
            "  </div>\n" +
            "</div>";
      }//渲染消息到页面
      console.log(html)
      this.content += html;
      this.$nextTick(() => {//将聊天框高度拉到最低
        let msg = document.getElementById('interact') // 获取对象
        msg.scrollTop = msg.scrollHeight // 滚动高度
      })
    },
    createContent(remoteUser, nowUser, texts) {  // 这个方法是用来将 json的聊天消息数据转换成 html的。
      let html = "";
      // 当前用户消息
      if (nowUser) { // nowUser 表示是否显示当前用户发送的聊天消息，绿色气泡
        html = "<div class=\"el-row\" style=\"padding: 5px 0\">\n" +
            "  <div class=\"el-col el-col-22\" style=\"text-align: right; padding-right: 10px\">\n" +
            "    <div class=\"tip left\">" + texts + "</div>\n" +
            "  </div>\n" +
            "  <div class=\"el-col el-col-2\">\n" +
            "  <span class=\"el-avatar el-avatar--circle\" style=\"height: 40px; width: 40px; line-height: 40px;\">\n" +
            " <img src=\""+"https://cube.elemecdn.com/0/88/03b0d39583f48206768a7534e55bcpng.png"+"\" style=\"object-fit: cover;\">\n" +
            "  </span>\n" +
            "  </div>\n" +
            "</div>";
      } else if (remoteUser) {   // remoteUser表示远程用户聊天消息，蓝色的气泡
        html = "<div class=\"el-row\" style=\"padding: 5px 0\">\n" +
            "  <div class=\"el-col el-col-2\" style=\"text-align: right\">\n" +
            "  <span class=\"el-avatar el-avatar--circle\" style=\"height: 40px; width: 40px; line-height: 40px;\">\n" +
            " <img src=\""+"https://cube.elemecdn.com/0/88/03b0d39583f48206768a7534e55bcpng.png"+"\" style=\"object-fit: cover;\">\n" +
            "  </span>\n" +
            "  </div>\n" +
            "  <div class=\"el-col el-col-22\" style=\"text-align: left; padding-left: 10px\">\n" +
            "    <div class=\"tip right\">" + texts + "</div>\n" +
            "  </div>\n" +
            "</div>";
      }//渲染消息到页面
      this.content += html;
      this.$nextTick(() => {//将聊天框高度拉到最低
        let msg = document.getElementById('interact') // 获取对象
        msg.scrollTop = msg.scrollHeight // 滚动高度
      })
    },
    //创建websocket
    initSocket(){
      let this_ = this
      if (typeof (WebSocket) == "undefined") {
        console.log("您的浏览器不支持WebSocket");
      } else {
        console.log("您的浏览器支持WebSocket");
        let socketUrl = "ws://localhost:8888/websocket/"+this.$store.state.info.userId+"/2/"+this.$store.state.curFriend.id;
        if (socket != null) {
          socket.close();
          socket = null;
        }
        // 开启一个websocket服务
        socket = new WebSocket(socketUrl);
        //打开事件
        socket.onopen = function () {
          console.log("websocket已打开");
        };
        //  浏览器端收消息，获得从服务端发送过来的文本消息
        socket.onmessage = function (msg) {
          //根据传输进来的消息类型，如图片，视频等，调用不同的方法进行渲染
          console.log("收到数据====" + msg.data)
          console.log(this_.$store.state.curFriend)
          this_.createContent(this_.$store.state.curFriend, null, msg.data)
        };
        //关闭事件
        socket.onclose = function () {
          console.log("websocket已关闭");
        };
        //发生了错误事件
        socket.onerror = function () {
          console.log("websocket发生了错误");
        }
      }
    },
    init() {
      this.user={
        userId: this.$store.state.info.userId,
        username: "御坂美琴",
        organization: "常盘台中学",
        age: 12,
        gender: "female",
        avatar: "https://cube.elemecdn.com/0/88/03b0d39583f48206768a7534e55bcpng.png",
        description: "乐",
        address: '上海市普陀区'
      }
      //获取在线用户
      this.users=[{
        userId:"1",
        username:"1",
        organization:"华东师范大学",
        age:12,
        gender:"male",
        avatar:"https://cube.elemecdn.com/0/88/03b0d39583f48206768a7534e55bcpng.png",
        description:"乐",
        address: '上海市普陀区'
      }, {
        userId:"2",
        username:"2",
        organization:"华东师范大学",
        age:12,
        gender:"male",
        avatar:"https://cube.elemecdn.com/0/88/03b0d39583f48206768a7534e55bcpng.png",
        description:"乐",
        address: '上海市普陀区'
      }, {
        userId:"3",
        username:"3",
        organization:"华东师范大学",
        age:12,
        gender:"male",
        avatar:"https://cube.elemecdn.com/0/88/03b0d39583f48206768a7534e55bcpng.png",
        description:"乐",
        address: '上海市普陀区'
      }, {
        userId:"4",
        username:"4",
        organization:"华东师范大学",
        age:12,
        gender:"male",
        avatar:"https://cube.elemecdn.com/0/88/03b0d39583f48206768a7534e55bcpng.png",
        description:"乐",
        address: '上海市普陀区'
      }]
      /*if (typeof (WebSocket) == "undefined") {
        console.log("您的浏览器不支持WebSocket");
      } else {
        console.log("您的浏览器支持WebSocket");
        let socketUrl = "ws://localhost:8888/websocket/"+this.user.userId+"/2/"+this.$store.state.curFriend.userId;
        if (socket != null) {
          socket.close();
          socket = null;
        }
        // 开启一个websocket服务
        socket = new WebSocket(socketUrl);
        //打开事件
        socket.onopen = function () {
          console.log("websocket已打开");
        };
        //  浏览器端收消息，获得从服务端发送过来的文本消息
        socket.onmessage = function (msg) {
          console.log("收到数据====" + msg)
          _this.createContent(this.$store.state.curFriend, null, msg)
        };
        //关闭事件
        socket.onclose = function () {
          console.log("websocket已关闭");
        };
        //发生了错误事件
        socket.onerror = function () {
          console.log("websocket发生了错误");
        }
      }*/
    }
  }
}

</script>

<style>
.el-carousel__item h3 {
  color: #475669;
  font-size: 14px;
  opacity: 0.75;
  line-height: 150px;
  margin: 0;
}

.el-carousel__item:nth-child(2n) {
  background-color: #99a9bf;
}

.el-carousel__item:nth-child(2n+1) {
  background-color: #d3dce6;
}
.tip {
  color: white;
  text-align: center;
  border-radius: 10px;
  font-family: sans-serif;
  padding: 10px;
  width:auto;
  display:inline-block !important;
}

.right {
  background-color: deepskyblue;
}
.left {
  background-color: forestgreen;
}
.upDemo{
  display: inline;
}
</style>

