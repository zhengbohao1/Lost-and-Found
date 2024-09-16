<template>
    <el-container class="login-container">
      <el-main>
        <el-form
          ref="loginForm"
          :model="form"
          :rules="rules"
          label-position="top"
          label-width="80px"
          class="login-form"
        >
            <img style="height: 80px;margin-top: 10px;" src="@/assets/logo_longer.png">
          <el-form-item prop="email">
            <el-input v-model="form.email" placeholder="请输入邮箱" size="large">
              <template #prepend>
                <el-icon><User /></el-icon>
              </template>
            </el-input>
          </el-form-item>
          <el-form-item prop="password">
            <el-input v-model="form.password" :type="passwordType" placeholder="请输入密码" size="large">
              <template #prepend>
                <el-icon><Lock /></el-icon>
              </template>
              <template #append>
            <el-button 
              type="text" 
              @click="togglePasswordType" 
              :icon="passwordType === 'password' ? View : Hide"
            ></el-button>
            </template>
            </el-input>
          </el-form-item>
          <el-form-item prop="identity">
            <el-radio-group v-model="form.identity" size="large">
            <el-radio :label='"1"'>管理员</el-radio>
            <el-radio :label='"2"'>用户</el-radio>
          </el-radio-group>
          </el-form-item>
          <el-form-item prop="verifyCode">
            <el-col :span="10">
              <el-input v-model="form.verifyCode" placeholder="请输入验证码" size="large"></el-input>
            </el-col>
            <el-col :span="4"></el-col>
            <el-col :span="10"><el-image size="large" /></el-col>
          </el-form-item>
          <el-from-item>
            <el-checkbox :label="label" style="margin-left: -70%;" v-model="remeberme" size="large">记住我</el-checkbox>
          </el-from-item>
          <el-form-item style="width: 100%">
            <el-button class="login-button" type="primary" size="large" @click="login" style="width: 100%;">登录</el-button>
          </el-form-item>
          <el-form-item>
            <el-col :span="8">
              <el-link class="login-link" type="primary" size="large">忘记密码？</el-link>
            </el-col>
            <el-col :span="8"></el-col>
            <el-col :span="8"><el-link class="login-link" @click="sign" size="large">注册</el-link></el-col>
          </el-form-item>
        </el-form>
      </el-main>
    </el-container>
  </template>
  
  <script setup>
  import { ElMessage} from 'element-plus';
  import { ref, reactive } from 'vue';
  import Cookies from 'js-cookie'
  import { useRouter } from 'vue-router'
  import { View,Hide } from '@element-plus/icons-vue'
  
  const form = reactive({
    email: null,
    password: null,
    identity: null,
    verifyCode: null,
  });
  
  const rules = reactive({
    email: [
      { required: true, message: '请输入邮箱', trigger: ['blur','change'] },
      { min: 13, max: 13, message: '用户名长度应为 13 个字符', trigger: ['blur','change'] },
    ],
    password: [
      { required: true, message: '请输入密码', trigger: ['blur','change'] },
      { min: 6, max: 20, message: '密码长度在 6 到 20 个字符之间', trigger: ['blur','change'] },
    ],
    verifyCode:[
    { required: true, message: '请输入验证码', trigger: ['blur','change'] }
    ],
  });

  const remeberme = ref(false);

  const passwordType = ref('password');

  const togglePasswordType = () =>  {  
      passwordType.value = passwordType.value === 'password' ? 'text' : 'password';  
    }
  
  const router = useRouter();
  const sign = () => {
    router.push('/sign');
  }

  const saveCookies = () =>{
     if(remeberme.value){
      {
        Cookies.set('email', form.email, { expires: 3 });
        Cookies.set('password', form.password, { expires: 3 });
     }
     }
  }

  const login = () => {
    if(form.identity == null){
      ElMessage({  
          message: '请选择身份',  
          type: 'error' });
    }else if(form.email == null){
      ElMessage({  
          message: '请输入邮箱', 
          type: 'error' });
    }else if(form.password == null){
      ElMessage({  
          message: '请输入密码', 
          type: 'error' });
    }else if(form.verifyCode == null){
      ElMessage({
        message: '请输入验证码',  
          type: 'error' 
      })
    }
    else{
      saveCookies();
    }
  }
  </script>
  
  <style scoped>
  .login-container {
    height: 90vh;
    display: flex;
    justify-content: center;
    align-items: center;
    background-color: #f5f7fa;
  }
  
  .login-form {
    position: absolute;
    width: 23%;
    top: 50vh;
    left: 50vw;
    transform: translate(-50%, -50%); /* 反向移动自身宽高的50%，实现居中 */ 
    padding: 2%;
    border-radius: 15px;
    background-color: #fff;
    box-shadow: 0 2px 4px rgba(0, 0, 0, 0.12), 0 0 6px rgba(0, 0, 0, 0.04);
  }

  </style>