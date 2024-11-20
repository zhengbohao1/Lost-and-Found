<template>
  <el-container>
    <el-main>
      <el-scrollbar class="cards-container">
        <!-- 显示错误提示 -->
        <el-result v-if="errorState.errorProblem.failJoin" icon="error" title="连接失败">
          <template #extra>
            <el-button @click="handleRetry">重试</el-button>
          </template>
        </el-result>

        <!-- 显示暂无数据提示 -->
        <el-empty v-if="!loading && !errorState.errorProblem.failJoin && !displayedCards.length" description="暂无数据"/>

        <!-- 显示加载中的卡片 -->
        <el-card v-if="loading&&!errorState.errorProblem.failJoin" class="custom-card" v-loading="loading">
          <el-descriptions title="未知" direction="vertical" border style="margin-top: 20px;">
            <el-descriptions-item :rowspan="2" :width="140" label="照片" align="center">
              <el-image style="width: 100px;" :src="'默认图片链接'" fit="cover"/>
            </el-descriptions-item>
            <el-descriptions-item label="发布人">未知</el-descriptions-item>
            <el-descriptions-item label="发布时间">未知</el-descriptions-item>
            <el-descriptions-item label="状态">
              <el-tag size="large">已审核</el-tag>
            </el-descriptions-item>
            <el-descriptions-item :span="3">
              <div class="description-actions">
                <el-button type="danger" size="medium" :icon="Close" disabled>删除</el-button>
              </div>
            </el-descriptions-item>
          </el-descriptions>
        </el-card>

        <!-- 显示实际数据的卡片 -->
        <el-card
          v-for="item in displayedCards"
          :key="item.id"
          class="custom-card"
          @click="selectCard(item)">
          <el-descriptions
            :title="item.itemName"
            direction="vertical"
            border
            :column="4" 
            style="margin-top: 20px;">
            <el-descriptions-item
              :rowspan="2"
              :width="200"
              label="照片"
              align="center">
              <el-image
                style="width: 100px;"
                :src="'http://localhost:8090/common/download?name=' + item.imgUrl || '默认图片链接'"
                fit="cover"/>
            </el-descriptions-item>
            <el-descriptions-item label="发布人">
              {{ item.finderId || '未知' }}
            </el-descriptions-item>
            <el-descriptions-item label="物品">
              {{ item.itemName || '未知' }}
            </el-descriptions-item>
            <el-descriptions-item label="发布时间">
              {{ item.createdAt || '未知' }}
            </el-descriptions-item>
            <el-descriptions-item label="类型">
              <el-tag size="large">{{ '失物' }}</el-tag>
            </el-descriptions-item>
            <el-descriptions-item label="状态">
              <el-tag size="large">{{ '未审核' }}</el-tag>
            </el-descriptions-item>
            <el-descriptions-item :span="2">
              <div class="description-actions">
                <el-popconfirm
                  confirm-button-text='确定'
                  cancel-button-text='取消'
                  :icon="InfoFilled"
                  icon-color="orangered"
                  title="核验通过？"
                  @confirm="() => passItem(item)">
                  <template #reference>
                    <el-button type="success" size="medium" :icon="Check" @click.stop>通过</el-button>
                  </template>
                </el-popconfirm>
                <el-popconfirm
                  confirm-button-text='确定'
                  cancel-button-text='取消'
                  :icon="InfoFilled"
                  icon-color="red"
                  title="确认退回？"
                  @confirm="() => approveItem(item)">
                  <template #reference>
                    <el-button type="danger" size="medium" :icon="Close" @click.stop>退回</el-button>
                  </template>
                </el-popconfirm>
              </div>
            </el-descriptions-item>
          </el-descriptions>
        </el-card>
      </el-scrollbar>
    </el-main>

    <el-footer>
      <el-pagination
        background
        layout="prev, pager, next"
        :total="totalCards"
        :page-size="pageSize"
        :current-page="currentPage"
        @current-change="handleCurrentChange">
      </el-pagination>
    </el-footer>
  </el-container>

   <!-- el-drawer 用于显示详细信息 -->
   <el-drawer
      v-model="drawerVisible"
      title="详细信息"
      direction="rtl"
      size="45%"
      @close="clearSelectedCard">
      <template #default="{ close }">
        <div>
          <h1 style="text-align: center;">{{ selectedCard.itemName }}</h1>
          <!-- 使用 ElCarousel 组件来展示轮播图 -->
          <el-carousel height="400px">
            <el-carousel-item class="carousel">
              <img :src="'http://localhost:8090/common/download?name=' + selectedCard.imgUrl" alt="Example Image"
                @load="adjustImageSize($event)" ref="images" />
            </el-carousel-item>
          </el-carousel>
          <el-descriptions :column="2" border>
            <el-descriptions-item label="发布人">{{ selectedCard.finderId }}</el-descriptions-item>
            <el-descriptions-item label="发现地点">{{ selectedCard.foundLocation }}</el-descriptions-item>
            <el-descriptions-item span="2" label="描述">{{ selectedCard.description }}</el-descriptions-item>
            <el-descriptions-item label="找到时间"><div>{{ selectedCard.foundDate }}</div></el-descriptions-item>
            <el-descriptions-item label="状态">{{ selectedCard.status === 0 ? '待审核' : '已审核' }}</el-descriptions-item>
            <el-descriptions-item label="创建时间"><div>{{ selectedCard.createdAt }}</div></el-descriptions-item>
            <el-descriptions-item label="更新时间"><div>{{ selectedCard.updatedAt }}</div></el-descriptions-item>
          </el-descriptions>
          <!-- 在drawer中添加删除按钮 -->
          <div style="text-align: center; margin-top: 10px;">
            <el-popconfirm
              confirm-button-text='确定'
              cancel-button-text='取消'
              icon="el-icon-info"
              icon-color="red"
              title="确认退回？"
              @confirm="() => approveItem(selectedCard)">
              <template #reference>
                <el-button type="danger" size="medium" :icon="Close" @click.stop>退回</el-button>
              </template>
            </el-popconfirm>
            <el-popconfirm
              confirm-button-text='确定'
              cancel-button-text='取消'
              icon="el-icon-info"
              icon-color="green"
              title="核验通过？"
              @confirm="() => passItem(selectedCard)">
              <template #reference>
                <el-button type="success" size="medium" :icon="Check" @click.stop>通过</el-button>
              </template>
            </el-popconfirm>
          </div>
        </div>
      </template>
    </el-drawer>
</template>

<script setup>
import { ref, computed, onMounted } from 'vue';
import { Check, Close, InfoFilled } from '@element-plus/icons-vue';
import {showErrorState} from '@/stores/showErrorState';
import { passPost, backPost, queryAllPost } from '@/apis/found';
import { ElNotification, ElMessage } from 'element-plus';

// 定义错误状态管理器
const errorState = showErrorState();

const currentPage = ref(1);
const pageSize = 5;
const totalCards = ref();
const posts = ref([]);
const loading = ref(true);
const drawerVisible = ref(false); // 控制 el-drawer 的显示状态
const selectedCard = ref(null); // 存储当前选中的卡片信息

const displayedCards = computed(() => {
  totalCards.value = posts.value.length;
  const start = (currentPage.value - 1) * pageSize;
  const end = start + pageSize;
  return posts.value.slice(start, end);
});

const adjustImageSize = (event) => {
  const img = event.target;
  const naturalWidth = img.naturalWidth;
  const naturalHeight = img.naturalHeight;
  const imageRatio = naturalWidth / naturalHeight;
  if (imageRatio > 1) {
    img.style.width = '100%';
    img.style.height = 'auto';
  } else {
    img.style.height = '390px';
    img.style.width = 'auto';
  }
}

const fetchData = async () => {
  try {
    const response = await queryAllPost();
    if (response && response.data) {
      posts.value = response.data.filter(item => item.reviewProcess === 0);

      loading.value = false;
    } else {
      console.error('Response data is missing or incorrect.');
    }
  } catch (error) {
    console.error('Error fetching data:', error);
  }
};

const handleCurrentChange = (val) => {
  currentPage.value = val;
  fetchData();
};

const selectCard = (card) => {
  selectedCard.value = card;
  console.log('Selected card:', selectedCard.value);
  console.log('Drawer Visible before click:', drawerVisible.value);
  drawerVisible.value = true;
  console.log('Drawer Visible after click:', drawerVisible.value);
};

const clearSelectedCard = () => {
  drawerVisible.value = false;
  selectedCard.value = null;
};

const approveItem = async (item) => {
  await backPost(item.id);
  ElMessage({
    message: '退回完成',
    type: 'success',
  });
  fetchData();
  drawerVisible.value = false;
};

const passItem = async (item) => {
  await passPost(item.id);
  ElMessage({
    message: '核验通过',
    type: 'success',
  });
  fetchData();
  drawerVisible.value = false;
};

const handleRetry = () => {
  fetchData();
};

onMounted(() => {
  fetchData();
});
</script>

<style scoped>
.cards-container {
  height: 581px;
  overflow-y: auto; /* 溢出时启用垂直滚动 */
}

.custom-card {
  margin: 10px;
  cursor: pointer;
}

.description-actions {
  display: flex;
  justify-content: flex-end; /* 将按钮放在右侧 */
  padding-top: 10px; /* 增加与顶部的距离 */
}

.carousel {
  overflow: hidden;
  display: grid;
  place-items: center;
}
</style>