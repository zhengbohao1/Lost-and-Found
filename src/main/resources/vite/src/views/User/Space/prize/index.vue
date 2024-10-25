<template>
    <div  v-for="item in prizeList">
    <img :src="item.prizeImageUrl" :key="item.id" />
    </div>
</template>

<script setup>
import { ref, reactive, onMounted } from 'vue'
import { getPrizeList } from '@/apis/prize'

const prizeList = ref([])

const getPrize = async () => {
    await getPrizeList().then(res => {
        prizeList.value = res.data
        console.log(prizeList.value)
    })
}

onMounted(() => {
    getPrize()
})
</script>

<style scoped></style>