<template>
  <div class="stats">
    <h2>年間成績</h2>
    
    <el-table :data="playerStats" v-loading="loading" stripe>
      <el-table-column prop="playerName" label="プレイヤー名" />
      <el-table-column prop="totalGames" label="参加回数" />
      <el-table-column prop="averageRank" label="平均順位" :formatter="formatRank" />
      <el-table-column prop="averageScore" label="平均得点" :formatter="formatScore" />
      <el-table-column prop="topRate" label="トップ率" :formatter="formatPercentage" />
      <el-table-column prop="rentalRate" label="連対率" :formatter="formatPercentage" />
      <el-table-column prop="totalScore" label="累計得点" :formatter="formatScore" />
    </el-table>
  </div>
</template>

<script setup lang="ts">
import { ref, onMounted } from 'vue'
import { playerApi, gameApi } from '@/api'

const loading = ref(false)
const playerStats = ref<any[]>([])

const formatRank = (row: any, column: any, cellValue: number) => {
  return cellValue.toFixed(2)
}

const formatScore = (row: any, column: any, cellValue: number) => {
  return cellValue.toFixed(0)
}

const formatPercentage = (row: any, column: any, cellValue: number) => {
  return `${(cellValue * 100).toFixed(1)}%`
}

const fetchStats = async () => {
  loading.value = true
  try {
    const playersResponse = await playerApi.getAll()
    const players = playersResponse.data

    const stats = await Promise.all(
      players.map(async (player) => {
        try {
          const resultsResponse = await gameApi.getByPlayerId(player.id)
          const results = resultsResponse.data

          if (results.length === 0) {
            return {
              playerName: player.name,
              totalGames: 0,
              averageRank: 0,
              averageScore: 0,
              topRate: 0,
              rentalRate: 0,
              totalScore: 0
            }
          }

          const totalGames = results.length
          const averageRank = results.reduce((sum, r) => sum + r.rank, 0) / totalGames
          const averageScore = results.reduce((sum, r) => sum + r.finalScore, 0) / totalGames
          const totalScore = results.reduce((sum, r) => sum + r.finalScore, 0)
          
          const rank1Count = results.filter(r => r.rank === 1).length
          const rank2Count = results.filter(r => r.rank === 2).length
          
          const topRate = rank1Count / totalGames
          const rentalRate = (rank1Count + rank2Count) / totalGames

          return {
            playerName: player.name,
            totalGames,
            averageRank,
            averageScore,
            topRate,
            rentalRate,
            totalScore
          }
        } catch (error) {
          console.error(`プレイヤー ${player.name} の成績取得に失敗:`, error)
          return {
            playerName: player.name,
            totalGames: 0,
            averageRank: 0,
            averageScore: 0,
            topRate: 0,
            rentalRate: 0,
            totalScore: 0
          }
        }
      })
    )

    playerStats.value = stats.sort((a, b) => b.totalScore - a.totalScore)
  } catch (error) {
    console.error('統計データの取得に失敗しました:', error)
  } finally {
    loading.value = false
  }
}

onMounted(() => {
  fetchStats()
})
</script>

<style scoped>
.stats {
  padding: 20px;
}
</style>
