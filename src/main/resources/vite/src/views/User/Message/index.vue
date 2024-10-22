<template>
    <el-card class="header-card"></el-card>
    
    <div style="margin-top: 15px;">
        <el-button @click="toggleReplyMe" :type="replyMe ? 'primary' : ''">回复我的</el-button>
        <el-button  @click="toggleClaimFoundMe" :type="claimFoundMe ? 'primary' : ''">失物认领</el-button>
    </div>

    <div class="body-card">
        <el-scrollbar style="height: 100%;">
            <el-card class="message-card" v-if="claimFoundMe" v-for="claim in claimFoundList" :key="claim.id" @click="getClaimFound(claim)" title="点击查看详情">
                <el-row :gutter="20">
                    <el-col :span="50">
                        <a :href="`/user/index/${claim.userId}`"> 
                            <el-avatar :src="'http://localhost:8090/user/getAvatarById?userId='+claim.userId" size="large"></el-avatar>
                        </a>
                    </el-col>
                    <el-col :span="8">
                        <el-row>
                            <span>{{ claim.userName }}向您发送了寻物申请</span>
                        </el-row>
                        <el-row style="margin-top: 10px">
                            <span class="time">{{ claim.studentId }}</span>
                        </el-row>
                    </el-col>
                </el-row>
            </el-card>
            <div v-if="replyMe">

                <el-divider></el-divider>
            </div>
        </el-scrollbar>
    </div>

    <div class="overlay" v-if="showClaimFound">
        <el-button class="backPage" @click="showClaimFound=false" :icon="Close"></el-button>
        <ClaimFound :claimFound="claimFound" ref="overlay"></ClaimFound>
    </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { claimToMe } from '@/apis/user'
import ClaimFound from '@/components/user/ClaimFound.vue'
import { Close } from '@element-plus/icons-vue';

const claimFoundList = ref([])

const replyMe = ref(false);
const claimFoundMe = ref(false);

const toggleReplyMe = () => {
  replyMe.value = true;
  claimFoundMe.value = false;
};

const toggleClaimFoundMe = () => {
  claimFoundMe.value = true;
  replyMe.value = false;
}

const showClaimFound = ref(false)

const claimFound = ref({})

const fetchClaim = async () => {
    await claimToMe().then(res => {
            claimFoundList.value = res.data
            console.log(res.data)
        }
    )
}

const getClaimFound = (claim) => {
    showClaimFound.value = true
    claimFound.value = claim
    console.log(claimFound.value)
}

onMounted(async () => {
    fetchClaim();
})
</script>

<style scoped>
.header-card{
    height: 130px;
}

.body-card{
    margin-top: 15px;
    height: 465px;
    background-color: azure;
}

.message-card{
    margin-top: 15px;
    width: 1050px;
    margin-left: 20px ;
}

.time{
    font-size: 14px;
    color: #999;
}

.overlay {
  position: fixed;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  background-color: rgba(0, 0, 0, 0.5); /* 灰色背景，透明度为0.5 */
  z-index: 99;
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