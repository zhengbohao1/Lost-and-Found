<template>  
    <div> 
      <el-row :gutter="20" class="full-height-row">  
        <el-col :span="8" class="left-card-col">  
          <el-card class="full-height-card">  
            <div class="scrollable-content">  
                <el-row>
                    <el-col :span="4">
                        <el-avatar title="点击修改头像" size="large" :src="'http://localhost:8090/user/getAvatarById?userId='+userStore.userInfo.userId"></el-avatar>
                    </el-col>
                    <el-col :span="3"></el-col>
                    <el-col :span="17">
                        <span class="username">{{ userStore.userInfo.nickName }}</span>
                        <br>
                        <span style="font-size: 12px; color: #989">用户编号：{{ userStore.userInfo.userId }}</span>
                    </el-col>
                </el-row>
                <el-row style="margin-top: 15px;">
                  <el-button>编辑信息</el-button>
                </el-row>
            </div>  
          </el-card>  
        </el-col>  
        <el-col :span="16" class="right-card-col">  
          <el-card class="full-height-card">  
            <div class="scrollable-content">  
              <!-- 右侧内容 -->  
            </div>  
          </el-card>  
        </el-col>  
      </el-row>  

      <transition name="btnin">
        <div style="margin-top: 15px; gap: 20px; display: flex; justify-content: center">
          <el-button style="font-size:16px" v-for="item in buttonList" @click="toggleMessage(item)" 
          :type="activeIndex == item.isActive ? 'primary' : 'text'">{{ item.name }}</el-button>
        </div>
      </transition>

    <div style="margin-top: 15px; gap: 20px; display: flex; justify-content: center" v-if="activeIndex == 3">
      <el-button @click="toggleWaitFound"  :type="activeIndexWait == 1? 'primary' : 'text'">失物招领</el-button>
      <el-button @click="toggleWaitLost"  :type="activeIndexWait == 2 ? 'primary' : 'text'">寻物启事</el-button>
    </div>

    <div style="margin-top: 15px; gap: 20px; display: flex; justify-content: center" v-if="activeIndex == 4">
      <el-button @click="toggleBackFound"  :type="activeIndexBack == 1? 'primary' : 'text'">失物招领</el-button>
      <el-button @click="toggleBackLost"  :type="activeIndexBack == 2 ? 'primary' : 'text'">寻物启事</el-button>
    </div>


    <div class="main-card">
     
        <router-view v-slot="{ Component }">
          <transition name="fade" mode="out-in">
            <keep-alive>
              <component :is="Component" />
            </keep-alive>
          </transition>
        </router-view>
      </div>
    </div>  
  </template>  
    
  <script setup>  
  import { onMounted, ref } from 'vue';
  import { useUserStore } from '@/stores/user'; 
  import { useRouter } from 'vue-router';

  const userStore = useUserStore();
  const router = useRouter();

  const buttonList = ref([
    { name: '我捡到的', isActive: 1 },
    { name: '我丢失的', isActive: 2 },
    { name: '待审核的', isActive: 3 },
    { name: '未通过的', isActive: 4 },
    { name: '奖品兑换', isActive: 5 },
])

const activeIndex = ref(1);

const toggleMessage = (item) => {
    switch (item.isActive) {
        case 1:
          router.push('/user/space/found');
          break;
        case 2:
          router.push('/user/space/lost');
          break;
        case 3:
          break;
        case 4:
            break;
        case 5:
          router.push('/user/space/prize');
          break;
    }
  activeIndex.value = item.isActive;
};

const activeIndexWait = ref(0);

const toggleWaitFound = () => {
  router.push('/user/space/waitfound');
  activeIndexWait.value = 1;
}

const toggleWaitLost = () => {
  router.push('/user/space/waitlost');
  activeIndexWait.value = 2;
}

const activeIndexBack = ref(0);

const toggleBackFound = () => {
  router.push('/user/space/backfound');
  activeIndexBack.value = 1;
}

const toggleBackLost = () => {
  router.push('/user/space/backlost');
  activeIndexBack.value = 2;
}

  onMounted(() => {
    if(!userStore.userToken){
      ElMessage.warning('请先登录')
      router.push('/user')
    }
  })
  </script>  
    
    <style scoped>
    .main-card {
        margin-top: 15px;
        height: 590px;
        overflow-y: auto; /* 添加滚动条 */
    }
    
    .full-height-row {  
        height: 170px; /* 使行的高度继承自父容器 */
    }
    
    .full-height-card {  
        height: 100%; /* 使卡片高度为父容器高度 */
        overflow: hidden; /* 隐藏卡片内部的溢出内容 */
        padding: 10px; /* 为卡片内容添加内边距 */
        box-sizing: border-box; /* 确保内边距不会增加卡片的高度 */
    }
    
    .scrollable-content {  
        height: calc(100% - 32px); /* 减去卡片上下内边距的总和，确保内容区域高度正确 */
        overflow-y: auto; /* 启用垂直滚动条 */
    }
    
    .username {
        font-weight: bold;
        font-size: 24px;
    }

    @keyframes sheen {
  0% {
    transform: skewY(-45deg) translateX(0);
  }
  100% {
    transform: skewY(-45deg) translateX(12.5em);
  }
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