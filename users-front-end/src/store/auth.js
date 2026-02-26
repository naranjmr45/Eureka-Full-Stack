import { defineStore } from 'pinia'
import { computed, ref } from 'vue'
import api from '@/api'

//const baseUrl = 'https://didactic-fiesta-5gpgwjjwrr4jh7rjg-8082.app.github.dev'

export const useAuth = defineStore('auth', () => {
    const token = ref('')

    const isLoggedIn = computed(() => {        
        // Add "typeof" check to ensure it's a string before calling .includes
        return token.value && 
               typeof token.value === 'string' && 
               !token.value.includes('error');
    })

    const login = async (credentials) => {
        try {
            const response = await api.post(`/users-ws/users/login`, credentials);
            
            
            // 1. Access the custom header
            // Note: Axios headers are usually lowercase
            const receivedToken = response.headers['token']; 
            
            console.log("Token from headers:", receivedToken);

            if (receivedToken) {
                token.value = receivedToken;
                // If you need the userId too:
                // const userId = response.headers['userid'];
            } else {
                console.warn("Response received, but 'token' header was missing.");
                token.value = 'error-no-token-header';
            }
        } catch (error) {
            console.error("Login failed", error);
            token.value = 'error-network'; 
        }
    }


    const logout = () => {
        token.value = ''
    }
    return {
        token, 
        isLoggedIn,
        login,
        logout
    }
})