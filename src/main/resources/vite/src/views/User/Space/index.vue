<template>  
    <div class="user-space-container"> 
      <el-row :gutter="20" class="full-height-row">  
        <el-col :span="8" class="left-card-col">  
          <el-card class="full-height-card">  
            <div class="scrollable-content">  
                <el-row>
                    <el-col :span="4">
                        <el-avatar size="large" :src="'http://localhost:8090/user/getAvatarById?userId='+userStore.userInfo.userId"></el-avatar>
                    </el-col>
                    <el-col :span="3"></el-col>
                    <el-col :span="17">
                        <span class="username">{{ userStore.userInfo.nickName }}</span>
                        <br>
                        <span style="font-size: 12px; color: #989">用户编号：{{ userStore.userInfo.userId }}</span>
                    </el-col>
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

      <div style="margin-top: 15px;">
        <el-button>我的动态</el-button>
        <el-button>未知</el-button>
        <el-button>奖励详情</el-button>
      </div>

    <el-scrollbar class="main-card">
      <el-card class="affix-card">
        <p v-for="n in 100" :key="n">右侧内容 {{ n }}</p> 
      </el-card>
    </el-scrollbar>
    </div>  
  </template>  
    
  <script setup>  
  import { useUserStore } from '@/stores/user';  

  const userStore = useUserStore();
  </script>  
    
    <style scoped>
    .main-card {
        margin-top: 15px;
        height: 440px;
        overflow-y: auto; /* 添加滚动条 */
    }
    
    /* 如果需要，可以移除下面这行代码 */
    .user-space-container {  
      height: 170px; /* 这个高度设置取决于你的布局需求 */
    }
    
    .full-height-row {  
        height: 100%; /* 使行的高度继承自父容器 */
    }
    
    .full-height-card {  
        height: 100%; /* 使卡片高度为父容器高度 */
        overflow: hidden; /* 隐藏卡片内部的溢出内容 */
        padding: 16px; /* 为卡片内容添加内边距 */
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

.button {
  padding: 0.5em 1em;
  text-align: center;
  text-decoration: none;
  color: #01d5ff; 
  border: 2px solid #01d5ff;
  font-size: 15px;
  display: inline-block;
  border-radius: 0.3em;
  transition: all 0.2s ease-in-out;
  position: relative;
  overflow: hidden;
}

  .button:before {
    content: "";
    background-color: rgba(255, 255, 255, 0.5);
    height: 100%;
    width: 3em;
    display: block;
    position: absolute;
    top: 0;
    left: -4.5em;
    transform: skewX(-45deg) translateX(0);
    transition: none;
  }

  .button:hover {
    background-color: #01d5ff;
    color: #fff;
    border-bottom: 6px solid #007ecc;
  }

    .button:hover:before {
      transform: skewX(-45deg) translateX(13.5em);
      transition: all 0.5s ease-in-out;
    }

    .button:active {
      background-color: #007ecc;
      border-color: #007ecc;
      border-bottom: 0px solid #007ecc;
      color: #fff;
      /* 可以添加额外的动画效果，例如缩放或阴影 */
      transform: scale(0.98);
      box-shadow: 0 2px 5px rgba(0, 0, 0, 0.2);
    }
</style>