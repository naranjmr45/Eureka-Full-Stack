<template>
    <div class="ui huge menu">
        <div v-if="authStore.isLoggedIn" class="ui input focus">
            <input v-model="userStore.searchQuery" type="text" placeholder="Search users...">
        </div>

        <div class="item">
            <a @click="userStore.onStatusCheck" class="ui button" :class="{ 'loading': userStore.isLoading }">
                Status Check
            </a>
            {{ userStore.statusMessage }}
        </div>

        <div v-if="authStore.isLoggedIn" class="right menu">
            <div class="item">
                <a @click="authStore.logout" class="primary ui button">
                    Logout
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

</script>

<style scoped></style>