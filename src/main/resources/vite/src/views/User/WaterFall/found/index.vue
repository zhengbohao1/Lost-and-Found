<template v-if="cards.length>0">
    <div class="Empty" v-if="cards.length === 0">
    <el-empty description="没有帖子..."/>
  </div>
  <el-scrollbar v-else>
    <div v-infinite-scroll="loadMore" :infinite-scroll-disabled="disabled" :infinite-scroll-distance="200">
      <ViewCard :card_columns="card_columns"></ViewCard>
    </div>
    <transition
        name="fade"
        @before-enter="onBeforeEnter"
        @after-enter="onAfterEnter"
        @before-leave="onBeforeLeave"
        @after-leave="onAfterLeave"
    >
      <div class="overlay" v-if="show">
        <button style="display:none;" class="backPage" @click="close">
          <el-icon>
            <Back/>
          </el-icon>
        </button>
        <card-detail :detail="detail" @afterDoComment="afterDoComment" ref="overlay"/>
      </div>
    </transition>
  </el-scrollbar>
</template>

<script setup>
import { onMounted, onUnmounted, ref, nextTick, watch } from 'vue';
import ViewCard from '@/components/user/Card.vue';
import { queryPost } from '@/apis/found';
import { useRoute } from 'vue-router';
import { waterFallInit, waterFallMore, resizeWaterFall } from '@/utils/waterFall';

const cards = ref([]);  //包含了所有帖子的所有内容

// 主页卡片 //////////////////////////////////////////////////////////////////

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

// 卡片详情 //////////////////////////////////////////////////////////////////

const postDetail = ref(null);
const show = ref(false);
const overlayX = ref(0); // 覆盖层的水平位置
const overlayY = ref(0); // 覆盖层的垂直位置
const overlay = ref(null)

const getDetails = async (id) => Details.getDetail(id)

const showMessage = async (id, left, top) => {
  window.history.pushState({}, "", `/explore/${id}`);
  overlayX.value = left;
  overlayY.value = top;
  await getDetails(id);
  show.value = true;
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

<style scoped>
  .overlay {
    position: fixed;
    top: 0;
    left: 0;
    width: 100%;
    height: 100%;
    z-index: 9999;
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
</style>