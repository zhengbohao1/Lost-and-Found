<template>
  <div>
    <el-form
      ref="registerFormRef"
      :model="form"
      :rules="rules"
      label-width="80px"
    >
      <el-form-item label="用户名" prop="username">
        <el-input v-model="form.username" placeholder="请输入用户名"></el-input>
      </el-form-item>
      <el-form-item label="密码" prop="password">
        <el-input
          :type="passwordType"
          v-model="form.password"
          placeholder="请输入密码"
          @focus="showConfirmPassword = true"
          @blur="showConfirmPassword = false"
        >
          <template #append>
            <el-button
              type="text"
              @click="togglePasswordType"
              :icon="passwordType === 'password' ? View : Hide"
            ></el-button>
          </template>
        </el-input>
      </el-form-item>
      <el-form-item label="确认密码" prop="confirmPassword" v-if="showConfirmPassword">
        <el-input
          :type="confirmPasswordType"
          v-model="form.confirmPassword"
          placeholder="请再次输入密码"
          @keyup.enter="handleSubmit"
        >
          <template #append>
            <el-button
              type="text"
              @click="toggleConfirmPasswordType"
              :icon="confirmPasswordType === 'password' ? View : Hide"
            ></el-button>
          </template>
        </el-input>
      </el-form-item>
      <el-form-item>
        <el-button type="primary" @click="handleSubmit">注册</el-button>
      </el-form-item>
    </el-form>
  </div>
</template>

<script>
import { reactive, ref } from 'vue';
import { ElMessage } from 'element-plus';
import { View as EyeOpenIcon, Hide as EyeClosedIcon } from '@element-plus/icons-vue';

export default {
  setup() {
    const registerFormRef = ref(null);
    const form = reactive({
      username: '',
      password: '',
      confirmPassword: '',
    });
    const showConfirmPassword = ref(false); // 控制确认密码输入框的显示
    const passwordType = ref('password');
    const confirmPasswordType = ref('password');

    // 自定义验证器函数
    const validateConfirmPassword = (rule, value, callback) => {
      if (value === '') {
        callback(new Error('请再次输入密码'));
      } else if (value !== form.password) {
        callback(new Error('两次输入的密码不一致'));
      } else {
        callback();
      }
    };

    // 验证规则
    const rules = reactive({
      username: [
        { required: true, message: '请输入用户名', trigger: 'blur' }
      ],
      password: [
        { required: true, message: '请输入密码', trigger: 'blur' }
      ],
      confirmPassword: [
        { required: true, message: '请再次输入密码', trigger: 'blur' },
        { validator: validateConfirmPassword, trigger: 'blur' }
      ]
    });

    const togglePasswordType = () => {
      passwordType.value = passwordType.value === 'password' ? 'text' : 'password';
    };

    const toggleConfirmPasswordType = () => {
      confirmPasswordType.value = confirmPasswordType.value === 'password' ? 'text' : 'password';
    };

    const handleSubmit = () => {
      registerFormRef.value.validate(valid => {
        if (valid) {
          ElMessage.success('注册成功！');
          // 这里可以添加真实的注册逻辑
        } else {
          ElMessage.error('表单验证失败');
        }
      });
    };

    return {
      registerFormRef,
      form,
      rules,
      handleSubmit,
      togglePasswordType,
      toggleConfirmPasswordType,
      passwordType,
      confirmPasswordType,
      showConfirmPassword,
      View: EyeOpenIcon,
      Hide: EyeClosedIcon
    };
  },
};
</script>

<style scoped>
/* 可以在这里添加样式 */
</style>