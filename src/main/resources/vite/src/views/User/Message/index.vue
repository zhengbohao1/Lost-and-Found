<template>
    <el-card class="header-card"></el-card>
    <el-scrollbar class="body-card">
        <el-card>
            <p v-for="n in 100" :key="n">右侧内容 {{ n }}</p> 
        </el-card>
    </el-scrollbar>
</template>

<script setup>
</script>

<style scoped>
.header-card{
    height: 160px;
}

.body-card{
    margin-top: 15px;
    height: 480px;
}
</style>