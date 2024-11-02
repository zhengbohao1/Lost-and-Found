<template>
     <div class="messagepage">
        <div class="selectpage">
            <SelectCard style="margin-top: 5px;" v-for="item in selectList" :details="item"></SelectCard>
        </div>
        <el-scrollbar class="mainpage">
            <router-view @message="handleMessage" v-slot="{ Component }">
                <transition name="fate" mode="out-in">
                    <keep-alive>
                        <component :is="Component" />
                    </keep-alive>
                </transition>
            </router-view>
        </el-scrollbar>
        <div class="otherpage">
            <SideCard :details="details">
                <div v-if="reply">
                    <el-row>
                        <el-avatar :src="'http://localhost:8090/user/getAvatarById?userId='+reply.senderId" :size="50"></el-avatar>
                        <span class="name">{{ senderName }}</span>
                    </el-row>
                    <el-row class="content">
                        <span>{{ reply.messageContent }}</span>
                    </el-row>
                    <el-row class="time">
                        <span>{{ reply.createdAt }}</span>
                    </el-row>
                    <el-row class="time"><el-link @click="goToPost()">前往帖子</el-link></el-row>
                </div>
            </SideCard>
        </div>
    </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { useUserStore } from '@/stores/user'
import { useRouter } from 'vue-router'
import { getUserName } from '@/apis/user'
import SelectCard from '@/components/public/SelectCard.vue'
import SideCard from '@/components/public/SideCard.vue'

const userStore = useUserStore()
const router = useRouter()

const selectList = ref([
    { content: '回复我的', width: 180, height: 60, fontsize: 18, basePath: '/user/message', path: '/user/message/reply' },
    { content: '向我认领', width: 180, height: 60, fontsize: 18, basePath: '/user/message', path: '/user/message/claim' },
    { content: '失物找回', width: 180, height: 60, fontsize: 18, basePath: '/user/message', path: '/user/message/find' },
    { content: '我是失主', width: 180, height: 60, fontsize: 18, basePath: '/user/message', path: '/user/message/owner' },
    { content: '审核通过', width: 180, height: 60, fontsize: 18, basePath: '/user/message', path: '/user/message/pass' },
    { content: '退回帖子', width: 180, height: 60, fontsize: 18, basePath: '/user/message', path: '/user/message/back' },
    { content: '获得打赏', width: 180, height: 60, fontsize: 18, basePath: '/user/message', path: '/user/message/pay' },
])

const details = ref({
    width: 250,
    height: 250,
    icon: 'mdi-arrow-right-circle'
})

const reply = ref(null)
const senderName = ref('')

const handleMessage = async (item) => {
    await getUserName(item.senderId).then(res => {
        senderName.value = res.data
    })
    reply.value = item
}

const goToPost = () => {
    if(reply.value.postCategory == 0){
        router.push(`/user/found/${reply.value.relatedPostId}`)
    }else{
        router.push(`/user/lost/${reply.value.relatedPostId}`)
    }
}

onMounted(async () => {
    if(!userStore.userToken){
      ElMessage.warning('请先登录')
      router.push('/user')
    }
    if(userStore.userToken){
    }
});
</script>

<style scoped>
.messagepage{
    display: flex;
    flex-direction: row;
    width: 1090px;
    height: 100%;
    gap: 10px
}

.selectpage{
    width: 200px;
}

.mainpage{
    width: 610px;
}

.otherpage{
    width: 260px;
}

.messgae{
    height: 600px;
    overflow: auto;
}

.name{
    margin-left: 15px;
    margin-top: 10px;
    font-size: 20px
}

.content{
    justify-content: center;
    margin-top: 10px;
}

.time{
    font-size: 14px;
    color: #aaa;
    justify-content: center;
    margin-top: 20px;
}

.fade-enter-active,
.fade-leave-active {
  transition: opacity 0.5s ease;
}

.fade-enter-from,
.fade-leave-to {
  opacity: 0;
}  
</style>