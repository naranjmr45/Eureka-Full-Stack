<template>
    <div class = "item">
            <a @click="getUsers" 
               class="ui button" 
               :class="{ 'loading': isLoading }">
               Get All Users
            </a>
            {{ users }}
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
import { ref } from 'vue'

// Use a ref to store the status if you want to display it in the UI later
const users = ref('')
const isLoading = ref(false)

const getUsers = async () => {
    isLoading.value = true
    try {
        const response = await fetch('https://didactic-fiesta-5gpgwjjwrr4jh7rjg-8082.app.github.dev/users-ws/users');
        
        if (!response.ok) {
            users.value = response.status + " error"
            throw new Error(`HTTP error! status: ${response.status}`);
        }

        // Use .text() since your Java backend returns a plain String
        const data = await response.text();
        
        users.value = data;
        
    } catch (error) {
        console.error("Error connecting to Spring Boot:", error);
    } finally {
        isLoading.value = false
    }
}
</script>

<style scoped>
    caption {
        font-size: x-large;
        padding: 10px
    }
</style>