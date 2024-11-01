<template>
    <div>
        <LoadView v-if="loading"></LoadView>
       <div v-else>
        <el-card class="message-card" v-if="details.length > 0" v-for="item in details" :key="item.id" @click="readMessage(item)" title="点击查看详情">
            <el-badge :is-dot="!item.isRead" style="margin-left: 750px; margin-top: -20px;"></el-badge>
                <el-row :gutter="20">
                    <el-col :span="50">
                        <a :href="`/user/index/${item.userId}`"> 
                            <el-avatar :src="'http://localhost:8090/user/getAvatarById?userId='+item.userId" size="large"></el-avatar>
                        </a>
                    </el-col>
                    <el-col :span="10">
                        <el-row>
                            <span>{{ item.userName }}<i class="text">似乎找到了你的东西</i></span>
                        </el-row>
                        <el-row style="margin-top: 10px">
                            <span class="time">{{ item.studentId }}</span>
                            <el-link @click="getDetails(item)" style="margin-left: 30px;">点击查看详情</el-link>
                        </el-row>
                    </el-col>
                </el-row>
            </el-card>
        <div class="Empty" v-if="details.length == 0">
            <el-empty description="还没有消息"></el-empty>
        </div>
       </div>

        <transition name="fade">
            <div class="backover" v-if="showDetails"></div>
        </transition>
        <transition name="card">
            <div class="overlay" v-if="showDetails">
                <el-button class="backPage" @click="showDetails=false" :icon="Close"></el-button>
                <ClaimLost :claim-lost="detail" ref="overlay"></ClaimLost>
            </div>
        </transition>
    </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { useUserStore } from '@/stores/user'
import { foundMine } from '@/apis/user'
import { markFindTipsAsRead } from '@/apis/msg'
import ClaimLost from '@/components/user/ClaimLost.vue'
import LoadView from '@/components/public/LoadView.vue'
import { Close } from '@element-plus/icons-vue';

const loading = ref(true)

const userStore = useUserStore()

const details = ref([])
const showDetails = ref(false)
const detail = ref({})

const fetchData = async () =>{
    await foundMine( userStore.userInfo.userId ).then(res => {
        if(res.code == 1){
            details.value = res.data
        }
        loading.value = false
    })
}

const readMessage = async (item) => {
    await markFindTipsAsRead(item.id).then(res => {
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