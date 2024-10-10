<template>  
  <div class="box">  
    <div style="border-radius: 0.8rem;background-color:#fff;">  
      <el-row :gutter="20">  
        <!-- 图片区 -->  
        <el-col :span="50">  
          <div class="banner">  
            <el-carousel height="600px">  
              <el-carousel-item class="carousel-item-center">  
                <img style="object-fit: cover;"  
                     @load="adjustImageSize($event)" ref="images"  
                     src="D:\picture\Full_of_youself-assets\Full_of_youself.png"  
                     alt=""/>  
              </el-carousel-item>  
            </el-carousel>  
          </div>  
        </el-col>  
        <!-- 图片区结束 -->  
        <el-col :span="50">
          <div class="info" style="width: 300px;margin-top: 20px;">
            <!-- 卡片头部 -->
            <el-row style="align-items: center;width: 500px;">
              <a :href="`/user/index/`">
                <el-avatar :src="1" size="large"/>
              </a>
              <div class="username">test</div>
            </el-row>
            <!-- 卡片头部结束 -->
            <div class="main-content">
              <!-- 卡片内容 -->
              <el-row style="margin-top: 20px;">
                <h2>test</h2>
              </el-row>
              <el-row>
                <div class="content">test</div>
              </el-row>
              <el-row>
                <time class="time">test</time>
              </el-row>
              <!-- 卡片内容结束 -->
              <hr/>
                <div class="comments" v-if="comments" v-infinite-scroll="load" :infinite-scroll-disabled="disabled">
              </div>
            </div>
          </div>
        </el-col>
      </el-row>  
    </div>  
  </div>  
</template>  
  
<script setup>  
import { ref } from 'vue'

const comments = ref([])

emit('afterDoComment'); //发送评论时通知父组件更新评论

const adjustImageSize = (event) => {
  const img = event.target;
  const naturalWidth = img.naturalWidth;
  const naturalHeight = img.naturalHeight;
  const imageRatio = naturalWidth / naturalHeight;
  if (imageRatio > 1) {
    img.style.width = '100%';
    img.style.height = 'auto';
  } else {
    img.style.height = '100%';
    img.style.width = 'auto';
  }
}
</script>  
  
<style scoped>  
.box {  
  position: absolute;  
  left: 200px;  
  top: 100px;  
  border-radius: 0.8rem;  
  width: 950px;  
  height: 600px;  
  margin-top: 5px;  
  box-shadow: -16px 28px 28px -3px rgba(0, 0, 0, 0.1), 0px 10px 61px -8px rgba(0, 0, 0, 0.1);  
}  
  
.banner {  
  background-color: rgb(242, 242, 242);
  width: 600px;  
  border-radius: 0.8rem 0 0 0.8rem;  
}  

.username {
  margin-left: 20px;
  font-size: 20px;
}

.main-content::-webkit-scrollbar {
  width: 0.1em; /* 设置滚动条宽度为0.1em */
  background-color: transparent; /* 设置滚动条背景颜色为透明 */
}

.main-content {
  height: 420px;
  overflow-y: scroll;
}

  
.content {
  margin: 0;
  font-weight: 400;
  font-size: 16px;
  line-height: 28px;
  color: #333;
  white-space: pre-wrap;
  overflow-wrap: break-word;
}

.time {
  font-size: 12px;
  color: #999;
}

.carousel-item-center {  
  display: flex;  
  justify-content: center; /* 水平居中（如果需要） */  
  align-items: center; /* 垂直居中 */  
  height: 100%; /* 确保容器高度被设置 */  
}  
  
/* 确保图片不会超出轮播图项容器的边界 */  
.carousel-item-center img {  
  max-width: 100%;  
  max-height: 100%; /* 可选，根据实际需要调整 */  
}  
</style>