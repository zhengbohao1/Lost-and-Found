<template>
  <el-card v-if="!afterClosed" class="mycard" 
    :style="{'width': (closew ? 40 : details.width) + 'px',
    'height': (closeh ? 40 : details.height) + 'px',
    'border-radius': (closew ? 50 : 0) + 'px',
     ...mystyle}">
    <el-row v-show="!closew" style="z-index: 10">
      <v-btn density="compact" icon="mdi-close" style="position: absolute; top:0; right:0;" @click="closed"></v-btn>
    </el-row>
    <slot></slot>
  </el-card>
  <v-btn size="small" :style="{...mystyle}" v-if="afterClosed" :icon="details.icon" @click="open"></v-btn>
</template>

<script setup>
import { ref } from 'vue';

// 定义并接收来自父组件的props
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

const closew = ref(false);
const closeh = ref(false);
const afterClosed = ref(false);

const closed = () => {
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
</script>

<style scoped>
.mycard {
  -webkit-transition: width 0.4s ease-in-out, height 0.4s ease-in-out, border-radius 0.4s ease-in-out;
  transition: width 0.4s ease-in-out, height 0.4s ease-in-out, border-radius 0.4s ease-in-out;
}
</style>

一个方形转圆形的组件，建议在侧边使用，关闭后会以vbtn的方式显示，点击后可以再次打开。
支持自定义参数动态调整大小
外部手动提高z-index可以当作悬浮小窗口使用
允许外部传递非重叠的style
需要
width
heigth
icon