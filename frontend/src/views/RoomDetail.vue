<template>
  <div class="room-detail" v-if="room">
    <div class="header-section">
      <div>
        <h2>{{ room.name }}</h2>
        <p>{{ room.yearMonth }}</p>
      </div>
      <div class="actions">
        <el-button @click="showPlayerDialog = true">
          <el-icon><User /></el-icon>
          プレイヤー追加
        </el-button>
        <el-button type="primary" @click="$router.push(`/rooms/${room.id}/games/new`)">
          <el-icon><Plus /></el-icon>
          対局記録
        </el-button>
      </div>
    </div>

    <el-row :gutter="20">
      <el-col :span="12">
        <el-card title="参加プレイヤー">
          <template #header>
            <span>参加プレイヤー ({{ room.players?.length || 0 }}人)</span>
          </template>
          <el-table :data="room.players" size="small">
            <el-table-column prop="name" label="名前" />
            <el-table-column prop="nickname" label="ニックネーム" />
          </el-table>
        </el-card>
      </el-col>
      <el-col :span="12">
        <el-card title="ルール設定">
          <template #header>
            <span>ルール設定</span>
            <el-button size="small" @click="showRuleDialog = true">編集</el-button>
          </template>
          <div v-if="room.ruleSetting">
            <p>ウマ: {{ room.ruleSetting.uma1st }}/{{ room.ruleSetting.uma2nd }}/{{ room.ruleSetting.uma3rd }}/{{ room.ruleSetting.uma4th }}</p>
            <p>オカ: {{ room.ruleSetting.oka }}</p>
            <p>トビ罰符: {{ room.ruleSetting.tobiPenalty }}</p>
          </div>
        </el-card>
      </el-col>
    </el-row>

    <el-card title="対局履歴" style="margin-top: 20px;">
      <el-table :data="gameStore.games" v-loading="gameStore.loading">
        <el-table-column prop="gameDate" label="対局日時" :formatter="formatDateTime" />
        <el-table-column prop="startingPlayerName" label="起家" />
        <el-table-column label="結果">
          <template #default="scope">
            <div v-for="result in scope.row.results" :key="result.playerId" class="result-row">
              {{ result.rank }}位: {{ result.playerName }} ({{ result.finalScore }})
              <el-tag v-if="result.isTobi" type="danger" size="small">トビ</el-tag>
            </div>
          </template>
        </el-table-column>
      </el-table>
    </el-card>

    <el-dialog v-model="showPlayerDialog" title="プレイヤー追加" width="400px">
      <el-select v-model="selectedPlayerId" placeholder="プレイヤーを選択" style="width: 100%">
        <el-option
          v-for="player in availablePlayers"
          :key="player.id"
          :label="player.name"
          :value="player.id"
        />
      </el-select>
      <template #footer>
        <el-button @click="showPlayerDialog = false">キャンセル</el-button>
        <el-button type="primary" @click="addPlayer">追加</el-button>
      </template>
    </el-dialog>

    <el-dialog v-model="showRuleDialog" title="ルール設定" width="500px">
      <el-form :model="ruleForm" label-width="100px">
        <el-form-item label="1位ウマ">
          <el-input-number v-model="ruleForm.uma1st" />
        </el-form-item>
        <el-form-item label="2位ウマ">
          <el-input-number v-model="ruleForm.uma2nd" />
        </el-form-item>
        <el-form-item label="3位ウマ">
          <el-input-number v-model="ruleForm.uma3rd" />
        </el-form-item>
        <el-form-item label="4位ウマ">
          <el-input-number v-model="ruleForm.uma4th" />
        </el-form-item>
        <el-form-item label="オカ">
          <el-input-number v-model="ruleForm.oka" />
        </el-form-item>
        <el-form-item label="トビ罰符">
          <el-input-number v-model="ruleForm.tobiPenalty" />
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="showRuleDialog = false">キャンセル</el-button>
        <el-button type="primary" @click="saveRules">保存</el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script setup lang="ts">
import { ref, onMounted, computed } from 'vue'
import { useRoute } from 'vue-router'
import { useRoomStore } from '@/stores/room'
import { usePlayerStore } from '@/stores/player'
import { useGameStore } from '@/stores/game'
import { ruleApi } from '@/api'
import { ElMessage } from 'element-plus'
import { Plus, User } from '@element-plus/icons-vue'

const route = useRoute()
const roomStore = useRoomStore()
const playerStore = usePlayerStore()
const gameStore = useGameStore()

const room = computed(() => roomStore.currentRoom)
const showPlayerDialog = ref(false)
const showRuleDialog = ref(false)
const selectedPlayerId = ref<number>()

const ruleForm = ref({
  uma1st: 20000,
  uma2nd: 10000,
  uma3rd: -10000,
  uma4th: -20000,
  oka: 25000,
  tobiPenalty: -30000
})

const availablePlayers = computed(() => {
  const roomPlayerIds = room.value?.players?.map(p => p.id) || []
  return playerStore.players.filter(p => !roomPlayerIds.includes(p.id))
})

const formatDateTime = (row: any, column: any, cellValue: string) => {
  return new Date(cellValue).toLocaleString('ja-JP')
}

const addPlayer = async () => {
  if (!selectedPlayerId.value || !room.value) return

  try {
    await roomStore.addPlayerToRoom(room.value.id, selectedPlayerId.value)
    ElMessage.success('プレイヤーを追加しました')
    showPlayerDialog.value = false
    selectedPlayerId.value = undefined
  } catch (error) {
    ElMessage.error('プレイヤーの追加に失敗しました')
  }
}

const saveRules = async () => {
  if (!room.value) return

  try {
    await ruleApi.createOrUpdate({
      roomId: room.value.id,
      ...ruleForm.value
    })
    ElMessage.success('ルール設定を保存しました')
    showRuleDialog.value = false
    await roomStore.fetchRoom(room.value.id)
  } catch (error) {
    ElMessage.error('ルール設定の保存に失敗しました')
  }
}

onMounted(async () => {
  const roomId = Number(route.params.id)
  await Promise.all([
    roomStore.fetchRoom(roomId),
    playerStore.fetchPlayers(),
    gameStore.fetchGamesByRoom(roomId)
  ])

  if (room.value?.ruleSetting) {
    ruleForm.value = { ...room.value.ruleSetting }
  }
})
</script>

<style scoped>
.room-detail {
  padding: 20px;
}

.header-section {
  display: flex;
  justify-content: space-between;
  align-items: flex-start;
  margin-bottom: 20px;
}

.header-section h2 {
  margin: 0;
}

.actions {
  display: flex;
  gap: 10px;
}

.result-row {
  margin: 2px 0;
  font-size: 12px;
}
</style>
