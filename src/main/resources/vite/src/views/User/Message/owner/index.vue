<template>
    <div>
        <el-card class="message-card" v-if="details.length > 0" v-for="item in details" :key="item.id" @click="readMessage(item)" title="点击查看详情">
            <el-badge :is-dot="!item.isRead" style="margin-left: 750px; margin-top: -20px;"></el-badge>
                <el-row :gutter="20">
                    <el-col :span="10">
                        <el-row>
                            <span>你的失物申请通过了</span>
                        </el-row>
                        <el-row style="margin-top: 10px">
                            <span class="time">{{ item.createdAt }}</span>
                            <el-link @click="getDetails(item)" style="margin-left: 30px;">点击查看详情</el-link>
                        </el-row>
                    </el-col>
                </el-row>
            </el-card>
        <div class="Empty" v-if="details.length == 0">
            <el-empty description="还没有消息"></el-empty>
        </div>

        <transition name="fade">
            <div class="backover" v-if="showDetails"></div>
        </transition>
        <transition name="card">
            <div class="overlay" v-if="showDetails">
                <el-button class="backPage" @click="showDetails=false" :icon="Close"></el-button>
                <ClaimFound :claim-found="detail" ref="overlay"></ClaimFound>
            </div>
        </transition>
    </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { useUserStore } from '@/stores/user'
import { iAmOwner } from '@/apis/found'
import ClaimFound from '@/components/user/ClaimFound.vue'
import { Close } from '@element-plus/icons-vue';

const userStore = useUserStore()

const details = ref([])
const showDetails = ref(false)
const detail = ref({})

const fetchData = async () =>{
    await iAmOwner( userStore.userInfo.userId ).then(res => {
        details.value = res.data
    })
}

const readMessage = async () => {
    await markCommentAsRead(messageId).then(res => {
        fetchData();
        userStore.coutMsg();
    });
}

const getDetails = (item) => {
    detail.value = item
    showDetails.value = true
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

/* 详情卡片过渡效果 */
.card-enter-active {
  transition: all 0.3s ease;
}

.card-leave-active {
  transition: all 0.3s cubic-bezier(1.0, 0.5, 0.8, 1.0);
}

.card-enter-from,
.card-leave-to {
  transform: scale(0.5);
  opacity: 0; 
}

.fade-in {
  opacity: 0;
  animation: fadeIn 2s ease-in-out forwards;
}

@keyframes fadeIn {
  from { opacity: 0; }
  to { opacity: 1; }
}
</style>