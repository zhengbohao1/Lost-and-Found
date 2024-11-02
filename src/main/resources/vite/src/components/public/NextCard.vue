<template>
    <el-card v-show="!afterClosed" class="mycard" :style="{'width': (closew ? 40 : details.width) + 'px', 
    'height': (closeh ? 0 : details.height) + 'px', 
    'border-radius': (closew ? 50 : 0) + 'px', 
    ...mystyle}">
      <el-row v-show="!closew">
        <v-btn density="compact" icon="mdi-close" style="z-index:10;position: absolute; top:0; right:0;" @click="closed"></v-btn>
      </el-row>
      <slot></slot>
    </el-card>
  </template>
  
  <script setup>
  import { ref, watch, defineEmits } from 'vue';
  
  const props = defineProps({
    details: {
      type: Object,
      required: true
    },
    mystyle: {
        type: Object,
        default: {}
    }
  });
  
  const closew = ref(true);
  const closeh = ref(true);
  const afterClosed = ref(true);
  const emit = defineEmits(['closed']);
  
  const closed = () => {
    emit('closed',false);
    closew.value = true;
    setTimeout(() => {
      closeh.value = true;
    }, 400);
    setTimeout(() => {
      afterClosed.value = true;
    }, 800);
  }
  
  const open = () => {
    afterClosed.value = false;
    closew.value = true;
    setTimeout(() => {
      closeh.value = false;
    }, 0);
    setTimeout(() => {
      closew.value = false;
    }, 400);
  }
  
  watch(() => props.details.close, (newVal) => {
    if (newVal) {
      closed();
    } else {
      open();
    }
  }),{ immediate: true };
  </script>
  
  <style scoped>
  .mycard {
    -webkit-transition: width 0.4s ease-in-out, height 0.4s ease-in-out, border-radius 0.4s ease-in-out;
    transition: width 0.4s ease-in-out, height 0.4s ease-in-out, border-radius 0.4s ease-in-out;
  }
</style>

一个衔接其他组件的功能拓展卡片，也是先变高再变宽
由于是功能拓展，所以留了控制显示用的参数
它的开关由父组件操控，所以没有提供开关按钮
需要
width
height
控制开关的close
支持外部style（主要用来写margin）