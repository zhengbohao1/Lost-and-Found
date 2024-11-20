<template>
    <div>
      <div class="cards-container">
        <!-- 显示加载中的卡片 -->
        <el-card v-if="loading" class="custom-card" v-loading="loading">
          <el-descriptions title="未知" direction="vertical" border style="margin-top: 20px;">
            <el-descriptions-item :rowspan="2" :width="140" label="照片" align="center">
              <el-image style="width: 100px;" :src="'默认图片链接'" fit="cover"/>
            </el-descriptions-item>
            <el-descriptions-item label="发布人">未知</el-descriptions-item>
            <el-descriptions-item label="发布时间">未知</el-descriptions-item>
            <el-descriptions-item label="类型">
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
        <div v-for="col in card_columns" :key="col.id">
        <el-card
          v-for="item in col "
          :key="item.id"
          class="custom-card"
          @click="selectCard(item)">

          <div class="card">
          <a :href="'http://localhost:8090/common/download?name=' + item.imgUrl">
            <img
                :src="'http://localhost:8090/common/download?name=' + item.imgUrl"
                class="image"
                @load="handleLoad(card)"
                alt=""
            />
          </a>
          <div style="padding: 10px">
            <div style="margin-bottom: 10px; height: 20px; overflow: hidden; text-overflow: ellipsis; white-space: nowrap;">
              <span style="font-size: 1.0rem;" @click="details(item.id)">{{ item.itemName }}</span>
            </div>
            </div>
          </div>

          <el-descriptions
              :title="item.itemName"
              direction="vertical"
              border
              style="margin-top: 20px;">
              <el-descriptions-item
                :rowspan="2"
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
            <el-descriptions-item label="发布时间">
              {{ item.createdAt || '未知' }}
            </el-descriptions-item>
            <el-descriptions-item label="类型">
              <el-tag size="large">{{ item.status === 0 ? '待审核' : '已审核' }}</el-tag>
            </el-descriptions-item>
            <el-descriptions-item :span="3">
              <div class="description-actions">
                <el-button type="success" size="medium" :icon="Check" @click.stop="approveItem(item)">通过</el-button>
                <el-button type="danger" size="medium" :icon="Close" @click.stop="deleteItem(item)">退回</el-button>
              </div>
            </el-descriptions-item>
          </el-descriptions>
        </el-card>
        </div>
      </div>
      <el-pagination
        background
        layout="prev, pager, next"
        :total="totalCards"
        :page-size="pageSize"
        :current-page="currentPage"
        @current-change="handleCurrentChange">
      </el-pagination>
    </div>
  </template>
  
  <script setup>
  import { ref, computed, onMounted, toRef, watch } from 'vue';
  import { Check, Close } from '@element-plus/icons-vue';
  import { queryPost } from '@/apis/main';
  
  const currentPage = ref(1);
  const pageSize = 2;
  const totalCards = ref();
  const posts = ref([]);
  const loading = ref(true);
  
  const displayedCards = computed(() => {
    totalCards.value = posts.value.length;
    const start = (currentPage.value - 1) * pageSize;
    const end = start + pageSize;
    return posts.value.slice(start, end);
  });
  
  // 获取未审核的帖子
  const props = defineProps({
  card_columns: {
    type: Object,
    required: true
  }
});

const card_columns = toRef(props, 'card_columns');
  
  const handleCurrentChange = (val) => {
    currentPage.value = val;
  };

  watch(card_columns, () => {
  console.log('传递过来的数据', card_columns.value);
}, { deep: true, immediate: true });
  
  const selectCard = (card) => {
    console.log('选择了:', card);
    // 这里可以添加更多的逻辑，例如高亮选中的卡片
  };
  
  const approveItem = (item) => {
    console.log('通过:', item);
    // 实现通过逻辑
  };
  
  const deleteItem = (item) => {
    console.log('删除:', item);
    // 实现删除逻辑
  };
  
  onMounted(() => {
    //fetchData();
  });
  </script>
  
  <style scoped>
  .cards-container {
    display: flex;
    flex-direction: column; /* 确保卡片垂直堆叠 */
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

  .card {
  border-radius: 0.8rem;
  background-color: transparent;
}
  </style>