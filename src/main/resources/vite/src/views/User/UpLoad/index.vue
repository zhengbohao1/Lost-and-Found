<template>
  <div>
    <el-form :model="form" :rules="rules" ref="formRef" label-width="100px">
      <el-form-item label="物品" prop="itemName">
        <el-input v-model="form.itemName" class="custom-width"></el-input>
      </el-form-item>
      
      <el-form-item label="描述" prop="description">
        <el-input type="textarea" v-model="form.description" class="custom-width"></el-input>
      </el-form-item>

      <el-form-item label="日期" prop="foundDate">
        <el-date-picker
          v-model="form.foundDate"
          type="datetime"
          placeholder="选择日期时间"
          class="custom-width"
        >
        </el-date-picker>
      </el-form-item>

      <el-form-item label="发现地址" prop="foundLocation">
        <el-input v-model="form.foundLocation" class="custom-width"></el-input>
      </el-form-item>

      <el-form-item label="类型" prop="type">
        <el-select v-model="form.type" placeholder="请选择" class="custom-width">
          <el-option label="失物招领" value="found"></el-option>
          <el-option label="寻物启事" value="lost"></el-option>
        </el-select>
      </el-form-item>

      <el-form-item label="上传图片" prop="image">
        <el-upload
          class="avatar-uploader"
          action="http://localhost:8090/common/upload"
          list-type="picture-card"
          ref="upload"
          :on-preview="handlePictureCardPreview"
          :on-remove="handleRemove"
          :on-change="handleChange"
          :pn-error="handleError"
          v-model:file-list="fileList"
          :auto-upload="false"
          :on-exceed="handleExceed"
          :limit="1"
        >
          <el-icon class="avatar-uploader-icon"><Plus /></el-icon>
        </el-upload>
        <el-dialog :visible.sync="dialogVisible">
          <img width="100%" :src="dialogImageUrl" alt="预览图片">
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
import { useUserStore } from '@/stores/user';
import { addFoundPost } from '@/apis/found';
import { addLostPost } from '@/apis/lost';
import { useRouter } from 'vue-router';
import { Plus } from '@element-plus/icons-vue';

const formRef = ref(null);
const upload = ref(null)
const fileList = ref([]);
const dialogImageUrl = ref('');
const dialogVisible = ref(false);
const uploadSuccess = ref(true);

const userStore = useUserStore();
const finderId = userStore.userInfo.userId;
const status = ref(0);
const claimantId = ref(null);
const imgUrl = ref('');

const router = useRouter();

const form = reactive({
  itemName: '',
  description: '',
  foundDate: '',
  foundLocation: '',
  type: ''
});

const validateLogo = (rule, value, callback) => {
  if (!fileList.value.length) {
    callback(new Error('请上传图片'));
  } else {
    callback();
  }
};

const rules = reactive({
  itemName: [
    { required: true, message: '请输入标题', trigger: 'blur' },
  ],
  description: [
    { required: true, message: '请输入正文内容', trigger: 'blur' },
  ],
  foundDate: [
    { required: true, message: '请选择日期', trigger: 'change' },
  ],
  foundLocation:[
    { required: true, message: '请输入地点', trigger: 'blur' },
  ],
  type: [
    { required: true, message: '请选择类型', trigger: 'change' },
  ],
  image: [
    { required: true, validator: validateLogo, trigger: 'change' },
  ]
});

function handlePictureCardPreview(file) {
  dialogImageUrl.value = file.url;
  dialogVisible.value = true;
}

function handleRemove(file, fileList) {
  ElMessage.warning('替换图片');
}

function handleChange(uploadFile, uploadFiles) {
  const allowedTypes = ['image/jpeg', 'image/png', 'image/gif'];
  if (!allowedTypes.includes(uploadFile.raw.type)) {
    ElMessage.error('只允许上传 JPG/PNG/GIF 类型的图片');
    return false;
  }
  imgUrl.value = uploadFile.name;
  return true;
}

const handleExceed = () => {
  ElMessage.warning(
      '最多添加1张图片!'
  )
}

function handleError(){
  uploadSuccess.value = false;
  ElMessage({
    message: '上传失败',
    type: 'error',
  })
}

async function submitForm() {
  try {
    // 验证表单数据
    await formRef.value.validate(async valid => {
      if (valid) {
        // 触发上传
        upload.value.submit();
        if(uploadSuccess){
            // 构建 POST 数据
          // 发送 POST 请求
          if(form.type == 'found'){
            const postData = {
              itemName: form.itemName,
              description: form.description,
              foundDate: form.foundDate,
              foundLocation: form.foundLocation,
              status: status.value,
              finderId: finderId,
              claimantId: claimantId.value,
              imgUrl: imgUrl.value,
              reviewProcess: '0'
            };  
            await addFoundPost(postData);
          }else{
            const postData = {
              itemName: form.itemName,
              description: form.description,
              lostDate: form.foundDate,
              lostLocation: form.foundLocation,
              status: status.value,
              ownerId: finderId,
              claimantId: claimantId.value,
              imgUrl: imgUrl.value,
              reviewProcess: '0'
            };  
            await addLostPost(postData);
          }
        }else{
          ElMessage({
            message: '提交失败',
            type: 'error',
          });
        }
        // 成功后的处理
        ElMessage({
          message: '我可以发布',
          type: 'success',
        });
      }
      resetForm();
      setTimeout(() => {
        router.push('/user');
      },2000)
    });
  } catch (error) {
    // 失败后的处理
    console.error('Error:', error);
    ElMessage({
      message: '提交失败，请检查网络或数据',
      type: 'error',
    });
  }
}

function resetForm() {
  formRef.value.resetFields();
  fileList.value = [];
  imgUrl.value = '';
}

onMounted(() => {
  if(!userStore.userToken){
      ElMessage.warning('请先登录')
      router.push('/user')
  }
  nextTick(() => {
  });
});
</script>

<style scoped>
.custom-width {
  width: 400px; /* 将所有组件的宽度设置为40% */
}

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
  border-color: #409EFF;
}

.el-icon.avatar-uploader-icon {
  font-size: 28px;
  color: #8c939d;
  width: 178px;
  height: 178px;
  text-align: center;
}
</style>