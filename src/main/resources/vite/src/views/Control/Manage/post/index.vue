<template>
    <div>
      <div class="cards-container">
        <el-card
          v-for="item in displayedCards"
          :key="item.id"
          class="custom-card"
          @click="selectCard(item)"
        >
          <el-descriptions
                :title= item.title 
                direction="vertical"
                border
                style="margin-top: 20px"
            >
            <el-descriptions-item
                :rowspan="2"
                :width="140"
                label="Photo"
                align="center"
                >
                <el-image
                    style="width: 100px; height: 100px"
                />
            </el-descriptions-item>
            <el-descriptions-item label="发布人">
              {{ item.postTime }}
            </el-descriptions-item>
            <el-descriptions-item label="发布时间">
              {{ item.author }}
            </el-descriptions-item>
            <el-descriptions-item label="类型">
                <el-tag size="small">{{ item.type }}</el-tag>
            </el-descriptions-item>
            <el-descriptions-item :span="3">
              <div class="description-actions">
                <el-button type="success" size="medium" :icon="Check" @click.stop="approveItem(item)">通过</el-button>
                <el-button type="danger" size="medium" :icon="Close" @click.stop="deleteItem(item)">删除</el-button>
              </div>
            </el-descriptions-item>
          </el-descriptions>
        </el-card>
      </div>
      <el-pagination
        background
        layout="prev, pager, next"
        :total="totalCards"
        :page-size="pageSize"
        :current-page="currentPage"
        @current-change="handleCurrentChange"
      >
      </el-pagination>
    </div>
  </template>
  
  <script setup>
  import { ref, computed } from 'vue';
  import { Check, Close } from '@element-plus/icons-vue';
  
  const cards = [
    { id: 1, title: '标题一', postTime: '2024-09-20', author: '张三', image: 'https://via.placeholder.com/150', type: '拾取' },
    { id: 2, title: '标题二', postTime: '2024-09-19', author: '李四' },
    { id: 3, title: '标题三', postTime: '2024-09-18', author: '王五', image: 'https://via.placeholder.com/150', type: '寻物'},
    { id: 4, title: '标题四', postTime: '2024-09-17', author: '赵六' },
  ];
  
  const currentPage = ref(1);
  const pageSize = 2;
  const totalCards = cards.length;
  
  const displayedCards = computed(() => {
    const start = (currentPage.value - 1) * pageSize;
    const end = start + pageSize;
    return cards.slice(start, end);
  });
  
  const handleCurrentChange = (val) => {
    currentPage.value = val;
  };
  
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
  </style>