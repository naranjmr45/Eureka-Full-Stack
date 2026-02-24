const { defineConfig } = require('@vue/cli-service')

module.exports = defineConfig({
  transpileDependencies: true,
  devServer: {
    client: {
      // Explicitly use wss for secure connection in Codespaces
      webSocketURL: 'wss://0.0.0.0/ws', 
    },
    allowedHosts: 'all',
  }
})