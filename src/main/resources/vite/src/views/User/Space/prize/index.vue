<template v-if="prizeList.length>0">  
    <div>
      <div class="Empty" v-if="prizeList.length === 0">
        <el-empty description="没有奖品..."/>
      </div>
      <el-scrollbar v-else>
        <div v-infinite-scroll="loadMore" :infinite-scroll-disabled="disabled" :infinite-scroll-distance="200">
          <ViewCard :card_columns="card_columns" @show-detail="showMessage" ></ViewCard>
        </div>
      </el-scrollbar>
      <div class="backover" v-if="show"></div>
      <transition name="fade">
          <div class="overlay" v-if="show">
            <el-button class="backPage" @click="close" :icon="Close"></el-button>
            <CardDetail :postid="postid" @afterDoComment="afterDoComment" ref="overlay"></CardDetail>
          </div>
      </transition>
    </div>
    </template>
    

<script setup>
import { ref, reactive, onMounted } from 'vue'
import { getPrizeList, exchangePrize } from '@/apis/prize'

import ViewCard from '@/components/user/PrizeCard.vue';
import { waterFallInitForPrize, waterFallMore, resizeWaterFall } from '@/utils/waterFall';
import { ElMessage } from 'element-plus'
import { Close } from '@element-plus/icons-vue';

const disabled = ref(true);

const columns = ref(0);
const card_columns = ref({});
const arrHeight = ref([]);

const prizeList = ref([])

const getPrize = async () => {
    await getPrizeList().then(res => {
        prizeList.value = res.data
        console.log('image', prizeList.value)
        waterFallInitForPrize(columns, card_columns, arrHeight, prizeList)
    })
    disabled.value = false;
    console.log('121',prizeList.value[0])
}

    // 无限滚动
    const loadMore = async () => {
      disabled.value = true;
      const res = await getPrizeList();
      const more = res.data;
      if (more.length > prizeList.value.length) {
        const newPost = more.slice(prizeList.value.length, more.length);
        prizeList.value.push(...newPost);
        disabled.value = false;
        // 调用 waterFallMore 更新布局
        waterFallMore(columns.value, card_columns.value, arrHeight.value, prizeList.value);
      } else {
        disabled.value = true;
      }
    };

    // 卡片详情 //////////////////////////////////////////////////////////////////
    const show = ref(false);
    const overlayX = ref(0); // 覆盖层的水平位置
    const overlayY = ref(0); // 覆盖层的垂直位置
    const overlay = ref(null)
    const postid = ref(null)

onMounted(() => {
    getPrize()
})
</script>

<style scoped>
.backover{
  position: fixed;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  background-color: rgba(0, 0, 0, 0.5); /* 灰色背景，透明度为0.5 */
  z-index: 100;
}

.overlay {
  position: fixed;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  z-index: 101;
}

.backPage {
  position: fixed;
  top: 5%;
  left: 3%;
  justify-content: center;
  align-items: center;
  width: 40px;
  height: 40px;
  border-radius: 40px;
  border: 1px solid var(--color-border);
  cursor: pointer;
  transition: all .3s;
}

.fade-enter-active {
  transition: all 0.3s ease;
}

.fade-leave-active {
  transition: all 0.3s cubic-bezier(1.0, 0.5, 0.8, 1.0);
}

.fade-enter-from,
.fade-leave-to {
  transform: scale(0.5); /* 缩放比例，初始为缩小的一半 */
  opacity: 0; /* 不透明度为0，隐藏元素 */
}
</style>