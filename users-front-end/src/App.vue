<template>
    <div>
        <AppHeader></AppHeader>
        <AuthenticationPage></AuthenticationPage>
        <UsersTable></UsersTable>
    </div>
</template>

<script>
import AppHeader from './components/AppHeader.vue';
import AuthenticationPage from './components/AuthenticationPage.vue';
import UsersTable from './components/UsersTable.vue';
export default {
    name: 'App',
    components: {
        AppHeader,
        UsersTable,
        AuthenticationPage,
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