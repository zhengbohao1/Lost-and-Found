<template>
    <div class="box">
      <div style="border-radius: 0.8rem; background-color: #fff;">
        <el-row :gutter="20">
          <!-- 图片区 -->
          <el-col  :span="50">
            <div class="banner">
              <el-carousel height="600px" v-load="loading">
                <el-carousel-item>
                  <n-image
                    class="carousel-item-center"
                    style="width: 100%; height: 100%"
                    fit="cover"
                    @load="adjustImageSize($event)"
                    ref="images"
                    :src="fileList.length>0 ?  fileList[0].url : 'http://localhost:8090/common/download?name='+post.imgUrl"
                  />
                </el-carousel-item>
              </el-carousel>
            </div>
          </el-col>
  
          <!-- 图片区结束 -->
          <el-col :span="50">
            <div v-if="!changePost" class="info" style="width: 300px; margin-top: 20px;">
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
                  <time class="timeF">丢失时间：{{ formatDate(post.foundDate) }}</time>
                  <time class="pos">{{ post.foundLocation }}</time>
                </el-row>
                <el-row>
                  <time class="time">{{ formatDate(post.createdAt) }}</time>
                </el-row>
                <!-- 卡片内容结束 -->
                <hr />
                <div class="comments" :infinite-scroll-disabled="disabled">
                  <el-empty description="现在还没有评论" />
                </div>
              </div>
              <el-divider />
            </div>
            <div v-else>
                <div class="info" style="width: 300px; margin-top: 20px;">
                    <el-row style="align-items: center; width: 500px;">
                        <a :href="`/user/index/`">
                        <el-avatar :src="'http://localhost:8090/user/getAvatarById?userId='+post.finderId" size="large" />
                        </a>
                        <div class="username">{{ senderName }}</div>
                    </el-row>
                    <div class="main-content" style="height:420px ">
                      <el-row style="margin-top: 50px;">
                        <el-row>
                         <span style="font-size: 16px; margin-bottom: 10px;">失物名</span>
                        </el-row>
                        <el-input v-model="itemName" :placeholder="post.itemName" maxlength="20"></el-input>
                      </el-row>
                      <el-row style="margin-top: 30px;">
                        <el-row>
                          <span style="font-size: 16px; margin-bottom: 10px;">描述</span>
                        </el-row>
                        <el-input v-model="description" :placeholder="post.description" type="textarea"></el-input>
                      </el-row>
                      <el-row style="margin-top: 30px;">
                        <el-row>
                          <span style="font-size: 16px; margin-bottom: 10px;">捡拾地点</span>
                        </el-row>
                        <el-input v-model="foundLocation" :placeholder="post.foundLocation" maxlength="100"></el-input>
                      </el-row>
                       <el-row style="margin-top: 30px;">
                          <span style="font-size: 16px; margin-bottom: 10px; margin-right: 15px;">捡拾时间</span>
                          <el-date-picker
                            v-model="foundDate"
                            type="datetime"
                            :placeholder="post.foundDate"
                            class="custom-width">
                          </el-date-picker>
                        </el-row>
                        <el-row style="margin-top: 30px; text-align: center">
                          <el-upload
                            class="avatar-uploader"
                            action="http://localhost:8090/common/upload"
                            list-type="picture-card"
                            ref="upload"
                            :on-preview="handlePictureCardPreview"
                            :on-remove="handleRemove"
                            :on-change="handleChange"
                            :pn-error="handleError"
                            v-model:file-list="fileList"
                            :auto-upload="false"
                            :on-exceed="handleExceed"
                            :limit="1"
                          >
                            <el-icon class="avatar-uploader-icon"><Plus /></el-icon>
                          </el-upload>
                        </el-row>
                      <el-row style="margin-top: 10px;">
                        <time class="time">{{ post.createdAt }}</time>
                      </el-row>
                    </div>
                  </div>
                <el-divider />
            </div>
  
            <div class="bottomArea">
                <div v-if="!changePost">
                    <div v-if="post.reviewProcess == '2'">
                        <el-row style="width: 280px;">
                        <el-col :span="4"></el-col>
                        <el-col :span="12">
                            <el-button type="text" class="button" @click="changePost=true" >修改</el-button>
                        </el-col>
                        <el-col :span="8">
                            <el-button type="text" class="button2" @click="" >删除</el-button>
                        </el-col>
                        </el-row>
                    </div>
                    <div v-if="post.reviewProcess == '0'">
                        <sapn>审核中</sapn>
                    </div>
                </div>
                <div v-else>
                    <el-row style="width: 280px;">
                        <el-col :span="4"></el-col>
                        <el-col :span="12">
                            <el-button type="text" class="button" @click="sendPost" >发布</el-button>
                        </el-col>
                        <el-col :span="8">
                          <el-button type="text" class="button2" @click="quitEdit" >取消</el-button>
                        </el-col>
                    </el-row>
                </div>
            </div>
          </el-col>
        </el-row>
      </div>
    </div>
  </template>
  
  <script setup>
  import { ref, defineEmits, watch, toRef, onMounted, reactive  } from 'vue';
  import { Edit, ChatRound, Promotion, Phone, Paperclip, EditPen, Plus } from "@element-plus/icons-vue";
  import { useUserStore } from '@/stores/user';
  import { getPostById, updatePost } from '@/apis/lost';
  import { getUserName } from '@/apis/user';
  import { useRouter } from "vue-router";
  import { ElMessage } from 'element-plus';
  import { isClient } from 'element-plus/es/utils/browser.mjs';
  
  const router = useRouter()
  const userStore = useUserStore();
  
  const changePost = ref(false);
  const post = ref({});
  const senderName = ref('');
  
  const loading = ref(true);
  
  const fileList = ref([]);
  const imgUrl = ref('');
  const itemName = ref('');
  const description = ref('');
  const foundDate = ref('');
  const foundLocation = ref('');
  const upload = ref(null)

  const dialogImageUrl = ref('');
const dialogVisible = ref(false);
const uploadSuccess = ref(true);
  
  const props = defineProps({
    postid: {
      type: String,
      required: true
    }
  });
  
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
    loading.value = false;
  }
  
  const fetchDetail = async () => {
    try {
      const response = await getPostById(postid.value);
      post.value = response.data;
    } catch (error) {
      console.error(error);
    }
  }
  
  const sendPost = async () => {
    if(itemName.value == ''&& description.value == ''&&foundDate.value == ''&&foundLocation.value == ''&& fileList.value.length == 0){
      ElMessage.warning('没有改变');
    }else{
      if(fileList.value.length > 0){
        upload.value.submit();
      }
      const data = ref({
        id: postid.value,
        itemName: itemName.value == '' ? post.value.itemName : itemName.value,
        description: description.value == '' ? post.value.description : description.value,
        foundDate: foundDate.value,
        foundLocation: foundLocation.value,
        finderId: userStore.userInfo.userId,
        imgUrl: imgUrl.value,
        review_process: 0,
      })
      await updatePost(data.value).then((res) => {
        if (res.code == 1) {
          ElMessage.success('修改成功');
        }else{
          ElMessage.error('修改失败');
        }
      })
    }
  }
  
  const quitEdit = () => {
    fileList.value.splice(0, fileList.value.length);
    imgUrl.value = '';
    changePost=false
  }
  
  function handlePictureCardPreview(file) {
    dialogImageUrl.value = file.url;
    dialogVisible.value = true;
  }
  
  function handleRemove(file, fileList) {
    ElMessage.warning('替换图片');
  }
  
  function handleChange(uploadFile, uploadFiles) {
    const allowedTypes = ['image/jpeg', 'image/png', 'image/gif'];
    if (!allowedTypes.includes(uploadFile.raw.type)) {
      ElMessage.error('只允许上传 JPG/PNG/GIF 类型的图片');
      return false;
    }
    imgUrl.value = uploadFile.name;
    fileList.value = uploadFiles;
    console.log('1213',fileList.value);
    return true;
  }
  
  const handleExceed = () => {
    ElMessage.warning(
        '最多添加1张图片!'
    )
  }
  
  function handleError(){
    uploadSuccess.value = false;
    ElMessage({
      message: '上传失败',
      type: 'error',
    })
  }

  //时间显示
const formatDate = (date) => {
  if (!date) return '';
  const options = { year: 'numeric', month: 'long', day: 'numeric', hour: 'numeric', minute: 'numeric' };
  return new Date(date).toLocaleDateString('zh-CN', options);
};
  
  onMounted(async () => {
    await fetchDetail();
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