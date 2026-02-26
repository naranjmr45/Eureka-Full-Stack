<template>
    <form v-if="!authStore.isLoggedIn" @submit.prevent="handleLogin" class="ui form">
        <div class="field">
            <label>Email</label>
            <input type="text" required placeholder="Email" v-model="credentials.email">
        </div>
        <div class="field">
            <label>Password</label>
            <input type="text" required placeholder="Password" v-model="credentials.password">
        </div>
        <button class="ui button" type="submit">Login</button>
    </form>
</template>

<script setup>
import { useAuth } from '@/store/auth';
import { reactive } from 'vue';

const authStore = useAuth();

const credentials = reactive({
    email: '',
    password: ''
})

const handleLogin = () => {
    authStore.login(credentials)
}

</script>

<style scoped>
form {
    display: flex;
    justify-content: center
}
</style>