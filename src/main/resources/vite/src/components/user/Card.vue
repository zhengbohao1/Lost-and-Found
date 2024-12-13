<template>
  <div class="col">
    <div v-for="column in card_columns" :key="column.id">
      <section v-for="card in column" :key="card.id">
        <div v-show="loading" class="card">
          <a :href="`/explore/${card.id}`" @click.prevent="details(card.id)">
            <img
                :src="'http://localhost:8090/common/download?name='+card.imgUrl"
                class="image"
                @load="handleLoad"
                alt=""
            />
          </a>
          <div style="padding: 10px">
            <div style="margin-bottom: 10px; height: 20px; overflow: hidden; text-overflow: ellipsis; white-space: nowrap;">
              <span style="font-size: 1.0rem;" @click="details(card.id)">{{ card.itemName }}</span>
            </div>
            <div class="bottom">
              <el-row style="display: flex; align-items: center;">
                <router-link :to="`/user/index/${card.finderId}`">
                  <el-avatar
                  :src="'http://localhost:8090/user/getAvatarById?userId='+(card.finderId? card.finderId : card.ownerId)" size="small"
                  />
                </router-link>
                <div class="username">{{ card.nickName }}</div>
              </el-row>
            </div>
          </div>
        </div>

        <div v-if="!loading">
          <div class="card loading">
            <div class="image" :style="{height: card.height / (card.width / 250) + 'px'}">
            </div>
            <div style="padding: 10px">
              <div
                  style="margin-bottom: 10px;height: 20px;overflow: hidden;text-overflow: ellipsis;white-space: nowrap;">
                <span style="font-size: 1.0rem;" @click="details(card.id)">{{ card.itemName }}</span>
              </div>
              <div class="bottom">
                <el-row style="align-items: center;">
                  <RouterLink :to="`/user/index/${card.userId}`">
                    <div class="avatar"></div>
                  </RouterLink>
                  <div class="username">{{ card.nickName }}</div>
                </el-row>
              </div>
            </div>
          </div>
        </div>

      </section>
    </div>
  </div>
</template>

<script setup>
import { ref, toRef, computed, onMounted } from 'vue';
import { ElRow, ElAvatar } from 'element-plus';
import { RouterLink } from 'vue-router';

const loading =ref(false);

const props = defineProps({
  card_columns: {
    type: Object,
    required: true
  }
});

const card_columns = toRef(props, 'card_columns');

const emit = defineEmits(['show-detail'])
const details = (id) => {
  const target = event.target;
  const left = target.x;
  const top = target.y;
  console.log(left, top)
  emit('show-detail', id, left, top)
    //left, top确定详情页的显示位置
}

const handleLoad = (event) => {
  loading.value = true;
};

onMounted(async () => {
  try {
    card_columns.value = props.card_columns;
  } catch (error) {
    console.error('Error loading data:', error);
    loading.value = false;
  }
});
</script>

<style scoped>
.col {
  display: flex;
  flex-direction: row;
  flex-wrap: wrap; /* 允许换行 */
  justify-content: center;
  gap: 20px; /* 给卡片之间增加间隔 */
}

/* 添加边框样式 */
.image {
  width: 240px;
  border-radius: 0.8rem;
  object-fit: cover;
  border: 1px solid #ccc; /* 边框颜色和宽度 */
}

.image:hover {
  opacity: 0.7;
}

.loading .image,
.loading .avatar {
  background: gainsboro linear-gradient(
      100deg,
      rgba(255, 255, 255, 0) 40%,
      rgba(255, 255, 255, .5) 50%,
      rgba(255, 255, 255, 0) 60%
  );
  background-size: 200% 100%;
  background-position-x: 180%;
  animation: 1s loading ease-in-out infinite;
}

/*css动画 */
@keyframes loading {
  to {
    background-position-x: -20%;
  }
}

.loading .avatar {
  border-radius: 50%;
  height: 24px;
  width: 24px;
}

section {
  width: 240px;
  break-inside: avoid; /* 防止卡片被分割在两列中 */
  margin: 5px 0; /* 调整外边距 */
}

.card {
  border-radius: 0.8rem;
  background-color: transparent;
}

.username {
  margin-left: 10px;
  font-weight: 200;
  font-size: 0.8rem;
}

.hide-scrollbar::-webkit-scrollbar {
  display: none; /* Hide scrollbar for Chrome, Safari and Opera */
}

.hide-scrollbar {
  -ms-overflow-style: none;  /* Hide scrollbar for IE and Edge */
  scrollbar-width: none;     /* Hide scrollbar for Firefox */
}
</style>