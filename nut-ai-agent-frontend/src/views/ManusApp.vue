<template>
  <div class="chat-container">
    <!-- 聊天头部 -->
    <div class="chat-header">
      <button @click="goBack" class="back-btn">
        返回
      </button>
      <h2>🤖 AI超级智能体</h2>
      <p style="opacity: 0.8; margin-top: 5px;">强大的AI助手</p>
    </div>

    <!-- 聊天消息区域 -->
    <div class="chat-messages" ref="messagesContainer">
      <div v-for="(message, index) in messages" :key="index" :class="['message', message.type]">
        <!-- AI消息：头像在左，内容在右 -->
        <template v-if="message.type === 'ai'">
          <div class="message-avatar">
            🤖
          </div>
          <div 
            class="message-content" 
            :class="{ 'markdown-content': !message.isThinking, 'thinking-indicator': message.isThinking }"
            v-html="message.isThinking ? message.content : renderMessageContent(message.content)"
          >
          </div>
        </template>
        <!-- 用户消息：内容在左，头像在右 -->
        <template v-else>
          <div class="message-content markdown-content" v-html="renderMessageContent(message.content)">
          </div>
          <div class="message-avatar">
            👤
          </div>
        </template>
      </div>
      
      <!-- 正在输入提示 -->
      <div v-if="isLoading" class="message ai">
        <div class="message-avatar">
          🤖
        </div>
        <div class="message-content thinking-indicator">
          <span>🤔 正在分析中...</span>
        </div>
      </div>
    </div>

    <!-- 输入区域 -->
    <div class="chat-input">
      <div class="input-group">
        <input 
          v-model="inputMessage" 
          @keyup.enter="sendMessage"
          placeholder="请输入你的问题..." 
          :disabled="isLoading"
        />
        <button 
          class="send-btn" 
          @click="sendMessage"
          :disabled="isLoading || !inputMessage.trim()"
        >
          发送
        </button>
      </div>
    </div>
  </div>
</template>

<script>
import { connectManusSSE } from '../api'
import { renderMarkdown } from '../utils/markdown'

export default {
  name: 'ManusApp',
  data() {
    return {
      messages: [],
      inputMessage: '',
      isLoading: false,
      currentEventSource: null
    }
  },
  mounted() {
    // 添加欢迎消息
    this.messages.push({
      type: 'ai',
      content: '你好！我是AI超级智能体🤖 我可以帮助你解决各种问题，请告诉我你需要什么帮助吧！'
    })
  },
  beforeUnmount() {
    // 组件销毁前关闭SSE连接
    if (this.currentEventSource) {
      this.currentEventSource.close()
    }
  },
  methods: {
    renderMessageContent(content) {
      return renderMarkdown(content)
    },
    
    goBack() {
      if (this.currentEventSource) {
        this.currentEventSource.close()
      }
      this.$router.push('/')
    },
    
    sendMessage() {
      if (!this.inputMessage.trim() || this.isLoading) return
      
      const message = this.inputMessage.trim()
      
      // 添加用户消息
      this.messages.push({
        type: 'user',
        content: message
      })
      
      // 清空输入框
      this.inputMessage = ''
      this.isLoading = true
      
      // 滚动到底部
      this.$nextTick(() => {
        this.scrollToBottom()
      })
      
      // 准备接收AI回复
      let stepCount = 0
      let thinkingMessageIndex = -1
      
      // 建立SSE连接
      this.currentEventSource = connectManusSSE(
        message,
        (data) => {
          // 接收到数据
          if (data && data.trim()) {
            // 第一次接收到数据时，关闭加载状态
            if (stepCount === 0) {
              this.isLoading = false
            }
            
            // 如果有思考提示消息，先移除它
            if (thinkingMessageIndex >= 0) {
              this.messages.splice(thinkingMessageIndex, 1)
              thinkingMessageIndex = -1
            }
            
            // 每次收到数据都创建一个新的步骤气泡
            stepCount++
            this.messages.push({
              type: 'ai',
              content: data.trim()
            })
            
            // 添加思考提示（除非这是最后一个响应）
            this.messages.push({
              type: 'ai',
              content: '🤔 智能体正在思考下一步...',
              isThinking: true
            })
            thinkingMessageIndex = this.messages.length - 1
            
            this.$nextTick(() => {
              this.scrollToBottom()
            })
          }
        },
        (error) => {
          // 处理错误
          console.error('连接错误:', error)
          this.isLoading = false
          
          // 只有在还没有收到任何步骤时才添加错误消息
          if (stepCount === 0) {
            this.messages.push({
              type: 'ai',
              content: '抱歉，连接出现问题，请稍后重试。'
            })
          }
        },
        () => {
          // 连接完成，移除最后的思考提示
          if (thinkingMessageIndex >= 0) {
            this.messages.splice(thinkingMessageIndex, 1)
          }
          this.isLoading = false
          this.currentEventSource = null
        }
      )
    },
    
    scrollToBottom() {
      const container = this.$refs.messagesContainer
      if (container) {
        container.scrollTop = container.scrollHeight
      }
    }
  }
}
</script> 