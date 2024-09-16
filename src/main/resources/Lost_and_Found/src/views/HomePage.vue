<template>  
    <el-container style="height: 80vh;">  
      <el-header>
        <el-row>
        <el-col :span="3">
           <el-image class="component" :src="require('@/assets/logo_longer.png')" style="height: 80%;"/>
        </el-col>
      </el-row>
      </el-header>  
        <el-divider></el-divider>
        <el-aside width="200px">Aside</el-aside>  
          
        <el-main>Main Content</el-main>  
      </el-container>   
  </template>  
    
  <script setup>  
  </script>  
    
  <style scoped>  
  </style>