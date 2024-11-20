import { defineStore } from "pinia";
import { ref } from "vue";

export const showErrorState = defineStore('showErrorState', () => {
    const showError = ref({ value: false });
    const errorMessage = ref('');

    const showErrorMsg = (msg) => {
        showError.value.value = true;
        errorMessage.value = msg;
    }

    const hideErrorMsg = () => {
        showError.value.value = false;
        errorMessage.value = '';
    }

    return {
        showError,
        errorMessage,
        showErrorMsg,
        hideErrorMsg
    }
});