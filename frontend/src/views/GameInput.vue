<template>
  <div class="game-input">
    <h2>対局記録</h2>
    
    <el-form :model="gameForm" label-width="120px">
      <el-form-item label="対局日時">
        <el-date-picker
          v-model="gameForm.gameDate"
          type="datetime"
          placeholder="対局日時を選択"
          format="YYYY-MM-DD HH:mm"
          value-format="YYYY-MM-DDTHH:mm:ss"
        />
      </el-form-item>
      
      <el-form-item label="起家">
        <el-select v-model="gameForm.startingPlayerId" placeholder="起家を選択">
          <el-option
            v-for="player in roomPlayers"
            :key="player.id"
            :label="player.name"
            :value="player.id"
          />
        </el-select>
      </el-form-item>

      <el-divider>得点入力</el-divider>

      <div v-for="(result, index) in gameForm.results" :key="index" class="player-result">
        <el-row :gutter="20">
          <el-col :span="6">
            <el-form-item :label="`プレイヤー${index + 1}`">
              <el-select v-model="result.playerId" placeholder="プレイヤーを選択">
                <el-option
                  v-for="player in roomPlayers"
                  :key="player.id"
                  :label="player.name"
                  :value="player.id"
                />
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="6">
            <el-form-item label="素点">
              <el-input-number v-model="result.rawScore" :min="0" :max="100000" />
            </el-form-item>
          </el-col>
          <el-col :span="6">
            <el-form-item label="トビ">
              <el-switch v-model="result.isTobi" />
            </el-form-item>
          </el-col>
          <el-col :span="6">
            <el-form-item label="最終得点">
              <el-input :value="calculateFinalScore(result)" readonly />
            </el-form-item>
          </el-col>
        </el-row>
      </div>

      <el-form-item>
        <el-button type="primary" @click="submitGame" :loading="loading">
          対局を記録
        </el-button>
        <el-button @click="$router.back()">キャンセル</el-button>
      </el-form-item>
    </el-form>
  </div>
</template>

<script setup lang="ts">
import { ref, onMounted, computed } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import { useRoomStore } from '@/stores/room'
import { useGameStore } from '@/stores/game'
import { ElMessage } from 'element-plus'

const route = useRoute()
const router = useRouter()
const roomStore = useRoomStore()
const gameStore = useGameStore()

const loading = ref(false)
const roomId = Number(route.params.roomId)

const gameForm = ref({
  gameDate: new Date().toISOString().slice(0, 19),
  startingPlayerId: null as number | null,
  results: [
    { playerId: null as number | null, rawScore: 25000, isTobi: false },
    { playerId: null as number | null, rawScore: 25000, isTobi: false },
    { playerId: null as number | null, rawScore: 25000, isTobi: false },
    { playerId: null as number | null, rawScore: 25000, isTobi: false }
  ]
})

const roomPlayers = computed(() => roomStore.currentRoom?.players || [])
const ruleSetting = computed(() => roomStore.currentRoom?.ruleSetting)

const calculateFinalScore = (result: any) => {
  if (!ruleSetting.value) return 0
  
  const results = gameForm.value.results.filter(r => r.playerId && r.rawScore !== null)
  if (results.length !== 4) return 0

  const sortedResults = [...results].sort((a, b) => b.rawScore - a.rawScore)
  const playerIndex = sortedResults.findIndex(r => r.playerId === result.playerId)
  
  if (playerIndex === -1) return 0

  const rank = playerIndex + 1
  let finalScore = (result.rawScore - ruleSetting.value.oka) / 1000

  switch (rank) {
    case 1:
      finalScore += ruleSetting.value.uma1st / 1000
      break
    case 2:
      finalScore += ruleSetting.value.uma2nd / 1000
      break
    case 3:
      finalScore += ruleSetting.value.uma3rd / 1000
      break
    case 4:
      finalScore += ruleSetting.value.uma4th / 1000
      break
  }

  if (result.isTobi) {
    finalScore += ruleSetting.value.tobiPenalty / 1000
  }

  return Math.round(finalScore * 1000)
}

const submitGame = async () => {
  if (!gameForm.value.startingPlayerId) {
    ElMessage.error('起家を選択してください')
    return
  }

  const validResults = gameForm.value.results.filter(r => r.playerId)
  if (validResults.length !== 4) {
    ElMessage.error('4人のプレイヤーを選択してください')
    return
  }

  const playerIds = validResults.map(r => r.playerId)
  if (new Set(playerIds).size !== 4) {
    ElMessage.error('同じプレイヤーが重複しています')
    return
  }

  loading.value = true
  try {
    await gameStore.createGame({
      roomId,
      gameDate: gameForm.value.gameDate,
      startingPlayerId: gameForm.value.startingPlayerId,
      results: validResults.map(r => ({
        playerId: r.playerId!,
        rawScore: r.rawScore,
        isTobi: r.isTobi
      }))
    })

    ElMessage.success('対局を記録しました')
    router.push(`/rooms/${roomId}`)
  } catch (error) {
    ElMessage.error('対局の記録に失敗しました')
  } finally {
    loading.value = false
  }
}

onMounted(async () => {
  await roomStore.fetchRoom(roomId)
})
</script>

<style scoped>
.game-input {
  padding: 20px;
  max-width: 800px;
}

.player-result {
  margin-bottom: 20px;
  padding: 15px;
  border: 1px solid #ebeef5;
  border-radius: 4px;
}
</style>
