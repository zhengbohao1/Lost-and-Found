<template>
  <div class="common-layout">
    <el-container>
      <el-header  style="height: 160px; padding: 0;">
        <div style="z-index: 80; width: 50px; height: 70px; position: absolute; left: 1000px; margin-top: 40px;">
          <SearchBox></SearchBox>
        </div>
        <Header></Header>
      </el-header>
      <el-container style="height: 660px;">
        <el-aside :width="isSidebarOpen ? '200px' : '70px'" class="sidebar">
          <el-menu :default-active="activeIndex" class="el-menu-vertical-demo"
                   :router="true"
                   :collapse="!isSidebarOpen"
                   @open="handleOpen"
                   @close="handleClose">
            <!-- 折叠按钮 -->
            <el-tooltip :content="isSidebarOpen ? '收起' : '展开'" placement="right">
              <el-button text style="margin-left: 7px; margin-top: 20px; margin-bottom: 5px; background-color: #f1f1f1;"
                        :icon="isSidebarOpen ? Fold : Expand"
                        @click="toggleSidebar"></el-button>
            </el-tooltip>

            <!-- 登录状态相关的元素 -->
            <el-menu-item :index="userStore.userToken ? '/user/space' : ''"
                          :class="{'top-menu-item': true, 'collapsed': !isSidebarOpen}">
              <template v-if="!userStore.userToken">
                <el-icon><User /></el-icon>
                <span style="margin-left: 30px" @click="handleLoginClick">立即登录</span>
              </template>
              <template v-else>
                <el-avatar :src="'http://localhost:8090/user/getAvatarById?userId='+userStore.userInfo.userId" fit="cover"></el-avatar>
                <span style=" margin-left: 30px;" class="username">{{ userStore.userInfo.nickName }}</span>
              </template>
            </el-menu-item>

            <!-- 常规菜单项 -->
            <el-menu-item index="/user/found">
              <el-icon><House /></el-icon>
              <span style="margin-left: 30px">失物广场</span>
            </el-menu-item>
            <el-menu-item index="/user/lost">
              <el-icon><House /></el-icon>
              <span style="margin-left: 30px">寻物广场</span>
            </el-menu-item>
            <el-menu-item @click="handleUpload">
              <el-icon><Promotion /></el-icon>
              <span style="margin-left: 40px">发布</span>
            </el-menu-item>
            <el-menu-item index="" @click="handleMessage">
              <el-icon><BellFilled /></el-icon>
              <el-badge v-if="numOfMessage > 0 " :value="numOfMessage" :max="99"></el-badge>
              <span :style="numOfMessage > 0 ? { 'margin-left': '20px' } : { 'margin-left': '40px' }">通知</span>
            </el-menu-item>
            <el-menu-item index="" @click="handleFeedback">
              <el-icon><WarnTriangleFilled /></el-icon>
              <span style="margin-left: 30px">意见反馈</span>
            </el-menu-item>
            <el-menu-item :index="''" :disabled="!userStore.userToken">
              <el-icon><SwitchButton /></el-icon>
              <span style="margin-left: 30px" @click="showLogoutConfirm">退出登录</span>
            </el-menu-item>
          </el-menu>
        </el-aside>
        <el-main id="content">
          <transition class="child">
            <div class="overlay" v-if="showLogin">
              <el-button class="close" @click="changeShow" plain round>
                <el-icon size="x-large">
                  <Close/>
                </el-icon>
              </el-button>
              <login @changeShow="changeShow"/>
            </div>
          </transition>
          <router-view :search="search"></router-view>
        </el-main>
      </el-container>
    </el-container>
  </div>
</template>

<script setup>
import { ref, computed, onMounted, watch } from 'vue';
import { Fold, Expand, House, SwitchButton, Promotion, WarnTriangleFilled, User, Comment, Search, Close, BellFilled } from '@element-plus/icons-vue';
import { useRouter } from 'vue-router';
import { useUserStore } from '@/stores/user';
import { getUserAvatar, countMessage } from '@/apis/user';

import Login from '@/views/Login/other.vue';
import Header from '@/components/function/Header.vue';
import SearchBox from '@/components/function/SearchBox.vue';

const router = useRouter();
const userStore = useUserStore();

const isSidebarOpen = ref(true);
const activeIndex = ref('1');
const post = ref(null); // 用于存储请求结果
const show = ref(false);

const numOfMessage = ref(0)

const search = ref(''); //搜索传参

const gettNum = async () => {
  await countMessage(userStore.userInfo.userId).then(res => {
    numOfMessage.value = res.data
  })
}

const showLogoutConfirm = () => {
  ElMessageBox.confirm(
    '您确定要退出登录吗？',
    '提示',
    {
      confirmButtonText: '确定',
      cancelButtonText: '取消',
      type: 'warning'
    }
  ).then(() => {
    loginOut();
  }).catch(() => {
    ElMessage({
      type: 'info',
      message: '已取消退出登录'
    });
  });
}

const loginOut = () => {
  userStore.userLogout();
  router.push('/user');
};

function toggleSidebar() {
  isSidebarOpen.value = !isSidebarOpen.value;
}

const handleOpen = (key, keyPath) => {
  console.log(key, keyPath);
}

function handleClose(key, keyPath) {
  console.log(key, keyPath);
}

function changeShow() {
  show.value = false;
}

function handleLoginClick() {
  window.history.pushState({}, "", `/user`);
  if (!userStore.userToken) {
    show.value = true;
  }}

function handleUpload() {
  if(userStore.userToken) {
    router.push('/user/upload');
  } else {
    show.value = true;
    ElMessage({
      message: '请先登录',
      type: 'warning',
    });
  }
}

const handleMessage = () =>
{
  if(userStore.userToken) {
    router.push('/user/message');
  } else {
    show.value = true;
    ElMessage({
      message: '请先登录',
      type: 'warning',
    });
  }
}

const handleFeedback = () =>
{
  if(userStore.userToken) {
    router.push('/user/feedback');
  } else {
    show.value = true;
    ElMessage({
      message: '请先登录',
      type: 'warning',
    });
  }
}

  const showLogin = computed(() => show.value && !userStore.userToken);

  watch(() => userStore.userInfo, (newInfo) => {
  if (newInfo) {
    show.value = false;
    ElMessage({
      message: '登录成功',
      type: 'success',
    })
    location.reload();
  } else {
    canlogout.value = false;
  }
});

onMounted(() => {
  gettNum();
})
</script>

<style scoped>
.sidebar {
  overflow: hidden; /* 防止滚动条出现 */
  transition: width 0.25s ease;
}

.overlay {
  position: fixed;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  background-color: rgba(0, 0, 0, 0.5); /* 设置透明度的背景色 */
  z-index: 9999; /* 设置一个较大的z-index值，确保图层位于其他内容之上 */
}

.close{
  border: 0;
  position: absolute;
  left: 70%;
  top: 16%;
  background-color: #ebeaea;
  z-index: 1000; /* 设置一个较大的z-index值，确保图层位于其他内容之上 */
}

.el-menu-vertical-demo:not(.el-menu--collapse) {
  width: 200px; /* 设置默认宽度 */
  height: 100%;
  background-color: #f1f1f1; /* 改变菜单背景颜色 */
  border-radius: 16px;
}

.el-menu--collapse {
  height: 100%;
  background-color: #f1f1f1; /* 折叠后的背景颜色 */
  border-radius: 16px;
}

.top-menu-item {
  margin-top: -10px; /* 调整顶部间距 */
}

.collapsed .top-menu-item .username {
  display: none; /* 折叠时隐藏用户名 */
}

.el-menu-vertical-demo .el-menu-item {
  margin: 15px 0; /* 上下各5px的间距 */
  border-radius: 16px; /* 设置圆角 */
  margin: 5px;
  color: #636363;
}

.el-menu-vertical-demo .el-menu-item:hover,
.el-menu-vertical-demo .el-submenu__title:hover {
  background-color: #b9b9b9; /* 改变悬停和激活时的背景色 */
  border-radius: 16px; /* 设置圆角 */
  color: #3a3a3a;
}

.avatar {
  width: 30px;
  height: 30px;
  border-radius: 50%;
  margin-right: 10px;
}

.username {
  font-weight: bold;
}
</style>