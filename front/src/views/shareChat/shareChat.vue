<template>
  <div id="shareChat">
    <el-container id="index" style="height: 100%; border: 1px solid #eee">
      <el-aside width="300px">
        <MyCard/>
      </el-aside>
      <el-container id="right">
        <el-header>
          <TopBar/>
        </el-header>
        <el-main>
          <ChatDemo/>
        </el-main>
      </el-container>
    </el-container>
  </div>
</template>

<script>
import MyCard from "@/components/MyCard/MyCard";
import TopBar from "@/components/TopBar/TopBar";
import ChatRoom from "@/views/ChatRoom";
import ChatDemo from "@/views/ChatDemo";
export default {
  name: "shareChat",
  components: {ChatDemo, ChatRoom, TopBar, MyCard},
  data() {
    const item = {
      date: '2016-05-02',
      name: '王小虎',
      address: '上海市普陀区金沙江路 1518 弄'
    };
    return {
      tableData: Array(20).fill(item)
    }
  },
  methods: {

  }
}
</script>

<style scoped>
.el-carousel__item h3 {
  color: #475669;
  font-size: 14px;
  opacity: 0.75;
  line-height: 200px;
  margin: 0;
}
.el-carousel__item:nth-child(2n) {
  background-color: #99a9bf;
}

.el-carousel__item:nth-child(2n+1) {
  background-color: #d3dce6;
}
#shareChat{
  margin: auto;
}
#index{
  height:100%
}
.el-header {
  /*background-color: #B3C0D1;*/
  /*color: #333;*/
  /*line-height: 60px;*/
  /*height: 70px;*/
  position: absolute;
  line-height: 50px;
  top: 0;
  left: 0;
  right: 0;
  background-color: #67C23A;
}

.el-aside {
  color: #DCDFE6;
  height: calc(100vh - 70px);
  position: absolute;
  /*width: 230px;*/
  top: 50px;  /* 距离上面50像素 */
  /*left: 0;*/
  /*bottom: 0;*/
  /*overflow-y: auto; !* 当内容过多时y轴出现滚动条 *!*/
  /*background-color: #333;*/
}

.el-main {
  position: absolute;
  top: 60px;
  left: 230px;
  bottom: 0;
  right: 0;  /* 距离右边0像素 */
  /*padding: 10px;*/
  overflow-y: auto; /* 当内容过多时y轴出现滚动条 */
  /* background-color: red; */
  /*padding: 0;*/
  /*height: calc(100vh - 70px);*/
}


</style>
