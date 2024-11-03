<template>
    <el-breadcrumb separator="/"> </el-breadcrumb>  
        <el-row>
            <el-col :span="20">
                <Tags></Tags>
            </el-col>
            <el-col :span="2"></el-col>
            <el-col :span="2">
                <el-icon :size="30" @click="test"><RefreshLeft/></el-icon>
            </el-col>
        </el-row>
    <RouterView style="background-color: rgba(160, 190, 190, 0.1); margin-top: 10px;"/>
</template>

<script setup>
import Tags from './Tags.vue'
import { RefreshLeft } from '@element-plus/icons-vue';
import { ElMessage } from 'element-plus';

const test = () => {
    ElMessage({
        message: 'Hello, world!',
        type: 'success',
    })
}
</script>
