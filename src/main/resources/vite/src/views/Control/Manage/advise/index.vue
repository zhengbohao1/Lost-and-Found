<template>
  <el-card class="manage-card" style="height: calc(100vh - 20px); overflow-y: auto;">
    <template #header>
      <div class="card-header">
        <span>用户管理</span>
        <el-button type="danger" :disabled="selectedRows.length === 0" @click="handleDelete">删除</el-button>
      </div>
    </template>

    <!-- 添加 ElResult 组件 -->
    <el-result v-if="showError.showError.value" icon="error" title="连接失败">
      <template #extra>
        <el-button @click="handleRetry">重试</el-button>
      </template>
    </el-result>

    <el-table 
      v-loading="loading"
      v-else
      ref="tableRef"
      :data="currentUsers"
      style="width: 100%"
      border
      @selection-change="handleSelectionChange"
    >
      <el-table-column type="selection" width="55"></el-table-column>
      <el-table-column prop="userId" label="用户编号"></el-table-column>
      <el-table-column prop="category" label="类别"></el-table-column>
      <el-table-column prop="createdAt" label="创建时间"></el-table-column>
      <el-table-column label="操作">
        <template #default="scope">
          <el-button type="primary" size="medium" :icon="InfoFilled" @click="handleView(scope.row)">查看</el-button>
          <el-popconfirm
            confirm-button-text='确定'
            cancel-button-text='取消'
            icon="el-icon-info"
            title="你确定要删除这条记录吗？"
            @confirm="deleteUser(scope.row)">
            <template #reference>
              <el-button type="danger" size="medium" :icon="Delete" @click.stop>删除</el-button>
            </template>
          </el-popconfirm>
        </template>
      </el-table-column>
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

  <el-drawer
    v-model="drawerVisible"
    direction="rtl"
    title="建议详情"
    :with-header="true"
    :destroy-on-close="true"
  >
    <div class="drawer-content" v-if="editingUser">
      <p style="text-align: center;">用户编号: {{ editingUser.userId }}</p>
      <p style="text-align: center;">类别: {{ editingUser.category }}</p>
      <p style="text-align: center;">创建时间: {{ editingUser.createdAt }}</p>
      <p style="text-align: center;">内容: {{ editingUser.content }}</p>
      <el-popconfirm
        confirm-button-text='确定'
        cancel-button-text='取消'
        icon="el-icon-info"
        title="你确定要删除这条记录吗？"
        @confirm="deleteUser(editingUser)">
        <template #reference>
          <el-button type="danger" size="medium" :icon="Delete" style="margin-top: 10px;">删除</el-button>
        </template>
      </el-popconfirm>
    </div>
    <template #footer>
      <span class="dialog-footer">
        <el-button @click="drawerVisible = false">关闭</el-button>
      </span>
    </template>
  </el-drawer>
</template>

<script setup>
import { ref, computed, onMounted } from 'vue';
import { queryAdvises } from '@/apis/advise';
import { showErrorState } from '@/stores/showErrorState';
import { ElMessage } from 'element-plus'; // 导入消息提示模块
import { Delete, InfoFilled } from '@element-plus/icons-vue';

// 从 store 获取错误显示状态
const showError = showErrorState();

// 用户数据
const users = ref([]);
const total = 100;
const currentPage = ref(1);
const pageSize = ref(10);
const selectedRows = ref([]);
const tableRef = ref(null);
const loading = ref(true);
const drawerVisible = ref(false);
const editingUser = ref({});
const formLabelWidth = ref('120px');

const fetchData = async () => {
  try {
    const result = await queryAdvises();
    users.value = result.data;
    loading.value = false;
    if (!Array.isArray(users.value)) {
      console.error('获取的列表不是一个数组');
    }
  } catch (error) {
    console.error('获取列表失败', error);
    showError.showErrorMsg('获取列表失败，请重试');
  }
};

const currentUsers = computed(() => {
  return users.value.slice((currentPage.value - 1) * pageSize.value, currentPage.value * pageSize.value);
});

const handleSizeChange = (val) => {
  pageSize.value = val;
};

const handleCurrentChange = (val) => {
  currentPage.value = val;
};

const handleSelectionChange = (selection) => {
  selectedRows.value = selection;
};

const deleteUser = async (item) => {
  try {
    await updateUser(item.userId, { status: 0 });
    ElMessage.success('删除成功');
    fetchData(); // 刷新表格数据
    drawerVisible.value = false; // 关闭抽屉
  } catch (error) {
    ElMessage.error('删除失败，请稍后再试');
  }
};

const handleView = (row) => {
  drawerVisible.value = true;
  editingUser.value = { ...row };
};

const handleRetry = () => {
  showError.hideErrorMsg(); // 关闭错误提示
  fetchData(); // 重新尝试加载数据
};

onMounted(() => {
  fetchData();
});
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
  border-width: 2px; /* 加粗边框 */
  border-style: solid; /* 边框类型，默认就是solid */
  border-color: #dcdfe6; /* 边框颜色，可以根据需要调整 */
  border-radius: 8px; /* 如果需要圆角，也可以同时设置 */
  overflow: hidden; /* 隐藏超出部分，确保圆角效果 */
}

.drawer-content {
  text-align: center;
  padding: 20px;
}

/* 如果需要加粗单元格内部的边框 */
.el-table .cell:before {
  border-left-width: 2px; /* 左边框 */
  border-bottom-width: 2px; /* 底边框 */
}

/* 如果需要加粗表头的边框 */
.el-table th {
  border-width: 2px; /* 加粗表头边框 */
}
</style>