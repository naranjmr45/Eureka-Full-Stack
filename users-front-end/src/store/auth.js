import { defineStore } from 'pinia'
import { computed, ref } from 'vue'

export const useAuth = defineStore('auth', () => {
    const token = ref('')

    const isLoggedIn = computed(() => {        
        return token.value !== '' && !token.value.includes('error');
    })

    const login = () => {
        token.value = "test123"
    }

    const logout = () => {
        token.value = '';
    }
    return {
        token, 
        isLoggedIn,
        login,
        logout
    }
})