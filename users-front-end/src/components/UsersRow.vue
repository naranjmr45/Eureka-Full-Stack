<template>
    <tr>
        <td>
            <div v-if="isEditing" class="ui input">
                <input v-model="editForm.firstName" type="text" />
            </div>
            <span v-else>{{ user.firstName }}</span>
        </td>
        <td>
            <div v-if="isEditing" class="ui input">
                <input v-model="editForm.lastName" type="text" />
            </div>
            <span v-else>{{ user.lastName }}</span>
        </td>
        <td>
            <div v-if="isEditing" class="ui input">
                <input v-model="editForm.email" type="text" />
            </div>
            <span v-else>{{ user.email }}</span>
        </td>
        <td>
            {{ user.userId }}
        </td>
        <td>
            <div v-if="isEditing">
                <button @click="handleSave" class="ui green button mini">Save</button>
                <button @click="isEditing = false" class="ui blue button mini">Cancel</button>
            </div>
            <button v-else @click="isEditing = true" class="ui blue button">Edit</button>
        </td>
        <td>
            <button @click="$emit('delete', user)" class="ui red button">Delete</button>
        </td>
    </tr>
</template>

<script setup>
import { reactive, ref } from 'vue';

const props = defineProps(['user']);
const emit = defineEmits(['update', 'delete']);

const isEditing = ref(false);
const editForm = reactive({ ...props.user });

const handleSave = () => {
    emit('update', { ...editForm });
    isEditing.value = false;
}
</script>