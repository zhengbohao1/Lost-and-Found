<template v-if="cards.length>0">
    <div class="Empty" v-if="cards.length === 0">
    <el-empty description="没有帖子..."/>
  </div>
  <div v-else>
    <div v-infinite-scroll="loadMore" :infinite-scroll-disabled="disabled" :infinite-scroll-distance="200">
      <ViewCard :card_columns="card_columns"></ViewCard>
    </div>
    </div>
</template>

<script setup>
import { onMounted, onUnmounted, ref, nextTick, watch } from 'vue';
import ViewCard from '@/components/user/Card.vue';
import { queryPost } from '@/apis/main';
import { useRoute } from 'vue-router';
import { waterFallInit, waterFallMore, resizeWaterFall } from '@/utils/waterFall';

// 主页卡片 //////////////////////////////////////////////////////////////////

const cards = ref([]);
const disabled = ref(true);
const route = useRoute();

const columns = ref(0);
const card_columns = ref({});
const arrHeight = ref([]);

const queryPosts = async () => {
  const res = await queryPost()
  cards.value = res.data
  console.log('test', cards.value)
  nextTick(() => {
    waterFallInit(columns, card_columns, arrHeight, cards)
    console.log('test2', card_columns)
  })
  disabled.value = false; // 启用滚动加载
};

// 无限滚动
const loadMore = async () => {
  disabled.value = true;
  const res = await queryPost();
  const more = res.data;
  if (more.length > cards.value.length) {
    const newPost = more.slice(cards.value.length, more.length);
    cards.value.push(...newPost);
    disabled.value = false;
    // 调用 waterFallMore 更新布局
    waterFallMore(columns.value, card_columns.value, arrHeight.value, cards.value);
  } else {
    disabled.value = true;
  }
};

watch(route, () => {
    queryPosts();
  });

onMounted(() => {
  queryPosts(); 
  // 清理函数
  onUnmounted(() => {
    window.removeEventListener('resize', () => {
      resizeWaterFall(columns.value, card_columns.value, arrHeight.value, cards.value);
    });
  });
});
</script>