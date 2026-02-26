<template>
    <div class="item">
        <a @click="userStore.getUsers" class="ui button" :class="{ 'loading': userStore.isLoading }">
            Get All Users
        </a>
    </div>
    <div>
        <table class="ui celled table">
            <caption>Users</caption>

            <thead>
                <tr>
                    <th>First Name</th>
                    <th>Last Name</th>
                    <th>Email</th>
                    <th>UserId</th>
                </tr>
            </thead>
            <tbody>
                <tr v-for="user in userStore.filteredUsers" :key="user.email">
                    <td>{{ user.firstName }}</td>
                    <td>{{ user.lastName }}</td>
                    <td>{{ user.email }}</td>
                    <td>{{ user.userId }}</td>
                </tr>
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