<template>
  <div class="container">
    <el-card>
      <div ref="chartDom" class="chart"></div>
    </el-card>
    <div ref="chartDom2" class="chart"></div>
  </div>
</template>

<script setup>
import { ref, onMounted, onUnmounted, nextTick } from 'vue';
import * as echarts from 'echarts';
import { ElCard } from 'element-plus';

const chartDom = ref(null);
let chartInstance = null;
const chartDom2 = ref(null);
let chartInstance2 = null;

onMounted(async () => {
  await nextTick(); // 确保DOM已经渲染完成
  chartInstance = echarts.init(chartDom.value);
  const option = {
    title: {
      text: 'Nightingale Chart',
      subtext: 'Fake Data',
      left: 'center'
    },
    tooltip: {
      trigger: 'item',
    },
    legend: {
      left: 'center',
      top: 'bottom',
      data: ['rose1', 'rose2', 'rose3'],
    },
    series: [
      {
        name: 'Radius Mode',
        type: 'pie',
        radius: ['40%', '70%'],
        center: ['25%', '50%'],
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
            fontSize: 25,
            fontWeight: 'bold'
          }
        },
        data: [
          { value: 40, name: '通过数' },
          { value: 33, name: '待审核数' },
          { value: 28, name: '未通过数' },
        ],
      },
    ],
  };
  chartInstance.setOption(option);

  chartInstance2 = echarts.init(chartDom2.value);
  const option2 = {
    title: {
      text: 'Nightingale Chart',
      subtext: 'Fake Data',
      left: 'center'
    },
    tooltip: {
      trigger: 'item',
      formatter: '{a} <br/>{b} : {c} ({d}%)'
    },
    legend: {
      left: 'center',
      top: 'bottom',
      data: ['rose1', 'rose2', 'rose3', 'rose4', 'rose5', 'rose6', 'rose7', 'rose8'],
    },
    series: [
      {
        name: 'Radius Mode',
        type: 'pie',
        radius: [10, 100],
        center: ['25%', '50%'],
        roseType: 'radius',
        itemStyle: {
          borderRadius: 5,
        },
        label: {
          show: false,
        },
        emphasis: {
          label: {
            show: true,
          }
        },
        data: [
          { value: 40, name: '通过数' },
          { value: 33, name: '待审核数' },
          { value: 28, name: '未通过数' },
        ],
      },
    ],
  };
  chartInstance2.setOption(option2);
});

onUnmounted(() => {
  if (chartInstance != null && chartInstance.dispose) {
    chartInstance.dispose();
  }
  if (chartInstance2 != null && chartInstance2.dispose) {
    chartInstance2.dispose();
  }
});
</script>

<style scoped>
.container {
  display: flex;
  flex-direction: column; /* 确保卡片垂直堆叠 */
  max-height: calc(100vh - 200px); /* 设置最大高度，减去其他元素的高度 */
  overflow-y: auto; /* 溢出时启用垂直滚动 */
}

.chart {
  width: 600px;
  height: 400px;
}
</style>