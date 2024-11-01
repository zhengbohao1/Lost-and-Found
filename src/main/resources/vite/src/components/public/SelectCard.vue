<template>
  <el-card @click="expand"
    :style="{ 
      height: (fold ? details.height : 70) + 'px', 
      width: (fold ? 100 : details.width) + 'px', 
      'font-size': (fold ? 12 : details.fontsize) + 'px',
      'background': fold ? 'linear-gradient(to right, #ccc, #fff)' : 'linear-gradient(to right, rgb(200, 240, 255)  , #fff)',
      'color': fold ? '#000' : 'rgb(160, 150, 255)'
    }" 
    class="mycard">
    <slot></slot>
    <span>{{ details.content }}</span>
  </el-card>
</template>
  
  <script setup>
  import { ref, watch, toRef } from 'vue';
  import { useRoute, useRouter } from 'vue-router';

  const route = useRoute();
  const router = useRouter();
  
  const props = defineProps({
    details: {
      type: Object,
      required: true
    }
  });

  const details = toRef(props, 'details');
  
  const fold = ref(true);
  
  const expand = () => {
    fold.value = !fold.value;
    if(fold.value) {
      router.push({ path: `${details.value.basePath}`});
    }else{
      router.push({ path: `${details.value.path}`});
    }
  }

  watch(route, (newRoute) => {
    if (newRoute.path !== details.value.path) {
      fold.value = true;
    }
  }, { immediate: true }); // 立即执行一次以初始化状态
  </script>
  
  <style scoped>
  .mycard {
    display: flex;
    justify-content: center;
    align-items: center;
    transition: width 0.4s ease-in-out, height 0.4s ease-in-out, font-size 0.4s ease-in-out, background 0.4s ease-in-out;
    overflow: hidden;
    cursor: pointer;
  }
  </style>

一个选中伸缩长度的卡片组件，添加了rotuer.push方法，支持自定义跳转
支持自定义卡片内容，支持自定义卡片样式
要提供一个基址和变址，用于在卡片被点击时进行跳转，取消选中弹回基址，选中跳转变址
height
width
fontsize
content
basePath
path