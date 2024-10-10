<template>
    <el-breadcrumb separator="/"> </el-breadcrumb>  
    <el-card>
        <el-row>
            <el-col :span="20">
                <Tags></Tags>
            </el-col>
            <el-col :span="2"></el-col>
            <el-col :span="2">
                <el-button type="primary" :icon="RefreshLeft" size="large"></el-button>
            </el-col>
        </el-row>
    </el-card>
    <RouterView style="background-color: rgba(181, 217, 255, 0.1); margin-top: 10px;"/>
</template>

<script setup>
import Tags from './Tags.vue'
import { RefreshLeft } from '@element-plus/icons-vue';

</script>
