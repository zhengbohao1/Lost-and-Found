<template>
    <el-container class="login-container">
      <el-main>
        <el-form
          ref="signForm"
          :model="form"
          :rules="rules"
          label-position="top"
          label-width="80px"
          class="login-form"
        >
        <img style="height: 80px;margin-top: 10px;" src="@/assets/logo_longer.png">
        <el-form-item prop="username">
            <el-input v-model="form.username" placeholder="昵称" size="large">
              <template #prepend>
                <el-icon><User /></el-icon>
              </template>
            </el-input>
          </el-form-item>
          <el-form-item prop="password">
            <el-input v-model="form.password" :type="passwordType" placeholder="密码" size="large">
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
          <el-form-item prop="confirmPassword">
            <el-input v-model="form.confirmPassword" :type="confirmpasswordType" placeholder="确认密码" size="large">
              <template #prepend>
                <el-icon><Lock /></el-icon>
              </template>
              <template #append>
            <el-button 
              type="text" 
              @click="toggleconfirmPasswordType" 
              :icon="confirmpasswordType === 'password' ? View : Hide"
            ></el-button>
            </template>
            </el-input>
          </el-form-item>
          <el-form-item prop="email">
            <el-input v-model="form.email" 
            type="text"
            @focus="showCode = true"
            @input="validateEmail"
            size="large">
              <template #prepend>
                <el-icon><Message /></el-icon>
              </template>
            </el-input>
          </el-form-item>
          <transition name="fade">
            <el-form-item prop="code" v-if="showCode">
              <el-input
                type="text"
                v-model="form.verifyCode"
                placeholder="验证码"
                size="large"
              >
                <template #append>
                  <el-button
                    type="text"
                    style="color:rgb(0, 116, 231);"
                    :disabled="!isValidEmail"
                    @click="openConfirmationDialog"
                  >发送验证码</el-button>
                </template>
              </el-input>
            </el-form-item>
          </transition>
          <el-form-item style="width: 100%">
            <el-button class="login-button" type="primary" size="large" @click="login" style="width: 100%;">立即注册</el-button>
          </el-form-item>
          <el-form-item >
            <el-link type="primary" @click="toLogin">已有账号？立即登录</el-link>
          </el-form-item>
        </el-form>
      </el-main>
        <el-dialog v-model="dialogVisible" title="确认操作" width="30vw" @close="resetForm">
        <el-form
          ref="checkForm"
          :model="check"
          :rules="checkRules"
        >
          <el-form-item prop="verifyCode">
            <el-input v-model="check.verifyCode" placeholder="验证码" clearable></el-input>
          </el-form-item>
        </el-form>
        <template #footer>
          <span class="dialog-footer">
            <el-button @click="dialogVisible = false">取消</el-button>
            <el-button type="primary" @click="confirmAction">确认</el-button>
          </span>
        </template>
      </el-dialog>
    </el-container>    
</template>

<script setup>
  import {  ref,reactive } from 'vue';
  import { useRouter } from 'vue-router';
  //import { ElMessage } from 'element-plus';
  import { View,Hide, Message } from '@element-plus/icons-vue'

  const form = reactive({
    username: null,
    email: null,
    password: null,
    confirmPassword: null,
    identity: null,
    verifyCode: null,
  });

  const validateConfirmPassword = (rule, value, callback) => {
      if (value === '') {
        callback(new Error('请再次输入密码'));
      } else if (value !== form.password) {
        callback(new Error('两次输入的密码不一致'));
      } else {
        callback();
      }
    };

  const rules = reactive({
    email: [
      { required: true, message: '填写邮箱', trigger: ['blur','change'] },
    ],
    username: [
      { required: true, message: '昵称不能为空', trigger: ['blur','change'] },
      { min: 1, max: 10, message: '昵称长度在 1 到 10 个字符之间', trigger: ['blur','change'] },
    ],
    password: [
      { required: true, message: '请输入密码', trigger: ['blur','change'] },
      { min: 6, max: 20, message: '密码长度在 6 到 20 个字符之间', trigger: ['blur','change'] },
    ],
    confirmPassword: [
      { required: true, message: '请确认密码', trigger: ['blur','change'] },
      { validator: validateConfirmPassword, trigger: 'blur'
      }
    ],
    verifyCode:[
    { required: true, message: '请输入验证码', trigger: ['blur','change'] }
    ],
  });

  const dialogVisible = ref(false); // 控制弹窗显示
  const check = reactive({
    verifyCode: '',
  });
  const checkRules = reactive({
    verifyCode: [
      { required: true, message: '请输入验证码', trigger: ['blur', 'change'] },
      { pattern: /^\d{6}$/, message: '验证码必须是六位数字', trigger: ['blur', 'change'] },
    ],
  })

  const passwordType = ref('password');
  const confirmpasswordType = ref('password');
  const showCode = ref(false);
  const isValidEmail = ref(false);

  const togglePasswordType = () =>  {  
      passwordType.value = passwordType.value === 'password' ? 'text' : 'password';  
    }
  const toggleconfirmPasswordType = () =>  {
    confirmpasswordType.value = confirmpasswordType.value === 'password' ? 'text' : 'password';
  }

  const openConfirmationDialog = () => {
    dialogVisible.value = true; // 打开弹窗
  };

  const confirmAction = () => {
    // 验证验证码是否符合规则
    //validateVerifyCode(check.verifyCode).then(() => {
      // 验证码正确后执行确认操作
      dialogVisible.value = false; // 关闭弹窗
    //}).catch((error) => {
     // ElMessage.error(error.message);
    //});
  };

  const validateEmail = () => {
    const emailRegex = /^([a-zA-Z]|[0-9])(\w|-)+@[a-zA-Z0-9]+\.([a-zA-Z]{2,4})$/; //邮箱正则表达式
    isValidEmail.value = emailRegex.test(form.email);
  };

  const router = useRouter();
  const toLogin = () => {
    router.push('/login');
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
  background-color: rgba(0, 0, 0, 0);
  box-shadow: 0 2px 4px rgba(0, 0, 0, 0.12), 0 0 6px rgba(0, 0, 0, 0.04);
}

.fade-enter-active, .fade-leave-active {  
  transition: opacity 1s;  
}  
.fade-enter, .fade-leave-to  {  
  opacity: 0;  
}  
</style>