<template>
    <div>
        <AppHeader></AppHeader>
        <h1>Table will go here</h1>
    </div>
</template>

<script>
import AppHeader from './components/AppHeader.vue';
export default {
    name: 'App',
    components: {
        AppHeader
    }
};
</script>

<style scoped>

    h3 {
        text-align: center;
    }

    button {
        display: flex;
        justify-content: center;
        align-items: center;
    }
</style>