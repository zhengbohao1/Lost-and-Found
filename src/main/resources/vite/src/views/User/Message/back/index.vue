<template>
  <div>
    <LoadView v-if="loading"></LoadView>
    <div v-else>
      <el-card class="message-card" v-for="item in detials" :key="item.id" @click="readMessage(item.id)">
        <el-badge :is-dot="!item.isRead" style="margin-left: 750px; margin-top: -20px;"></el-badge>
          <el-row :gutter="20">
            <el-col :span="50"><el-icon size="50" color="red"><CircleCloseFilled/></el-icon></el-col>
              <el-col :span="14">
                <el-row>
                  <span>{{ item.messageContent }}</span>
                </el-row>
                <el-row style="margin-top: 10px">
                    <span class="time">{{ item.createdAt }}</span>
                </el-row>
              </el-col>
            </el-row>
        </el-card>
        <div class="Empty" v-if="detials.length == 0">
          <el-empty description="还没有消息"></el-empty>
        </div>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { postBack } from '@/apis/user'
import { markCommentAsRead } from '@/apis/msg'
import { useUserStore } from '@/stores/user'
import LoadView from '@/components/public/LoadView.vue'
import { CircleCloseFilled } from '@element-plus/icons-vue';

const userStore = useUserStore();

const loading = ref(true);

const detials = ref([]);

//加载消息
const fetchData = async () => {
    await postBack(userStore.userInfo.userId).then(res => {
        detials.value = res.data;
        loading.value = false;
    });
}

//消息已读
const readMessage = async (messageId) => {
    await markCommentAsRead(messageId).then(res => {
        fetchData();
        userStore.coutMsg();
    });
}

onMounted(() => {
    fetchData();
})
</script>

<style scoped>
.message-card{
  margin-top: 15px;
  width: 600px;
  border-radius: 15px;
}

.time{
  margin-right: 20px;
  font-size: 14px;
  color: #999;
}

.text{
  font-size: 14px;
  color: #999;
  margin-left: 10px;
}

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
</style>