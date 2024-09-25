<template>
  <div id="app">
    <el-container>
      <el-aside width="200px" class="aside-shadow">
        <img src="@/assets/logo_longer.png" width="100%" />
        <Sidebar/>
      </el-aside>
      <el-container>
        <el-header>
          <!-- 导航栏 -->
          <div></div>
          <Header @update-collapse="handleCollapse" />
        </el-header>
        <el-main>
          <!-- 内容区域 -->
          <MainContent />
        </el-main>
      </el-container>
    </el-container>
  </div>
</template>

<script setup>
import { ref } from 'vue';
import Sidebar from '@/components/control/Sidebar.vue';
import Header from '@/components/control/Header.vue';
import MainContent from '@/components/control/MainContent.vue';

const message = ref(false);

const handleCollapse = (data) => {
  message.value = data;
};
</script>

<style>
.aside-shadow {
  box-shadow: 4px 0 8px rgba(0, 0, 0, 0.1); /* X轴偏移量，Y轴偏移量，模糊半径，颜色 */
}
</style>