<template>
   <transition name="slide-fade">
    <el-card v-if="sidebarVisible" class="sidebar">
      <el-select v-model="selectedValue" placeholder="替换顶部样式" class="select-box">
        <el-option
          v-for="option in options"
          :key="option.value"
          :label="option.label"
          :value="option.value">
        </el-option>
      </el-select>
      <div class="button-box">
        <el-button type="primary" @click="confirmSelection">确定</el-button>
        <el-button @click="handleRightClick">取消</el-button>
      </div>
    </el-card>
  </transition>
  <div id="app" ref="appRef" @contextmenu.prevent="handleRightClick">
  </div>
</template>

<script setup>
import { ref, onMounted, onUnmounted } from 'vue';
import barnersData from '@/utils/config.js';

const appRef = ref(null);
const sidebarVisible = ref(false);
const selectedValue = ref('');

let allImagesData = barnersData[0].data;
let compensate = 0; // 视窗补偿值
let layers = []; // DOM 集合的引用
let initX = 0;
let moveX = 0;
let startTime = 0;
const duration = 300; // 动画持续时间（毫秒）

const test = ref([]);

// 计算线性插值
const lerp = (start, end, amt) => (1 - amt) * start + amt * end;

// 初始化项目
function initItems() {
  compensate = window.innerWidth > 1650 ? window.innerWidth / 1650 : 1;
  const cloneData = JSON.parse(JSON.stringify(allImagesData));
  appRef.value.innerHTML = '';
  layers = [];

  for (let i = 0; i < cloneData.length; i++) {
    const item = cloneData[i];
    const layer = document.createElement('div');
    layer.classList.add('layer'); // 添加 layer css类名

    layer.style.zIndex = i * 3; // 设置 z-index

    if (compensate !== 1) {
      item.transform[4] = item.transform[4] * compensate;
      item.transform[5] = item.transform[5] * compensate;
    }

    layer.style.transform = new DOMMatrix(item.transform).toString();
    if (item.opacity) {
      layer.style.opacity = item.opacity[0];
    }

    const child = document.createElement(item.tagName || 'img');
    if (item.tagName === 'video') {
      child.loop = true;
      child.autoplay = true;
      child.muted = true;
    }
    child.src = item.src;
    child.style.filter = `blur(${item.blur}px)`;
    child.style.width = `${item.width * compensate}px`;
    child.style.height = `${item.height * compensate}px`;

    layer.appendChild(child);
    appRef.value.appendChild(layer);
  }

  layers = Array.from(appRef.value.querySelectorAll('.layer'));
  console.log(layers);
}

// 滑动操作
function mouseMove() {
  animate();
}

// 离开视窗或浏览器时执行回正动画
function leave() {
  startTime = 0;
  requestAnimationFrame(homing);
}

// 回正动画
function homing(timestamp) {
  if (!startTime) startTime = timestamp;
  const elapsed = timestamp - startTime;
  const progress = Math.min(elapsed / duration, 1);
  animate(progress);
  if (progress < 1) requestAnimationFrame(homing);
}

// 动画执行
function animate(progress = null) {
  if (layers.length <= 0) return;
  const isHoming = typeof progress === 'number';

  for (let i = 0; i < layers.length; i++) {
    const layer = layers[i];
    const item = allImagesData[i];
    let m = new DOMMatrix(item.transform);
    let move = moveX * item.a;
    let s = item.f ? item.f * moveX + 1 : 1;
    let g = moveX * (item.g || 0);

    if (isHoming) {
      m.e = lerp(moveX * item.a + item.transform[4], item.transform[4], progress);
      move = 0;
      s = lerp(item.f ? item.f * moveX + 1 : 1, 1, progress);
      g = lerp(item.g ? item.g * moveX : 0, 0, progress);
    }

    m = m.multiply(new DOMMatrix([m.a * s, m.b, m.c, m.d * s, move, g]));

    if (item.deg) {
      const deg = isHoming ? lerp(item.deg * moveX, 0, progress) : item.deg * moveX;
      m = m.multiply(new DOMMatrix([Math.cos(deg), Math.sin(deg), -Math.sin(deg), Math.cos(deg), 0, 0]));
    }

    if (item.opacity) {
      layer.style.opacity = isHoming && moveX > 0 ? lerp(item.opacity[1], item.opacity[0], progress) : lerp(item.opacity[0], item.opacity[1], (moveX / window.innerWidth) * 2);
    }

    layer.style.transform = m.toString();
  }
}

// 鼠标事件
const handleMouseEnter = (e) => { initX = e.pageX; };
const handleMouseMove = (e) => { moveX = e.pageX - initX; requestAnimationFrame(mouseMove); };
const handleMouseLeave = leave;

const options = ref([
  ...barnersData.map((banner, index) => ({
    value: index,
    label: banner.name
  }))
]);

// 处理右键点击事件
function handleRightClick(event) {
  sidebarVisible.value = !sidebarVisible.value;
}

function confirmSelection() {
  switch (selectedValue.value) {
    case 0:
      allImagesData = barnersData[0].data;
      initItems();
      break;
    case 1:
      allImagesData = barnersData[1].data;
      initItems();
      break;
    case 2:
      allImagesData = barnersData[2].data;
      initItems();
      break;
    case 3:
      allImagesData = barnersData[3].data;
      initItems();
      break;
      break;
    case 4:
      allImagesData = barnersData[4].data;
      initItems();
      break;
    case 5:
      allImagesData = barnersData[5].data;
      initItems();
      break;
    case 6:
      allImagesData = barnersData[6].data;
      initItems();
      break;
    case 7:
      allImagesData = barnersData[7].data;
      initItems();
      break;
    case 8:
      allImagesData = barnersData[8].data;
      initItems();
      break;
    case 9:
      allImagesData = barnersData[9].data;
      initItems();
      break;
    default:
      break;
  }
  // 处理用户选择的值
  handleRightClick(); // 关闭侧边栏
}

// 组件挂载时初始化
onMounted(() => {
  initItems();
  appRef.value.addEventListener('mouseenter', handleMouseEnter);
  appRef.value.addEventListener('mousemove', handleMouseMove);
  appRef.value.addEventListener('mouseleave', handleMouseLeave);
  window.addEventListener('resize', initItems);
  window.onblur = handleMouseLeave;
});

// 组件卸载时清理
onUnmounted(() => {
  document.getElementById('selectBox').removeEventListener('click', handleSelectBoxClick);
  appRef.value.removeEventListener('mouseenter', handleMouseEnter);
  appRef.value.removeEventListener('mousemove', handleMouseMove);
  appRef.value.removeEventListener('mouseleave', handleMouseLeave);
  window.removeEventListener('resize', initItems);
  window.onblur = null;
});
</script>

<style scoped>
#app {
  position: relative;
  overflow: hidden;
  margin: 0 auto;
  min-width: 1000px;
  min-height: 155px;
  height: 10vw;
  max-height: 240px;
}

.sidebar {
  position: absolute;
  top: 8px;
  left: 0;
  padding: 20px;
  background-color: #ffffff33;
  border: 1px solid #ddd;
  z-index: 100;
  width: 200px;
  height: 110px;
  display: flex;
  flex-direction: column;
  justify-content: space-between; /* 垂直方向上均匀分布 */
}

.select-box {
  flex-grow: 1; /* 使 select 占据剩余空间 */
  margin-bottom: 10px; /* 与按钮之间留有间隔 */
}

.button-box {
  display: flex;
  justify-content: space-between; /* 按钮之间水平居中 */
}

.slide-fade-enter-active {
  transition: all 0.3s ease;
}

.slide-fade-leave-active {
  transition: all 0.3s cubic-bezier(1.0, 0.5, 0.8, 1.0);
}

.slide-fade-enter-from,
.slide-fade-leave-to {
  transform: translateX(-10px);
  opacity: 0;
}
</style>

<style>
.layer {
  position: absolute;
  left: 0;
  top: 0;
  height: 100%;
  width: 100%; 
  display: flex;
  align-items: center;
  justify-content: center;
}

.layer img {
  user-select: none;
  pointer-events: none;
}

#selectBox > button {
  margin-right: 1em;
}
</style>