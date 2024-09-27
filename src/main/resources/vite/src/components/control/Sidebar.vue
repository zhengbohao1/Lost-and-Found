<template>
  <!-- 侧边栏 -->
  <el-menu
    :router="true"
    :default-active="activeIndex"
    class="sidebar-menu"
  >
    <el-menu-item index="/control/adminhome">
      <el-icon><HomeFilled /></el-icon>
      首页
    </el-menu-item>
    <el-sub-menu :index="'2'" :class="{ 'is-active': isActiveSubMenu('2') }">
      <template #title>
        <el-icon><Menu /></el-icon>
        <span>功能模块</span>
      </template>
      <el-menu-item index="/control/managepost">发帖审核</el-menu-item>
      <el-menu-item index="/control/manageusers">用户管理</el-menu-item>
    </el-sub-menu>
    <el-menu-item index="/control/settings">设置</el-menu-item>
  </el-menu>
</template>

<script setup>
import { HomeFilled, Menu } from "@element-plus/icons-vue";
import { ref, watch, toRefs } from 'vue';
import { useRoute } from 'vue-router';

const route = useRoute();
const activeIndex = ref('');

// 监听路由变化来更新 activeIndex
watch(() => route.path, (newPath) => {
  activeIndex.value = newPath;
});

// 计算属性来判断子菜单是否激活
const isActiveSubMenu = (index) => {
  return route.path.startsWith(`/${index.split('/').pop()}`);
};
</script>

<style>
.sidebar-menu .el-menu-item,
.sidebar-menu .el-sub-menu__title {
  border-radius: 10px;
  margin: 2px 0;
  font-size: 16px;
}

.sidebar-menu .el-menu-item.is-active {
  background-color: #a5b4ff85 !important;
  color: #947fff !important;
}

.sidebar-menu .el-sub-menu.is-active .el-sub-menu__title {
  color: #947fff !important;
}

.sidebar-menu .el-menu-item:hover,
.sidebar-menu .el-sub-menu__title:hover {
  background-color: #bebdbd50 !important;
}
</style>