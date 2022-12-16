<template>
  <el-descriptions class="margin-top" title="用户资料" :column="1"  border>
<!--    <template slot="extra" >-->
<!--      <el-button v-show="this.UserInfo.userId===this.$store.state.info.userId"-->
<!--                 type="primary" size="small" @click="changeInfo">修改资料</el-button>-->
<!--    </template>-->
<!--    id（邮箱）,用户名, 学校/公司, 年龄, 性别，头像, 地址（省/市）,个性签名（个人描述）, 待定:（朋友圈）-->
    <el-descriptions-item>
      <template slot="label">
        <i class="el-icon-linkNode"></i>邮箱
      </template>
      {{UserInfo.userId}}
    </el-descriptions-item>
    <el-descriptions-item>
      <template slot="label">
        <i class="el-icon-linkNode"></i>用户名
      </template>
      {{UserInfo.username}}
    </el-descriptions-item>

    <el-descriptions-item>
      <template slot="label">
        <i class="el-icon-mobile-phone"></i>工作地点
      </template>
      {{UserInfo.organization}}
    </el-descriptions-item>

    <el-descriptions-item>
      <template slot="label">
        <i class="el-icon-linkNode"></i>年龄
      </template>
      {{UserInfo.age}}
    </el-descriptions-item>

    <el-descriptions-item>
      <template slot="label">
        <i class="el-icon-linkNode"></i>性别
      </template>
      {{UserInfo.gender}}
    </el-descriptions-item>

    <el-descriptions-item>
      <template slot="label">
        <i class="el-icon-location-outline"></i>地址
      </template>
      {{UserInfo.address}}
    </el-descriptions-item>

    <el-descriptions-item>
      <template slot="label">
        <i class="el-icon-tickets"></i>个性签名
      </template>
      {{UserInfo.description}}
    </el-descriptions-item>

  </el-descriptions>
</template>

<script>
export default {
  name: "UserInfo",
  props:['UserInfo'],//接受用户信息
  data(){
    return{
    }
  },
  methods:{
    changeInfo(){
      this.$router.push({
        path:'/changeInfo'
      })
    }
  }
}
</script>

<style scoped>

</style>
