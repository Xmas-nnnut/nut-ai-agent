<template>
  <div class="chat-container">
    <!-- 聊天头部 -->
    <div class="chat-header">
      <button @click="goBack" class="back-btn">
        返回
      </button>
      <h2>💝 AI恋爱大师</h2>
      <p style="opacity: 0.8; margin-top: 5px;">专业的恋爱咨询师</p>
    </div>

    <!-- 聊天消息区域 -->
    <div class="chat-messages" ref="messagesContainer">
      <div v-for="(message, index) in messages" :key="index" :class="['message', message.type]">
        <!-- AI消息：头像在左，内容在右 -->
        <template v-if="message.type === 'ai'">
          <div class="message-avatar">
            💝
          </div>
          <div class="message-content markdown-content" v-html="renderMessageContent(message.content)">
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
          💝
        </div>
        <div class="message-content">
          <span>正在思考中...</span>
        </div>
      </div>
    </div>

    <!-- 输入区域 -->
    <div class="chat-input">
      <div class="input-group">
        <input 
          v-model="inputMessage" 
          @keyup.enter="sendMessage"
          placeholder="请输入你的恋爱问题..." 
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
import { connectLoveAppSSE, generateChatId } from '../api'
import { renderMarkdown } from '../utils/markdown'

export default {
  name: 'LoveApp',
  data() {
    return {
      messages: [],
      inputMessage: '',
      isLoading: false,
      chatId: '',
      currentEventSource: null
    }
  },
  mounted() {
    this.chatId = generateChatId()
    console.log('生成聊天室ID:', this.chatId)
    
    // 添加欢迎消息
    this.messages.push({
      type: 'ai',
      content: '你好！我是你的AI恋爱大师💝 有什么感情问题需要咨询吗？我会为你提供专业的建议~'
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
      let aiResponse = ''
      let hasCreatedAiMessage = false
      
      // 建立SSE连接
      this.currentEventSource = connectLoveAppSSE(
        message,
        this.chatId,
        (data) => {
          // 接收到数据
          if (data && data.trim()) {
            aiResponse += data
            
            // 第一次接收到数据时，关闭加载状态并创建AI消息
            if (!hasCreatedAiMessage) {
              this.isLoading = false
              hasCreatedAiMessage = true
              this.messages.push({
                type: 'ai',
                content: aiResponse
              })
            } else {
              // 更新最后一条AI消息
              this.messages[this.messages.length - 1].content = aiResponse
            }
            
            this.$nextTick(() => {
              this.scrollToBottom()
            })
          }
        },
        (error) => {
          // 处理错误
          console.error('连接错误:', error)
          this.isLoading = false
          
          // 只有在还没有创建AI消息时才添加错误消息
          if (!hasCreatedAiMessage) {
            this.messages.push({
              type: 'ai',
              content: '抱歉，连接出现问题，请稍后重试。'
            })
          }
        },
        () => {
          // 连接完成
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