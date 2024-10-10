<template>
    <el-row>
      <el-col :span="3" >
        <el-button :icon="isCollapsed==true? DArrowRight:DArrowLeft"
          @click="toggleIcon" style="margin-top: 20px">
        </el-button>
      </el-col>
        <el-col :span="9" >
        </el-col>
        <el-col :span="9">
        </el-col>
        <el-col :span="3" >
            <div>
                <el-popover
                placement="bottom"
                trigger="click"
                v-model="visible"
                >
                    <div class="popover-content">
                        <el-button type="text" @click="handleInfoButtonClick">退出登录</el-button>
                    </div>
                    <template #reference>
                        <el-button size="large" type="primary" :icon="UserFilled" style="margin-top: 10px" placeholder></el-button>
                    </template>
                </el-popover>
            </div>
        </el-col>
    </el-row>
</template>

<script setup>
import { ref,defineEmits } from 'vue';
import { useRouter } from 'vue-router';
import {Search, UserFilled, DArrowRight, DArrowLeft} from '@element-plus/icons-vue';
import { useUserStore } from '@/stores/user';

const isCollapsed = ref(false);

const visible = ref(false);

const userStore = useUserStore();
const router = useRouter();

const emit = defineEmits(['update-collapse']);

const toggleIcon = () => {
  isCollapsed.value = !isCollapsed.value;
  emit('update-collapse', isCollapsed.value);
};

const handlePrimaryButtonClick = () => {
  visible.value = false; // 关闭弹出框
}

const handleInfoButtonClick = () => {
  userStore.userLogout();
  router.push('/login');
}
</script>

<style>
.vertical-center {
  display: grid;
  align-items: center; /* 竖直居中 */
}

.popover-content {  
  display: flex;  
  flex-direction: column; 
  align-items: center; 
  justify-content: center; 
  height: 100%; 
}  
</style>