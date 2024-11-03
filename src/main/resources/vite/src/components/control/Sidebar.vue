<template>
  <!-- 侧边栏 -->
   <el-scrollbar style="height: 730px">
  <el-menu
    :router="true"
    class="sidebar-menu"
    height="100%"
  >
    <el-menu-item index="/control/adminhome">
      <el-icon><HomeFilled /></el-icon>
      首页
    </el-menu-item>
    <el-sub-menu :index="'/control/function'">
      <template #title>
        <el-icon><Menu /></el-icon>
        <span :style="{'color': (route.path.includes('/control/function')) ? '#947fff' : ''}">功能模块</span>
      </template>
      <el-sub-menu :index="'/control/function/found'">
        <template #title>
          <el-icon><Menu /></el-icon>
          <span :style="{'color': (route.path.includes('/control/function/found')) ? '#947fff' : ''}">失物招领</span>
        </template>
        <el-menu-item index="/control/function/found/passpost">发帖审核</el-menu-item>
        <el-menu-item index="/control/function/found/deletepost">帖子管理</el-menu-item>
      </el-sub-menu>
      <el-sub-menu :index="'/control/function/lost'">
        <template #title>
          <el-icon><Menu /></el-icon>
          <span :style="{'color': (route.path.includes('/control/function/lost')) ? '#947fff' : ''}">寻物启事</span>
        </template>
        <el-menu-item index="/control/function/lost/passpost">发帖审核</el-menu-item>
        <el-menu-item index="/control/function/lost/deletepost">帖子管理</el-menu-item>
      </el-sub-menu>
      <el-sub-menu index="/control/function/manageprize">
        <template #title>
          <el-icon><Present /></el-icon>
          <span :style="{'color': (route.path.includes('/control/function/manageprize')) ? '#947fff' : ''}">奖品管理</span>
        </template>
        <el-menu-item index="/control/function/manageprize">管理奖品</el-menu-item>
      </el-sub-menu>
      <el-menu-item index="/control/manageusers">用户管理</el-menu-item>
      <el-menu-item index="/control/manageadvises">用户建议</el-menu-item>
    </el-sub-menu>
    <el-menu-item index="/user">设置</el-menu-item>
  </el-menu>
</el-scrollbar> 
</template>

<script setup>
import { ref } from "vue";
import { HomeFilled, Menu, Present } from "@element-plus/icons-vue";
import { useRoute } from 'vue-router';

const route = useRoute();
</script>

<style scoped>
.sidebar-menu .el-menu-item,
.sidebar-menu .el-sub-menu__title {
  border-radius: 10px;
  margin: 2px 0;
  font-size: 16px;
}

.sidebar-menu .el-menu-item.is-active,
.sidebar-menu .el-sub-menu__title.is-active {
  background-color: #a5b4ff85 !important;
  color: #947fff !important;
}

.sidebar-menu .el-menu-item:hover,
.sidebar-menu .el-sub-menu__title:hover {
  background-color: #bebdbd50 !important;
}
  
</style>