<template>
    <div>
        <LoadView v-if="loading"></LoadView>
        <div v-else>
            <el-card class="message-card" v-if="details.length > 0" v-for="claim in details" :key="claim.id" @click="readMessage(claim)">
                <el-badge :is-dot="!claim.readStatus" style="margin-left: 750px; margin-top: -20px;"></el-badge>
                    <el-row :gutter="20">
                        <el-col :span="50">
                            <a :href="`/user/index/${claim.userId}`"> 
                                <el-avatar :src="'http://localhost:8090/user/getAvatarById?userId='+claim.userId" size="large"></el-avatar>
                            </a>
                        </el-col>
                    <el-col :span="20">
                        <el-row>
                            <span>{{ claim.userName }}<i class="text">向您发送了寻物申请</i></span>
                        </el-row>
                        <el-row style="margin-top: 10px">
                            <span class="time">{{ claim.studentId }}</span>
                            <span class="time">{{ claim.createdAt }}</span>
                            <el-link @click="getDetails(claim)" style="margin-left: 30px;">点击查看详情</el-link>
                        </el-row>
                    </el-col>
                </el-row>
            </el-card>
            <div class="Empty" v-if="details.length == 0">
                <el-empty description="还没有消息"></el-empty>
            </div>
        </div>

        <transition name="fade">
            <div class="backover" v-if="showDetail"></div>
        </transition>
        <transition name="card">
            <div class="overlay" v-if="showDetail">
                <el-button class="backPage" @click="showDetail=false" :icon="Close"></el-button>
                <ClaimFound :claim-found="detail" ref="overlay"></ClaimFound>
            </div>
        </transition>
    </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { claimToMe } from '@/apis/user'
import { markClaimAsRead } from '@/apis/msg'
import { useUserStore } from '@/stores/user'
import ClaimFound from '@/components/user/ClaimFound.vue'
import LoadView from '@/components/public/LoadView.vue'
import { ElMessage } from 'element-plus'
import { Close } from '@element-plus/icons-vue';

const loading = ref(true)

const userStore = useUserStore()

const details = ref([]);
const showDetail = ref(false)
const detail = ref({})

const fetchData = async () => {
    await claimToMe().then(res => {
        details.value = res.data;
        loading.value = false
        console.log(details.value)
    });
}

const getDetails = (claim) => {
    detail.value = claim;
    showDetail.value = true;
}

const readMessage = async (claim) => {
    if(claim.readStatus == 0){
        await  markClaimAsRead(claim.id)
        await fetchData();
        userStore.coutMsg();
    }   
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