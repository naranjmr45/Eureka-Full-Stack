import { defineStore } from "pinia";
import { ref } from "vue";

export const useError = defineStore('error', () => {
    const errorMessage = ref(null);
    const errorCode = ref(null);

    const setError = (message, status) => {
        errorMessage.value = message;
        errorCode.value = status;
    }

    const clearError = () => {
        errorMessage.value = null;
        errorCode.value = null;
    }

    return {
        errorMessage,
        errorCode,
        setError,
        clearError
    }
})