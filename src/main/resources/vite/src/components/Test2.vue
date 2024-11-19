<template>
  <div class="box">
    <div style="border-radius: 0.8rem; background-color: #fff;">
      <el-row :gutter="20">

        <el-col :span="50">
          <div class="banner">
            <el-carousel height="600px">
              <el-carousel-item>
                <n-image
                  class="carousel-item-center"
                  style="width: 100%; height: 100%"
                  fit="cover"
                  @load="adjustImageSize($event)"
                  ref="images"
                  :src="require('D:\picture\foot\FF33A69772CAD09EDDA1C0C6C282ED9C.jpg')"
                />
              </el-carousel-item>
            </el-carousel>
          </div>
        </el-col>
        <!-- 图片区结束 -->
        <el-col :span="50">
          <div class="info" style="width: 300px; margin-top: 20px;">
            <!-- 卡片头部 -->
            <el-row style="align-items: center; width: 500px;">
              <a>
                <el-avatar src="https://p1-arco.byteimg.com/tos-cn-i-uwbnlip3yd/3ee5f13fb09879ecb5185e440cef6eb9.png~tplv-uwbnlip3yd-webp.webp" size="large" />
              </a>
              <div class="username">我叫test</div>
            </el-row>
            <!-- 卡片头部结束 -->
            <div class="main-content" :style="{ height: !willSendComment? '420px':'350px' }">
              <!-- 卡片内容 -->
              <el-row class="title">
                <h2>标题test</h2>
              </el-row>
              <el-row>
                <div class="content">我写了test</div>
              </el-row>
              <el-row class="tagLine">
                <el-tag plain size="small">标签test</el-tag>
              </el-row>
              <el-row>
                <time class="time">时间test</time>
              </el-row>
              <!-- 卡片内容结束 -->
              <hr />
              <div class="comments" v-if="!comments" :infinite-scroll-disabled="disabled">
                <el-empty description="现在还没有评论" v-if="comments.length === 0"/>
              </div>
              <div v-else class="commentBox">
                <div class="commentTitle" style="margin-bottom: 5px;">共test条评论</div>
                <!--使用字节跳动的评论组件-->
                <div class="commentList">
                  <div v-for="comment in comments" :key="comment.id">
                    <a-comment
                      :author="comment.userName"
                      :content="comment.comment"
                      avatar="https://p1-arco.byteimg.com/tos-cn-i-uwbnlip3yd/3ee5f13fb09879ecb5185e440cef6eb9.png~tplv-uwbnlip3yd-webp.webp"
                      :datetime="formatDate(comment.createdAt)"
                    >
                      <template #actions>
                        <span class="action"> <IconMessage /> 回复 </span>
                      </template>
                      <div v-if="comment.replies" v-for="reply in comment.replies" :key="reply.id">
                        <a-comment 
                        :author="reply.userName"
                        :content="reply.comment"
                        avatar="https://p1-arco.byteimg.com/tos-cn-i-uwbnlip3yd/3ee5f13fb09879ecb5185e440cef6eb9.png~tplv-uwbnlip3yd-webp.webp"
                        :datetime="formatDate(reply.createdAt)"></a-comment>
                      </div>
                      <el-divider/> 
                    </a-comment>
                  </div>
                </div>
              </div>
            </div>
            <el-divider/>
          </div>
          <div class="bottomArea">
            <n-input round  placeholder="说点什么" :class="{'common-input': !toComment, 'commend-input': toComment}"
            @click="readyToCommend">
              <template #prefix>
                <n-avatar round size="small" src="https://p1-arco.byteimg.com/tos-cn-i-uwbnlip3yd/3ee5f13fb09879ecb5185e440cef6eb9.png~tplv-uwbnlip3yd-webp.webp"></n-avatar>
              </template>
            </n-input>
          </div>
        </el-col>
      </el-row>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue';


const comments =  [
        {
            "id": 1,
            "userId": "123",
            "userName": "123",
            "isParent": 1,
            "parentId": 0,
            "comment": "KKKLLLL",
            "createdAt": "2024-11-06T19:41:25",
            "postId": 3,
            "replies": [
              {
                "id": 1,
                "userId": "123",
                "userName": "123",
                "isParent": 1,
                "parentId": 0,
                "comment": "KKKLLLL",
                "createdAt": "2024-11-06T19:41:25",
                "postId": 3,
              }
            ]
        },
        {
            "id": 54,
            "userId": "88",
            "userName": "邓敏",
            "isParent": 1,
            "parentId": 0,
            "comment": "fadfdasfadfdf",
            "createdAt": "2024-11-06T19:42:34",
            "postId": 3
        },
        {
            "id": 54,
            "userId": "88",
            "userName": "邓敏",
            "isParent": 1,
            "parentId": 0,
            "comment": "fadfdasfadfdf",
            "createdAt": "2024-11-06T19:42:34",
            "postId": 3
        },
        {
            "id": 54,
            "userId": "88",
            "userName": "邓敏",
            "isParent": 1,
            "parentId": 0,
            "comment": "fadfdasfadfdf",
            "createdAt": "2024-11-06T19:42:34",
            "postId": 3
        },
        {
            "id": 54,
            "userId": "88",
            "userName": "邓敏",
            "isParent": 1,
            "parentId": 0,
            "comment": "fadfdasfadfdf",
            "createdAt": "2024-11-06T19:42:34",
            "postId": 3
        },
    ]
//其他工具类函数
//图片调整
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

//时间显示
const formatDate = (date) => {
  if (!date) return '';
  const options = { year: 'numeric', month: 'long', day: 'numeric', hour: 'numeric', minute: 'numeric' };
  return new Date(date).toLocaleDateString('zh-CN', options);
};

// 评论相关
const toComment = ref(false);

const readyToCommend = () => {
  if(!toComment.value){
    toComment.value = !toComment.value
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
  z-index: 999;
}

.banner {
  background-color: rgba(205, 235, 255, 0.6);
  width: 600px;
  border-radius: 0.8rem 0 0 0.8rem;
}

.carousel-item-center {
  border-radius: 0.8rem 0 0 0.8rem;
  display: flex;
  justify-content: center;
  align-items: center;  
}

.carousel-item-center img {
  max-width: 100%;
  max-height: 100%;
}

.username {
  margin-left: 20px;
  font-size: 20px;
}

.main-content{
  transition: height 0.4s ease-in-out;
}

.main-content::-webkit-scrollbar {
  width: 0.1em; /* 设置滚动条宽度为0.1em */
  background-color: transparent; /* 设置滚动条背景颜色为透明 */
}

.main-content {
  overflow-y: scroll;
}

.title{
  margin-top: 20px;
}

.content {
  margin-top:15px;
  font-weight: 400;
  font-size: 16px;
  line-height: 28px;
  color: rgba(205, 220, 220, 0.8);
  white-space: pre-wrap;
  overflow-wrap: break-word;
}

.tagLine{
  margin-top: 15px;
}   

.time {
  margin-top: 10px;
  margin-bottom: 10px;
  font-size: 12px;
  color: #c7d2d8;
}

.commentList{
  margin-top: 20px;
}

.commentTitle {
  font-size: 14px;
  line-height: 18px;
  color: #666;
}

.more {
  margin-left: 32px;
  margin-top: 16px;
  line-height: 18px;
  color: #13386c;
  cursor: pointer;
}

.bottomArea {
  position: absolute;
  z-index: 10;
  transition: max-height 0.3s ease-in-out; /* 添加过渡效果 */
  overflow: hidden; /* 防止内容溢出 */
}

.common-input{
  width: 120px;
  transition: 0.4s ease-in-out;
}

.comment-input{
  width: 300px;;
  transition: 0.4s ease-in-out;
}
</style>