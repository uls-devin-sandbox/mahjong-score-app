import { createRouter, createWebHistory } from 'vue-router'
import Home from '@/views/Home.vue'
import RoomDetail from '@/views/RoomDetail.vue'
import GameInput from '@/views/GameInput.vue'
import Players from '@/views/Players.vue'
import PlayerDetail from '@/views/PlayerDetail.vue'
import Stats from '@/views/Stats.vue'

const router = createRouter({
  history: createWebHistory(),
  routes: [
    {
      path: '/',
      name: 'Home',
      component: Home
    },
    {
      path: '/rooms/:id',
      name: 'RoomDetail',
      component: RoomDetail,
      props: true
    },
    {
      path: '/rooms/:roomId/games/new',
      name: 'GameInput',
      component: GameInput,
      props: true
    },
    {
      path: '/players',
      name: 'Players',
      component: Players
    },
    {
      path: '/players/:id',
      name: 'PlayerDetail',
      component: PlayerDetail,
      props: true
    },
    {
      path: '/stats',
      name: 'Stats',
      component: Stats
    }
  ]
})

export default router
