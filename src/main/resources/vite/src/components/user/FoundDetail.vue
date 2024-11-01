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
                <el-avatar :src="'http://localhost:8090/user/getAvatarById?userId='+post.finderId" size="large" />
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
                <time class="timeF">发现时间：{{ post.foundDate }}</time>
                <time class="pos">{{ post.foundLocation }}</time>
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
                  <div v-if="!toClaim">
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
                    <el-form :model="form" :rules="rules" ref="formRef">
                      <el-form-item prop="selectedType" style="margin-top: 10px; width: 100%;">
                        <el-select v-model="form.selectedType" placeholder="请选择类型">
                          <el-option label="QQ" value="qq"></el-option>
                          <el-option label="微信" value="wechat"></el-option>
                          <el-option label="电话" value="phone"></el-option>
                          <el-option label="邮件" value="email"></el-option>
                          <el-option label="X" value="x"></el-option>
                          <el-option label="LINE" value="line"></el-option>
                          <el-option label="TIM" value="tim"></el-option>
                        </el-select>
                      </el-form-item>

                      <!-- 联系方式 -->
                      <el-form-item prop="contact_details" >
                        <el-input
                          style="margin-top: 10px;"
                          v-model="form.contact_details"
                          maxlength="200"
                          :prefix-icon="Phone"
                          placeholder="输入联系方式"
                          :disabled="!form.selectedType"
                        ></el-input>
                      </el-form-item>

                      <el-form-item prop="evidence">
                        <el-input
                          style="margin-top: 10px;"
                          type="textarea"
                          v-model="form.evidence"
                          maxlength="200"
                          placeholder="认领证词"
                        ></el-input>
                      </el-form-item>

                      <!-- 备注 -->
                      <el-form-item prop="notes" >
                        <el-input
                          style="margin-top: 10px;"
                          v-model="form.notes"
                          maxlength="200"
                          :prefix-icon="Paperclip"
                          placeholder="备注"
                        ></el-input>
                      </el-form-item>

                      <!-- 学工号 -->
                      <el-form-item prop="student_id">
                        <el-input
                          style="margin-top: 10px;"
                          v-model="form.student_id"
                          maxlength="200"
                          :prefix-icon="EditPen"
                          placeholder="学工号"
                        ></el-input>
                      </el-form-item>

                      <!-- 真实姓名 -->
                      <el-form-item prop="user_name" >
                        <el-input
                          style="margin-top: 10px;"
                          v-model="form.user_name"
                          maxlength="200"
                          :prefix-icon="User"
                          placeholder="真实姓名"
                        ></el-input>
                      </el-form-item>

                      <!-- 提交按钮 -->
                        <div style="display: flex; margin-top: 30px; gap: 90px">
                          <el-button class="button" @click="sendClaim()" @keyup.enter="sendClaim()">确认认领</el-button>
                          <el-button class="button2" @click="toClaim=false">取消</el-button>
                        </div>
                    </el-form>
                  </div>
                </div>
              </div>
            </div>
            <el-divider />
          </div>

          <div class="bottomArea">
            <div v-if="!userStore.userToken">
              <el-link @click="router.push('/login');">请先登录</el-link>
            </div>
              <div v-else>
                <div v-if="!willSendComment">
                    <el-row>
                      <el-col :span="8">
                        <el-input
                          placeholder="评论"
                          :prefix-icon="Edit"
                          clearable
                          :disabled="toClaim"
                          @click="willSendComment = true"
                        />
                      </el-col>
                      <el-col :span="2"> </el-col>
                      <el-col :span="14">
                        <el-button class="button" @click="toClaim=true" 
                          v-if="userStore.userInfo.userId!=post.finderId&&!post.claimantId"
                          :disabled="toClaim"
                        >认领</el-button>
                        <el-button v-if="!post.claimantId" class="button2">暂无</el-button>
                        <span v-if="post.claimantId">此贴已完结</span>
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
                        <el-button class="button2" style="margin-top: 20px" @click="willSendComment = false;isreply = false" >
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
</template>

<script setup>
import { ref, defineEmits, watch, toRef, onMounted, reactive  } from 'vue';
import { Edit, ChatRound, Promotion, Phone, Paperclip, EditPen, User } from "@element-plus/icons-vue";
import { useUserStore } from '@/stores/user';
import { getPostById, getComment, getChildComment, postComment, postChildComment, claim } from '@/apis/found';
import { getUserName } from '@/apis/user';
import { useRouter, useRoute } from "vue-router";
import { ElMessage } from 'element-plus';
import { isClient } from 'element-plus/es/utils/browser.mjs';

const router = useRouter()

const content = ref('');
const post = ref({});
const commentInput = ref(null); 
const userStore = useUserStore();
const senderName = ref('');

const props = defineProps({
  postid: {
    type: String,
    required: true
  }
});

const route = useRoute();

const formRef = ref(null);
const form = reactive({
  contact_details: '',
  selectedType: '',
  evidence: '',
  notes: '',
  student_id: '',
  user_name: ''
})

const rules = reactive(
  {
    contact_details: [
      { required: true, message: '请输入联系方式', trigger: 'blur' }
    ],
    selectedType: [
      { required: true, message: '请选择联系方式类型', trigger: 'blur' }
    ],
    evidence: [
      { required: true, message: '请输入凭证', trigger: 'blur' }
    ],
    student_id: [
      { required: true, message: '请输入学工号', trigger: 'blur' }
    ],
    user_name: [
      { required: true, message: '请输入真实姓名', trigger: 'blur' }
    ]
  }
)

const willSendComment = ref(false);
const isreply = ref(false);
const toClaim = ref(false);

const emit = defineEmits(['afterDoComment']); // 发送评论时通知父组件更新评论

const postid = toRef(props, 'postid');

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

const sendClaim = async () => {
  await formRef.value.validate(async valid => {
    if(valid){
      const data = {
        contactDetails: form.contact_details,
        contactWay: form.selectedType,
        evidence: form.evidence,
        notes: form.notes,
        studentId: form.student_id,
        userName: form.user_name,
        postId: postid.value,
        finderId: post.value.finderId,
        readStatus: '0'
      }
      try {
        await claim(data).then(res => {
        const temp = res.code;
        if(temp!=1)
        {
          ElMessage.error('认领失败');
        }else{
          ElMessage.success(res.data);
        }
        })
        toClaim.value = false;
      }catch (error) {
        ElMessage.error('认领失败');
      }
    }
  })
}

const replyId = ref('');

// 发送评论的逻辑
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
        ElMessage.success('评论成功');
        willSendComment.value = false;
        content.value = '';
        updateComment();
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

const fetchDetail = async () => {
  try {
    const response = await getPostById(postid.value);
    post.value = response.data;
    previewList.value.push('http://localhost:8090/common/download?name='+post.value.imgUrl);
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
      }
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
  await fetchDetail();
  await fetchComment();
  await fetchComments();
  getUserName(post.value.finderId).then(response => {
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

    .button2 {
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

      .button2:before {
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

      .button2:hover {
        background-color: #ff3333;
        color: #fff;
        border-bottom: 3px solid #aa0000;
      }

        .button2:hover:before {
          transform: skewX(-45deg) translateX(13.5em);
          transition: all 0.5s ease-in-out;
        }

        .button2:active {
          background-color: #aa0000;
          border-color: #aa0000;
          border-bottom: 0px solid #aa0000;
          color: #fff;
          /* 可以添加额外的动画效果，例如缩放或阴影 */
          transform: scale(0.98);
          box-shadow: 0 2px 5px rgba(0, 0, 0, 0.6);
        }
</style>