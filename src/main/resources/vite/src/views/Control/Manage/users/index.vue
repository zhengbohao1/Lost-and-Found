<template>
    <el-card>
      <template #header>
        <div class="card-header">
          <span>用户管理</span>
          <el-button type="danger" :disabled="selectedRows.length === 0" @click="handleDelete">删除</el-button>
        </div>
      </template>

      <el-table 
        ref="tableRef"
        :data="currentUsers"
        style="width: 100%"
        border
        @selection-change="handleSelectionChange"
      >
        <el-table-column type="selection" width="55"></el-table-column>
        <el-table-column prop="userId" label="用户编号"></el-table-column>
        <el-table-column prop="email" label="邮箱"></el-table-column>
        <el-table-column prop="nickName" label="用户名"></el-table-column>
        <el-table-column label="状态" >
          <template #default="scope">
            <el-tag :type="scope.row.status ? 'success' : 'warning'">{{ scope.row.status ? '启用' : '禁用' }}</el-tag>
          </template>
        </el-table-column>
        <el-table-column label="操作">
          <template #default="scope">
            <el-popover
              placement="top"
              :visible="popoverVisible[scope.$index]"
              title="确认删除"
              trigger="click"
            >
              <template #reference>
                <el-button type="danger" @click="() => togglePopover(scope.$index)">删除</el-button>
              </template>
              <el-button size="mini" type="text" @click="() => togglePopover(scope.$index)">取消</el-button>
              <el-button type="primary" size="mini" @click="() => handleDeleteConfirm(scope.$index)">确定</el-button>
            </el-popover>
            <el-button type="primary" @click="openDrawer(scope.$index)">编辑</el-button>
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

    <el-drawer v-model="drawerVisible" title="编辑用户信息" :with-header="true">
      <el-form :model="editingUser">
        <el-form-item label="用户编号" :label-width="formLabelWidth">
          <el-input v-model="editingUser.userId" autocomplete="off"></el-input>
        </el-form-item>
        <el-form-item label="邮箱" :label-width="formLabelWidth">
          <el-input v-model="editingUser.email" autocomplete="off"></el-input>
        </el-form-item>
        <el-form-item label="用户名" :label-width="formLabelWidth">
          <el-input v-model="editingUser.nickName" autocomplete="off"></el-input>
        </el-form-item>
        <el-form-item label="状态" :label-width="formLabelWidth">
          <el-switch v-model="editingUser.status" :active-value="1" :inactive-value="0"></el-switch>
        </el-form-item>
      </el-form>
      <template #footer>
        <span class="dialog-footer">
          <el-button @click="drawerVisible = false">取 消</el-button>
          <el-button type="primary" @click="saveEditing">确 定</el-button>
        </span>
      </template>
    </el-drawer>
</template>

<script setup>
import { ref, computed, onMounted } from 'vue';

// 模拟用户数据
const users = [
    { userId: 1, nickName: '张三', email: 'zhangsan@example.com', status: 1 },
    { userId: 2, nickName: '李四', email: 'lisi@example.com', status: 0 },
    // 添加更多用户...
    { userId: 100, nickName: '王五', email: 'wangwu@example.com', status: 1 },
];

const total = 100;

const currentPage = ref(1);
const pageSize = ref(10);
const selectedRows = ref([]);
const popoverVisible = ref([]);

const tableRef = ref(null);

const drawerVisible = ref(false);
const editingUser = ref({});
const formLabelWidth = ref('120px');

const currentUsers = computed(() => {
    return users.slice((currentPage.value - 1) * pageSize.value, currentPage.value * pageSize.value);
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

const handleDeleteConfirm = (index) => {
    console.log(`确认删除用户`, users[index]);
    popoverVisible.value[index] = false;
};

const togglePopover = (index) => {
    popoverVisible.value[index] = !popoverVisible.value[index];
};

const openDrawer = (index) => {
    drawerVisible.value = true;
    editingUser.value = { ...currentUsers.value[index] };
};

const saveEditing = () => {
    const index = users.findIndex(user => user.userId === editingUser.value.userId);
    if (index > -1) {
        users.splice(index, 1, editingUser.value);
    }
    drawerVisible.value = false;
};
</script>

<style>
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