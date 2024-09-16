<template>
    <div>
        <el-collapse v-model="activeNames" @change="handleChange">
      <el-collapse-item title="第一项" name="1">
        <template #title>
          <span>第一项</span>
        </template>
        <div>这是第一项的内容。</div>
      </el-collapse-item>
      <el-collapse-item title="第二项" name="2">
        <template #title>
          <span>第二项</span>
        </template>
        <div>这是第二项的内容。</div>
      </el-collapse-item>
      <el-collapse-item title="第三项" name="3">
        <template #title>
          <span>第三项</span>
        </template>
        <div>这是第三项的内容。</div>
      </el-collapse-item>
    </el-collapse>
  </div>
</template>

<script setup>
import { ref } from 'vue'

const activeNames = ref(['执笔'])
</script>