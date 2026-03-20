import { defineStore } from "pinia";
import { computed, ref } from "vue";

export const useError = defineStore('error', () => {
    const errorMessage = ref(null);
    const errorCode = ref(null);

    const getError = computed(() => {
        return errorMessage.value + ": " + errorCode.value;
    })

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
        getError,
        setError,
        clearError
    }
})