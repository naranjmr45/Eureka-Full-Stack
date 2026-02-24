<template>
    <div class = "ui huge menu">

        <div class="ui input focus">
            <input @keyup.enter = "findByEmail" type="text" placeholder="Search...">
            {{ email }}
        </div>

        <div class = "item">
            <a @click="onStatusCheck" 
               class="ui button" 
               :class="{ 'loading': isLoading }">
               Status Check
            </a>
            {{ statusMessage }}
        </div>

        <div class = "right menu">
            <div class = "item">
                <a class = "ui button">
                    Login
                </a>
            </div>

            <div class = "item">
                <a class = "primary ui button">
                    Sign Up
                </a>
            </div>
        </div>
    </div>
</template>

<script setup>
import { ref } from 'vue'

// Use a ref to store the status if you want to display it in the UI later
const statusMessage = ref('')
const isLoading = ref(false)
const email = ref('')

const findByEmail = async (event) => {
    try {
        email.value = event.target.value
        const baseUrl = 'https://didactic-fiesta-5gpgwjjwrr4jh7rjg-8082.app.github.dev/users-ws/users';
        const response = await fetch(`${baseUrl}?email=${email.value}`);    

        if (!response.ok) {
            email.value = response.status + " error"
            throw new Error(`HTTP error! status: ${response.status}`);
        }

        const data = await response.text();
        
        email.value = data;
    } catch (error) {
        email.value = "Error"
        console.error(error);
    }
}

const onStatusCheck = async () => {
    isLoading.value = true
    try {
        const response = await fetch('https://didactic-fiesta-5gpgwjjwrr4jh7rjg-8082.app.github.dev/users-ws/users/status/check');
        
        if (!response.ok) {
            statusMessage.value = response.status + " error"
            throw new Error(`HTTP error! status: ${response.status}`);
        }

        // Use .text() since your Java backend returns a plain String
        const data = await response.text();
        
        statusMessage.value = data;
        
    } catch (error) {
        statusMessage.value = "Error"
        console.error("Error connecting to Spring Boot:", error);
    } finally {
        isLoading.value = false
    }
}
</script>

<style scoped>

</style>