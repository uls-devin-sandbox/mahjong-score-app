<template>
  <div class="home">
    <div class="header-section">
      <h2>部屋一覧</h2>
      <el-button type="primary" @click="showCreateDialog = true">
        <el-icon><Plus /></el-icon>
        新しい部屋を作成
      </el-button>
    </div>

    <el-table :data="roomStore.rooms" v-loading="roomStore.loading" stripe>
      <el-table-column prop="name" label="部屋名" />
      <el-table-column prop="yearMonth" label="年月" />
      <el-table-column prop="createdAt" label="作成日" :formatter="formatDate" />
      <el-table-column label="操作" width="120">
        <template #default="scope">
          <el-button 
            type="primary" 
            size="small" 
            @click="$router.push(`/rooms/${scope.row.id}`)"
          >
            詳細
          </el-button>
        </template>
      </el-table-column>
    </el-table>

    <el-dialog v-model="showCreateDialog" title="新しい部屋を作成" width="400px">
      <el-form :model="newRoom" label-width="80px">
        <el-form-item label="部屋名">
          <el-input v-model="newRoom.name" placeholder="例: 2025年6月" />
        </el-form-item>
        <el-form-item label="年月">
          <el-date-picker
            v-model="newRoom.yearMonth"
            type="month"
            placeholder="年月を選択"
            format="YYYY-MM"
            value-format="YYYY-MM"
          />
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="showCreateDialog = false">キャンセル</el-button>
        <el-button type="primary" @click="createRoom">作成</el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script setup lang="ts">
import { ref, onMounted } from 'vue'
import { useRoomStore } from '@/stores/room'
import { ElMessage } from 'element-plus'
import { Plus } from '@element-plus/icons-vue'

const roomStore = useRoomStore()
const showCreateDialog = ref(false)
const newRoom = ref({
  name: '',
  yearMonth: ''
})

const formatDate = (row: any, column: any, cellValue: string) => {
  return new Date(cellValue).toLocaleDateString('ja-JP')
}

const createRoom = async () => {
  if (!newRoom.value.name || !newRoom.value.yearMonth) {
    ElMessage.error('部屋名と年月を入力してください')
    return
  }

  try {
    await roomStore.createRoom(newRoom.value)
    ElMessage.success('部屋を作成しました')
    showCreateDialog.value = false
    newRoom.value = { name: '', yearMonth: '' }
  } catch (error) {
    ElMessage.error('部屋の作成に失敗しました')
  }
}

onMounted(() => {
  roomStore.fetchRooms()
})
</script>

<style scoped>
.home {
  padding: 20px;
}

.header-section {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 20px;
}

.header-section h2 {
  margin: 0;
}
</style>
