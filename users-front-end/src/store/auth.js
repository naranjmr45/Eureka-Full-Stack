import { defineStore } from 'pinia'
import { computed, ref } from 'vue'
import api from '@/api'

const decodeJwt = (token) => {
  try {
    // Get the payload (index 1) and decode it
    const base64Url = token.split('.')[1];
    const base64 = base64Url.replace(/-/g, '+').replace(/_/g, '/');
    return JSON.parse(window.atob(base64));
  } catch (e) {
    return null;
  }
};

export const useAuth = defineStore('auth', () => {
    const token = ref(localStorage.getItem('user-token') || '')

    const payload = computed(() => {
        if (!token.value) return null;
        return decodeJwt(token.value);
    })

    const isLoggedIn = computed(() => {
        // Add "typeof" check to ensure it's a string before calling .includes
        return token.value &&
            typeof token.value === 'string' &&
            !token.value.includes('error');
    })

    const isAdmin = computed(() => {
        return payload.value?.scope?.includes('ROLE_ADMIN') || false;
    })

    const userId = computed(() => {
        return payload.value?.sub || null;
    })

    const signUp = async (credentials) => {
        try {
            const response = await api.post('/users-ws/users', credentials);

            if (response.status != 201) {
                console.log(response.status);
                return;
            }
            console.log("Sign up worked: ", response.data)
        } catch (error) {
            console.error("Sign Up Failed", error);
            alert("Sign Up Failed", error);
        }
    }

    const login = async (credentials) => {
        try {
            const response = await api.post(`/users-ws/users/login`, credentials);


            // 1. Access the custom header
            // Note: Axios headers are usually lowercase
            const receivedToken = response.headers['token'];

            if (receivedToken) {
                token.value = receivedToken;
                localStorage.setItem('user-token', receivedToken)
            } else {
                console.warn("Response received, but 'token' header was missing.");
                token.value = 'error-no-token-header';
            }
        } catch (error) {
            console.error("Login failed", error);
            token.value = 'error-network';
            alert("Login Failed" + error)
        }
    }


    const logout = () => {
        token.value = ''
        localStorage.removeItem('user-token');
        window.location.href = '/login';
    }
    return {
        token,
        isLoggedIn,
        isAdmin,
        userId,
        login,
        logout,
        signUp
    }
})