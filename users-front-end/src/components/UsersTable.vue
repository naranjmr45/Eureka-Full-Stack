<template>
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

<script>
export default {
    name: 'UsersTable'
}
</script>

<style scoped>
    caption {
        font-size: x-large;
        padding: 10px
    }
</style>