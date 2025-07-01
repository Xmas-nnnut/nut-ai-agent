<template>
  <div class="home-container">
    <h1 class="home-title">AI智能助手平台</h1>
    <p style="font-size: 1.2rem; margin-bottom: 2rem; opacity: 0.9;">
      选择你想要使用的AI应用
    </p>
    
    <div class="app-cards">
      <div class="app-card" @click="navigateToLoveApp">
        <h3>💝 AI恋爱大师</h3>
        <p>专业的恋爱咨询师，为你提供贴心的情感建议和恋爱技巧，让你在爱情路上更加自信。</p>
      </div>
      
      <div class="app-card" @click="navigateToManusApp">
        <h3>🤖 AI超级智能体</h3>
        <p>强大的AI助手，可以帮助你解决各种问题，提供专业的建议和解决方案。</p>
      </div>
    </div>
  </div>
</template>

<script>
export default {
  name: 'Home',
  methods: {
    navigateToLoveApp() {
      this.$router.push('/love-app')
    },
    navigateToManusApp() {
      this.$router.push('/manus-app')
    }
  }
}
</script> 