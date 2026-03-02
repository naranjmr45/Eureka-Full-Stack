<template>
    <form @submit.prevent="handleSubmit" class="ui form">
        <div class="field">
            <label>First Name</label>
            <input type="text" required placeholder="First Name" v-model="formData.firstName">
        </div>
        <div class="field">
            <label>Last Name</label>
            <input type="text" required placeholder="Last Name" v-model="formData.lastName">
        </div>
        <div class="field">
            <label>Email</label>
            <input type="text" required placeholder="Email" v-model="formData.email">
        </div>
        <div class="field">
            <label>Password</label>
            <input type="text" required placeholder="Password" v-model="formData.password">
        </div>
        <button class="primary ui button" type="submit">{{ buttonText || 'Submit' }}</button>
    </form>
</template>

<script setup>
import { reactive } from 'vue';


const props = defineProps(['buttonText']);
const emits = defineEmits(['handleSubmit']);

const formData = reactive({
    firstName: '',
    lastName: '',
    email: '',
    password: ''
})

const handleSubmit = () => {
    emit('submit', {...formData});

    Object.keys(formData).forEach(key => formData[key] = '');
}
</script>