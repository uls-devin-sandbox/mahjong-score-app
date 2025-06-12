import { defineStore } from 'pinia'
import { ref } from 'vue'
import { gameApi, type Game, type CreateGameRequest } from '@/api'

export const useGameStore = defineStore('game', () => {
  const games = ref<Game[]>([])
  const currentGame = ref<Game | null>(null)
  const loading = ref(false)

  const fetchGamesByRoom = async (roomId: number) => {
    loading.value = true
    try {
      const response = await gameApi.getByRoomId(roomId)
      games.value = response.data
    } catch (error) {
      console.error('対局一覧の取得に失敗しました:', error)
    } finally {
      loading.value = false
    }
  }

  const createGame = async (gameRequest: CreateGameRequest) => {
    try {
      const response = await gameApi.create(gameRequest)
      games.value.unshift(response.data)
      return response.data
    } catch (error) {
      console.error('対局の記録に失敗しました:', error)
      throw error
    }
  }

  return {
    games,
    currentGame,
    loading,
    fetchGamesByRoom,
    createGame
  }
})
