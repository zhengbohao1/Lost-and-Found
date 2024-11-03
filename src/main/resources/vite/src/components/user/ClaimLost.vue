<template>
  <div class="box">
    <div style="border-radius: 0.8rem; background-color: #fff;">
      <el-row :gutter="20">
        <!-- 图片区 -->
        <el-col :span="50">
          <div class="banner">
            <el-carousel height="600px">
              <el-carousel-item>
                <el-image
                  class="carousel-item-center"
                  style="width: 100%; height: 100%"
                  fit="cover"
                  @load="adjustImageSize($event)"
                  ref="images"
                  :src="'http://localhost:8090/common/download?name='+post.imgUrl"
                  :preview-src-list="previewList"
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
              <a :href="`/user/index/`">
                <el-avatar :src="'http://localhost:8090/user/getAvatarById?userId='+post.wonerId" size="large" />
              </a>
              <div class="username">{{ senderName }}</div>
            </el-row>
            <!-- 卡片头部结束 -->
            <div class="main-content" :style="{ height: !willSendComment? '420px':'350px' }">
              <!-- 卡片内容 -->
              <el-row style="margin-top: 20px;">
                <h2>{{ post.itemName }}</h2>
              </el-row>
              <el-row>
                <div class="content">{{ post.description }}</div>
              </el-row>
              <el-row style="gap:15px">
                <time class="timeF">丢失时间：{{ post.lostDate }}</time>
                <time class="pos">{{ post.lostLocation }}</time>
              </el-row>
              <el-row>
                <time class="time">{{ post.createdAt }}</time>
              </el-row>
              <!-- 卡片内容结束 -->
              <hr />
              <div class="comments" v-if="comments" :infinite-scroll-disabled="disabled">
                <el-empty description="现在还没有评论" v-if="comments.length === 0"/>

                <!-- 评论列表 -->
                <div v-else class="commentBox">
                  <div v-if="showComment">
                    <div class="commentTitle" style="margin-bottom: 5px;">共{{ comments.length }}条评论</div>
                      <div  style="margin-top: 20px;" v-for="item in comments" :key="item.id">
                        <el-row :gutter="20">
                          <el-col :span="2.5">
                            <a :href="`/user/index/${item.userId}`"> 
                              <el-avatar :src="'http://localhost:8090/user/getAvatarById?userId='+item.userId" :size="30"></el-avatar>
                            </a>
                          </el-col>
                          <el-col :span="20" style="font-size: 14px">
                            <div style="color:#33333399; display: flex; gap: 30px;">
                              {{ item.userName }} 
                              <div v-if="post.finderId==item.userId">
                                <el-tag>作者</el-tag>
                              </div>
                            </div>
                            <div style="color:#333333;margin-top: 2px;margin-bottom: 5px;">{{ item.comment }}</div>
                            <time class="time">{{ item.createdAt }}</time>
                            <el-icon style="margin-left:60px; margin-top: 0px; font-size: small" @click="commentMain(item)">
                              <ChatRound/>
                            </el-icon>
                          </el-col>
                          <el-col style="margin-top: 5px;">
                            <div v-for="(reply, index) in shouldShowMore(item) ? item.replies : item.replies.slice(0, 3)" :key="reply.id" style="margin-left: 30px">
                              <el-row :gutter="20">
                                <el-col :span="2.5">
                                  <a :href="`/user/index/${reply.userId}`">
                                    <el-avatar :src="'http://localhost:8090/user/getAvatarById?userId='+reply.userId" :size="25"></el-avatar>
                                  </a>
                                </el-col>
                                <el-col :span="20" style="font-size: 12px">
                                  <div style="color:#33333399; display: flex; gap: 30px;">
                                    {{ reply.userName }} 
                                    <div v-if="post.finderId==reply.userId">
                                      <el-tag size="small">作者</el-tag>
                                    </div>
                                  </div>
                                  <div style="color:#333333;margin-top: 2px;margin-bottom: 10px;">{{ reply.comment }}</div>
                                  <time class="time">{{ reply.createAt }}</time>
                                </el-col>
                              </el-row>
                            </div>
                            <div class="more" @click="loadReply(item)" v-if="item.replies.length > 0">{{ !shouldShowMore(item)? '展开'+item.replies.length+'条回复': '收起'}}
                            </div>
                          </el-col>  
                        </el-row>
                        <el-divider/>
                      </div>
                    </div>
                    <div v-else>
                      <div class="claim-info">
                        <p>你好</p>
                        <p>我是 {{ claimLost.userName }}，我似乎找到了你掉的东西</p>
                        <p style="margin-top: 15px; margin-bottom: 15px;">{{ claimLost.content }}</p>
                        <el-image :src="'http://localhost:8090/common/download2?name='+claimLost.imgUrl"
                        :preview-src-list="previewListForSend"></el-image>
                        <p style="font-size: 15px; color: #777;">联系方式：</p>
                        <p style="font-size: 16px; color: #555; margin-left: 30px">{{ claimLost.contactWay }}：{{ claimLost.contactDetails }}</p>
                      </div>
                  </div>
                </div>
              </div>
            </div>
            <el-divider />
          </div>

            <div class="bottomArea">
              <div v-if="!showComment">
                <el-row style="width: 350px;">
                  <el-col :span="8">
                    <el-button class="button" @click="showComment=true" >显示评论</el-button>
                    </el-col>
                    <el-row :span="16">
                      <div v-if="!post.ownerId">
                        <el-button class="button-red" @click="">不是我的</el-button>
                        <el-button class="button-green" @click="verifyOwner">我的东西</el-button>
                      </div>
                      <el-button type="text" v-else class="button-green" @click="toThank">聊表谢意</el-button>
                    </el-row>
                  </el-row>
                </div>
              <div v-else>
                  <div v-if="!willSendComment">
                      <el-row>
                        <el-col :span="8">
                          <el-input
                            placeholder="评论"
                            :prefix-icon="Edit"
                            clearable
                            @click="willSendComment = true"
                          />
                        </el-col>
                        <el-col :span="2"> </el-col>
                        <el-col :span="14">
                          <el-button class="button" @click="showComment=false" 
                            v-if="'!userStore.userInfo.userId==post.finderId'"
                          >找回信息</el-button>
                          <el-button class="button-red">暂无</el-button>
                        </el-col>
                      </el-row>
                      </div>
                      <div v-else>
                        <el-row>
                        <el-col :span="22">
                          <el-input
                            v-model="content"
                            :placeholder=" !isreply? '说点什么…' : commentInput "
                            :prefix-icon="Edit"
                            @keyup.enter="sendComment()"
                            clearable
                            style="margin-top: 5px"
                          />
                        </el-col>
                        <el-col :span="8">
                          <el-button class="button" style="margin-top: 20px" @click="sendComment()" >发送</el-button>
                        </el-col>
                        <el-col :span="8"></el-col>
                        <el-col :span="8">
                          <el-button class="button-red" style="margin-top: 20px" @click="willSendComment = false;isreply = false" >
                                取消
                            </el-button>
                        </el-col>
                      </el-row>
                    </div>
                </div>
  
            </div>
          </el-col>
        </el-row>
      </div>
    </div>

    <div class="pay" :style=" {'height': (thankForSender ? 80 : 0) + 'px', 'padding': (thankForSender ? 10 : 0) + 'px', 'width': (thankForSender ? 330 : 0) + 'px'}">
      <v-row v-if="thankShow">
       <v-col cols="6">
          <v-number-input
          :max="20"
          :min="1"
          v-model="goldCoin"
          :reverse="false"
          controlVariant="default"
          :hideInput="false"
          :inset="false"
          variant="solo-filled"
          control-variant="split"
        ></v-number-input>
       </v-col>
        <v-col cols="6">
          <v-btn style="margin-left: 20px; margin-top: 5px;" icon="mdi-check" @click="thank"></v-btn>
          <v-btn style="margin-left: 20px; margin-top: 5px;" icon="mdi-close"></v-btn>
        </v-col>
      </v-row>
    </div>
  </template>
  
  <script setup>
  import { ref, watch, toRef, onMounted, reactive  } from 'vue';
  import { Edit, ChatRound, Promotion, Phone, Paperclip, EditPen, User } from "@element-plus/icons-vue";
  import { useUserStore } from '@/stores/user';
  import { getPostById, getComment, getChildComment, postComment, postChildComment, confirmOwner } from '@/apis/lost';
  import  { sendCoin } from '@/apis/prize';
  import { getUserName } from '@/apis/user';
  import { useRouter } from "vue-router";
  import { ElMessage } from 'element-plus';
  import { VNumberInput } from 'vuetify/labs/VNumberInput'
  
  const router = useRouter()
  
  const content = ref('');
  const post = ref({});
  const commentInput = ref(null); 
  const userStore = useUserStore();
  const senderName = ref('');
  
  const willSendComment = ref(false);
  const isreply = ref(false);
  const showComment = ref(false);
  
  const props = defineProps({
    claimLost: {
      type: Object,
      required: true
    }
  });
  
  const claimLost = toRef(props, 'claimLost');
  const postid = ref('')
  
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
  
  //表示感谢
  const thankForSender =ref(false)
  const thankShow = ref(false)
  const goldCoin = ref(1)

  const toThank = () => {
    thankForSender.value = !thankForSender.value
    if(!thankForSender.value){
      thankShow.value = !thankShow.value
    }else{
      setTimeout(() => {
        thankShow.value = !thankShow.value
      }, 400)
    }
  }

  const thank = async () => {
  const data = {
    goldCoin: goldCoin.value,
      targetUserId: claimLost.value.userId,
      postId: claimLost.value.postId,
      category: '1'
    }
    console.log(data)
    await sendCoin(data).then(res => {
      if(res.cod == 1){
        ElMessage.success('我们已传达你的谢意');
      }else{
        ElMessage.error(res.data);
      }
    })
  }

  //确认失主
  const verifyOwner = async () => {
    await confirmOwner(claimLost.value.userId,postid.value).then(res => {
      console.log(res)
      if(res.code == 1){
        ElMessage.success('确认成功');
      }else{
        ElMessage.error('好像有什么问题'); 
      }
    })
  }

  // 发送评论
  const replyId = ref('');
  
  const sendComment = async () => {
    try{
      if (content.value.trim().length > 0) {
        if(!isreply.value){
          const newComment = {
            is_parent: '0',
            parent_id: '0',
            comment: content.value,
            post_category: '',
            postId: postid.value,
            category: '0',
          }
          await postComment(newComment)
          willSendComment.value = false;
          content.value = '';
          updateComment();
          ElMessage.success('评论成功');  
        }else{
          const newComment = {
            isParent: '0',
            parentId: replyId.value,
            comment: content.value,
            postId: postid.value,
            category: '0',
          }
          await postChildComment(newComment);
          ElMessage.success('评论成功');
          willSendComment.value = false;
          content.value = '';
          isreply.value = false;
          fetchComments();
        }
      }
    }
    catch (error) {
      ElMessage.error('评论失败');
    }
  }
  
  const commentMain = async (item) => {
    if(!userStore.userToken){
      ElMessage.warning('请先登录');
    }else{
      content.value = ''
      willSendComment.value = true;
      isreply.value = true
      replyId.value = item.id
      commentInput.value = `回复${item.userName}: `
    }
  }
  
const previewList = ref([]);

const previewListForSend = ref([]);

const fetchDetail = async () => {
  try {
    const response = await getPostById(postid.value);
    post.value = response.data;
    console.log(response.data);
    previewList.value.push('http://localhost:8090/common/download?name='+post.value.imgUrl);
    previewListForSend.value.push('http://localhost:8090/common/download2?name='+claimLost.value.imgUrl);
  } catch (error) {
    console.error(error);
  }
}

  //评论部分
  const comments = ref([]);
  
  const fetchComment = async () => {
    try {
      const temp = await getComment(postid.value);
      comments.value = temp.data.map(comment => ({
        ...comment,
        replies: [] // 初始化每条评论的 replies 属性
      }));
    } catch (error) {
      ElMessage.error('获取评论失败');
      console.error(error);
    }
  };

  const updateComment = async() =>{
    const temp = await getComment(postid.value);  
      const originalLength = comments.value.length;
      const newComments = temp.data.map(comment => ({
        ...comment,
        replies: ref([]) // 初始化每条评论的 replies 属性
      }));
      const filteredNewComments = [];
      for(let i = originalLength; i < newComments.length; i++){
        filteredNewComments.push(newComments[i]);
      }
      comments.value.push(...filteredNewComments);
  }
  
  const fetchComments = async () => {
    try {
     for(const comment of comments.value) {
        const temp = await getChildComment(comment.id);
        if(temp.data.length == 0) {
          continue;
        };
        comment.replies = temp.data;
     }
    } catch (error) {
      ElMessage.error('获取评论失败');
    }
  };
  
  const loadReply = async (item) => {
    const offset = item.replies.length
    const id = item.id
    const res = await getChildComment(id)
    item.replies = res.data
    item.showMore = !item.showMore;
  }
  
  const shouldShowMore = (item) => {
    return item.showMore;
  };
  
  watch(postid, async (newPostId) => {
    await fetchDetail();
  });
  
  onMounted(async () => {
    postid.value = claimLost.value.postId;
    await fetchDetail();
    await fetchComment();
    await fetchComments();
    getUserName(post.value.ownerId).then(response => {
     senderName.value = response.data;
    });
  })
  </script>
  
  <style scoped>
  .full-width-input {
    width: 100%;
  }
  
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
    margin-top: 10px;
    font-size: 12px;
    color: #999;
  }
  
  .timeF {
    margin-top: 5px;
    font-size: 13px;
    color: #666666;
  }
  
  .pos {
    margin-top: 5px;
    margin-left: 10px;
    font-size: 13px;
    color: #666;
  }

  .pay{
    position: absolute;
    background-color: #fff;
    top:720px; 
    left: 800px;
    border-radius: 10px;
    -webkit-transition: height 0.4s ease-in-out, width 0.4s ease-in-out;
    transition:height 0.4s ease-in-out, width 0.4s ease-in-out;
  }
  
  .carousel-item-center {
    border-radius: 0.8rem 0 0 0.8rem;
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
  
  .bottomArea {
    position: absolute;
    z-index: 10;
    transition: max-height 0.3s ease-in-out; /* 添加过渡效果 */
    overflow: hidden; /* 防止内容溢出 */
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
      border-bottom: 3px solid #007ecc;
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
  
      .button-red {
        padding: 0.5em 1em;
        text-align: center;
        text-decoration: none;
        color: #ff3333;
        border: 2px solid #ff3333;
        font-size: 15px;
        display: inline-block;
        border-radius: 0.3em;
        transition: all 0.2s ease-in-out;
        position: relative;
        overflow: hidden;
      }
  
        .button-red:before {
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
  
        .button-red:hover {
          background-color: #ff3333;
          color: #fff;
          border-bottom: 3px solid #aa0000;
        }
  
          .button-red:hover:before {
            transform: skewX(-45deg) translateX(13.5em);
            transition: all 0.5s ease-in-out;
          }
  
          .button-red:active {
            background-color: #aa0000;
            border-color: #aa0000;
            border-bottom: 0px solid #aa0000;
            color: #fff;
            /* 可以添加额外的动画效果，例如缩放或阴影 */
            transform: scale(0.98);
            box-shadow: 0 2px 5px rgba(0, 0, 0, 0.6);
          }

          .button-green {
            padding: 0.5em 1em;
            text-align: center;
            text-decoration: none;
            color: #55ff55;
            border: 2px solid #55ff55;
            font-size: 15px;
            display: inline-block;
            border-radius: 0.3em;
            transition: all 0.2s ease-in-out;
            position: relative;
            overflow: hidden;
          }
      
            .button-green:before {
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
      
            .button-green:hover {
              background-color: #55ff55;
              color: #fff;
              border-bottom: 3px solid #2f902f;
            }
      
              .button-green:hover:before {
                transform: skewX(-45deg) translateX(13.5em);
                transition: all 0.5s ease-in-out;
              }
      
              .button-green:active {
                background-color: #2f902f;
                border-color: #2f902f;
                border-bottom: 0px solid #2f902f;
                color: #fff;
                /* 可以添加额外的动画效果，例如缩放或阴影 */
                transform: scale(0.98);
                box-shadow: 0 2px 5px rgba(0, 0, 0, 0.6);
              }

  .claim-info {
    font-family: Arial, sans-serif;
    font-size: 1rem;
    line-height: 1.6;
    padding: 20px;
    border: 1px solid #ccc;
    border-radius: 8px;
    background-color: #f9f9f9;
  }

.claim-info p {
  margin: 0 0 10px;
}

.claim-info p:first-child {
  font-weight: bold;
  font-size: 1.2rem;
  text-align: center;
}

.claim-info p:last-child {
  margin-bottom: 0;
}
  </style>