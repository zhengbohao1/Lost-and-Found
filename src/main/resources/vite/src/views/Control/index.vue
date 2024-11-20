<template>
  <div id="app" v-on:scroll.stop.prevent="handleScroll">
    <el-container>
      <el-aside width="200px" class="aside-shadow">
        <img src="@/assets/logo_longer.png" width="100%"/>
       <Sidebar/>
      </el-aside>
      <el-container>
        <el-header>
          <!-- 导航栏 -->
          <Header @update-collapse="handleCollapse" />
        </el-header>
        <el-main @touchmove.stop>
          <!-- 内容区域 -->
          <MainContent />
        </el-main>
      </el-container>
    </el-container>
  </div>
</template>

<script setup>
import { ref } from 'vue';
import 'element-plus/dist/index.css'
import Sidebar from '@/components/control/Sidebar.vue';
import Header from '@/components/control/Header.vue';
import MainContent from '@/components/control/MainContent.vue';

const message = ref(false);
const handleScroll=ref(true);

const handleCollapse = (data) => {
  message.value = data;
};

const RemoveScroll = () => {
  document.body.style.overflow = "hidden";
} 

RemoveScroll()  //禁用滚动条
</script>

<style scoped>
.aside-shadow {
  height: 96vh;
  box-shadow: 4px 0 8px rgba(0, 0, 0, 0.1); /* X轴偏移量，Y轴偏移量，模糊半径，颜色 */
}
</style>