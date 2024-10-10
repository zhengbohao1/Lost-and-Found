<template>
  <div class="common-layout">
    <el-container>
      <el-header>
        <el-row>
          <el-col :span="4">
            <span>Header</span>
          </el-col>
          <el-col :span="12"></el-col>
          <el-col :span="8">
            <div style="display: flex; justify-content: space-between; align-items: center; margin-top: 10px ;">
              <!-- 添加搜索框 -->
              <div class="input_box">
                <el-input placeholder="请输入内容" :prefix-icon="Search" size="large">
                  <template #append>
                      <el-button :icon="Search"> </el-button>
                    </template>
                </el-input>
              </div>
            </div>
          </el-col>
        </el-row>
      </el-header>
      <el-container style="height: 700px">
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
            <el-menu-item :index="userStore.userToken ? '/user/space' : '/user'"
                          :class="{'top-menu-item': true, 'collapsed': !isSidebarOpen}">
              <template v-if="!userStore.userToken">
                <el-icon><User /></el-icon>
                <span style="margin-left: 30px" @click="handleLoginClick">立即登录</span>
              </template>
              <template v-else>
                <el-avatar :src="'http://localhost:8090/user/getAvatarById?userId='+userStore.userInfo.data.userId" fit="cover"></el-avatar>
                <span style=" margin-left: 30px;" class="username">{{ userStore.userInfo.data.nickName }}</span>
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
            <el-menu-item index="">
              <el-icon><Promotion /></el-icon>
              <span style="margin-left: 40px" @click="handleUpload">发布</span>
            </el-menu-item>
            <el-menu-item index="/user/message">
              <el-icon><Comment /></el-icon>
              <el-badge v-if="value > 0 " :value="3" :hidden="value <= 0" :max="99" class="item-badge"></el-badge>
              <span :style="value > 0 ? { 'margin-left': '20px' } : { 'margin-left': '40px' }">通知</span>
            </el-menu-item>
            <el-menu-item index="/user/feedback">
              <el-icon><WarnTriangleFilled /></el-icon>
              <span style="margin-left: 30px">意见反馈</span>
            </el-menu-item>
            <!-- 添加 Popconfirm -->
            <el-popconfirm title="您确定要退出登录吗？" @confirm="loginOut" :disabled="!userStore.userToken" >
              <template #reference>
                <el-menu-item :disabled="!userStore.userToken">
                  <el-icon><SwitchButton /></el-icon>
                  <span style="margin-left: 10px">退出登录</span>
                </el-menu-item>
              </template>
            </el-popconfirm>
          </el-menu>
        </el-aside>
        <el-main id="content">
          <transition>
            <div class="overlay" v-if="showLogin">
              <el-button class="close" @click="changeShow" plain round>
                <el-icon size="x-large">
                  <Close/>
                </el-icon>
              </el-button>
              <login @changeShow="changeShow"/>
            </div>
          </transition>
          <router-view></router-view>
        </el-main>
      </el-container>
      <el-footer>Footer</el-footer>
    </el-container>
  </div>
</template>

<script setup>
import { ref, computed, onMounted, watch } from 'vue';
import { Fold, Expand, House, SwitchButton, Promotion, WarnTriangleFilled, User, Comment, Search, Close } from '@element-plus/icons-vue';
import { useRouter } from 'vue-router';
import { useUserStore } from '@/stores/user';
import { getUserAvatar } from '@/apis/user';

import Login from '@/views/Login/onther.vue';

const router = useRouter();
const userStore = useUserStore();

const isSidebarOpen = ref(true);
const activeIndex = ref('1');
const post = ref(null); // 用于存储请求结果
const show = ref(false);

const loginOut = () => {
  userStore.userLogout();
  console.log("userToken");
  console.log(userStore.userToken);
  console.log("userInfo");
  console.log(userStore.userInfo);
  if(userStore.userToken === null) {
      ElMessage({
      message: '登出成功',
      type: 'success',
    });
  }
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
  if (!userStore.userToken) {
    show.value = true;
  }}

function handleUpload() {
  if(userStore.userToken) {
    router.push('/upload');
  } else {
    show.value = true;
    ElMessage({
      message: '请先登录',
      type: 'warning',
    });
  }
}

  const showLogin = computed(() => show.value && !userStore.userToken);

  watch(() => userStore.userToken, (newToken) => {
  if (newToken) {
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

.input_box {
    width: 400px;
    margin-right: 15px;
    border-radius: 95px;
    background: rgba(227, 227, 227, 0.48);
}
/*搜索input框 */
:deep(.el-input__wrapper) {
    background-color: transparent;/*覆盖原背景颜色，设置成透明 */
    border-radius: 95px;
    border: 0;
    box-shadow: 0 0 0 0px;
}
/*搜索button按钮 */
:deep(.el-input-group__append) {
    background: rgba(219, 219, 219, 0.48);
    border-radius: 95px;
    border: 0;
    box-shadow: 0 0 0 0px;
}

:deep(.el-input-group__append :hover) {
    color: blue;
    font-size: 15px;
}
</style>