<template>
    <div>
      <div v-if="!loading">
        <el-card class="message-card" v-for="reply in details" :key="reply.id" @click="readMessage(reply.id)" style="margin-bottom: 15px" v-if="details.length!=0">
          <el-badge :is-dot="!reply.isRead" style="margin-left: 550px; margin-top: -20px;"></el-badge>
          <el-row :gutter="20">
              <el-col :span="50">
                <a :href="`/user/index/${reply.senderId}`"> 
                          <el-avatar :src="'http://localhost:8090/user/getAvatarById?userId='+reply.senderId" size="large"></el-avatar>
                </a>
              </el-col>
              <el-col :span="18">
                <el-row>
                  <span>{{ reply.senderId }}<i class="text">回复了你的评论</i></span>
                </el-row>
                <el-row style="margin-top: 10px">
                  <span class="text">{{ reply.createdAt }}</span>
                  <el-link @click="showDetail(reply)" style="margin-left: 20px;">点击查看详情</el-link>
                </el-row>
              </el-col>
            </el-row>
        </el-card>
      <div class="Empty" v-if="details.length == 0">
        <el-empty description="还没有回复你的评论哦~"></el-empty>
        </div>  
     </div>
     <LoadView v-else></LoadView>
    </div>
  </template>
  
  <script setup>
  import { ref, onMounted, defineEmits } from 'vue'
  import { replyToMe } from '@/apis/user'
  import { markCommentAsRead } from '@/apis/msg'
  import { useUserStore } from '@/stores/user'
  import LoadView from '@/components/public/LoadView.vue'
  
  const userStore = useUserStore();
  const loading = ref(true);
  
  const details = ref([])
  const emit = defineEmits(['message'])
  
  //加载消息
  const fetchData = async () => {
      await replyToMe(userStore.userInfo.userId).then(res => {
          details.value = res.data;
      });
      loading.value = false;
  }
  
  //消息已读
  const readMessage = async (messageId) => {
      await markCommentAsRead(messageId).then(res => {
          fetchData();
          userStore.coutMsg();
      });
  }
  
  const showDetail = (reply) => {
    emit('message', reply)
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