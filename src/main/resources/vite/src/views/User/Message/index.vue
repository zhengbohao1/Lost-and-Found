<template>
<div>
    <el-card class="header-card"></el-card>

    <div style="margin-top: 15px; gap: 20px; display: flex; justify-content: center">
        <el-button style="font-size:16px" v-for="item in buttonList" @click="toggleMessage(item)" 
        :type="activeIndex == item.isActive ? 'primary' : 'text'">{{ item.name }}</el-button>
    </div>

    <div class="body-card">
        <el-scrollbar style="height: 100%;">

            <transition-group name="fade">
            <el-card class="message-card" style="margin-bottom: 15px" v-if="activeIndex==1">
                <div v-for="reply in replyMeList" :key="reply.id">
                    <el-row :gutter="20">
                    <el-col :span="50">
                        <a :href="`/user/index/${reply.senderId}`"> 
                            <el-avatar :src="'http://localhost:8090/user/getAvatarById?userId='+reply.senderId" size="large"></el-avatar>
                        </a>
                    </el-col>
                    <el-col :span="10">
                        <el-row>
                            <span>{{ getSenderName(reply.senderId) }}<i class="text">回复了你的评论</i></span>
                        </el-row>
                        <el-row style="margin-top: 10px">
                            <span class="text">{{ reply.createdAt }}</span>
                        </el-row>
                    </el-col>
                </el-row>
                    <el-divider></el-divider>
                </div>
            </el-card>
            <div class="Empty" v-if="activeIndex == 1 && replyMeList.length == 0">
                <el-empty description="还没有消息"></el-empty>
            </div>

            <el-card class="message-card" v-if="activeIndex == 2 && claimFoundList.length > 0" v-for="claim in claimFoundList" :key="claim.id" @click="getClaimFound(claim)" title="点击查看详情">
                <el-row :gutter="20">
                    <el-col :span="50">
                        <a :href="`/user/index/${claim.userId}`"> 
                            <el-avatar :src="'http://localhost:8090/user/getAvatarById?userId='+claim.userId" size="large"></el-avatar>
                        </a>
                    </el-col>
                    <el-col :span="10">
                        <el-row>
                            <span>{{ claim.userName }}<i class="text">向您发送了寻物申请</i></span>
                        </el-row>
                        <el-row style="margin-top: 10px">
                            <span class="time">{{ claim.studentId }}</span>
                        </el-row>
                    </el-col>
                </el-row>
            </el-card>
            <div class="Empty" v-if="activeIndex == 2 && claimFoundList.length == 0">
                <el-empty description="还没有消息"></el-empty>
            </div>

            <el-card class="message-card" v-if="activeIndex == 3 && foundMineList.length > 0" v-for="item in foundMineList" :key="item.id" @click="getClaimFound(item)" title="点击查看详情">
                <el-row :gutter="20">
                    <el-col :span="50">
                        <a :href="`/user/index/${item.userId}`"> 
                            <el-avatar :src="'http://localhost:8090/user/getAvatarById?userId='+item.userId" size="large"></el-avatar>
                        </a>
                    </el-col>
                    <el-col :span="10">
                        <el-row>
                            <span>{{ item.userName }}<i class="text">向您发送了寻物申请</i></span>
                        </el-row>
                        <el-row style="margin-top: 10px">
                            <span class="time">{{ item.studentId }}</span>
                        </el-row>
                    </el-col>
                </el-row>
            </el-card>
            <div class="Empty" v-if="activeIndex == 3 && foundMineList.length == 0">
                <el-empty description="还没有消息"></el-empty>
            </div>

            <el-card class="message-card" v-if="activeIndex == 4 && passPostList.length > 0" v-for="item in passPostList" :key="item.id" @click="getClaimFound(claim)" title="点击查看详情">
                <el-row :gutter="20">
                    <el-col :span="50"><el-icon size="50" color="green"><SuccessFilled/></el-icon></el-col>
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
            <div class="Empty" v-if="activeIndex == 4 && passPostList.length == 0">
                <el-empty description="还没有消息"></el-empty>
            </div>
            
            <el-card class="message-card" v-if="activeIndex == 5 && backPostList.length > 0" v-for="item in backPostList" :key="item.id" @click="getClaimFound(claim)" title="点击查看详情">
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
            <div class="Empty" v-if="activeIndex == 5 && backPostList.length == 0">
                <el-empty description="还没有消息"></el-empty>
            </div>
        </transition-group>

        </el-scrollbar>
    </div>

    <div class="backover" v-if="showClaimFound"></div>
    <transition name="card">
        <div class="overlay" v-if="showClaimFound">
            <el-button class="backPage" @click="showClaimFound=false" :icon="Close"></el-button>
            <ClaimFound :claim-found="claimFound" ref="overlay"></ClaimFound>
        </div>
    </transition>
</div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { claimToMe, replyToMe, getUserName, postPass, foundMine, postBack } from '@/apis/user'
import { useUserStore } from '@/stores/user'
import { useRouter } from 'vue-router';
import ClaimFound from '@/components/user/ClaimFound.vue'
import { Close, SuccessFilled, CircleCloseFilled } from '@element-plus/icons-vue';

const buttonList = ref([
    { name: '回复我的', isActive: 1 },
    { name: '向我认领', isActive: 2 },
    { name: '失物找回', isActive: 3 },
    { name: '审核通过', isActive: 4 },
    { name: '退回帖子', isActive: 5 },
])

const userStore = useUserStore()
const router = useRouter()

const claimFoundList = ref([])
const foundMineList = ref([])
const replyMeList = ref([])
const passPostList = ref([])
const backPostList = ref([])

const activeIndex = ref(1);

const toggleMessage = (item) => {
    switch (item.isActive) {
        case 1:
            break;
        case 2:
            if(claimFoundList.value.length == 0) {
                fetchClaim();
            }
            break;
        case 3:
            if(foundMineList.value.length == 0)
            {
                fetchFoundMine();
            }
            break;
        case 4:
            if(passPostList.value.length == 0) {
                fetchPassPost();
            }
            break;
        case 5:
            if(backPostList.value.length == 0) {
                fetchBackPost();
            }
    }
  activeIndex.value = item.isActive;
};

const getSenderName = (id) => {
    return getUserName(id)
}

const showClaimFound = ref(false)

const claimFound = ref({})

const fetchClaim = async () => {
    await claimToMe().then(res => {
        claimFoundList.value = res.data;
    });
}

const getReplyMe = async () => {
    if(userStore.userInfo){
        await replyToMe(userStore.userInfo.userId).then(res => {
            replyMeList.value = res.data;
        });
    }
}

const fetchFoundMine = async () => {
    await foundMine(userStore.userInfo.userId).then(res => {
        foundMineList.value = res.data;
        console.log(foundMineList.value);
    })
}

const fetchPassPost = async () => {
    await postPass(userStore.userInfo.userId).then(res => {
        passPostList.value = res.data;
        console.log(passPostList.value);
    });
}

const fetchBackPost = async () => {
    await postBack(userStore.userInfo.userId).then(res => {
        backPostList.value = res.data;
    });
}

const getClaimFound = (claim) => {
    showClaimFound.value = true;
    claimFound.value = claim;
    console.log(claimFound.value);
}

onMounted(async () => {
    if(!userStore.userToken){
      ElMessage.warning('请先登录')
      router.push('/user')
    }
    if(userStore.userToken){
        getReplyMe();
    }
});
</script>

<style scoped>
.header-card{
    height: 100px;
}

.body-card{
    margin-top: 15px;
    height: 455px;
    background-color: azure;
}

.message-card{
    margin-top: 15px;
    width: 800px;
    margin-left: 13% ;
}

.time{
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

.fade-enter-active,
.fade-leave-active {
  transition: all 0.55s ease-out;
}


.fade-enter-to {
  position: absolute;
  right: 0;
}


.fade-enter-from {
  position: absolute;
  right: -100%;
}


.fade-leave-to {
  position: absolute;
  left: -100%;
}


.fade-leave-from {
  position: absolute;
  left: 0;
}

.card-enter-active {
  transition: all 0.3s ease;
}

.card-leave-active {
  transition: all 0.3s cubic-bezier(1.0, 0.5, 0.8, 1.0);
}

.card-enter-from,
.card-leave-to {
  transform: scale(0.5); /* 缩放比例，初始为缩小的一半 */
  opacity: 0; /* 不透明度为0，隐藏元素 */
}
</style>