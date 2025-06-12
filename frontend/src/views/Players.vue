<template>
  <div class="players">
    <div class="header-section">
      <h2>プレイヤー一覧</h2>
      <el-button type="primary" @click="showCreateDialog = true">
        <el-icon><Plus /></el-icon>
        新しいプレイヤーを追加
      </el-button>
    </div>

    <el-table :data="playerStore.players" v-loading="playerStore.loading" stripe>
      <el-table-column prop="name" label="名前" />
      <el-table-column prop="nickname" label="ニックネーム" />
      <el-table-column prop="createdAt" label="登録日" :formatter="formatDate" />
      <el-table-column label="操作" width="120">
        <template #default="scope">
          <el-button 
            type="primary" 
            size="small" 
            @click="$router.push(`/players/${scope.row.id}`)"
          >
            詳細
          </el-button>
        </template>
      </el-table-column>
    </el-table>

    <el-dialog v-model="showCreateDialog" title="新しいプレイヤーを追加" width="400px">
      <el-form :model="newPlayer" label-width="100px">
        <el-form-item label="名前" required>
          <el-input v-model="newPlayer.name" placeholder="プレイヤー名を入力" />
        </el-form-item>
        <el-form-item label="ニックネーム">
          <el-input v-model="newPlayer.nickname" placeholder="ニックネームを入力（任意）" />
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="showCreateDialog = false">キャンセル</el-button>
        <el-button type="primary" @click="createPlayer">追加</el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script setup lang="ts">
import { ref, onMounted } from 'vue'
import { usePlayerStore } from '@/stores/player'
import { ElMessage } from 'element-plus'
import { Plus } from '@element-plus/icons-vue'

const playerStore = usePlayerStore()
const showCreateDialog = ref(false)
const newPlayer = ref({
  name: '',
  nickname: ''
})

const formatDate = (row: any, column: any, cellValue: string) => {
  return new Date(cellValue).toLocaleDateString('ja-JP')
}

const createPlayer = async () => {
  if (!newPlayer.value.name) {
    ElMessage.error('名前を入力してください')
    return
  }

  try {
    await playerStore.createPlayer(newPlayer.value)
    ElMessage.success('プレイヤーを追加しました')
    showCreateDialog.value = false
    newPlayer.value = { name: '', nickname: '' }
  } catch (error) {
    ElMessage.error('プレイヤーの追加に失敗しました')
  }
}

onMounted(() => {
  playerStore.fetchPlayers()
})
</script>

<style scoped>
.players {
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
