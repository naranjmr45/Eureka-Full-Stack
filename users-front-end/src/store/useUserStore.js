import { defineStore } from "pinia";
import { ref } from "vue";
import api from "@/api";

const baseUrl = 'https://didactic-fiesta-5gpgwjjwrr4jh7rjg-8082.app.github.dev'

export const useUserStore = defineStore('user', () => {
    // --- STATE (Data) ---
    const statusMessage = ref('')
    const isLoading = ref(false)
    const emailData = ref('') // Renamed slightly to avoid confusion with the search input
    const users = ref([])

    // --- ACTIONS (Methods) ---
    
    // Tip: Pass the email string directly instead of the whole 'event' 
    // This makes the store "cleaner" and independent of the UI
    const findByEmail = async (emailValue) => {
        try {
            const response = await fetch(`${baseUrl}/users-ws/users?email=${emailValue}`);    

            if (!response.ok) {
                emailData.value = response.status + " error"
                return;
            }

            const data = await response.text();
            emailData.value = data;
        } catch (error) {
            emailData.value = "Error"
            console.error(error);
        }
    }

    const onStatusCheck = async () => {
        isLoading.value = true
        try {
            const response = await api.get(`/users-ws/users/status/check`);
            
            if (response.status != 200) {
                statusMessage.value = response.status + " error"
                return;
            }

            const data = await response.data;
            statusMessage.value = data;
        } catch (error) {
            statusMessage.value = "Error"
            console.error("Error connecting to Spring Boot:", error);
        } finally {
            isLoading.value = false
        }
    }
    
    const getUsers = async () => {
        try {
            const response = await api.get(`/users-ws/users`);
            
            if (response.status != 200) {
                users.value = response.status + " error"
                throw new Error(`HTTP error! status: ${response.status}`);
            }

            // Use .text() since your Java backend returns a plain String
            users.value = response.data;
            console.log(users.value)
            
        } catch (error) {
            console.error("Error connecting to Spring Boot:", error);
        } finally {
            isLoading.value = false
        }
    }

    // --- THE "SECRET SAUCE" ---
    // You MUST return anything you want to access in your components
    return { 
        statusMessage, 
        isLoading, 
        emailData,
        users, 
        findByEmail, 
        onStatusCheck,
        getUsers
    }
})
