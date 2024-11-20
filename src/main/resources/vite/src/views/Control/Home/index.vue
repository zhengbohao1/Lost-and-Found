<template>
  <el-scrollbar class="container">
    <el-row class="headcard">
      <el-avatar :size="60" src="https://cube.elemecdn.com/0/88/03b0d39583f48206768a7534e55bcpng.png"></el-avatar>
      <span style="margin-left: 20px;">今天，也是充满希望的一天</span>
    </el-row>
    <el-row class="datacard">
      <el-row>
        <div class="indatacard" style="background: linear-gradient(45deg, #ff7e5f, #feb47b);">
          <span style="font-size: 18px;">用户数</span>
          <el-row style="margin-top: 10px;">
            <el-icon class="number" :size="30"><User/></el-icon>
            <n-number-animation ref="numberAnimationInstRef" show-separator :from="0" :to="userNum" />
          </el-row>
        </div>
        <div class="indatacard" style="margin-left: 50px; background: linear-gradient(135deg, #4a148c, #ab47bc);">
          <span style="font-size: 18px;">今日新增</span>
          <el-row style="margin-top: 10px">
            <el-icon style="margin-right: 135px;" :size="30"><Plus/></el-icon>
            失物招领
            <n-number-animation ref="numberAnimationInstRef" show-separator :from="0" :to="passFound" />
            <span style="margin: 15px;"></span>
            寻物启事
            <n-number-animation ref="numberAnimationInstRef" show-separator :from="0" :to="passLost" />
          </el-row>
        </div>
      </el-row>
      <el-row>
        <div class="indatacard" style=" background: linear-gradient(135deg, #0d47a1, #64b5f6);">
          <span style="font-size: 18px;">失物招领</span>
          <el-row style="margin-top: 10px;">
            <el-icon class="number" :size="30"><ChatLineRound/></el-icon>
            <n-number-animation ref="numberAnimationInstRef" show-separator :from="0" :to="foundPostNum" />
          </el-row>
        </div>
        <div class="indatacard" style="margin-left: 50px; background: linear-gradient(135deg, #b71c1c, #ef5350);">
          <span style="font-size: 18px;">寻物启事</span>
            <el-row style="margin-top: 10px;">
              <el-icon class="number" :size="30"><ChatLineSquare/></el-icon>
              <n-number-animation ref="numberAnimationInstRef" show-separator :from="0" :to="lostPostNum" />
            </el-row>
        </div>
      </el-row>
    </el-row>
    <!--图表-->
    <el-row style="margin-top: 20px; gap:30px">
     <div class="charts">
      <div ref="chartDomPostDay" style="width: 100%; height: 400px;" class="chart"></div>
     </div>
     <div class="charts">
      <div ref="chartDomPost" style="width: 100%; height: 400px;" class="chart"></div>
     </div>
    </el-row>
    <el-row style="height: 20px;"></el-row>
  </el-scrollbar>
</template>

<script setup>
import { ref, onMounted, onUnmounted, nextTick } from 'vue';
import { userCount, foundAllPost, lostAllPost, foundAll, lostAll, foundCompetedByDay, lostCompetedByDay } from '@/apis/data';
import { User, ChatLineRound, ChatLineSquare, Plus } from "@element-plus/icons-vue";
import * as echarts from 'echarts';

const userNum = ref(0);
const foundPostNum = ref(0);
const lostPostNum = ref(0);
const passFound = ref(0);
const passLost = ref(0);

const foundDaily = ref([]);
const foundDate = ref([]);
const foundNum = ref([]);
const lostDaily = ref([]);
const lostNum = ref([]);

const fetchData = async () => {
  await userCount().then((res) => {
    userNum.value = res.data;
  });
  await foundAllPost().then((res) => {
    foundPostNum.value = res.data;
  });
  await lostAllPost().then((res) => {
    lostPostNum.value = res.data;
  });
  await foundAll().then((res) => {
    passFound.value = res.data;
  })
  await lostAll().then((res) => {
    passLost.value = res.data;
  })
  await foundCompetedByDay().then((res) => {
    foundDaily.value = res.data;
// 假设 foundDaily.value 是一个包含日期的对象数组
for (let i = 0; i < foundDaily.value.length; i++) {
  const dateStr = foundDaily.value[i].date;
  foundNum.value[i] = foundDaily.value[i].num;
  const dateObj = new Date(dateStr);
  if (!isNaN(dateObj.getTime())) {
    const year = dateObj.getFullYear();
    const month = String(dateObj.getMonth() + 1).padStart(2, '0');
    const day = String(dateObj.getDate()).padStart(2, '0');
    const formattedDate = `${year}-${month}-${day}`;
    foundDate.value.push(formattedDate);
  } else {
    console.error(`Invalid date: ${dateStr}`);
  }
}
  })
  await lostCompetedByDay().then((res) => {
    lostDaily.value = res.data;
    console.log(lostDaily.value);
    for (let i = 0; i < lostDaily.value.length; i++) {
      lostNum.value[i] = lostDaily.value[i].num;
    }
  })
}

const chartDomPost = ref(null);
let chartInstance = null;

const chartDomPostDay = ref(null);
let chartInstancePostDay = null;

onMounted(async () => {
  await fetchData();
  await nextTick(); // 确保DOM已经渲染完成
  //贴子数的饼图
  chartInstance = echarts.init(chartDomPost.value);
  const option = {
    tooltip: {
      trigger: 'item',
    },
    grid: {
      x: '100px',
      left: '100px',
    },
    legend: {
      left: 'center',
      top: 'bottom',
      data: ['剩余失物招领', '已完成失物招领', '剩余失物寻回', '已完成失物寻回'],
    },
    series: [
      {
        type: 'pie',
        radius: ['40%', '70%'],
        center: ['50%', '50%'],
        avoidLabelOverlap: false,
        itemStyle: {
          borderRadius: 10,
          borderColor: '#fff',
          borderWidth: 2
        },
        label: {
          show: false,
          position: 'center'
        },
        labelLine: {
          show: false
        },
        emphasis: {
          label: {
            show: true,
            fontSize: 16,
            fontWeight: 'bold'
          }
        },
        data: [
          { value: foundPostNum.value-passFound.value, name: '剩余失物招领' },
          { value: passFound.value, name: '已完成失物招领' },
          { value: lostPostNum.value-passLost.value, name: '剩余失物寻回' },
          { value: passLost.value, name: '已完成失物寻回' },
        ],
      },
    ],
  };
  chartInstance.setOption(option);

  chartInstancePostDay = echarts.init(chartDomPostDay.value);
  const option2 = {
  color: ['#80FFA5', '#00DDFF', '#37A2FF', '#FF0087', '#FFBF00'],
  title: {
    text: '每日数据'
  },
  tooltip: {
    trigger: 'axis',
    axisPointer: {
      type: 'cross',
      label: {
        backgroundColor: '#6a7985'
      }
    }
  },
  legend: {
    data: ['失物招领', '寻物启事']
  },
  toolbox: {
    feature: {
      saveAsImage: {}
    }
  },
  grid: {
    left: '3%',
    right: '4%',
    bottom: '3%',
    containLabel: true
  },
  xAxis: [
    {
      type: 'category',
      boundaryGap: false,
      data: foundDate.value
    }
  ],
  yAxis: [
    {
      type: 'value'
    }
  ],
  series: [
    {
      name: '失物招领',
      type: 'line',
      stack: 'Total',
      smooth: true,
      lineStyle: {
        width: 0
      },
      showSymbol: false,
      areaStyle: {
        opacity: 0.8,
        color: new echarts.graphic.LinearGradient(0, 0, 0, 1, [
          {
            offset: 0,
            color: 'rgb(128, 255, 165)'
          },
          {
            offset: 1,
            color: 'rgb(1, 191, 236)'
          }
        ])
      },
      emphasis: {
        focus: 'series'
      },
      data: foundNum.value
    },
    {
      name: '寻物启事',
      type: 'line',
      stack: 'Total',
      smooth: true,
      lineStyle: {
        width: 0
      },
      showSymbol: false,
      areaStyle: {
        opacity: 0.8,
        color: new echarts.graphic.LinearGradient(0, 0, 0, 1, [
          {
            offset: 0,
            color: 'rgb(0, 221, 255)'
          },
          {
            offset: 1,
            color: 'rgb(77, 119, 255)'
          }
        ])
      },
      emphasis: {
        focus: 'series'
      },
      data: lostNum.value
    },
  ]
};
chartInstancePostDay.setOption(option2);
});

onUnmounted(() => {
  if (chartInstance != null && chartInstance.dispose) {
    chartInstance.dispose();
  }
  if (chartInstancePostDay != null && chartInstancePostDay.dispose) {
    chartInstancePostDay.dispose();
  }
});
</script>

<style scoped>
.container {
  padding: 10px;
  display: flex;
  justify-content: center;
  height: 660px;
  overflow-y: auto; /* 溢出时启用垂直滚动 */
  gap: 20px;
}

.headcard{
  padding: 20px;
  font-size: 22px;
  font-weight: bold;
  width: 1050px;
  height: 100px;
  background-color: white;
  border-radius: 15px;
  box-shadow: 0cap 3px 3px 0 rgba(0, 0, 0, 0.1);
}

.datacard{
  width: 1050px;
  height: 250px;
  background-color: white;
  border-radius: 15px;
  box-shadow: 0cap 3px 3px 0 rgba(0, 0, 0, 0.1);
  margin-top: 20px;
  padding: 15px;
  gap:20px;
}

.indatacard{
  padding-top: 10px;
  padding-bottom: 10px;
  padding-left: 20px;
  padding-right: 20px;
  width: 480px;
  height: 100px;
  border-radius: 15px;
  color: white;
  font-weight: 200;
  font-size: 25px;
}

.number{
  margin-right: 380px;
}

.charts{
  height: 400px;
  width: 510px;
  background-color: white;
  border-radius: 15px;
  box-shadow: 0cap 3px 3px 0 rgba(0, 0, 0, 0.1);
}

.chart {
  width: 400px;
  height: 400px;
}
</style>