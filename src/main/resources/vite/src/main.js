import {createApp} from 'vue'
import {createPinia} from 'pinia'

import 'element-plus/dist/index.css'

import App from './App.vue'
import router from './router'
import piniaPluginPersistedstate from 'pinia-plugin-persistedstate'

// Vuetify
import 'vuetify/styles'
import { createVuetify } from 'vuetify'
import * as components from 'vuetify/components'
import * as directives from 'vuetify/directives'
import '@mdi/font/css/materialdesignicons.css'

const app = createApp(App)
const pinia = createPinia()
pinia.use(piniaPluginPersistedstate)

const vuetify = createVuetify({
    components,
    directives,
  })

app.use(pinia).use(router).use(vuetify).mount('#app')