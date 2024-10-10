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
    <el-sub-menu :index="'/control/function'">  
      <template #title>  
        <el-icon><Menu /></el-icon>  
        <span>功能模块</span>  
      </template>  
      <el-sub-menu :index="'/control/function/found'">  
        <template #title>  
          <el-icon><Menu /></el-icon>  
          <span>失物招领</span>  
        </template>  
        <el-menu-item index="/control/function/found/passpost">发帖审核</el-menu-item>  
        <el-menu-item index="/control/function/found/deletepost">帖子管理</el-menu-item>  
      </el-sub-menu>  
      <el-sub-menu :index="'/control/function/lost'">  
        <template #title>  
          <el-icon><Menu /></el-icon>  
          <span>寻物启事</span>  
        </template>  
        <el-menu-item index="/control/function/lost/passpost">发帖审核</el-menu-item>  
        <el-menu-item index="/control/function/lost/deletepost">帖子管理</el-menu-item>  
      </el-sub-menu>  
      <el-menu-item index="/manageusers">用户管理</el-menu-item>  
      <el-menu-item index="/manageadvises">用户建议</el-menu-item>  
    </el-sub-menu>   
    <el-menu-item index="/user">设置</el-menu-item>  
  </el-menu>  
</template>  
  
<script setup>  
import { HomeFilled, Menu } from "@element-plus/icons-vue";  
import { useRoute } from 'vue-router';  
  
</script>  

<style >
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


.sidebar-menu .el-menu-item:hover,
.sidebar-menu .el-sub-menu__title:hover {
  background-color: #bebdbd50 !important;
}
</style>