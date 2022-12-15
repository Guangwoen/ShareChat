<!--
<template>
  <div class="chat-box">
    <header>聊天室人数：{{count}}</header>
    <div class="msg-box" ref="msg-box">
      <div
          v-for="(i,index) in list"
          :key="index"
          class="msg"
          :style="i.userId === userId?'flex-direction:row-reverse':''"
      >
        <div class="user-head">
          <div
              class="head"
              :style="` background: hsl(${getUserHead(i.userId,'bck')}, 88%, 62%); clip-path:polygon(${getUserHead(i.userId,'polygon')}% 0,100% 100%,0% 100%); transform: rotate(${getUserHead(i.userId,'rotate')}deg)`"
          ></div>
        </div>
        <div class="user-msg">
          <span
              :style="i.userId === userId?'float: right':''"
              :class="i.userId === userId?'right':'left'"
          >{{i.content}}</span>
        </div>
      </div>
    </div>
    <div class="input-box">
      <input type="text" ref="sendMsg" v-model="contentText" @keyup.enter="sendText()" />
      <div class="btn" :class="{['btn-active']:contentText}" @click="sendText()">发送</div>
    </div>
  </div>
</template>

<script>
export default {
  name:"ChatDemo",
  data() {
    return {
      ws: null,
      count: 0,
      userId: null, //当前用户ID
      list: [], //聊天记录的数组
      contentText: "", //input输入的值
    };
  },
  created() {
    this.getUserID();
  },
  mounted() {
    this.initWebSocket();
  },
  methods: {
    //根据时间戳作为当前用户ID
    getUserID() {
      this.userId = new Date().getTime();
    },
    //根据userID生成一个随机头像
    getUserHead(id, type) {
      let ID = String(id);
      if (type === "bck") {
        return Number(ID.substring(ID.length - 3));
      }
      if (type === "polygon") {
        return Number(ID.substring(ID.length - 2));
      }
      if (type === "rotate") {
        return Number(ID.substring(ID.length - 3));
      }
    },
    //滚动条到底部
    scrollBottom() {
      let el = this.$refs["msg-box"];
      el.scrollTop = el.scrollHeight;
    },
    //发送聊天信息
    sendText() {
      let _this = this;
      _this.$refs["sendMsg"].focus();
      if (!_this.contentText) {
        return;
      }
      let params = {
        userId: _this.userId,
        msg: _this.contentText,
      };
      _this.ws.send(JSON.stringify(params)); //调用WebSocket send()发送信息的方法
      _this.contentText = "";
      setTimeout(() => {
        _this.scrollBottom();
      }, 500);
    },
    //进入页面创建websocket连接
    initWebSocket() {
      let _this = this;
      //判断页面有没有存在websocket连接
      if (window.WebSocket) {
        // 此处的 :8181 端口号 要与后端配置的一致
        // let ws = new WebSocket("ws://192.168.5.42:9502");
        let ws = new WebSocket("ws://localhost:8181"); //这里是我本地测试
        _this.ws = ws;
        ws.onopen = function (e) {
          console.log("服务器连接成功");
        };
        ws.onclose = function (e) {
          console.log("服务器连接关闭");
        };
        ws.onerror = function () {
          console.log("服务器连接出错");
        };
        ws.onmessage = function (e) {
          //接收服务器返回的数据
          let resData = JSON.parse(e.data);
          if (resData.funName === "userCount") {
            _this.count = resData.users;
            _this.list = resData.chat;
          } else {
            _this.list = [
              ..._this.list,
              { userId: resData.userId, content: resData.msg },
            ];
          }
        };
      }
    },
  },
};
</script>

<style lang="scss" scoped>
.chat-box {
  margin: 0 auto;
  background: #fafafa;
  position: absolute;
  height: 100%;
  width: 100%;
  // max-width: 700px;
  header {
    position: fixed;
    width: 100%;
    height: 3rem;
    background: #409eff;
    // max-width: 700px;
    display: flex;
    justify-content: center;
    align-items: center;
    font-weight: bold;
    color: white;
    font-size: 1rem;
  }
  .msg-box {
    position: absolute;
    height: calc(100% - 6.5rem);
    width: 100%;
    margin-top: 3rem;
    overflow-y: scroll;
    .msg {
      width: 95%;
      min-height: 2.5rem;
      margin: 1rem 0.5rem;
      position: relative;
      display: flex;
      justify-content: flex-start !important;
      .user-head {
        min-width: 2.5rem;
        width: 20%;
        width: 2.5rem;
        height: 2.5rem;
        border-radius: 50%;
        background: #f1f1f1;
        display: flex;
        justify-content: center;
        align-items: center;
        .head {
          width: 1.2rem;
          height: 1.2rem;
        }
        // position: absolute;
      }
      .user-msg {
        width: 80%;
        // position: absolute;
        word-break: break-all;
        position: relative;
        z-index: 5;
        span {
          display: inline-block;
          padding: 0.5rem 0.7rem;
          border-radius: 0.5rem;
          margin-top: 0.2rem;
          font-size: 0.88rem;
        }
        .left {
          background: white;
          animation: toLeft 0.5s ease both 1;
        }
        .right {
          background: #53a8ff;
          color: white;
          animation: toright 0.5s ease both 1;
        }
        @keyframes toLeft {
          0% {
            opacity: 0;
            transform: translateX(-10px);
          }
          100% {
            opacity: 1;
            transform: translateX(0px);
          }
        }
        @keyframes toright {
          0% {
            opacity: 0;
            transform: translateX(10px);
          }
          100% {
            opacity: 1;
            transform: translateX(0px);
          }
        }
      }
    }
  }
  .input-box {
    padding: 0 0.5rem;
    position: absolute;
    bottom: 0;
    width: 97%;
    height: 3.5rem;
    background: #fafafa;
    box-shadow: 0 0 5px #ccc;
    display: flex;
    justify-content: space-between;
    align-items: center;
    input {
      height: 2.3rem;
      display: inline-block;
      width: 100%;
      padding: 0.5rem;
      border: none;
      border-radius: 0.2rem;
      font-size: 0.88rem;
    }
    .btn {
      height: 2.3rem;
      min-width: 4rem;
      background: #e0e0e0;
      padding: 0.5rem;
      font-size: 0.88rem;
      color: white;
      text-align: center;
      border-radius: 0.2rem;
      margin-left: 0.5rem;
      transition: 0.5s;
      line-height: 2.3rem;
    }
    .btn-active {
      background: #409eff;
    }
  }
}
</style>
-->
<template>
  <div>
    <el-row>
      <el-col :span="8">
        <el-card style="width: 400px; height: 100%; color: #333">
          <div style="padding-bottom: 10px; border-bottom: 1px solid #ccc">在线用户</div>
          <div style="padding: 10px 0" v-for="user in users" :key="user.username">
            <el-avatar v-show="user.userId" id="avatar" size="small" :src="user.avatar" style="margin-top: 10px;margin-left: 10px"/>
            <span style="font-size: medium">{{ user.username }}</span>
            <i class="el-icon-chat-dot-round" style="margin-left: 10px; font-size: 16px; cursor: pointer"
               @click="selectFriend(user)"></i>
            <span style="font-size: 12px;color: limegreen; margin-left: 5px" v-if="user.username === chatUser">chatting...</span>
          </div>
        </el-card>
        <el-divider></el-divider>
        <div class="block" style="width: 400px; height: 100px">
          <el-carousel indicator-position="outside" trigger="click" height="250px" width="100%" :autoplay="false">
            <el-carousel-item v-for="item in 4" :key="item">
              <div style="height: 350px; overflow:auto; border-top: 1px solid #ccc" v-html="content"></div>
            </el-carousel-item>
          </el-carousel>
        </div>
      </el-col>
      <el-col :span="10">
        <div style="width: 800px; margin: 0 auto; background-color: white;
                    border-radius: 5px; box-shadow: 0 0 10px #ccc">
          <div style="text-align: left; line-height: 50px; margin-left: auto">
            <el-avatar v-show="$store.state.curFriend.userId" id="avatar" size="middle" :src="$store.state.curFriend.avatar" style="margin-top: 10px;margin-left: 10px"/>
            <span style="margin-bottom: 100px;margin-left: 15px; font-size: x-large">{{$store.state.curFriend.username}}</span>
          </div>

<!--          消息框-->
          <div id="interact" style="height: 350px; overflow:auto; border-top: 1px solid #ccc" v-html="content"></div>

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
let socket;

export default {
  name: "ChatDemo",
  components: {MayLike},
  data() {
    return {
      shareVisible:false,
      circleUrl: 'https://cube.elemecdn.com/3/7c/3ea6beec64369c2642b92c6726f1epng.png',
      user: {},
      isCollapse: false,
      users: [],//好友
      chatUser: {},
      text: "",
      messages: [],
      content: ''
    }
  },
  created() {
    // this.init()
    this.initDemo()
  },
  mounted() {
    Bus.$on('sendmsg',user=>{
      this.selectFriend(user)
    })
  },
  methods: {
    share(){

    },
    shareChat(){
      this.shareVisible=true
    },
    selectFriend(user){
      this.$store.state.curFriend = user
      this.content=''
      //获得消息记录
      let texts=["嘿","你丫瞅什么呢","是我","你爹","对了","要不要来我的妙妙屋"];
      for (let i = 0; i < texts.length; i++) {
        if (i%2===0)
          this.createContent(null, this.user, texts[i])
        else
          this.createContent(this.user, null, texts[i])
      }
    },
    send() {
      const time = new Date();

      if (this.$store.state.curFriend.userId===undefined) {
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
          /*let message = {from: this.user.userId, to: this.$store.state.curFriend.userId, text: this.text}
          socket.send(JSON.stringify(message));  // 将组装好的json发送给服务端，由服务端进行转发*/
          this.messages.push({user: this.user.userId, text: this.text})

          // 构建消息内容，本人消息
          this.createContent(null, this.user, this.text)
          this.text = '';
        }
      }
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
            " <img src=\""+nowUser.avatar+"\" style=\"object-fit: cover;\">\n" +
            "  </span>\n" +
            "  </div>\n" +
            "</div>";
      } else if (remoteUser) {   // remoteUser表示远程用户聊天消息，蓝色的气泡
        html = "<div class=\"el-row\" style=\"padding: 5px 0\">\n" +
            "  <div class=\"el-col el-col-2\" style=\"text-align: right\">\n" +
            "  <span class=\"el-avatar el-avatar--circle\" style=\"height: 40px; width: 40px; line-height: 40px;\">\n" +
            " <img src=\""+remoteUser.avatar+"\" style=\"object-fit: cover;\">\n" +
            "  </span>\n" +
            "  </div>\n" +
            "  <div class=\"el-col el-col-22\" style=\"text-align: left; padding-left: 10px\">\n" +
            "    <div class=\"tip right\">" + texts + "</div>\n" +
            "  </div>\n" +
            "</div>";
      }
      console.log(html)
      this.content += html;
      this.$nextTick(() => {//将聊天框高度拉到最低
        let msg = document.getElementById('interact') // 获取对象
        msg.scrollTop = msg.scrollHeight // 滚动高度
      })
    },
    init() {
      this.user = sessionStorage.getItem("user") ? JSON.parse(sessionStorage.getItem("user")) : {}
      let username = this.user.username;
      let _this = this;
      if (typeof (WebSocket) == "undefined") {
        console.log("您的浏览器不支持WebSocket");
      } else {
        console.log("您的浏览器支持WebSocket");
        let socketUrl = "ws://localhost:9090/websocket/user1/2/user2/" + username;
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
          console.log("收到数据====" + msg.data)
          let data = JSON.parse(msg.data)  // 对收到的json数据进行解析， 类似这样的： {"users": [{"username": "zhang"},{ "username": "admin"}]}
          if (data.users) {  // 获取在线人员信息
            _this.users = data.users.filter(user => user.username !== username)  // 获取当前连接的所有用户信息，并且排除自身，自己不会出现在自己的聊天列表里
          } else {
            // 如果服务器端发送过来的json数据 不包含 users 这个key，那么发送过来的就是聊天文本json数据
            //  // {"from": "zhang", "text": "hello"}
            if (data.from === _this.chatUser) {
              _this.messages.push(data)
              // 构建消息内容
              // _this.createContent(data.from, null, data.text)
              _this.createContent(this.$store.state.curFriend, null, data.text)
            }
          }
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
    initDemo(){
      this.user={
        userId: this.$store.state.info.userId,
        username: "御坂美琴",
        organization: "常盘台中学",
        age: 12,
        gender: "female",
        avatar: "https://cube.elemecdn.com/0/88/03b0d39583f48206768a7534e55bcpng.png",
        description: "乐",
        address: '上海市普陀区'
      };
      //获取好友列表,获取最近聊天者以初始化聊天页面
      this.users=[{
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
      this.chatUser=this.$store.state.curFriend
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
</style>

