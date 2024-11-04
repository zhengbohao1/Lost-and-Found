<template>  
  <div>
    <LoadView v-if="loading"></LoadView>
    <div v-else>
      <div class="Empty" v-if="cards.length === 0">
        <el-empty :description=" route.query.input ? '没有找到相关帖子' : '没有帖子...'"></el-empty>
      </div>
      <div v-else>

        <!---试做-->
        <div class="push">
          <h1 style="  margin-bottom: 30px;">可能与你有关的帖子</h1>
          <n-carousel show-arrow autoplay style="width: 300px; height: 300px;">
            <n-image class="carousel-img" :src="'http://localhost:8090/common/download?name='+cards[0].imgUrl"
              @load="adjustImageSize($event)"
            ></n-image>
            <n-image class="carousel-img" :src="'http://localhost:8090/common/download?name='+cards[2].imgUrl"
              @load="adjustImageSize($event)"
            ></n-image>

            <template #arrow="{ prev, next }">
              <div class="custom-arrow">
                <button type="button" class="custom-arrow--left" @click="prev">
                  <n-icon><ArrowLeftBold /></n-icon>
                </button>
                <button type="button" class="custom-arrow--right" @click="next">
                  <n-icon><ArrowRightBold /></n-icon>
                </button>
              </div>
            </template>
            <template #dots="{ total, currentIndex, to }">
              <ul class="custom-dots">
                <li
                  v-for="index of total"
                  :key="index"
                  :class="{ ['is-active']: currentIndex === index - 1 }"
                  @click="to(index - 1)"
                />
              </ul>
            </template>
          </n-carousel>

          <el-scrollbar height="300">
            
          </el-scrollbar>
        </div>

        <div v-infinite-scroll="loadMore" :infinite-scroll-disabled="disabled" :infinite-scroll-distance="200">
          <ViewCard :card_columns="card_columns" @show-detail="showMessage" ></ViewCard>
        </div>
      </div>
      <div class="backover" v-if="show"></div>
      <transition name="fade">
          <div class="overlay" v-if="show">
            <el-button class="backPage" @click="close" :icon="Close"></el-button>
            <CardDetail :postid="postid" @afterDoComment="afterDoComment" ref="overlay"></CardDetail>
            <!--路由中携带了id，可以选择不用父子传递信息，而使用path传值或者pinia的方式-->
          </div>
        </transition>
    </div>
  </div>
</template>

<script setup>
import { onMounted, onUnmounted, ref, nextTick, watch } from 'vue';
import ViewCard from '@/components/user/Card.vue';
import CardDetail from '@/components/user/FoundDetail.vue';
import LoadView from '@/components/public/LoadView.vue';
import { queryPost, search } from '@/apis/found';
import { getUserName } from '@/apis/user';
import { useRoute } from 'vue-router';
import { waterFallInit, waterFallMore, resizeWaterFall } from '@/utils/waterFall';
import { Close, ArrowLeftBold, ArrowRightBold } from '@element-plus/icons-vue';

const cards = ref([]);  //包含了所有帖子的所有内容

// 主页卡片 //////////////////////////////////////////////////////////////////

const loading = ref(true);
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
  loading.value = false;
  disabled.value = false; // 启用滚动加载
};

const querySearchPosts = async () => {
  loading.value = true;
  await search(route.query.input).then(res => {
    if(res.code == 1){
      let data = res.data;
      data = data.filter(item => item.reviewProcess == 1);
      cards.value = data;
      addSenderName()
      nextTick(() => {
        waterFallInit(columns, card_columns, arrHeight, cards)
      })
  disabled.value = false; // 启用滚动加载
    }else{
      ElMessage.error('搜索失败')
    }
    loading.value = false;
  })
}

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

// 顶部推荐 //////////////////////////////////////////////////////////////////

//图片调整
const adjustImageSize = (event) => {
    const img = event.target;
    const naturalWidth = img.naturalWidth;
    const naturalHeight = img.naturalHeight;
    const imageRatio = naturalWidth / naturalHeight;
    if (imageRatio > 1) {
      img.style.width = '100%';
      img.style.height = 'auto';
    } else {
      img.style.height = '100%';
      img.style.width = 'auto';
    }
  }

// 卡片详情 //////////////////////////////////////////////////////////////////
const show = ref(false);
const overlayX = ref(0); // 覆盖层的水平位置
const overlayY = ref(0); // 覆盖层的垂直位置
const overlay = ref(null)
const postid = ref(null)

//显示卡片详情
const showMessage = async (id, left, top) => {
  window.history.pushState({}, "", `/user/found/${id}`);
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

onMounted(() => {
  if(route.query.input){
    querySearchPosts();
  }else{
    queryPosts(); 
  }
  // 清理函数
  onUnmounted(() => {
    window.removeEventListener('resize', () => {
      resizeWaterFall(columns.value, card_columns.value, arrHeight.value, cards.value);
    });
  });
});
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

.push {
  height: 350px;
  padding-left: 30px;
  margin-bottom: 45px;
}

.carousel-img {
    background-color: rgb(242, 242, 242);
    width: 300px;
    height: 300px;
    border-radius: 0.8rem;
    justify-content: center;
    align-items: center;
  }

  .custom-arrow {
  display: flex;
  position: absolute;
  bottom: 25px;
  right: 10px;
}

.custom-arrow button {
  display: inline-flex;
  align-items: center;
  justify-content: center;
  width: 28px;
  height: 28px;
  margin-right: 12px;
  color: #666;
  background-color: rgba(121, 121, 121, 0.5);
  border-width: 0;
  border-radius: 20px;
  transition: background-color 0.3s cubic-bezier(0.4, 0, 0.2, 1);
  cursor: pointer;
}

.custom-dots {
  display: flex;
  margin: 0;
  padding: 0;
  position: absolute;
  bottom: 20px;
  left: 20px;
}

.custom-dots li {
  display: inline-block;
  width: 12px;
  height: 4px;
  margin: 0 3px;
  border-radius: 4px;
  background-color: rgba(150, 150, 150, 0.4);
  transition:
    width 0.3s,
    background-color 0.3s cubic-bezier(0.4, 0, 0.2, 1);
  cursor: pointer;
}

.custom-dots li.is-active {
  width: 40px;
  background: #999;
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