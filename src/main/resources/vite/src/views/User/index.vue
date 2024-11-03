<template>
  <div style="z-index: 80; position: absolute; left: 1020px; margin-top: 55px;">
     <SearchBox></SearchBox>
  </div>
  <div class="common-layout">
    <el-container>
      <el-header  style="height: 160px; padding: 0;">
        <Header></Header>
      </el-header>
      <el-container style="height: 660px;">
        <el-aside :width="isSidebarOpen ? '200px' : '70px'" class="sidebar">
          <el-menu :default-active="activeIndex" class="el-menu-vertical-demo"
                   :router="true"
                   :collapse="!isSidebarOpen">
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
              <el-badge v-if="userStore.messageNum > 0 " :value="userStore.messageNum" :max="99"></el-badge>
              <span :style="userStore.messageNum > 0 ? { 'margin-left': '20px' } : { 'margin-left': '40px' }">通知</span>
            </el-menu-item>
            <el-menu-item index="" @click="handleFeedback">
              <el-icon><WarnTriangleFilled /></el-icon>
              <span style="margin-left: 30px">意见反馈</span>
            </el-menu-item>
            <el-menu-item :index="''"   v-if="userStore.userToken">
              <el-icon><SwitchButton /></el-icon>
              <span style="margin-left: 30px" @click="showLogoutConfirm">退出登录</span>
            </el-menu-item>
          </el-menu>
        </el-aside>
        <el-main id="content">
          <div class="backover" v-if="showLogin"></div>
          <transition name="fade">
            <div class="overlay" v-if="showLogin">
              <el-button class="close" @click="changeShow" plain round>
                <el-icon size="x-large">
                  <Close/>
                </el-icon>
              </el-button>
              <login @changeShow="changeShow"/>
            </div>
          </transition>

          <router-view v-slot="{ Component }">
            <transition name="route" mode="out-in" appear>
            <keep-alive>
                <component :is="Component" />
            </keep-alive>
            </transition>
          </router-view>

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
import { countMessage } from '@/apis/user';

import { ElMessage, ElMessageBox } from 'element-plus';

import Login from '@/views/Login/other.vue';
import Header from '@/components/function/Header.vue';
import SearchBox from '@/components/function/SearchBox.vue';

const router = useRouter();
const userStore = useUserStore();

const isSidebarOpen = ref(true);
const activeIndex = ref('1');
const post = ref(null); // 用于存储请求结果
const show = ref(false);

const search = ref(''); //搜索传参


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

function changeShow() {
  show.value = false;
}

function handleLoginClick() {
  window.history.pushState({}, "", `/user`);
  if (!userStore.userToken) {
    show.value = true;
  }}

function handleUpload() {
  window.history.pushState({}, "", `/user`);
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

const handleMessage = () => {
  window.history.pushState({}, "", `/user`);
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

//为了确保登录未过期，在进入页面之前试运行一次连接

onMounted(() => {
  if(userStore.userToken) {
    userStore.testLink();
  }
  {
    userStore.coutMsg();
  }
})
</script>

<style scoped>
.sidebar {
  overflow: hidden; /* 防止滚动条出现 */
  transition: width 0.25s ease;
}

.backover{
  position: fixed;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  background-color: rgba(0, 0, 0, 0.5); /* 灰色背景，透明度为0.5 */
  z-index: 100;
}

.overlay {
  position: fixed;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  z-index: 101; /* 设置一个较大的z-index值，确保图层位于其他内容之上 */
}

.close{
  border: 0;
  position: absolute;
  left: 100px;
  top: 80px;
  background-color: #ebeaea;
  z-index: 1000; /* 设置一个较大的z-index值，确保图层位于其他内容之上 */
}

.el-menu-vertical-demo:not(.el-menu--collapse) {
  width: 200px; /* 设置默认宽度 */
  height: 100%;
  background-color: #f1f1f1; /* 改变菜单背景颜色 */
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

.fade-enter-active {
  transition: all 0.3s ease;
}

.fade-leave-active {
  transition: all 0.3s cubic-bezier(1.0, 0.5, 0.8, 1.0);
}

.fade-enter-from,
.fade-leave-to {
  transform: scale(0.5); /* 缩放比例，初始为缩小的一半 */
  opacity: 0;
}

.route-enter-active,
.route-leave-active {
  transition: opacity 0.5s ease;
}

.route-enter-from,
.route-leave-to {
  opacity: 0;
}
</style>