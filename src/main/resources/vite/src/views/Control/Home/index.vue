<template>
    <div ref="chartDom" style="width: 600px; height: 400px;"></div>
  </template>
  
  <script setup>
  import { ref, onMounted, onUnmounted, nextTick } from 'vue';
  import * as echarts from 'echarts';
  
  // 创建一个响应式引用来保存DOM元素
  const chartDom = ref(null);
  let chartInstance = null;
  
  // 初始化ECharts实例并设置配置项（这里以折线图为例，但可灵活替换）
  onMounted(async () => {
    await nextTick(); // 确保DOM已经渲染完成
    chartInstance = echarts.init(chartDom.value);
    const option = {
      // 这里是ECharts的配置项，可以根据需要绘制不同类型的图表
      title: {
        text: 'ECharts 示例图表'
      },
      tooltip: {},
      xAxis: {
        data: ["类别1", "类别2", "类别3", "类别4", "类别5"]
      },
      yAxis: {},
      series: [{
        name: '数据系列',
        type: 'line', // 这里可以是'line'、'bar'、'pie'等，根据图表类型选择
        data: [120, 200, 150, 80, 70]
      }]
    };
    chartInstance.setOption(option);
  });
  
  // 销毁ECharts实例
  onUnmounted(() => {
    if (chartInstance != null && chartInstance.dispose) {
      chartInstance.dispose();
    }
  });
  </script>
  
  <style scoped>
  /* 添加一些CSS样式来美化图表容器（可选） */
  </style>
  