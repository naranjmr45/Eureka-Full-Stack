<template>
    <div v-if="authStore.isLoggedIn" id="userTable">
        <div class="item">
            <a @click="userStore.getUsers" class="ui button" :class="{ 'loading': userStore.isLoading }">
                Get All Users
            </a>
        </div>
        <div id="userTable">
            <table class="ui celled table">
                <caption>Users</caption>

                <thead>
                    <tr>
                        <th>First Name</th>
                        <th>Last Name</th>
                        <th>Email</th>
                        <th>UserId</th>
                    </tr>
                </thead>
                <tbody>
                    <tr v-for="user in userStore.filteredUsers" :key="user.userId">
                        <td>
                            <div v-if="editingId === user.userId" class="ui input">
                                <input v-model="editForm.firstName" type="text" />
                            </div>
                            <span v-else>{{ user.firstName }}</span>
                        </td>
                        <td>
                            <div v-if="editingId === user.userId" class="ui input">
                                <input v-model="editForm.lastName" type="text" />
                            </div>
                            <span v-else>{{ user.lastName }}</span>
                        </td>
                        <td>
                            <div v-if="editingId === user.userId" class="ui input">
                                <input v-model="editForm.email" type="text" />
                            </div>
                            <span v-else>{{ user.email }}</span>
                        </td>
                        <td>{{ user.userId }}</td>
                        <!-- Actions -->
                        <td>
                            <div v-if="editingId === user.userId">
                                <button @click="saveUpdate" class="ui green button mini">Update</button>
                                <button @click="cancelEdit" class="ui basic button mini">Cancel</button>
                            </div>
                            <button v-else @click="startEdit(user)" class="ui yellow button">Edit</button>
                        </td>
                        <td>
                            <button @click="confirmDelete(user)" class="ui red button">Delete</button>
                        </td>
                    </tr>
                </tbody>

            </table>
        </div>
    </div>
</template>

<script setup>
import { useUserStore } from '@/store/useUserStore';
import { useAuth } from '@/store/auth';
import { ref, reactive } from 'vue';

const userStore = useUserStore();
const authStore = useAuth();

const editingId = ref(null);

const editForm = reactive({
    userId: '',
    firstName: '',
    lastName: '',
    email: ''
})

const startEdit = (user) => {
    editingId.value = user.userId;

    editForm.userId = user.userId;
    editForm.firstName = user.firstName;
    editForm.lastName = user.lastName;
    editForm.email = user.email;
}

const cancelEdit = () => {
    editingId.value = null;
}

const saveUpdate = async () => {
    const success = await userStore.updateUser({ ...editForm })
    if (success) {
        editingId.value = null;
    }
}

const confirmDelete = (user) => {
    const message = `Are you sure you want to delete ${user.firstName} (${user.email})?`;

    if (window.confirm(message)) {
        userStore.deleteUser(user.email);
    }
}
</script>

<style scoped>
caption {
    font-size: x-large;
    padding: 10px
}

.userTable {
    display: flex;
    justify-content: center;
}
</style>