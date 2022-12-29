<template>
  <el-form class="login-container" :model="ruleForm" :rules="rules" ref="ruleForm" label-width="100px">
    <h1 class="title">修改资料</h1>
    <!--用户名 学校/公司, 年龄, 性别，头像, 地址（省/市）,个性签名（个人描述-->

    <el-form-item label="昵称" prop="userName">
      <el-input v-model="ruleForm.userName" :placeholder="UserInfo.username" value="UserInfo.username"/>
    </el-form-item>

    <el-form-item label="工作单位" prop="organization">
      <el-input v-model="ruleForm.organization" :placeholder="UserInfo.organization"/>
    </el-form-item>

    <el-form-item label="地区" >
      <el-cascader size="large" :options="options" v-model="selectedOptions" :placeholder="UserInfo.address">
      </el-cascader>
    </el-form-item>

    <el-form-item label="年龄" prop="age">
      <el-input v-model.number="ruleForm.age" :placeholder="UserInfo.age"/>
    </el-form-item>

    <el-form-item label="性别" prop="gender">
      <el-radio-group v-model="ruleForm.gender">
        <el-radio label="男"/>
        <el-radio label="女"/>
      </el-radio-group>
    </el-form-item>

    <el-form-item label="个性签名" prop="description">
      <el-input type="textarea" v-model="ruleForm.description" :placeholder="UserInfo.description"/>
    </el-form-item>

    <el-form-item label="个人头像" prop="avatar">
      <el-upload class="avatar-uploader"
                 action="http://127.0.0.1:8888/api/user/media/avatar"
                 :show-file-list="false"
                 :on-success="handleAvatarSuccess"
                 :before-upload="beforeAvatarUpload">
        <img :src="UserInfo.avatar" class="avatar" alt="">
      </el-upload>
    </el-form-item>

    <el-form-item>
      <el-button type="primary" @click="submitForm('ruleForm')">提交</el-button>
      <el-button @click="resetForm('ruleForm')">重置</el-button>
    </el-form-item>

  </el-form>
</template>

<script>
import { provinceAndCityData, regionData, provinceAndCityDataPlus, regionDataPlus, CodeToText, TextToCode } from 'element-china-area-data'
import UserInfo from "@/components/UserInfo/UserInfo";
import axios from "axios";
import TopBar from "@/components/TopBar/TopBar.vue";

export default {
  name: "ChangeInfo",
  props:['UserInfo','formVisible'],//接受用户信息
  data(){
    return {
      options: regionData,
      selectedOptions: [],
      ruleForm: {
        userName: '',
        organization:'',
        age:'',
        region: '',
        gender: '',
        description: '',
        address:'',
        avatar: '',
        imageUrl:''
      },
      rules: {
        userName: [
          { required: false, message: '请输入用户名', trigger: 'blur' },
          { min: 6, max: 12, message: '长度在 6 到 12 个字符', trigger: 'blur' }
        ],
        organization:[
          { required: false, message: '请输入您的工作单位', trigger: 'change' }
        ],
        age:[
          { required: false, message: '请输入您的年龄', trigger: 'change' },
          { type: 'number', message: '年龄必须为数字值'}
        ],
        region: [
          { required: false, message: '请选择活动区域', trigger: 'change' }
        ],
        gender: [
          { required: false, message: '请填写您的性别', trigger: 'change' }
        ],
        description: [
          { required: false, message: '请填写个性签名', trigger: 'blur' }
        ]
      }
    }
  },
  mounted() {

  },
  methods:{
    handleAvatarSuccess(res, file) {
      this.ruleForm.imageUrl=res.msg
      this.$store.state.userInfo.avatar=res.msg
      alert(this.$store.state.userInfo.avatar)
    },
    beforeAvatarUpload(file) {
      const isJPG = file.type === "image/png" || "image/jpg" || "image/jpeg";
      const isLt2M = file.size / 1024 / 1024 < 100;
      if (!isJPG) {
        this.$message.error("上传头像图片只能是 JPG/PNG/JPEG 格式!");
      }
      if (!isLt2M) {
        this.$message.error("上传头像图片大小不能超过 2MB!");
      }
      return isJPG && isLt2M;
    },
    submitForm(formName) {
      let _this=this
      for (let i = 0; i < this.selectedOptions.length; i++) {
        if (i === 0) { this.ruleForm.address+=CodeToText[this.selectedOptions[i]] }
        if (i === 1) { this.ruleForm.address+=CodeToText[this.selectedOptions[i]] }
        if (i === 2) { this.ruleForm.address+=CodeToText[this.selectedOptions[i]] }
      }
      for (const index in this.ruleForm){
        if (index!=='region'&&this.ruleForm[index]===''){
          this.ruleForm[index]=UserInfo[index]
        }
      }
      this.$refs[formName].validate((valid) => {//验证表单
        if (valid) {
          //提交表单
          let userInfo={
            id:_this.$store.state.info.userId,
            name:_this.ruleForm.userName===undefined?_this.$store.state.userInfo.username:_this.ruleForm.userName,
            workplace:_this.ruleForm.organization===undefined? _this.$store.state.userInfo.organization:_this.ruleForm.organization,
            region:_this.ruleForm.address===undefined? _this.$store.state.userInfo.address:_this.ruleForm.address,
            age:_this.ruleForm.age===undefined? _this.$store.state.userInfo.age:_this.ruleForm.age,
            gender:_this.ruleForm.gender===undefined? _this.$store.state.userInfo.gender:_this.ruleForm.gender,
            signature:_this.ruleForm.description===undefined? _this.$store.state.userInfo.description:_this.ruleForm.description,
            headPicture:_this.ruleForm.imageUrl===undefined? _this.$store.state.userInfo.imageUrl:_this.ruleForm.imageUrl
          }
          console.log(_this.ruleForm);
          console.log(userInfo)
          this.$http.put("http://127.0.0.1:8888/api/user/updateUserInfo",userInfo).then((res) => {
            console.log(res)
            if (res.data.data.result===true){
              console.log('提交成功')
            }
            if(res.data.data.result===false){
              alert("提交失败")
            }
            this.$emit("childVisible")
          })
        } else {
          console.log('error submit!!');
          return false;
        }
      });
    },
    resetForm(formName) {
      this.$refs[formName].resetFields();
    }
  }
}
</script>

<style scoped>
.avatar-uploader .el-upload {
  border: 1px dashed #d9d9d9;
  border-radius: 6px;
  cursor: pointer;
  position: relative;
  overflow: hidden;
}
.avatar-uploader .el-upload:hover {
  border-color: #409EFF;
}
.avatar-uploader-icon {
  font-size: 28px;
  color: #8c939d;
  width: 178px;
  height: 178px;
  line-height: 178px;
  text-align: center;
}
.avatar {
  width: 178px;
  height: 178px;
  display: block;
}

.login-container {
  border-radius: 10px;
  margin: 0 auto;
  width: 400px;
  background: #fff;
  text-align: left;
}

.title {
  margin: 0 auto 40px auto;
  text-align: center;
  color: #505458;
}
</style>
