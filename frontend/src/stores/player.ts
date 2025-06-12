import { defineStore } from 'pinia'
import { ref } from 'vue'
import { playerApi, type Player } from '@/api'

export const usePlayerStore = defineStore('player', () => {
  const players = ref<Player[]>([])
  const currentPlayer = ref<Player | null>(null)
  const loading = ref(false)

  const fetchPlayers = async () => {
    loading.value = true
    try {
      const response = await playerApi.getAll()
      players.value = response.data
    } catch (error) {
      console.error('プレイヤー一覧の取得に失敗しました:', error)
    } finally {
      loading.value = false
    }
  }

  const fetchPlayer = async (id: number) => {
    loading.value = true
    try {
      const response = await playerApi.getById(id)
      currentPlayer.value = response.data
      return response.data
    } catch (error) {
      console.error('プレイヤーの取得に失敗しました:', error)
      throw error
    } finally {
      loading.value = false
    }
  }

  const createPlayer = async (player: Omit<Player, 'id' | 'createdAt'>) => {
    try {
      const response = await playerApi.create(player)
      players.value.push(response.data)
      return response.data
    } catch (error) {
      console.error('プレイヤーの作成に失敗しました:', error)
      throw error
    }
  }

  return {
    players,
    currentPlayer,
    loading,
    fetchPlayers,
    fetchPlayer,
    createPlayer
  }
})
