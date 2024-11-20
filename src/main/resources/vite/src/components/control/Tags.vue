<template>  
    <el-tabs v-model="activeTabName" @tab-click="handleTabClick" @tab-remove="handleTabRemove" type="card" class="demo-tabs" editable>  
      <el-tab-pane  
        v-for="tab in tabs"  
        :key="tab.name"  
        :label="tab.label"  
        :name="tab.name"  
      >  
        <!-- 可以在这里放置标签的内容 -->
      </el-tab-pane>  
    </el-tabs>  
  </template>  
  
  <script setup>  
  import { ref, watch, onMounted } from 'vue';  
  import { useRoute, useRouter } from 'vue-router';  
  
  const activeTabName = ref('');  
  const tabs = ref([]);  
  const router = useRouter();  
  const route = useRoute();  
  
  //增添标签页
  watch(() => route, (to, from) => {  // 监听路由变化  
    //获取当前路由的名称或唯一标识符（这里使用名称）  
    const tabName = to.path; // 使用路径的最后一部分  
    const tabLabel = to.meta.title || '未命名页面'; // 使用meta中的title，如果没有则默认为'未命名页面'  
  
    const existingTab = tabs.value.find(tab => tab.name === tabName);  // 检查tabs数组中是否已存在该标签页  
    if (!existingTab) { 
      tabs.value.push({ name: tabName, label: tabLabel });  //添加标签页
    }  
  
    activeTabName.value = tabName;  // 更新激活的标签页  
  }, { immediate: true, deep: true }); // 使用deep: true来确保对象内部的属性变化也能被监听到  
  

  const handleTabClick = (tab, event) => {
    // 点击标签页时，跳转到对应的路由（这里假设路由名称与标签页名称一致）
    const path = `${tab.paneName}`; 
    router.push(path); // 或者使用 path: `/some-path/${tab.name}` 如果路由配置是这样的话
  }  

  const handleTabRemove = (targetName) => {  
  // 从 tabs 数组中移除目标标签页  
  const newTabs = tabs.value.filter(tab => tab.name !== targetName);  
  tabs.value = newTabs;  
  
  // 如果被移除的是当前激活的标签页，尝试激活最以后一个一个标签页  
  // 如果没有其他标签页，则不设置 activeTabName（或设置为空字符串）  
  if (activeTabName.value === targetName && newTabs.length > 0) {  
    activeTabName.value = newTabs[newTabs.length - 1].name; // 激活最后一个标签页  
    const path = `/control/${activeTabName.value}`;
    router.push( path );
  } else if (newTabs.length === 0) {  
    activeTabName.value = ''; // 如果没有标签页了，则清空 activeTabName  
    router.push('/control'); // 跳转到默认路由
  }  
};  

  onMounted(() => {  
    // 组件挂载时，可以根据需要初始化tabs（但在这个例子中，由于使用了watch，所以不需要在这里初始化）  
  });  
  </script>  
  
  <style scoped>  
  .demo-tabs > .el-tabs__content {
  padding: 32px;
  color: #6b778c;
  font-size: 32px;
  font-weight: 600;
}
  </style>