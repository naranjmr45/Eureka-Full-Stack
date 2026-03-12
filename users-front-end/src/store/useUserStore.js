import { defineStore } from "pinia";
import { ref, computed } from "vue";
import api from "@/api";

export const useUserStore = defineStore('user', () => {
    // --- STATE (Data) ---
    const statusMessage = ref('')
    const isLoading = ref(false)
    const searchQuery = ref('')
    const users = ref([])
    const totalPages = ref(0);
    const currentPage = ref(0);

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
            // Check if the server actually sent a response body
            if (error.response && error.response.data) {
                // This maps to the 'message' key in your Spring Map
                statusMessage.value = error + ": " +  error.response.data.message;
            } else {
                // Fallback for network errors or no response
                statusMessage.value = "An unexpected error occurred.";
            }
            console.error("Error connecting to Spring Boot:", error.response);
        } finally {
            isLoading.value = false
        }
    }

    const getUsers = async (page = 0) => {
        try {
            const response = await api.get(`/users-ws/users?page=${page}`);

            if (response.status != 200) {
                users.value = response.status + " error"
                throw new Error(`HTTP error! status: ${response.status}`);
            }

            // Use .text() since your Java backend returns a plain String
            console.log(response.data)
            totalPages.value = response.data.totalPages;
            users.value = response.data.content;
            currentPage.value = response.data.number;

        } catch (error) {
            console.error("Error connecting to Spring Boot:", error);
        } finally {
            isLoading.value = false
        }
    }

    const deleteUser = async (emailValue) => {
        try {
            const response = await api.delete(`/users-ws/users?email=${emailValue}`);

            if (response.status != 200) {
                console.log(response.data);
                return;
            }

            users.value = users.value.filter(user => user.email !== emailValue);

        } catch (error) {
            console.error(error)
        }
    }

    const updateUser = async (credentials) => {
        try {
            const response = await api.patch('/users-ws/users', credentials);

            if (response.status === 200) {
                const index = users.value.findIndex(u => u.userId === credentials.userId);
                if (index !== -1) {
                    users.value[index] = { ...users.value[index], ...credentials };
                }
                return true;
            }
        } catch (error) {
            console.error(error);
            return false;
        }
    }

    // You MUST return anything you want to access in your components
    return {
        statusMessage,
        isLoading,
        searchQuery,
        users,
        filteredUsers,
        totalPages,
        currentPage,
        updateUser,
        deleteUser,
        findByEmail,
        onStatusCheck,
        getUsers
    }
})
