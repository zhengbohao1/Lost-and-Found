<template>
    <el-card class="manage-card" style="height: 610px;">
      <template #header>
        <div class="card-header">
          <span>兑奖记录管理</span>
        </div>
      </template>
  
      <!-- 添加 ElResult 组件 -->
      <el-result v-if="errorState.errorProblem.failJoin" icon="error" title="连接失败">
        <template #extra>
          <el-button @click="handleRetry">重试</el-button>
        </template>
      </el-result>
  
      <el-table 
        v-loading="loading"
        v-else
        ref="tableRef"
        :data="currentExchanges"
        style="width: 100%"
        border
      >
        <el-table-column prop="userId" label="用户编号"></el-table-column>
        <el-table-column prop="prizeName" label="奖品名称"></el-table-column>
        <el-table-column prop="exchangedTime" label="兑换时间">
          <template #default="scope">
            {{ formatDate(scope.row.exchangedTime) }}
          </template>
        </el-table-column>
        <el-table-column prop="nickName" label="用户名"></el-table-column>
      </el-table>
  
      <template #footer>
        <el-pagination
          style="justify-content: center;"
          @size-change="handleSizeChange"
          @current-change="handleCurrentChange"
          :current-page="currentPage"
          :page-sizes="[10, 20, 50]"
          :page-size="pageSize"
          layout="total, sizes, prev, pager, next, jumper"
          :total="total"
        >
        </el-pagination>
      </template>
    </el-card>
  </template>

<script setup>
import { ref, onMounted, computed } from "vue";
import { allExchange } from "@/apis/prize";
import { showErrorState } from '@/stores/showErrorState';
import { ElMessage, ElResult } from 'element-plus'; // 导入消息提示模块

const errorState = showErrorState();

//数据获得
const exchanges = ref([]);
const total = ref(0);
const loading = ref(true)

const fetchData = async () => {
    await allExchange().then((res) => {
        if(res.code === 1){
            exchanges.value = res.data;
            total.value = res.data.length;
            loading.value = false
        }else{
            ElMessage.error(res.msg);
        }
    })
}

//时间显示
const formatDate = (date) => {
  if (!date) return '';
  const options = { year: 'numeric', month: 'long', day: 'numeric', hour: 'numeric', minute: 'numeric' };
  return new Date(date).toLocaleDateString('zh-CN', options);
};

//分页
const currentPage = ref(1);
const pageSize = ref(10);
const tableRef = ref(null);

const currentExchanges = computed(() => {
  return exchanges.value.slice((currentPage.value - 1) * pageSize.value, currentPage.value * pageSize.value);
});

const handleSizeChange = (val) => {
  pageSize.value = val;
  fetchData();
};

const handleCurrentChange = (val) => {
  currentPage.value = val;
  fetchData();
};

onMounted(() => {
    fetchData();
})
</script>

<style scoped>
.card-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.el-card {
  margin-top: 20px;
}

.el-table {
  border-width: 2px; 
  border-style: solid; 
  border-color: #dcdfe6;
  border-radius: 8px; 
  overflow: hidden;
}

.el-table .cell:before {
  border-left-width: 2px; 
  border-bottom-width: 2px; 
}

.el-table th {
  border-width: 2px;
}
</style>