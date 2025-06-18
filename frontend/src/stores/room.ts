import { defineStore } from 'pinia'
import { ref } from 'vue'
import { roomApi, type Room } from '@/api'

export const useRoomStore = defineStore('room', () => {
  const rooms = ref<Room[]>([])
  const currentRoom = ref<Room | null>(null)
  const loading = ref(false)

  const fetchRooms = async () => {
    loading.value = true
    try {
      const response = await roomApi.getAll()
      rooms.value = response.data
    } catch (error) {
      console.error('部屋一覧の取得に失敗しました:', error)
    } finally {
      loading.value = false
    }
  }

  const fetchRoom = async (id: number) => {
    loading.value = true
    try {
      const response = await roomApi.getById(id)
      currentRoom.value = response.data
      return response.data
    } catch (error) {
      console.error('部屋の取得に失敗しました:', error)
      throw error
    } finally {
      loading.value = false
    }
  }

  const createRoom = async (room: Omit<Room, 'id' | 'createdAt'>) => {
    try {
      const response = await roomApi.create(room)
      rooms.value.unshift(response.data)
      return response.data
    } catch (error) {
      console.error('部屋の作成に失敗しました:', error)
      throw error
    }
  }

  const addPlayerToRoom = async (roomId: number, playerId: number) => {
    try {
      await roomApi.addPlayer(roomId, playerId)
      if (currentRoom.value && currentRoom.value.id === roomId) {
        await fetchRoom(roomId)
      }
    } catch (error) {
      console.error('プレイヤーの追加に失敗しました:', error)
      throw error
    }
  }

  return {
    rooms,
    currentRoom,
    loading,
    fetchRooms,
    fetchRoom,
    createRoom,
    addPlayerToRoom
  }
})
