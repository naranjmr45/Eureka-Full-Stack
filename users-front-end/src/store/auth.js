import { defineStore } from 'pinia'
import { computed, ref } from 'vue'
import axios from 'axios'

const baseUrl = 'https://didactic-fiesta-5gpgwjjwrr4jh7rjg-8082.app.github.dev'

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
            const response = await axios.post(`${baseUrl}/users-ws/users/login`, credentials, {
                headers: { 'Content-Type': 'application/json'}
            })

            // If Spring returns an error (like 401), .json() might fail or 
            // return an error object. We handle that here.
            if(response.ok) {
                console.log(credentials)
                const data = await response.json();
                token.value = data.token;
            } else {
                token.value = 'error-login-failed';
            }
        } catch (error) {
            console.error("Login failed", error);
            // FIX: Set a string message, not the 'error' object itself
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