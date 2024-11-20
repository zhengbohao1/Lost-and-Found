<template>
    <div class="dynamic-detail">
      <el-card shadow="hover">
        <div slot="header" class="clearfix">
          <el-row>
            <el-col :span="16">
              <h3>{{ dynamicTitle }}</h3>
            </el-col>
            <el-col :span="8">
              <el-button type="text">发布于 {{ publishedDate }}</el-button>
            </el-col>
          </el-row>
        </div>
        <p>{{ dynamicContent }}</p>
        <div slot="footer">
          <el-button type="text" @click="likePost">{{ likeCount }} 点赞</el-button>
          <el-button type="text" @click="dislikePost">{{ dislikeCount }} 踩</el-button>
        </div>
      </el-card>
      <el-card class="box-card" v-for="comment in comments" :key="comment.id">
        <div slot="header" class="clearfix">
          <el-avatar shape="square" :size="50" fit="cover" :src="comment.avatar"></el-avatar>
          <span style="margin-left: 10px;">{{ comment.name }}</span>
        </div>
        <p>{{ comment.content }}</p>
      </el-card>
    </div>
  </template>
  
  <script>
  export default {
    data() {
      return {
        dynamicTitle: '这是一个动态标题',
        publishedDate: '2024-09-20',
        dynamicContent: '这是一段动态内容...',
        likeCount: 0,
        dislikeCount: 0,
        comments: [
          {
            id: 1,
            name: '用户A',
            avatar: 'https://oss.webapp.com/avatar.jpg',
            content: '这是评论内容',
          },
          // 更多评论...
        ],
      };
    },
    methods: {
      likePost() {
        this.likeCount++;
      },
      dislikePost() {
        this.dislikeCount++;
      },
    },
  };
  </script>
  
  <style scoped>
  .dynamic-detail .el-card {
    margin-bottom: 20px;
  }
  </style>