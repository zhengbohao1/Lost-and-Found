import { defineStore } from "pinia";
import { ref } from "vue";

export const showErrorState = defineStore('showErrorState', () => {
    const errorProblem = ref({ failJoin: false });
    const errorMessage = ref('');

    const showErrorMsg = (msg) => {
        showError.value.value = true;
        errorMessage.value = msg;
    }

    const hideErrorMsg = () => {
        errorProblem.value.value = false;
        errorMessage.value = '';
    }

    return {
        errorProblem,
        errorMessage,
        showErrorMsg,
        hideErrorMsg
    }
});