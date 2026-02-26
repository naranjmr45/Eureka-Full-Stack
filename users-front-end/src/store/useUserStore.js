import { defineStore } from "pinia";
import { ref, computed } from "vue";
import api from "@/api";

export const useUserStore = defineStore('user', () => {
    // --- STATE (Data) ---
    const statusMessage = ref('')
    const isLoading = ref(false)
    const searchQuery = ref('') 
    const users = ref([])

    const filteredUsers = computed(() => {
         if (!searchQuery.value) return users.value;
        
        const query = searchQuery.value.toLowerCase();
        return users.value.filter(user => 
            user.email.toLowerCase().includes(query) ||
            user.firstName.toLowerCase().includes(query) ||
            user.lastName.toLowerCase().includes(query)
        );
    })

    // --- ACTIONS (Methods) ---

    // Tip: Pass the email string directly instead of the whole 'event' 
    // This makes the store "cleaner" and independent of the UI
    const findByEmail = async (emailValue) => {
        try {
            const response = await api.get(`/users-ws/users?email=${emailValue}`);
            if (response.status != 200) {
                searchQuery.value = response.status + " error"
                return;
            }
            searchQuery.value = response.data
        } catch (error) {
            searchQuery.value = "Error"
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
        searchQuery,
        users,
        filteredUsers,
        findByEmail,
        onStatusCheck,
        getUsers
    }
})
