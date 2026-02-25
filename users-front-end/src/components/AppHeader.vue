<template>
    <div class = "ui huge menu">
        <div v-if="authStore.isLoggedIn" class="ui input focus">
            <input @keyup.enter = "handleSearch" type="text" placeholder="Search...">
            <span>{{ userStore.emailData }}</span>
        </div>

        <div v-if="authStore.isLoggedIn" class = "item">
            <a @click="userStore.onStatusCheck" 
            class="ui button" 
            :class="{ 'loading': userStore.isLoading }">
            Status Check
            </a>
            {{ userStore.statusMessage }}
        </div>

        <div v-if="authStore.isLoggedIn" class = "right menu">
            <div class = "item">
                <a @click="authStore.logout" class = "primary ui button">
                    Logout
                </a>
            </div>
        </div>

        <div v-else class = "right menu">
            <div class = "item">
                <a @click="authStore.login" class = "ui button">
                    Login
                </a>
            </div>

            <div class = "item">
                <a class = "primary ui button">
                    Sign Up
                </a>
            </div>
        </div>
    </div>
</template>

<script setup>
    import { useUserStore } from '@/store/useUserStore';
    import { useAuth } from '@/store/auth';

    const userStore = useUserStore();
    const authStore = useAuth();

    // This handles the UI event and passes the data to Pinia
    const handleSearch = (event) => {
        userStore.findByEmail(event.target.value)
    }
</script>

<style scoped>

</style>