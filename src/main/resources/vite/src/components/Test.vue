<template>  
  <div class="space-container"> 
    <transition name="fade-up">
    <el-row v-show="needHeader" :gutter="20" class="full-height-row">  
      <el-col :span="8" class="left-card-col">  
        <el-card class="full-height-card">  
          <div class="scrollable-content">  
              <el-row>
                  <el-col :span="4">
                      <el-avatar title="点击修改头像" size="large" :src="'http://localhost:8090/user/getAvatarById?userId='+userStore.userInfo.userId" @click="needChangeAvatar = true"></el-avatar>
                  </el-col>
                  <el-col :span="3"></el-col>
                  <el-col :span="17">
                      <span class="username">{{ userStore.userInfo.nickName }}</span>
                      <br>
                      <span style="font-size: 12px; color: #989">用户编号：{{ userStore.userInfo.userId }}</span>
                  </el-col>
              </el-row>
              <el-row style="margin-top: 15px;">
                <el-button @click="needEditInfo = true">编辑信息</el-button>
              </el-row>
          </div>  
        </el-card>  
      </el-col>  
      <el-col :span="16" class="right-card-col">  
        <el-card class="full-height-card">  
          <div class="scrollable-content">  
            <div v-if="!needEditInfo">
              <el-row>
                <el-icon style="margin-right: 10px;"><Coin/></el-icon><span>积分：{{ userStore.coinNum }}</span>
              </el-row>  
            </div>
            <div v-else>
              <el-row>
                <el-col :span="22">
                  <el-link @click="needEditPassword = true">更新密码</el-link>
                </el-col>
                <el-col :span="2">
                  <el-button circle type="primary" @click="needEditInfo = false" :icon="Close"></el-button>
                </el-col>
              </el-row>
            </div>
          </div>  
        </el-card>  
      </el-col>  
    </el-row>  
  </transition>

      <div offset="200" style="margin-top: 15px; gap: 20px; display: flex; justify-content: center;">
        <el-tooltip :content="needHeader ? '收起' : '展开'" placement="bottom">
          <el-button :icon="needHeader ? Fold : Expand" @click="needHeader = !needHeader"></el-button>
        </el-tooltip>
        <el-button style="font-size:16px" v-for="item in buttonList" @click="toggleMessage(item)" 
        :type="activeIndex == item.isActive ? 'primary' : 'text'">{{ item.name }}</el-button>
      </div>

  <el-affix offset="245" style="margin-top: 15px; gap: 20px; display: flex; margin-left: 465px;" v-if="activeIndex == 3">
    <el-button @click="toggleWaitFound"  :type="activeIndexWait == 1? 'primary' : 'text'">失物招领</el-button>
    <el-button @click="toggleWaitLost"  :type="activeIndexWait == 2 ? 'primary' : 'text'">寻物启事</el-button>
  </el-affix>

  <div style="margin-top: 15px; gap: 20px; display: flex; justify-content: center" v-if="activeIndex == 4">
    <el-button @click="toggleBackFound"  :type="activeIndexBack == 1? 'primary' : 'text'">失物招领</el-button>
    <el-button @click="toggleBackLost"  :type="activeIndexBack == 2 ? 'primary' : 'text'">寻物启事</el-button>
  </div>


  <div class="main-card" :style="{ height: needHeader? ( activeIndex==3||activeIndex==4 ? '340px' :'380px' ) : ( activeIndex==3||activeIndex==4 ? '510px' :'555px' ) }">
   
      <router-view v-slot="{ Component }">
        <el-scrollbar>
        <transition name="fade" mode="out-in">
          <keep-alive>
            <component :is="Component" />
          </keep-alive>
        </transition>
        </el-scrollbar>
      </router-view>
      
  </div>

    <el-dialog v-model="needChangeAvatar" title="更换头像" width="500" style="text-align: center">
        <el-upload
          list-type="picture-card"
          :class="{ 'showBtnDealImg': canAdd, 'disUoloadSty': !canAdd}"
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
        <template #footer>
          <div class="dialog-footer">
            <el-button @click="dialogFormVisible = false">取消</el-button>
            <el-button type="primary" @click="toChangeAvatar">更换</el-button>
          </div>
        </template>
    </el-dialog>

    <el-dialog v-model="needEditPassword" title="更新密码" width="500" style="text-align: center">
       <el-form ref="formResetRef" :model="formReset" :rules="rulesReset">
        <el-form-item prop="oldPassword">
          <el-input v-model="formReset.oldPassword" placeholder="请输入旧的密码" :prefix-icon="Lock" show-password/>
        </el-form-item>
        <el-form-item prop="newPassword">
          <el-input v-model="formReset.newPassword" placeholder="请输入新的密码" :prefix-icon="Lock" show-password/>
        </el-form-item>
        <el-form-item prop="retryPwd">
          <el-input v-model="formReset.retryPwd" placeholder="请确认新的密码" :prefix-icon="Lock" show-password/>
        </el-form-item>
       </el-form>
        <template #footer>
          <div class="dialog-footer">
            <el-button @click="dialogFormVisible = false">取消</el-button>
            <el-button type="primary" @click="resetPassword">确定</el-button>
          </div>
        </template>
    </el-dialog>

  </div>  

</template>  
  
<script setup>  
import { onMounted, ref } from 'vue';
import { useUserStore } from '@/stores/user'; 
import { useRouter } from 'vue-router';
import { getCoin } from '@/apis/prize';
import { changeAvatar, updatePassword } from '@/apis/user';
import {Expand, Fold, Coin, Plus, Lock, Close} from "@element-plus/icons-vue";

const userStore = useUserStore();
const router = useRouter();

const needHeader = ref(true);
const needEditInfo = ref(false);
const needEditPassword = ref(false);

const buttonList = ref([
  { name: '我捡到的', isActive: 1 },
  { name: '我丢失的', isActive: 2 },
  { name: '待审核的', isActive: 3 },
  { name: '未通过的', isActive: 4 },
  { name: '奖品兑换', isActive: 5 },
])

const activeIndex = ref(0);

const toggleMessage = (item) => {
  switch (item.isActive) {
      case 1:
        router.push('/user/space/found');
        break;
      case 2:
        router.push('/user/space/lost');
        break;
      case 3:
        activeIndexWait.value = 1;
        router.push('/user/space/waitfound');
        break;
      case 4:
        activeIndexBack.value = 1;
        router.push('/user/space/backfound');
        break;
      case 5:
        router.push('/user/space/prize');
        break;
  }
activeIndex.value = item.isActive;
};

const activeIndexWait = ref(1);

const toggleWaitFound = () => {
router.push('/user/space/waitfound');
activeIndexWait.value = 1;
}

const toggleWaitLost = () => {
router.push('/user/space/waitlost');
activeIndexWait.value = 2;
}

const activeIndexBack = ref(1);

const toggleBackFound = () => {
router.push('/user/space/backfound');
activeIndexBack.value = 1;
}

const toggleBackLost = () => {
router.push('/user/space/backlost');
activeIndexBack.value = 2;
}

const watchRouter = () => {
const path = router.currentRoute.value.path;
if(path === '/user/space/waitfound') {
  activeIndex.value = 3;
  activeIndexWait.value = 1;
}else if(path === '/user/space/waitlost') {
  activeIndex.value = 3;
  activeIndexWait.value = 2;
}else if(path === '/user/space/backfound') {
  activeIndex.value = 4;
  activeIndexBack.value = 1;
}else if(path === '/user/space/backlost') {
  activeIndex.value = 4;
  activeIndexBack.value = 2;
}else if(path === '/user/space/prize') {
  activeIndex.value = 5;
}
}

//修改密码
const formResetRef = ref(null)
const formReset = ref({
  oldPassword: '',
  newpassword: '',
  retryPwd: '',
})

const rulesReset = {
  oldPassword: [
    {required: true, message: '密码不能为空', trigger: 'blur'},
    {min: 6, max: 18, message: '密码应为6-18位', trigger: 'blur'},
    {
      validator: (rule, value, callback) => { //验证邮箱的有效格式
        var pswRegExp = /^(?=.*\d)(?=.*[a-zA-Z]).*$/;
        if (!pswRegExp.test(value) && value !== '') {
          callback(new Error('密码需要含有字母和数字！'));
        } else {
          callback();
        }
      }
    }
  ],
  newpassword: [
    {required: true, message: '密码不能为空', trigger: 'blur'},
    {min: 6, max: 18, message: '密码应为6-18位', trigger: 'blur'},
    {
      validator: (rule, value, callback) => { //验证邮箱的有效格式
        var pswRegExp = /^(?=.*\d)(?=.*[a-zA-Z]).*$/;
        if (!pswRegExp.test(value) && value !== '') {
          callback(new Error('密码需要含有字母和数字！'));
        } else {
          callback();
        }
      }
    }
  ],
  retryPwd: [
    {required: true, message: '密码不能为空', trigger: 'blur'},
    {min: 6, max: 14, message: '密码不符合要求', trigger: 'blur'},
    {
      validator: (rule, value, callback) => {
        if (value !== formReset.value.newPassword) {
          callback(new Error('两次密码不一致！'));
        } else {
          callback();
        }
      }
    }
  ],
}

const resetPassword = async () => {
  const data = {
    oldPwd: formReset.value.oldPassword,
    newPwd: formReset.value.newPassword,
    rePwd: formReset.value.retryPwd
  }
  formResetRef.value.validate(async (valid) => {
    if (valid) {
      await updatePassword(data).then(res => {
      if (res.code === 1) {
        ElMessage.success('密码修改成功！')        
      }else{
        ElMessage.error(res.msg)
      }
    })
  }})
}

//头像上传
const canAdd = ref(true)
const needChangeAvatar = ref(false)

const upload = ref(null)
const fileList = ref([]);
const imgUrl = ref('');
const avatar = ref([])
const dialogImageUrl = ref('');
const dialogVisible = ref(false);
const uploadSuccess = ref(true);

function handlePictureCardPreview(file) {
dialogImageUrl.value = file.url;
dialogVisible.value = true;
}

function handleRemove(file, fileList) {
ElMessage.warning('替换图片');
canAdd.value = true;
}

function handleChange(uploadFile, uploadFiles) {
const allowedTypes = ['image/jpeg', 'image/png'];
if (!allowedTypes.includes(uploadFile.raw.type)) {
  ElMessage.error('只允许上传 JPG/PNG 类型的图片');
  return false;
}
if (uploadFiles.length === 1) {
  canAdd.value = false;
  console.log(canAdd.value)
}
imgUrl.value = uploadFile.name;
avatar.value = uploadFile.raw;

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

const toChangeAvatar = () => {
const formData = new FormData();
formData.append('avatar', avatar.value);
changeAvatar(formData).then(res => {
  console.log(res)
  if(res.code === 1){
    ElMessage.success('更换头像成功');
  }
})
toChangeAvatar.value = false;
}

onMounted(() => {
  if(!userStore.userToken){
    ElMessage.warning('请先登录')
    router.push('/user')
  }else{
    userStore.getCoinNum();
    watchRouter();
  }
})
</script>  
  
  <style scoped>
  .space-container::-webkit-scrollbar {
    width: 0.1em; /* 设置滚动条宽度为0.1em */
    background-color: transparent; /* 设置滚动条背景颜色为透明 */
  }

  .main-card {
      margin-top: 15px;
      overflow-y: auto; /* 添加滚动条 */
  }

  .main-content {
    overflow-y: scroll;
  }
  
  .full-height-row {  
      height: 170px; /* 使行的高度继承自父容器 */
  }
  
  .full-height-card {  
      height: 100%; /* 使卡片高度为父容器高度 */
      overflow: hidden; /* 隐藏卡片内部的溢出内容 */
      padding: 10px; /* 为卡片内容添加内边距 */
      box-sizing: border-box; /* 确保内边距不会增加卡片的高度 */
  }
  
  .scrollable-content {  
      height: calc(100% - 32px); /* 减去卡片上下内边距的总和，确保内容区域高度正确 */
      overflow-y: auto; /* 启用垂直滚动条 */
  }
  
  .username {
 
      font-size: 24px;
  }

  @keyframes sheen {
0% {
  transform: skewY(-45deg) translateX(0);
}
100% {
  transform: skewY(-45deg) translateX(12.5em);
}
}

.fade-enter-active,
.fade-leave-active {
transition: opacity 0.5s ease;
}

.fade-enter-from,
.fade-leave-to {
opacity: 0;
}  
</style>

<style>
  .showBtnDealImg .el-upload--picture-card{
  }

  .disUoloadSty .el-upload--picture-card{
    display:none;   /* 上传按钮隐藏 */
  }
</style>