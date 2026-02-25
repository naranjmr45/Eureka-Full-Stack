<template>
    <div class = "item">
            <a @click="userStore.getUsers" 
               class="ui button" 
               :class="{ 'loading': userStore.isLoading }">
               Get All Users
            </a>
            {{ userStore.users }}
        </div>
    <div>
        <table class = "ui celled table">
            <caption>Users</caption>

            <thead>
                <tr>
                    <th>First Name</th>
                    <th>Last Name</th>
                    <th>Email</th>
                    <th>Password</th>
                </tr>
            </thead>
            <tbody>

            </tbody>

        </table>
    </div>
</template>

<script setup>
import { useUserStore } from '@/store/useUserStore';

const userStore = useUserStore();
</script>

<style scoped>
    caption {
        font-size: x-large;
        padding: 10px
    }
</style>