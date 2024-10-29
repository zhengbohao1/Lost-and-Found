<template>
    <div class="col">
      <div v-for="column in card_columns" :key="column.id">
        <section v-for="card in column" :key="card.id">
          <div class="card">
            <a :href="`/explore/${card.id}`" @click.prevent="details(card.id)">
              <img
                  :src="'http://localhost:8090/common/download?name=' + card.imgUrl"
                  class="image"
                  @load="handleLoad(card)"
                  alt=""
              />
            </a>
            <div style="padding: 10px">
              <div style="margin-bottom: 10px; height: 20px; overflow: hidden; text-overflow: ellipsis; white-space: nowrap;">
                <span style="font-size: 1.0rem;" @click="details(card.id)">{{ card.itemName }}</span>
              </div>
              <div class="bottom">
                <el-row style="display: flex; align-items: center;">
                  <router-link :to="`/user/index/${card.finderId}`">
                    <el-avatar
                    :src="'http://localhost:8090/user/getAvatarById?userId='+card.userId" size="small"
                    />
                  </router-link>
                  <div class="username">{{ card.nickName }}</div>
                </el-row>
              </div>
            </div>
          </div>
        </section>
      </div>
    </div>
  </template>