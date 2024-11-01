<template>
  <div class="bodypage">
    <div class="sidepage">
      <!--个人信息-->
      <SideCard :details="details">
        <el-row>
          <el-avatar @click="details_avatar.close=false" :src="'http://localhost:8090/user/getAvatarById?userId='+userStore.userInfo.userId" :size="50"></el-avatar>
          <span class="name">{{ userStore.userInfo.nickName }}</span>
        </el-row>
        <el-row>
          <span class="uid">用户编号：{{ userStore.userInfo.userId }}</span>
        </el-row>
        <el-row class="function">
          <el-link @click="details_reset.close = false">重置密码</el-link>
        </el-row>
      </SideCard>
      <!--重置密码-->
      <NextCard :details="details_reset" :mystyle="mystyle" @closed="details_reset.close = true">
        <v-form v-model="valid">
          <v-row>
            <v-text-field v-model="oldPassword" type="password" :counter="16"
            :rules="passwordRule"
            label="旧密码"
            required
          ></v-text-field>
          </v-row>
          <v-row>
            <v-text-field v-model="newPassword" type="password" :counter="16"
            :rules="passwordRule"
            label="新密码"
            required
          ></v-text-field>
          </v-row>
          <v-row>
            <v-text-field v-model="confirmPassword" type="password" :counter="16"
            :rules="confirmPasswordRule"
            label="确认密码"
            required
          ></v-text-field>
          </v-row>
          <v-btn style="position: relative; top: -55px; left: 185px" icon="mdi-check" density="compact" @click="resetPassword"></v-btn>
        </v-form>
      </NextCard>
      <!--头像-->
      <NextCard  :details="details_avatar" :mystyle="mystyle" @closed="details_avatar.close = true">
        <el-row style="justify-content: center">
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
          <v-icon icon="mdi-plus" size="50"></v-icon>
        </el-upload>
        </el-row>
        <el-row style="justify-content: center; margin-top: 20px;">
            <v-btn icon="mdi-check" @click="toChangeAvatar"></v-btn>
        </el-row>
      </NextCard>
      <!--帖子-->
      <SideCard :details="details_post" :mystyle="mystyle">
        <transition-group name="expand">
          <el-row>
            <el-link class="postlink" v-if="activeIndex!=1" @click="activeIndex=1">发布过的帖子</el-link>
            <div v-if="activeIndex==1">
              <el-link class="postlink" @click="router.push('/user/space/found')">失物招领</el-link>
              <el-link class="postlink" @click="router.push('/user/space/lost')">寻物启事</el-link>
            </div>
          </el-row>
          <el-row>
            <el-link class="postlink" v-if="activeIndex!=2" @click="activeIndex=2">审核中的帖子</el-link>
            <div v-if="activeIndex==2">
              <el-link class="postlink" @click="router.push('/user/space/waitfound')">失物招领</el-link>
              <el-link class="postlink"  @click="router.push('/user/space/waitlost')">寻物启事</el-link>
            </div>
          </el-row>
          <el-row>
            <el-link class="postlink" v-if="activeIndex!=3" @click="activeIndex=3">被退回的帖子</el-link>
            <div v-if="activeIndex==3">
              <el-link class="postlink" @click="router.push('/user/space/backfound')">失物招领</el-link>
              <el-link class="postlink"  @click="router.push('/user/space/backlost')">寻物启事</el-link>
            </div>
          </el-row>
        </transition-group>
      </SideCard>
      <!--积分-->
      <SideCard :details="details_coin" :mystyle="mystyle">
        <v-row>
          <v-chip size="large" prepend-icon="mdi-cash" variant="text">
            {{ userStore.coinNum }}
          </v-chip>
        </v-row>
        <v-row>
          <el-link @click="router.push('/user/space/prize')">
            <v-icon density="compact" icon="mdi-coffee-to-go-outline" />
            前往奖品兑换页面
          </el-link>
        </v-row>
      </SideCard>
    </div>

    <div class="mainpage">
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
  </div>
</template>

<script setup>
import { ref } from 'vue'
import { useRouter } from 'vue-router'
import { useUserStore } from '@/stores/user' 
import { changeAvatar, updatePassword } from '@/apis/user';
import { ElMessage } from 'element-plus'
import {Expand, Fold, Coin, Plus, Lock, Close} from "@element-plus/icons-vue";

import SideCard from '@/components/public/SideCard.vue'
import NextCard from '@/components/public/NextCard.vue'

const router = useRouter()
const userStore = useUserStore()

const details = ref({
  width: 255,
  height: 200,
  icon: 'mdi-account-circle-outline',
})

const details_coin = ref({
  width: 255,
  height: 100,
  icon: 'mdi-cash'
})

const mystyle = ref({
  marginTop: '15px',
})

//帖子相关
const details_post = ref({
  width: 255,
  height: 180,
  icon: 'mdi-folder-account-outline e'
})

const activeIndex = ref(0)

//重置密码
const details_reset = ref({
  width: 255,
  height: 255,
  close: true,
})

//换头像
const details_avatar = ref({
  width: 255,
  height: 255,
  close: true,
})

const canAdd = ref(true)

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
    details_avatar.value.close = true;
  }
})
}

//密码表单
const valid = ref(false)
const oldPassword = ref('')
const newPassword = ref('')
const confirmPassword = ref('')
const passwordRule = [
  value => !!value || '密码不能为空',
  value => (value && value.length >= 6) || '密码至少需要6个字符',
  value => (value && /[a-zA-Z]/.test(value) && /\d/.test(value)) || '密码必须包含字母和数字'
];
const confirmPasswordRule = [
value => !!value || '密码不能为空',
  value => (value && value.length >= 6) || '密码至少需要6个字符',
  value => (value && /[a-zA-Z]/.test(value) && /\d/.test(value)) || '密码必须包含字母和数字',
  value => (newPassword.value === value) || '两次输入的密码不一致'
]

const resetPassword = async () => {
  const data = {
    oldPwd: oldPassword.value,
    newPwd: newPassword.value,
    rePwd: confirmPassword.value
  }
  if (valid) {
      await updatePassword(data).then(res => {
      if (res.code === 1) {
        ElMessage.success('密码修改成功！')    
        details_reset.value.close = true    
      }else{
        ElMessage.error(res.msg)
      }
    })
  }
}
</script>

<style scoped>
.bodypage {
  width: 1090px;
  height: 100%;
  gap: 10px;
  display: flex;
  flex-direction: row;
}

.sidepage {
  height: 100%;
  display: flex;
  flex-direction: column;
}

.mainpage {
  height: 100%;
  background-color: #ffffff;
  flex-grow: 1; /* 让 .mainpage 占据剩余的空间 */
}

.name{
    font-weight: bold;
    margin-left: 15px;
    margin-top: 10px;
    font-size: 20px
}

.uid{
  margin-top: 10px; 
  font-size: 16px; 
  color: #666;
}

.function{
  margin-top: 30px; 
  font-size: 16px; 
}

.postlink{
  margin-right: 15px;
  margin-top: 15px;
  font-size: 16px;
}

.fade-enter-active,
.fade-leave-active {
  transition: opacity 0.5s ease;
}

.fade-enter-from,
.fade-leave-to {
  opacity: 0;
}

.expand-enter-active, .expand-leave-active {  
  transition: all 0.5s;  
}  
.expand-enter, .expand-leave-to {  
  opacity: 0;  
  transform: translateY(30px);  
}
</style>

<style>
  .showBtnDealImg .el-upload--picture-card{
  }

  .disUoloadSty .el-upload--picture-card{
    display:none;   /* 上传按钮隐藏 */
  }
</style>