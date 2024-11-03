<template>
    <el-container>
        <el-main>
            <el-scrollbar class="cards-container" :style="{'height': (showAddPrize ? 321 : 571) + 'px'}">
                <el-result v-if="errorState.errorProblem.failJoin" icon="error" title="连接失败">
                    <template #extra>
                        <el-button @click="handleRetry">重试</el-button>
                    </template>
                </el-result>

                <el-empty v-if="!loading && !errorState.errorProblem.failJoin && !prizeList.length" description="暂无数据"/>

                <el-card
                    v-for="item in prizeList"
                    :key="item.id"
                    class="custom-card"
                    @click="selectCard(item)">
                    <el-descriptions
                        :title="item.prizeName"
                        direction="vertical"
                        border
                        :column="4" 
                        style="margin-top: 20px;">
                        <el-descriptions-item
                        :rowspan="2"
                        :width="140"
                        label="奖品图片"
                        align="center">
                        <el-image
                            style="width: 100px;"
                            :src="'http://localhost:8090/prize/image?prizeImageUrl=' + item.prizeImageUrl || '默认图片链接'"
                            fit="cover"/>
                        </el-descriptions-item>
                        <el-descriptions-item label="奖品名称">
                        {{ item.prizeName || '未知' }}
                        </el-descriptions-item>
                        <el-descriptions-item label="库存">
                        {{ item.prizeQuantity || '未知' }}
                        </el-descriptions-item>
                        <el-descriptions-item label="兑换价格">
                        {{ item.prizePrice || '未知' }}
                        </el-descriptions-item>
                    </el-descriptions>
                </el-card>
            </el-scrollbar>
        </el-main>

        <el-footer>
            <el-row>
                <el-card v-show="showAddPrize" class="addprize" :style="{ 'height': (toAddPrize? 260 : 0) + 'px' }">
                  <v-form>
                    <el-container>
                      <el-aside style="width: 200px;">
                        <el-upload
                          list-type="picture-card"
                          :class="{ 'showBtnDealImg': canAdd, 'disUoloadSty': !canAdd}"
                          ref="upload"
                          :on-preview="handlePictureCardPreview"
                          :on-remove="handleRemove"
                          :on-change="handleChange"
                          :pn-error="handleError"
                          v-model:file-list="fileList"
                          :auto-upload="false"
                          :on-exceed="handleExceed"
                          :limit="1"
                          :rules="imgRule"
                        >
                          <v-icon icon="mdi-plus" size="50"></v-icon>
                        </el-upload>
                        <v-btn style="margin-left: 25px; margin-top: 20px;" @click="addNewPrize">确认添加</v-btn>
                      </el-aside>
                      <el-main style="width: 500px; padding: 5px">
                        <v-col>
                          <v-number-input v-model="prizePrice"  style="height: 65px; width: 300px;" label="价格" min="1"></v-number-input>
                          <v-number-input v-model="prizeQuantity" style="height: 65px; width: 300px;" label="库存" min="0"></v-number-input>
                          <v-text-field v-model="prizeName" style="height: 65px; width: 300px;" label="奖品名"></v-text-field>
                        </v-col>
                      </el-main>
                    </el-container> 
                  </v-form>
                </el-card>
            </el-row>
            <el-row>
                <el-pagination
                    background
                    layout="prev, pager, next"
                    :total="allPrizeLists"
                    :page-size="pageSize"
                    :current-page="currentPage"
                    @current-change="handleCurrentChange">
                </el-pagination>
                <v-btn prepend-icon="mdi-plus" style="margin-left: 700px" @click="openAddPrize">添加奖品</v-btn>
            </el-row>
        </el-footer>  
    </el-container>

    <el-drawer
      v-model="drawerVisible"
      title="详细信息"
      direction="rtl"
      size="45%"
      @close="clearSelectedCard">
      <template #default="{ close }">
        <div>
          <h1 style="text-align: center;">{{ selectedCard.itemName }}</h1>
          <!-- 使用 ElCarousel 组件来展示轮播图 -->
          <el-carousel height="400px">
            <el-carousel-item class="carousel">
              <el-image :src="'http://localhost:8090/prize/image?prizeImageUrl=' + selectedCard.prizeImageUrl"
                @load="adjustImageSize($event)" ref="images" />
            </el-carousel-item>
          </el-carousel>
        </div>
        <div style="width: 350px; display: flex; margin-top: 20px; margin-left: 130px">
          <v-number-input v-model="selectedCard.prizeQuantity" lable="库存"></v-number-input>
          <v-btn style="margin-left: 50px; margin-top: 10px" @click="addNewQuantity">添加库存</v-btn>
        </div>
        <div style="width: 350px; display: flex; margin-top: 20px; margin-left: 130px">
          <v-number-input v-model="selectedCard.prizePrice" lable="价格" disabled></v-number-input>
          <v-btn style="margin-left: 50px; margin-top: 10px" @click="addNewQuantity" disabled>改变价格</v-btn>
        </div>
        <div style="width: 250px; display: flex; margin-top: 20px; margin-left: 250px">
          <el-popconfirm
            confirm-button-text='确定'
            cancel-button-text='取消'
            :icon="InfoFilled"
            icon-color="red"
            title="你确定要删除这个奖品吗？"
            @confirm="() => deleteOldPrize()">
            <template #reference>
              <v-btn color="error" prepend-icon="mdi-delete" style="margin-top: 10px">删除奖品</v-btn>
            </template>
          </el-popconfirm>
        </div>
      </template>
    </el-drawer>
</template>

<script setup>
import { onMounted, ref, computed } from 'vue'
import { getPrizeList, addPrize, updatePrize, deletePrize } from '@/apis/prize'
import { showErrorState } from '@/stores/showErrorState';
import { VNumberInput } from 'vuetify/labs/VNumberInput';
import{ ElMessage } from 'element-plus'
import { InfoFilled } from '@element-plus/icons-vue'

const errorState = showErrorState()

const currentPage = ref(1);
const pageSize = 5;
const loading = ref(true)

const toAddPrize = ref(false)
const showAddPrize = ref(false)


const prizeLists = ref([])
const allPrizeLists = ref([])

const fetchData = async () => {
    await getPrizeList().then(res => {
        prizeLists.value = res.data
        loading.value = false
    })
}

//数据的分页处理
const prizeList = computed(() => {
    allPrizeLists.value = prizeLists.value.length;
  const start = (currentPage.value - 1) * pageSize;
  const end = start + pageSize;
  return prizeLists.value.slice(start, end);
});

const handleCurrentChange = (val) => {
  currentPage.value = val;
  fetchData();
};

//添加页的展开
const openAddPrize = () => {
   if(toAddPrize.value){
    toAddPrize.value = false
    setTimeout(() => {
      showAddPrize.value = false
    },400)
   }else{
    showAddPrize.value = true
    setTimeout(() => {
      toAddPrize.value = true
    },10)
   }
}

//详情显示
const drawerVisible = ref(false)
const selectedCard = ref(null)

const selectCard = (item) => {
  selectedCard.value = item;
  drawerVisible.value = true;
};

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
    img.style.height = '390px';
    img.style.width = 'auto';
  }
}

//图片的上传
const upload = ref(null)
const fileList = ref([]);
const imgUrl = ref('');
const prizeImg = ref([])
const dialogImageUrl = ref('');
const dialogVisible = ref(false);
const uploadSuccess = ref(true);
const canAdd = ref(true);

function handlePictureCardPreview(file) {
  dialogImageUrl.value = file.url;
  dialogVisible.value = true;
}

function handleRemove(file, fileList) {
  ElMessage.warning('替换图片');
  canAdd.value = true;
}

function handleChange(uploadFile, uploadFiles) {
  const allowedTypes = ['image/jpeg', 'image/png'];
  if (!allowedTypes.includes(uploadFile.raw.type)) {
    ElMessage.error('只允许上传 JPG/PNG 类型的图片');
    return false;
  }
  if (uploadFiles.length === 1) {
    canAdd.value = false;
    console.log(canAdd.value)
  }
  imgUrl.value = uploadFile.name;
  prizeImg.value = uploadFile.raw;

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

//添加奖品
const prizeName = ref('')
const prizePrice = ref(1)
const prizeQuantity = ref(1)

const imgRule = [value =>{if (value) return true
  return 'You must enter a first name.'},]

const addNewPrize = async () => {
  const data = {
    prizeName: prizeName.value,
    prizePrice: prizePrice.value,
    prizeQuantity: prizeQuantity.value,
    prizeImageUrl: imgUrl.value,
  }
  await addPrize(data).then(res => {
    if(res.code==1){
      ElMessage.success('添加成功')
      prizeName.value = ''
      prizePrice.value = 1
      prizeQuantity.value = 1
      imgUrl.value = ''
      fetchData()
      openAddPrize()
    } else{
      ElMessage.error(res.msg)
    }
  })
}

//修改库存
const addNewQuantity = async () => {
  console.log(selectedCard)
  await updatePrize(selectedCard.value.id, selectedCard.value.prizeQuantity).then(res => {
    if(res.code==1){
      ElMessage.success('修改成功')
      fetchData()
      drawerVisible.value = false
      selectedCard.value = ''
    }else{
      ElMessage.error(res.msg)
    }
  })
}

//删除奖品
const deleteOldPrize = async () => {
  await deletePrize(selectedCard.value.id).then(res => {
    if(res.code==1){
      ElMessage.success('删除成功')
      fetchData()
      drawerVisible.value = false
      selectedCard.value = ''
    }else{
      ElMessage.error(res.msg)
    }
  })
}

onMounted(() => {
    fetchData()
})
</script>

<style scoped>
.cards-container {
  overflow-y: auto; /* 溢出时启用垂直滚动 */
  transition: height 0.4s ease-in-out;
}

.custom-card {
  margin: 10px;
  cursor: pointer;
}

.addprize{
    margin-bottom: 10px;
    width: 1050px;
    border-radius: 15px;
    transition: height 0.4s ease-in-out;
    align-content: center;
    padding: 5px;

}

.carousel {
  overflow: hidden;
  display: grid;
  place-items: center;
}
</style>

<style>
  .showBtnDealImg .el-upload--picture-card{
  }

  .disUoloadSty .el-upload--picture-card{
    display:none;   /* 上传按钮隐藏 */
  }
</style>