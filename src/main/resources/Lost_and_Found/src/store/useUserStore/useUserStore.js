import { defineStore } from 'pinia'

export const useUserStore =
  defineStore('useUserStore', {
    state: () => ({
        count:{
          count: 33
        }
    }),
    getters: {},
    actions: {},
  })
