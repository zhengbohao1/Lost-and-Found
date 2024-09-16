<template>  
  <el-container style="height: 80vh;">  
    <el-header>
      <el-row>
      <el-col :span="3">
         <el-image class="component" :src="require('@/assets/logo_longer.png')" style="width: 100%;"/>
      </el-col>
      <el-col :span="18">
        <el-menu  
            style="margin: 0; padding: 0; height: 100%;"
            mode="horizontal"  
          >  
          <el-menu-item index="1" style="width:23%; font-size: 20px;">首页</el-menu-item>
          <el-menu-item index="1" style="width:23%; font-size: 20px;">关于我们</el-menu-item>
          <el-menu-item index="1" style="width:23%; font-size: 20px;">联系我们</el-menu-item>
          <el-menu-item index="1" style="width:23%; font-size: 20px;">加入我们</el-menu-item>
        </el-menu>
      </el-col>
      <el-col :span="3">
        <el-button type="primary" round style="height: 60%; width: 60%;font-size: 20px;" @click="login">登录</el-button>
      </el-col>
    </el-row>
    </el-header>      
      <el-main>Main Content</el-main>  
    </el-container>   
</template>  
  
<script setup>
import {useRouter} from 'vue-router'

const router = useRouter();
const login = () => {
  router.push('/login');
} //跳转至登录页面
</script>  
  
<style scoped>  
</style>