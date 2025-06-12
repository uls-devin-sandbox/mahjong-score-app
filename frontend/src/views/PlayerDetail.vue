<template>
  <div class="player-detail" v-if="player">
    <h2>{{ player.name }}の成績</h2>
    <p v-if="player.nickname">ニックネーム: {{ player.nickname }}</p>

    <el-row :gutter="20">
      <el-col :span="8">
        <el-card title="基本統計">
          <div class="stat-item">
            <span class="stat-label">参加回数:</span>
            <span class="stat-value">{{ stats.totalGames }}回</span>
          </div>
          <div class="stat-item">
            <span class="stat-label">平均順位:</span>
            <span class="stat-value">{{ stats.averageRank.toFixed(2) }}</span>
          </div>
          <div class="stat-item">
            <span class="stat-label">平均得点:</span>
            <span class="stat-value">{{ stats.averageScore.toFixed(0) }}</span>
          </div>
        </el-card>
      </el-col>
      <el-col :span="8">
        <el-card title="順位別成績">
          <div class="stat-item">
            <span class="stat-label">1位:</span>
            <span class="stat-value">{{ stats.rank1 }}回 ({{ ((stats.rank1 / stats.totalGames) * 100).toFixed(1) }}%)</span>
          </div>
          <div class="stat-item">
            <span class="stat-label">2位:</span>
            <span class="stat-value">{{ stats.rank2 }}回 ({{ ((stats.rank2 / stats.totalGames) * 100).toFixed(1) }}%)</span>
          </div>
          <div class="stat-item">
            <span class="stat-label">3位:</span>
            <span class="stat-value">{{ stats.rank3 }}回 ({{ ((stats.rank3 / stats.totalGames) * 100).toFixed(1) }}%)</span>
          </div>
          <div class="stat-item">
            <span class="stat-label">4位:</span>
            <span class="stat-value">{{ stats.rank4 }}回 ({{ ((stats.rank4 / stats.totalGames) * 100).toFixed(1) }}%)</span>
          </div>
        </el-card>
      </el-col>
      <el-col :span="8">
        <el-card title="その他">
          <div class="stat-item">
            <span class="stat-label">トップ率:</span>
            <span class="stat-value">{{ ((stats.rank1 / stats.totalGames) * 100).toFixed(1) }}%</span>
          </div>
          <div class="stat-item">
            <span class="stat-label">連対率:</span>
            <span class="stat-value">{{ (((stats.rank1 + stats.rank2) / stats.totalGames) * 100).toFixed(1) }}%</span>
          </div>
          <div class="stat-item">
            <span class="stat-label">トビ回数:</span>
            <span class="stat-value">{{ stats.tobiCount }}回</span>
          </div>
        </el-card>
      </el-col>
    </el-row>

    <el-card title="対局履歴" style="margin-top: 20px;">
      <el-table :data="gameResults" v-loading="loading">
        <el-table-column prop="gameDate" label="対局日時" :formatter="formatDateTime" />
        <el-table-column prop="rank" label="順位" />
        <el-table-column prop="rawScore" label="素点" />
        <el-table-column prop="finalScore" label="最終得点" />
        <el-table-column label="トビ">
          <template #default="scope">
            <el-tag v-if="scope.row.isTobi" type="danger" size="small">トビ</el-tag>
          </template>
        </el-table-column>
      </el-table>
    </el-card>
  </div>
</template>

<script setup lang="ts">
import { ref, onMounted, computed } from 'vue'
import { useRoute } from 'vue-router'
import { usePlayerStore } from '@/stores/player'
import { gameApi, type GameResult } from '@/api'

const route = useRoute()
const playerStore = usePlayerStore()

const player = computed(() => playerStore.currentPlayer)
const gameResults = ref<GameResult[]>([])
const loading = ref(false)

const stats = computed(() => {
  if (!gameResults.value.length) {
    return {
      totalGames: 0,
      averageRank: 0,
      averageScore: 0,
      rank1: 0,
      rank2: 0,
      rank3: 0,
      rank4: 0,
      tobiCount: 0
    }
  }

  const results = gameResults.value
  const totalGames = results.length
  const averageRank = results.reduce((sum, r) => sum + r.rank, 0) / totalGames
  const averageScore = results.reduce((sum, r) => sum + r.finalScore, 0) / totalGames
  
  const rankCounts = results.reduce((counts, r) => {
    counts[`rank${r.rank}`] = (counts[`rank${r.rank}`] || 0) + 1
    return counts
  }, {} as Record<string, number>)

  const tobiCount = results.filter(r => r.isTobi).length

  return {
    totalGames,
    averageRank,
    averageScore,
    rank1: rankCounts.rank1 || 0,
    rank2: rankCounts.rank2 || 0,
    rank3: rankCounts.rank3 || 0,
    rank4: rankCounts.rank4 || 0,
    tobiCount
  }
})

const formatDateTime = (row: any, column: any, cellValue: string) => {
  return new Date(cellValue).toLocaleString('ja-JP')
}

onMounted(async () => {
  const playerId = Number(route.params.id)
  await playerStore.fetchPlayer(playerId)
  
  loading.value = true
  try {
    const response = await gameApi.getByPlayerId(playerId)
    gameResults.value = response.data
  } catch (error) {
    console.error('対局履歴の取得に失敗しました:', error)
  } finally {
    loading.value = false
  }
})
</script>

<style scoped>
.player-detail {
  padding: 20px;
}

.stat-item {
  display: flex;
  justify-content: space-between;
  margin-bottom: 10px;
}

.stat-label {
  font-weight: bold;
}

.stat-value {
  color: #409eff;
}
</style>
