<template>
    <form v-if="!authStore.isLoggedIn" @submit.prevent="handleLogin" class="ui form">
        <div class="field">
            <label>Email</label>
            <input type="text" required placeholder="Email" v-model="loginCredentials.email">
        </div>
        <div class="field">
            <label>Password</label>
            <input type="text" required placeholder="Password" v-model="loginCredentials.password">
        </div>
        <button class="ui button" type="submit">Login</button>
    </form>

    <h3 v-if="!authStore.isLoggedIn">Create An Account</h3>

    <UserForm v-if="!authStore.isLoggedIn" button-text="Sign Up" @submit="authStore.signUp" />
</template>

<script setup>
import { useAuth } from '@/store/auth';
import { reactive } from 'vue';
import UserForm from './UserForm.vue';

const authStore = useAuth();

const loginCredentials = reactive({
    email: '',
    password: ''
})


const handleLogin = () => {
    authStore.login(loginCredentials)
    loginCredentials.email = '';
    loginCredentials.password = '';
}

</script>

<style scoped>
form {
    display: flex;
    justify-content: center
}

h3 {
    display: flex;
    justify-content: center
}
</style>