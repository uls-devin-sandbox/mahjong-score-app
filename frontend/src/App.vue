<template>
  <div id="app">
    <el-container>
      <el-header>
        <div class="header-content">
          <h1>麻雀得点集計アプリ</h1>
          <el-menu
            :default-active="$route.path"
            mode="horizontal"
            router
            class="nav-menu"
          >
            <el-menu-item index="/">トップ</el-menu-item>
            <el-menu-item index="/players">プレイヤー一覧</el-menu-item>
            <el-menu-item index="/stats">年間成績</el-menu-item>
          </el-menu>
        </div>
      </el-header>
      <el-main>
        <router-view />
      </el-main>
    </el-container>
  </div>
</template>

<script setup lang="ts">
</script>

<style scoped>
.header-content {
  display: flex;
  justify-content: space-between;
  align-items: center;
  height: 100%;
}

.header-content h1 {
  margin: 0;
  color: #409eff;
}

.nav-menu {
  border-bottom: none;
}
</style>
