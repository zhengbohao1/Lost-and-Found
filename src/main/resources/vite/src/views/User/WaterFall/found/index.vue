<template v-if="cards.length>0">  
    <div class="Empty" v-if="cards.length === 0">
    <el-empty description="没有帖子..."/>
  </div>
  <el-scrollbar v-else>
    <div v-infinite-scroll="loadMore" :infinite-scroll-disabled="disabled" :infinite-scroll-distance="200">
      <ViewCard :card_columns="card_columns" @show-detail="showMessage" ></ViewCard>
    </div>
  </el-scrollbar>
  <transition
        name="fade"
        @before-enter="onBeforeEnter"
        @after-enter="onAfterEnter"
        @before-leave="onBeforeLeave"
        @after-leave="onAfterLeave"
    >
      <div class="overlay" v-if="show">
        <el-button class="backPage" @click="close" :icon="Close"></el-button>
        <CardDetail :postid="postid" @afterDoComment="afterDoComment" ref="overlay"></CardDetail>
        <!--路由中携带了id，可以选择不用父子传递信息，而使用path传值或者pinia的方式-->
      </div>
    </transition>
</template>

<script setup>
import { onMounted, onUnmounted, ref, nextTick, watch } from 'vue';
import ViewCard from '@/components/user/Card.vue';
import CardDetail from '@/components/user/FoundDetail.vue';
import { queryPost } from '@/apis/found';
import { getUserName } from '@/apis/user';
import { useRoute } from 'vue-router';
import { waterFallInit, waterFallMore, resizeWaterFall } from '@/utils/waterFall';
import { Close } from '@element-plus/icons-vue';


const cards = ref([]);  //包含了所有帖子的所有内容

// 主页卡片 //////////////////////////////////////////////////////////////////

const disabled = ref(true);
const route = useRoute();

const columns = ref(0);
const card_columns = ref({});
const arrHeight = ref([]);

const addSenderName = async () => {
  cards.value = cards.value.map((card) => ({
    ...card,
    nickName: '',
  }));
  for(let card of cards.value){
    getUserName(card.finderId).then(response => {
      card.nickName  = response.data;
    });
  }
};

const queryPosts = async () => {
  const res = await queryPost()
  if(res.msg == 'error'){
    ElMessage.error('获取帖子失败')
    return;
  }
  cards.value = res.data
  addSenderName()
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
const postid = ref(null)

//显示卡片详情
const showMessage = async (id, left, top) => {
  window.history.pushState({}, "", `/user/found/explore/${id}`);
  overlayX.value = left;
  overlayY.value = top;
  postid.value = id;
  show.value = true;
  //改变详情页的显示状态
};

const close = () => {
  show.value = false;
  window.history.pushState({}, "", `/user/found`);
}

const needToLog = ref(false);

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
  background-color: rgba(0, 0, 0, 0.5); /* 灰色背景，透明度为0.5 */
  z-index: 100;
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