<template>
    <div v-if="authStore.isLoggedIn" id="userTable">
        <div class="item">
        <a @click="userStore.getUsers" class="ui button" :class="{ 'loading': userStore.isLoading }">
            Get All Users
        </a>
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
                <tr v-for="user in userStore.filteredUsers" :key="user.email">
                    <td>{{ user.firstName }}</td>
                    <td>{{ user.lastName }}</td>
                    <td>{{ user.email }}</td>
                    <td>{{ user.userId }}</td>
                    <td>
                        <button class="ui yellow button">Update</button>
                    </td>
                    <td>
                        <button @click="confirmDelete(user)" class="ui red button">Delete</button>
                    </td>
                </tr>
            </tbody>

        </table>
    </div>
    </div>
</template>

<script setup>
import { useUserStore } from '@/store/useUserStore';
import { useAuth } from '@/store/auth';
import { reactive } from 'vue';

const userStore = useUserStore();
const authStore = useAuth();

const updateCredentials = reactive({
    firstName: '',
    lastName: '',
    email: ''
})

const updateUser = () => {
    userStore.updateUser(updateCredentials)
}

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