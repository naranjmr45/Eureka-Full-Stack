<template>
    <div v-if="authStore.isLoggedIn" id="userTable">
        <div class="ui menu">
            <div class="item">
                <a @click="userStore.getUsers" class="ui button" :class="{ 'loading': userStore.isLoading }">
                    Get All Users
                </a>
            </div>
            <div class="right menu">
                <a class="ui button">
                    Create User
                </a>
            </div>
        </div>
        <div id="userTable">
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
                    <UsersRow v-for="user in userStore.filteredUsers" :key="user.userId" :user="user"
                        @update="userStore.updateUser" @delete="confirmDelete" />
                </tbody>

            </table>
        </div>
    </div>
</template>

<script setup>
import { useUserStore } from '@/store/useUserStore';
import { useAuth } from '@/store/auth';
import UsersRow from './UsersRow.vue';

const userStore = useUserStore();
const authStore = useAuth();


const confirmDelete = (user) => {
    const message = `Are you sure you want to delete ${user.firstName} (${user.email})?`;

    if (window.confirm(message)) {
        userStore.deleteUser(user.email);
    }
}
</script>

<style scoped>
caption {
    font-size: x-large;
    padding: 10px
}

.userTable {
    display: flex;
    justify-content: center;
}
</style>