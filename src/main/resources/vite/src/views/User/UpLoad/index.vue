<template>
    <div>
      <el-form :model="form" :rules="rules" ref="formRef" label-width="100px">
        <el-form-item label="标题" prop="title">
          <el-input v-model="form.title"></el-input>
        </el-form-item>
        
        <el-form-item label="正文内容" prop="content">
          <el-input type="textarea" v-model="form.content"></el-input>
        </el-form-item>
  
        <el-form-item label="日期" prop="date">
          <el-date-picker
            v-model="form.date"
            type="datetime"
            placeholder="选择日期时间">
          </el-date-picker>
        </el-form-item>
  
        <el-form-item label="类型" prop="type">
          <el-select v-model="form.type" placeholder="请选择">
            <el-option label="失物" value="lost"></el-option>
            <el-option label="招领" value="found"></el-option>
          </el-select>
        </el-form-item>
  
        <el-form-item label="上传图片" prop="image">
        <el-upload
          class="avatar-uploader"
          action="YOUR_API_URL"
          list-type="picture-card"
          :on-preview="handlePictureCardPreview"
          :on-remove="handleRemove"
          :file-list="fileList"
          :auto-upload="false"
        >
          <img v-if="fileList.length" :src="fileList[0].url" class="avatar" />
          <el-icon v-else class="avatar-uploader-icon"><Plus /></el-icon>
        </el-upload>
        <el-dialog :visible.sync="dialogVisible">
          <img width="100%" :src="dialogImageUrl" alt="">
        </el-dialog>
      </el-form-item>
  
        <el-form-item>
          <el-button type="primary" @click="submitForm">提交</el-button>
          <el-button @click="resetForm">重置</el-button>
        </el-form-item>
      </el-form>
    </div>
  </template>
  
  <script setup>
  import { ref, reactive, onMounted, nextTick } from 'vue';
  import { ElMessage } from 'element-plus';
  
  const formRef = ref(null);
  const fileList = ref([]);
  const dialogImageUrl = ref('');
  const dialogVisible = ref(false);
  
  const form = reactive({
    title: '',
    content: '',
    date: '',
    type: ''
  });
  
  const rules = reactive({
    title: [
      { required: true, message: '请输入标题', trigger: 'blur' },
    ],
    content: [
      { required: true, message: '请输入正文内容', trigger: 'blur' },
    ],
    date: [
      { required: true, message: '请选择日期', trigger: 'change' },
    ],
    type: [
      { required: true, message: '请选择类型', trigger: 'change' },
    ],
    image:[
      { required: true, message: '请上传图片', trigger: 'change' },
    ]
  });
  
  function handlePictureCardPreview(file) {
    dialogImageUrl.value = file.url;
    dialogVisible.value = true;
  }
  
  function handleRemove(file, fileList) {
    console.log(file, fileList);
  }
  
  function submitForm() {
    formRef.value.validate(valid => {
      if (valid) {
        // 手动上传文件
        const uploadComponent = formRef.value.querySelector('.el-upload').$children[0];
        uploadComponent.submit();
        alert('提交成功！');
      } else {
        console.log('error submit!!');
        return false;
      }
    });
  }
  
  function resetForm() {
    formRef.value.resetFields();
    fileList.value = [];
  }
  
  onMounted(() => {
    // 确保上传组件在DOM中存在后再引用
    nextTick(() => {
      const uploadComponent = formRef.value.querySelector('.el-upload').$children[0];
      // 存储上传组件的引用
      uploadComponent && (uploadComponent.$parent.uploadRef = uploadComponent);
    });
  });
  </script>
  
  <style scoped>
.avatar-uploader .avatar {
  width: 178px;
  height: 178px;
  display: block;
}

.avatar-uploader .el-upload {
  border: 1px dashed var(--el-border-color);
  border-radius: 6px;
  cursor: pointer;
  position: relative;
  overflow: hidden;
  transition: var(--el-transition-duration-fast);
}

.avatar-uploader .el-upload:hover {
  border-color: var(--el-color-primary);
}

.el-icon.avatar-uploader-icon {
  font-size: 28px;
  color: #8c939d;
  width: 178px;
  height: 178px;
  text-align: center;
}
</style>