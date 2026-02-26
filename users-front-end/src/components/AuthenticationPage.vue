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

    <h3 v-if="!authStore.isLoggedIn">Or</h3>

    <form v-if="!authStore.isLoggedIn" @submit.prevent="handleSignUp" class="ui form">
        <div class="field">
            <label>First Name</label>
            <input type="text" required placeholder="First Name" v-model="signUpCredentials.firstName">
        </div>
        <div class="field">
            <label>Last Name</label>
            <input type="text" required placeholder="Last Name" v-model="signUpCredentials.lastName">
        </div>
        <div class="field">
            <label>Email</label>
            <input type="text" required placeholder="Email" v-model="signUpCredentials.email">
        </div>
        <div class="field">
            <label>Password</label>
            <input type="text" required placeholder="Password" v-model="signUpCredentials.password">
        </div>
        <button class="primary ui button" type="submit">Sign Up</button>
    </form>
</template>

<script setup>
import { useAuth } from '@/store/auth';
import { reactive } from 'vue';

const authStore = useAuth();

const loginCredentials = reactive({
    email: '',
    password: ''
})

const signUpCredentials = reactive({
    firstName: '',
    lastName: '',
    email: '',
    password: ''
})

const handleLogin = () => {
    authStore.login(loginCredentials)
}

const handleSignUp = () => {
    authStore.signUp(signUpCredentials)
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