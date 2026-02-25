import { defineStore } from 'pinia'
import { computed, ref } from 'vue'

const baseUrl = 'https://didactic-fiesta-5gpgwjjwrr4jh7rjg-8082.app.github.dev'

export const useAuth = defineStore('auth', () => {
    const token = ref('')

    const isLoggedIn = computed(() => {        
        return token.value !== '' && !token.value.includes('error');
    })

    const login = async (credentials) => {
        try {
            const response = await fetch(`${baseUrl}/users-ws/users/login`, {
                method: 'POST',
                headers: { 'Content-Type': 'application/json'},
                body: JSON.stringify(credentials)
            })

            const data = await response.json();

            if(response.ok) {
                token.value = data.token;
            } else {
                token.value = 'error';
            }
        } catch (error) {
            console.error("Login failed", error)
            token.value = error;
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