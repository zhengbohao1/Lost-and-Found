<template>
  <div class="common-layout">
    <el-container>
      <el-header>Header</el-header>
      <el-container style="height: 700px">
        <el-aside :width="isSidebarOpen ? '200px' : '70px'" class="sidebar">
          <el-menu :default-active="activeIndex" class="el-menu-vertical-demo"
                    :router="true"
                    :collapse="!isSidebarOpen"
                    @open="handleOpen"
                    @close="handleClose">
              <el-tooltip :content="isSidebarOpen ? '收起' : '展开'" placement="right">
                <el-button text style="margin-left: 7px; margin-top: 20px; margin-bottom: 50px; background-color: #f1f1f1;"
                          :icon="isSidebarOpen ? Fold : Expand"
                          @click="toggleSidebar"></el-button>
            </el-tooltip>
            <el-menu-item index="/home">
              <el-icon><House /></el-icon>
              <span style="margin-left: 20px">主页</span>
            </el-menu-item>
            <el-menu-item index="/upload">
              <el-icon><Promotion /></el-icon>
              <span style="margin-left: 20px">发布</span>
            </el-menu-item>
            <el-menu-item index="1">
              <el-icon><SwitchButton /></el-icon>
              <span style="margin-left: 10px">退出登录</span>
            </el-menu-item>
          </el-menu>
        </el-aside>
        <el-main id="content">
          <router-view></router-view>
        </el-main>
      </el-container>
      <el-footer>Footer</el-footer>
    </el-container>
  </div>
</template>

<script setup>
import { ref, computed } from 'vue';
import { Fold, Expand, House, SwitchButton, Promotion } from '@element-plus/icons-vue';

const isSidebarOpen = ref(true);
const activeIndex = ref('1');
const post = ref(null); // 用于存储请求结果

function toggleSidebar() {
  isSidebarOpen.value = !isSidebarOpen.value;
}

const handleOpen = (key, keyPath) => {
  console.log(key, keyPath);
}

function handleClose(key, keyPath) {
  console.log(key, keyPath);
}

</script>

<style scoped>
/* 你的样式 */
.sidebar {
  overflow: hidden; /* 防止滚动条出现 */
  transition: width 0.25s ease;
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

.el-menu-vertical-demo .el-menu-item, 
.el-menu-vertical-demo .el-submenu__title {
  border-radius: 16px; /* 设置圆角 */
  margin: 5px;
  color:#636363;
}

.el-menu-vertical-demo .el-menu-item:hover,
.el-menu-vertical-demo .el-submenu__title:hover {
  background-color: #b9b9b9; /* 改变悬停和激活时的背景色 */
  border-radius: 16px; /* 设置圆角 */
  color: #3a3a3a;
}
</style>