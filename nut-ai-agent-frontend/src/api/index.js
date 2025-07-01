import axios from 'axios'

// 配置基础URL
const API_BASE_URL = 'http://localhost:8123/api'

// 创建axios实例
const apiClient = axios.create({
  baseURL: API_BASE_URL,
  timeout: 30000,
  headers: {
    'Content-Type': 'application/json'
  }
})

// 生成聊天室ID
export function generateChatId() {
  return 'chat_' + Date.now() + '_' + Math.random().toString(36).substr(2, 9)
}

// AI恋爱大师SSE连接
export function connectLoveAppSSE(message, chatId, onMessage, onError, onComplete) {
  const url = `${API_BASE_URL}/ai/love_app/chat/sse?message=${encodeURIComponent(message)}&chatId=${encodeURIComponent(chatId)}`
  
  const eventSource = new EventSource(url)
  let hasReceivedData = false
  let isClosed = false
  
  eventSource.onmessage = function(event) {
    hasReceivedData = true
    if (onMessage && !isClosed) {
      onMessage(event.data)
    }
  }
  
  eventSource.onerror = function(error) {
    console.error('SSE连接错误:', error)
    if (!isClosed) {
      isClosed = true
      eventSource.close()
      
      // 只有在连接建立后没有接收到数据时才报告错误
      if (!hasReceivedData && onError) {
        onError(error)
      } else if (onComplete) {
        // 如果已经接收到数据，将错误视为正常结束
        onComplete()
      }
    }
  }
  
  eventSource.addEventListener('end', function() {
    if (!isClosed) {
      isClosed = true
      eventSource.close()
      if (onComplete) {
        onComplete()
      }
    }
  })
  
  return eventSource
}

// AI超级智能体SSE连接
export function connectManusSSE(message, onMessage, onError, onComplete) {
  const url = `${API_BASE_URL}/ai/manus/chat?message=${encodeURIComponent(message)}`
  
  const eventSource = new EventSource(url)
  let hasReceivedData = false
  let isClosed = false
  
  eventSource.onmessage = function(event) {
    hasReceivedData = true
    if (onMessage && !isClosed) {
      onMessage(event.data)
    }
  }
  
  eventSource.onerror = function(error) {
    console.error('SSE连接错误:', error)
    if (!isClosed) {
      isClosed = true
      eventSource.close()
      
      // 只有在连接建立后没有接收到数据时才报告错误
      if (!hasReceivedData && onError) {
        onError(error)
      } else if (onComplete) {
        // 如果已经接收到数据，将错误视为正常结束
        onComplete()
      }
    }
  }
  
  eventSource.addEventListener('end', function() {
    if (!isClosed) {
      isClosed = true
      eventSource.close()
      if (onComplete) {
        onComplete()
      }
    }
  })
  
  return eventSource
}

export default apiClient 