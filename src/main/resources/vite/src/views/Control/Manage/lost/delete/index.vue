<template>
  <div>
    <el-card class="custom-card">
        <el-row>
          <el-col :span="2">
            <el-checkbox v-model="selectAll" @change="toggleAllSelections">全选</el-checkbox>
          </el-col>
          <el-col :span="18">
          </el-col>
          <el-col :span="4">
            <el-button type="danger" :icon="Delete" @click="batchDelete">批量删除</el-button>
          </el-col>
        </el-row>
      </el-card>
    <el-scrollbar class="cards-container">

      <el-result v-if="showError.showError.value" icon="error" title="连接失败">
        <template #extra>
          <el-button @click="handleRetry">重试</el-button>
        </template>
      </el-result>

      <el-card v-if="loading&&!showError.showError.value" class="custom-card" v-loading="loading">
        <el-descriptions title="未知" direction="vertical" border style="margin-top: 20px;">
          <el-checkbox v-model="selectAll" @change="toggleAllSelections"></el-checkbox>
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
              <el-button type="link" size="default" :icon="Close" disabled>删除</el-button>
            </div>
          </el-descriptions-item>
        </el-descriptions>
      </el-card>

      <el-empty v-if="!loading && !showError.showError.value && !displayedCards.length" description="暂无数据"/>

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
            style="margin-top: 20px;">
            <el-descriptions-item
              :rowspan="3"
              :width="140"
              label="照片"
              align="center">
              <el-image
                  style="width: 100px;"
                  :src="'http://localhost:8090/common/download?name='+item.imgUrl || '默认图片链接'"
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
            <el-tag size="large">{{ '已审核' }}</el-tag>
          </el-descriptions-item>
          <el-descriptions-item :span="2">
            <div class="description-actions">
              <el-row>
                <el-col :span="4">
                  <el-checkbox :label="item.id" v-model="selectedCards" ></el-checkbox>
                </el-col>
                <el-col :span="12"></el-col>
                <el-col :span="8">
                  <el-popconfirm
                    confirm-button-text='确定'
                    cancel-button-text='取消'
                    icon="InfoFilled"
                    icon-color="red"
                    title="你确定要删除这条记录吗？"
                    @confirm="() => deleteItem(item)">
                    <template #reference>
                      <el-button type="danger" :icon="Delete" @click.stop>删除</el-button>
                    </template>
                  </el-popconfirm>
                </el-col>
              </el-row>
            </div>
          </el-descriptions-item>
        </el-descriptions>
      </el-card>
      <el-pagination
        background
        layout="prev, pager, next"
        :total="totalCards"
        :page-size="pageSize"
        :current-page="currentPage"
        @current-change="handleCurrentChange">
      </el-pagination>
    </el-scrollbar>

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
          <el-descriptions :column="2" border >
            <el-descriptions-item label="发布人">{{ selectedCard.finderId }}</el-descriptions-item>
            <el-descriptions-item label="发现地点">{{ selectedCard.foundLocation }}</el-descriptions-item>
            <el-descriptions-item span="2" label="描述">{{ selectedCard.description }}</el-descriptions-item>
            <!-- 使用 span 属性控制同一行中的列数 -->
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
              :icon="InfoFilled"
              icon-color="red"
              title="你确定要删除这条记录吗？"
              @confirm="() => deleteItem(selectedCard)">
              <template #reference>
                <el-button type="danger" :icon="Delete" @click.stop>删除</el-button>
              </template>
            </el-popconfirm>
          </div>
        </div>
      </template>
    </el-drawer>
  </div>
</template>

<script setup>
import { ref, computed, onMounted, watch } from 'vue';
import { showErrorState } from '@/stores/showErrorState';
import { Check, Delete, InfoFilled } from '@element-plus/icons-vue';
import { deletePost, queryPost, deletePosts } from '@/apis/lost';
import { ElNotification } from 'element-plus';
import { ElCheckbox, ElCheckboxGroup } from 'element-plus'; // 引入必要的组件

const currentPage = ref(1);
const pageSize = 3;
const totalCards = ref();
const posts = ref([]);
const loading = ref(true);
const drawerVisible = ref(false); // 控制 el-drawer 的显示状态
const selectedCard = ref(null); // 存储当前选中的卡片信息
const selectedCards = ref([]); // 存储被选中的卡片ID
const showError = showErrorState();
const selectAll = ref(false);

const adjustImageSize = (event) => {
  const img = event.target;
  const naturalWidth = img.naturalWidth;
  const naturalHeight = img.naturalHeight;
  const imageRatio = naturalWidth / naturalHeight;

  if (imageRatio > 1) {
    // 如果图片比容器更宽，则按宽度缩放
    img.style.width = '100%';
    img.style.height = 'auto';
  } else {
    // 如果图片比容器更高，则按高度缩放
    img.style.width = 'auto';
    img.style.height = '390px';
  }
}

const displayedCards = computed(() => {
  totalCards.value = posts.value.length;
  const start = (currentPage.value - 1) * pageSize;
  const end = start + pageSize;
  return posts.value.slice(start, end);
});

const toggleAllSelections = (value) => {
  if (value) {
    // 选择所有
    displayedCards.value.forEach(card => selectedCards.value.push(card.id));
  } else {
    // 取消所有选择
    selectedCards.value.splice(0, selectedCards.value.length);
  }
};

// 获取帖子
const fetchData = async () => {
  try {
    const response = await queryPost();
    if (response && response.data) {
      posts.value = response.data;
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
  // 当改变页数时重新加载数据
  fetchData();
  // 页面改变时取消全选状态
  selectAll.value = false;
  selectedCards.value = [];
};

const handleRetry = () => {
  fetchData();
};

const selectCard = (card) => {
  selectedCard.value = card; // 保存当前选中的卡片信息
  drawerVisible.value = true;
};

const clearSelectedCard = () => {
  drawerVisible.value = false; // 关闭 el-drawer
  selectedCard.value = null; // 清空当前选中的卡片信息
};

const deleteItem = async (item) => {
  try {
    await deletePost(item.id);
    fetchData(); // 删除后重新获取数据以反映最新状态
    drawerVisible.value = false; // 关闭弹窗
    ElMessage({
      type: 'success',
      message: '删除成功'
    })
  } catch (error) {
    console.error('Error deleting item:', error);
    // 如果有错误发生，也可以显示错误通知
    ElMessage({
      type: 'error',
      message: '记录删除失败，请稍后再试。',
    });
  }
};

const batchDelete = async () => {
  try {
    const idsToDelete = selectedCards.value.join(',');
    await deletePosts(idsToDelete);
    fetchData(); // 删除后重新获取数据以反映最新状态
    drawerVisible.value = false; // 关闭弹窗
    ElMessage({
      type: 'success',
      message: '删除成功'
    })
  } catch (error) {
    console.error('Error deleting items:', error);
  }
}

onMounted(() => {
  fetchData();
});
</script>

<style scoped>
.cards-container {
  height: 530px; /* 设置最大高度，减去其他元素的高度 */
}

.custom-card {
  margin: 10px;
  cursor: pointer;
}

.description-actions {
  padding-top: 10px; /* 增加与顶部的距离 */
}

.carousel {
  overflow: hidden;
  display: grid;
  place-items: center;
}
</style>