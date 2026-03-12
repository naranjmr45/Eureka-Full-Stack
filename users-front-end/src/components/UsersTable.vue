<template>
    <div v-if="authStore.isLoggedIn" id="userTable">
        <div class="ui menu">
            <div class="item">
                <a @click="userStore.getUsers(0)" class="ui button" :class="{ 'loading': userStore.isLoading }">
                    Get All Users
                </a>
            </div>
            <div v-if="authStore.isAdmin" class="right menu">
                <button @click="showCreateForm = !showCreateForm" class="ui button">
                    {{ showCreateForm ? 'Cancel' : 'Create User' }}
                </button>
            </div>
        </div>

        <div v-if="showCreateForm" class="ui segment">
            <h4>Add New User</h4>
            <UserForm buttonText="Add User to System" @submit="handleAdminCreateUser" />
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

            <div class="ui pagination menu" v-if="userStore.totalPages > 1">
                <a class="item" 
                :class="{ 'disabled': userStore.currentPage === 0 }" 
                @click="userStore.getUsers(userStore.currentPage - 1)">
                    Previous
                </a>

                <a v-for="pageIndex in userStore.totalPages" 
                :key="pageIndex" 
                class="item" 
                :class="{'active': userStore.currentPage === (pageIndex - 1)}" @click="userStore.getUsers(pageIndex - 1)">
                    {{ pageIndex }}
                </a>

                <a class="item" 
                :class="{ 'disabled': userStore.currentPage === userStore.totalPages - 1 }" 
                @click="userStore.getUsers(userStore.currentPage + 1)">
                    Next
                </a>

            </div>
        </div>
    </div>
</template>

<script setup>
import { useUserStore } from '@/store/useUserStore';
import { useAuth } from '@/store/auth';
import UsersRow from './UsersRow.vue';
import UserForm from './UserForm.vue';
import { ref } from 'vue';

const showCreateForm = ref(false);

const userStore = useUserStore();
const authStore = useAuth();


const handleAdminCreateUser = async (userData) => {
    await authStore.signUp(userData);
    showCreateForm.value = false;
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