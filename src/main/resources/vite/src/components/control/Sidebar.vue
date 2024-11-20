<template>
  <!-- 侧边栏 -->
  <el-menu
    :router="true"
    class="sidebar-menu"
    height="100%"
  >
    <el-menu-item index="/adminhome">
      <el-icon><HomeFilled /></el-icon>
      首页
    </el-menu-item>
    <el-sub-menu :index="'/control/function'" :class="{ 'is-active': isActiveSubMenu('/control/function') }">
      <template #title>
        <el-icon><Menu /></el-icon>
        <span>功能模块</span>
      </template>
      <el-sub-menu  :index="'/control/function/found'" :class="{ 'is-active': isActiveSubMenu('/control/function/found')}">
        <template #title>
          <el-icon><Menu /></el-icon>
          <span>失物</span>
        </template>
        <el-menu-item index="/control/function/found/passpost">发帖审核</el-menu-item>
        <el-menu-item index="/control/function/found/deletepost">帖子管理</el-menu-item>
      </el-sub-menu>
      <el-menu-item index="/manageusers">用户管理</el-menu-item>
      <el-menu-item index="/manageadvises">用户建议</el-menu-item>
    </el-sub-menu>
    <el-menu-item index="/settings">设置</el-menu-item>
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
  const lastSegment = index.split('/').pop();  
  return route.matched.some(record => record.path.endsWith(`/${lastSegment}`));  
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