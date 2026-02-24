<template>
    <div>
        <AppHeader></AppHeader>
        <UsersTable></UsersTable>
    </div>
</template>

<script>
import AppHeader from './components/AppHeader.vue';
import UsersTable from './components/UsersTable.vue';
export default {
    name: 'App',
    components: {
        AppHeader,
        UsersTable
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