<template>
    <div>
      <el-form :model="form" label-width="80px" ref="formRef" :rules="rules">
        <el-form-item label="类别" prop="category">
          <el-select v-model="form.category" placeholder="请选择类别">
            <el-option label="其他" value="其他"></el-option>
            <!-- 可以根据实际情况添加更多选项 -->
          </el-select>
        </el-form-item>
        <el-form-item label="内容" prop="content">
          <el-input type="textarea" v-model="form.content" placeholder="请输入内容"></el-input>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" @click="submitForm">发送</el-button>
          <el-button @click="resetForm">重置</el-button>
        </el-form-item>
      </el-form>
    </div>
  </template>
  
  <script setup>
  import { ref, reactive, onMounted, computed } from 'vue';
  import { useUserStore } from '@/stores/user';
  import { ElMessage, ElNotification } from 'element-plus';
  import { addAdvise } from '@/apis/advise';
  
  const formRef = ref(null);
  const form = reactive({
    userId: '',
    category: '',
    content: '',
    createdAt: ''
  });
  
  const userStore = useUserStore();
  const userInfo = computed(() => userStore.userInfo);
  
  const rules = reactive({
    category: [{ required: true, message: '请选择类别', trigger: 'change' }],
    content: [{ required: true, message: '请输入内容', trigger: 'blur' }]
  });
  
  const submitForm = () => {
    formRef.value.validate(async valid => {
      if (valid) {
        form.createdAt = new Date().toISOString();
        form.userId = userInfo.value.userId; // 使用用户的userId
        await saveData(form);
        ElNotification({
          title: '成功',
          message: '数据已保存',
          type: 'success'
        });
        resetForm();
      } else {
        ElMessage.error('请检查表单');
      }
    });
  };
  
  const saveData = async formData => {
    // 这里可以添加保存数据的逻辑，例如发送到服务器
    await addAdvise(formData);
  };
  
  const resetForm = () => {
    formRef.value.resetFields();
    form.userId = userInfo.value.user_id; // 重置时也设置userId
  };
  
  onMounted(() => {
    form.userId = userInfo.value.user_id; // 初始化时设置userId
  });
  </script>
  
  <style scoped>
  .el-form {
    max-width: 400px;
    margin: 0 auto;
  }
  </style>