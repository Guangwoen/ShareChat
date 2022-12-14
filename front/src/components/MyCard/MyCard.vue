<template>
  <div id="mycard">
    <el-menu default-active="1-4-1" class="el-menu-vertical-demo" @open="handleOpen" @close="handleClose" :collapse="isCollapse">
      <el-submenu index="1">
        <template slot="title">
          <i class="el-icon-s-promotion"></i>
          <span slot="title">消息列表</span>
        </template>
        <el-menu-item-group>
          <span slot="title">分组一</span>
          <el-menu-item index="1-1">选项1</el-menu-item>
          <el-menu-item index="1-2">选项2</el-menu-item>
        </el-menu-item-group>
        <el-menu-item-group title="分组2">
          <el-menu-item index="1-3">选项3</el-menu-item>
        </el-menu-item-group>
        <el-submenu index="1-4">
          <span slot="title">选项4</span>
          <el-menu-item index="1-4-1">选项1</el-menu-item>
        </el-submenu>
      </el-submenu>
      <el-menu-item index="2">
        <i class="el-icon-user-solid" @click="drawer = true"></i>
        <span slot="title">联系人</span>
      </el-menu-item>
      <el-menu-item index="3">
        <i class="el-icon-male" @click="dialogVisible = true"></i>
        <span slot="title">感兴趣的人</span>
      </el-menu-item>
      <el-menu-item index="4">
        <i class="el-icon-setting"></i>
        <span slot="title">设置</span>
      </el-menu-item>
      <el-menu-item index="5">
        <i v-show="!isCollapse" class="el-icon-s-fold" @click="isCollapse=true"></i>
        <i v-show="isCollapse" class="el-icon-s-unfold" @click="isCollapse=false"></i>
      </el-menu-item>
    </el-menu>
    <el-drawer title="好友列表" :visible.sync="drawer" :direction="direction" >
      <FriendList :drawer="drawer" @close="drawer=false"/>
    </el-drawer>


    <el-dialog :visible.sync="dialogVisible" width="30%" >
      <h2 align="center">可能感兴趣的人</h2>
      <MayLike></MayLike>
    </el-dialog>

  </div>
</template>

<script>
import FriendList from "@/components/friend/FriendList";
import MayLike from "@/components/friend/MayLike";
export default {
  name: "MyCard",
  components: {FriendList,MayLike},
  data() {
    return {
      dialogVisible: false,
      drawer: false,
      direction: 'rtl',
      isCollapse: true
    };
  },
  methods: {
    handleOpen(key, keyPath) {
      console.log(key, keyPath);
    },
    handleClose(key, keyPath) {
      console.log(key, keyPath);
    }
  }
}

</script>

<style scoped>
.el-menu-vertical-demo:not(.el-menu--collapse) {
  width: 200px;
  min-height: 400px;
}
</style>
