<template>
  <div class="knowledge-app">
    <!-- 顶部导航栏 -->
    <header class="app-header">
      <div class="header-content">
        <div class="logo">
          <el-icon size="24"><Document /></el-icon>
          <span class="logo-text">智能知识库问答系统</span>
        </div>
        <div class="header-actions">
          <el-button link class="header-btn">
            <el-icon><User /></el-icon>
            我的账号
          </el-button>
        </div>
      </div>
    </header>

    <!-- 主体内容 -->
    <div class="main-content">
      <!-- 左侧边栏 -->
      <div class="sidebar">
        <!-- 侧边栏分类 -->
        <div class="sidebar-tabs">
          <el-button 
            type="default" 
            :class="{ active: activeTab === 'all' }"
            @click="activeTab = 'all'"
            class="tab-btn"
          >
            <el-icon><FolderOpened /></el-icon>
            全部文档
          </el-button>
          <el-button 
            type="default" 
            :class="{ active: activeTab === 'recent' }"
            @click="activeTab = 'recent'"
            class="tab-btn"
          >
            <el-icon><Clock /></el-icon>
            最近使用
          </el-button>
          <el-button 
            type="default" 
            :class="{ active: activeTab === 'star' }"
            @click="activeTab = 'star'"
            class="tab-btn"
          >
            <el-icon><Star /></el-icon>
            我的收藏
          </el-button>
        </div>

        <!-- 上传区域 -->
          <el-upload
            ref="uploadRef"
            drag
            class="upload-area"
            :auto-upload="false"
            :on-change="handleFileChange"
            accept=".txt,.docx,.pdf"
            :limit="1"
            :file-list="fileList"
            :disabled="uploading"
           >
            <el-icon class="el-icon--upload" size="24"><UploadFilled /></el-icon>
            <div class="el-upload__text">
              点击或拖拽上传文档
            </div>
            <div class="el-upload__tip">
              支持 .txt / .docx / .pdf 格式，单个文件 ≤ 10MB
            </div>
          </el-upload>

        <!-- 核心修复：只有文档列表为空时，才显示「暂无文档」 -->
  <div class="empty-tip" v-if="docList.length === 0">
    <el-icon size="48" color="#C0C6D0"><Document /></el-icon>
    <div class="empty-text">暂无文档</div>
    <div class="empty-desc">上传文档后即可进行智能问答</div>
  </div>

  <!-- 核心修复：文档列表有数据时，循环渲染 -->
  <div class="doc-list" v-else>
    <div
      v-for="doc in docList"
      :key="doc.id"
      class="doc-item"
      :class="{ active: selectedDoc?.id === doc.id }"
      @click="selectDoc(doc)"
    >
      <div class="doc-icon">
        <el-icon color="#409EFF"><Document /></el-icon>
      </div>
      <div class="doc-info">
        <div class="doc-name" :title="doc.fileName">{{ doc.fileName }}</div>
        <div class="doc-meta">
          <span>{{ ((doc.fileSize || 0) / 1024).toFixed(2) }} KB</span>
          <span>{{ new Date(doc.createTime).toLocaleDateString() }}</span>
        </div>
      </div>
      <!-- 改回 type="text" 消除警告 -->
      <el-button 
        type="text" 
        icon="Delete" 
        class="delete-btn"
        @click.stop="handleDelete(doc.id, doc.fileName)"
        size="small"
      >
      </el-button>
    </div>
  </div>
</div>

      <!-- 右侧聊天/文档内容面板 -->
      <div class="chat-panel">
        <!-- 文档信息卡片 + 切换标签 -->
        <div class="doc-info-card" v-if="selectedDoc">
          <div class="doc-info-header">
            <el-icon><Document /></el-icon>
            <span class="doc-title">{{ selectedDoc.fileName || '未命名文档' }}</span>
            <el-tag size="small" type="success">已解析</el-tag>
          </div>
          <div class="doc-info-meta">
            <span>文件大小：{{ ((selectedDoc.fileSize || 0) / 1024).toFixed(2) }} KB</span>
            <span>上传时间：{{ formatTime(selectedDoc.createTime) }}</span>
            <span>字数：{{ selectedDoc.content ? selectedDoc.content.replace(/\s/g, '').length : '--' }}</span>
          </div>
          <!-- 问答/内容切换标签 -->
          <div class="panel-tabs">
            <el-button 
              type="default" 
              :class="{ active: rightPanelTab === 'chat' }"
              @click="rightPanelTab = 'chat'"
              class="panel-tab-btn"
            >
              智能问答
            </el-button>
            <el-button 
              type="default" 
              :class="{ active: rightPanelTab === 'content' }"
              @click="rightPanelTab = 'content'"
              class="panel-tab-btn"
            >
              文档内容
            </el-button>
          </div>
        </div>

        <!-- 核心内容区 -->
        <div class="panel-content">
          <!-- 未选择文档的空状态 -->
          <div v-if="!selectedDoc" class="no-doc-tip">
            <div class="no-doc-illustration">
              <el-icon size="64" color="#C0C6D0"><ChatDotRound /></el-icon>
            </div>
            <div class="no-doc-text">请先从左侧选择文档</div>
            <div class="no-doc-desc">选择文档后即可查看内容或进行智能问答</div>
          </div>

          <!-- 智能问答面板 -->
          <div v-else-if="rightPanelTab === 'chat'" class="chat-content">
            <div class="chat-messages">
              <!-- 欢迎消息 -->
              <div class="message assistant" v-if="messages.length === 0">
                <div class="message-avatar">
                  <el-icon><Avatar /></el-icon>
                </div>
                <div class="message-content">
                  你好！我已加载文档《{{ selectedDoc.fileName || '未命名文档' }}》，有什么问题可以尽管问我 😊
                </div>
              </div>

              <!-- 聊天消息列表 -->
              <div
                v-for="(msg, index) in messages"
                :key="index"
                class="message"
                :class="msg.role"
              >
                <div class="message-avatar">
                  <el-icon v-if="msg.role === 'assistant'"><Avatar /></el-icon>
                  <el-icon v-else><User /></el-icon>
                </div>
                <div class="message-content" v-html="renderMarkdown(msg.content)"></div>
              </div>

              <!-- 加载中的AI回复 -->
              <div class="message assistant typing" v-if="typing">
                <div class="message-avatar">
                  <el-icon><Avatar /></el-icon>
                </div>
                <div class="message-content">
                  <div class="typing-dots">
                    <span></span><span></span><span></span>
                  </div>
                </div>
              </div>
            </div>
          </div>

          <!-- 文档内容面板 -->
          <div v-else-if="rightPanelTab === 'content'" class="doc-content">
            <div class="doc-content-wrapper">
              <div v-if="!selectedDoc.content" class="no-content-tip">
                <el-icon size="48" color="#C0C6D0"><DocumentRemove /></el-icon>
                <div class="no-content-text">暂无文档内容</div>
                <div class="no-content-desc">请重新上传文档，或检查文档解析是否正常</div>
              </div>
              <pre v-else class="doc-text">{{ selectedDoc.content }}</pre>
            </div>
          </div>
        </div>

        <!-- 底部输入框 -->
        <div class="chat-input-wrapper" v-if="selectedDoc && rightPanelTab === 'chat'">
          <div class="input-container">
            <el-input
              v-model="inputText"
              placeholder="输入你的问题，按回车或点击发送按钮提问..."
              type="textarea"
              :rows="3"
              resize="none"
              class="custom-input"
            />
            <div class="input-actions">
              <el-button 
                type="primary" 
                @click="sendMessage" 
                :disabled="!inputText.trim() || typing"
                class="send-btn"
                size="large"
              >
                <el-icon><ArrowRight /></el-icon>
                发送
              </el-button>
            </div>
            <div class="input-tip">
              <span>支持 Markdown 格式，可输入代码、列表等</span>
              <span v-if="inputText.length > 0">{{ inputText.length }} / 2000</span>
            </div>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, computed, onMounted, onUnmounted, getCurrentInstance } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import {
  FolderOpened, Clock, Star, StarFilled, UploadFilled,
  DocumentRemove, Document, Avatar, User, ArrowRight, ChatDotRound, Delete
} from '@element-plus/icons-vue'
import axios from 'axios'

// Axios配置
axios.defaults.baseURL = 'http://localhost:8080/api'
axios.defaults.withCredentials = true
axios.defaults.timeout = 30000

// 新增：el-upload组件实例，用于手动控制上传
const uploadRef = ref(null)

// 工具函数
const formatTime = (time) => {
  if (!time) return ''
  const date = new Date(time)
  return isNaN(date.getTime()) 
    ? '' 
    : `${date.getFullYear()}-${(date.getMonth() + 1).toString().padStart(2, '0')}-${date.getDate().toString().padStart(2, '0')} ${date.getHours().toString().padStart(2, '0')}:${date.getMinutes().toString().padStart(2, '0')}`
}

const { proxy } = getCurrentInstance()
const renderMarkdown = (content) => {
  if (!content) return ''
  return proxy?.$marked ? proxy.$marked(content) : content.replace(/\n/g, '<br>')
}

// 状态管理
const fileList = ref([])
// 【核心修复】初始值强制为数组
const docList = ref([])
const selectedDoc = ref(null)
const messages = ref([])
const inputText = ref('')
const uploading = ref(false)
const uploadProgress = ref(0)
const typing = ref(false)
const activeTab = ref('all')
const rightPanelTab = ref('chat')

// 【核心修复】计算属性强制保证数组类型，彻底解决filter报错
const filteredDocList = computed(() => {
  // 第一步：强制把docList.value转成数组，兜底空数组
  const rawList = Array.isArray(docList.value) ? docList.value : []
  // 第二步：过滤null/undefined元素
  const validDocs = rawList.filter(item => item !== null && item !== undefined)
  
  // 标签筛选逻辑
  if (activeTab.value === 'all') return validDocs
  if (activeTab.value === 'recent') {
    const sevenDaysAgo = new Date()
    sevenDaysAgo.setDate(sevenDaysAgo.getDate() - 7)
    return validDocs.filter(doc => new Date(doc.createTime) >= sevenDaysAgo)
  }
  if (activeTab.value === 'star') {
    return validDocs.filter(doc => doc.starred)
  }
  return validDocs
})

// 辅助方法
const getDocIconColor = (fileName) => {
  if (!fileName) return '#909399'
  const ext = fileName.split('.').pop().toLowerCase()
  const colorMap = { docx: '#409EFF', txt: '#67C23A', pdf: '#F56C6C' }
  return colorMap[ext] || '#909399'
}

// 核心：获取文档列表方法（必须和后端接口一致）
const getDocList = async () => {
  try {
    console.log('=== 开始获取文档列表 ===')
    // 必须和后端地址完全一致：http://localhost:8080/api/document/listAll
    const res = await axios.get('/document/listAll')
    console.log('=== 后端返回的文档列表 ===', res.data)
    
    // 把后端返回的数据赋值给前端变量
    docList.value = res.data || []
    console.log('=== 前端赋值后的文档列表 ===', docList.value)
  } catch (error) {
    console.error('获取文档列表失败：', error)
    ElMessage.error('获取文档列表失败')
  }
}

// 新增：删除文档方法
const handleDelete = async (docId, fileName) => {
  try {
    // 确认删除
    await ElMessageBox.confirm(
      `确定要删除文档【${fileName}】吗？删除后无法恢复！`,
      '删除确认',
      {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }
    )

    // 调用后端删除接口
    const res = await axios.delete(`/document/delete/${docId}`)
    if (res.data.success) {
      ElMessage.success('文档删除成功！')
      // 刷新文档列表
      await getDocList()
      // 如果删除的是当前选中的文档，清空选中状态
      if (selectedDoc.value && selectedDoc.value.id === docId) {
        selectedDoc.value = null
        messages.value = []
      }
    } else {
      ElMessage.error(res.data.message || '删除失败')
    }
  } catch (error) {
    if (error !== 'cancel') { // 排除取消操作的异常
      console.error('删除文档失败：', error)
      ElMessage.error('删除失败：' + (error.response?.data?.message || error.message))
    }
  }
}

const handleFileChange = async (file) => {
  if (!file || !file.raw) return
  uploading.value = true

  try {
    const formData = new FormData()
    formData.append('file', file.raw)
    
    const res = await axios.post('/document/upload', formData, {
      headers: { 'Content-Type': 'multipart/form-data' }
    })

    console.log('=== 上传成功 ===', res.data)
    ElMessage.success('文档上传成功！')
    
    // 上传成功后，清空文件列表，刷新文档列表
    fileList.value = []
    uploadRef.value?.clearFiles()
    await getDocList() // 强制刷新列表，前端立即显示新上传的文档

  } catch (error) {
    console.error('上传失败：', error)
    ElMessage.error('上传失败：' + (error.response?.data?.message || error.message))
  } finally {
    uploading.value = false
  }
}

const getDocDetail = async (docId) => {
  if (!docId) return null
  try {
    const res = await axios.get(`/document/getById/${docId}`)
    return res.data || null
  } catch (error) {
    console.error('获取文档详情失败：', error)
    ElMessage.error('获取文档详情失败')
    return null
  }
}

const selectDoc = async (doc) => {
  if (!doc) return // 拦截空文档
  const docDetail = await getDocDetail(doc.id)
  selectedDoc.value = docDetail || doc
  messages.value = []
  rightPanelTab.value = 'chat'
}

const toggleStar = async (doc) => {
  if (!doc) return // 拦截空文档
  try {
    doc.starred = !doc.starred
    ElMessage.success(doc.starred ? '已收藏' : '已取消收藏')
  } catch (e) {
    console.error('收藏失败：', e)
    ElMessage.error('收藏操作失败')
  }
}

const sendMessage = async () => {
  if (!inputText.value.trim() || typing.value || !selectedDoc.value) return
  const userMsg = { role: 'user', content: inputText.value }
  messages.value.push(userMsg)
  const question = inputText.value
  inputText.value = ''
  typing.value = true

  try {
    const res = await axios.post('/knowledge/generate-answer', {
      question,
      docId: selectedDoc.value.id,
      fileName: selectedDoc.value.fileName
    })
    typing.value = false
    messages.value.push({
      role: 'assistant',
      content: res.data || '抱歉，未能获取到回答，请重新上传文档。'
    })
  } catch (error) {
    typing.value = false
    console.error('AI回答失败:', error)
    // 【修复】捕获向量库不存在的错误，给用户友好提示
    let errorMsg = '抱歉，问答服务异常。'
    if (error.response && error.response.data && error.response.data.msg) {
      errorMsg = error.response.data.msg
    }
    messages.value.push({
      role: 'assistant',
      content: `❌ ${errorMsg} 请检查向量服务是否正常启动。`
    })
  }
}

// 事件监听（修复卸载时的空指针）
let keydownHandler = null
onMounted(() => {
  console.log('=== 页面加载完成，初始化文档列表 ===')
  getDocList()
})

onUnmounted(() => {
  // 确保卸载时移除有效监听，避免操作已销毁的响应式数据
  if (keydownHandler) {
    document.removeEventListener('keydown', keydownHandler)
  }
  // 清空响应式数据，避免卸载后残留引用
  selectedDoc.value = null
  messages.value = []
  docList.value = []
})
</script>

<style scoped>
/* 保留你原有所有样式，新增删除按钮样式 */
.knowledge-app {
  display: flex;
  flex-direction: column;
  width: 100vw;
  height: 100vh;
  background: linear-gradient(135deg, #F5F7FA 0%, #E4E8F0 100%);
  overflow: hidden;
}

.app-header {
  background: linear-gradient(90deg, #4FACFE 0%, #0082F5 100%);
  box-shadow: 0 4px 24px rgba(14, 71, 161, 0.3);
  border-bottom: 1px solid rgba(255, 255, 255, 0.2);
  padding: 0 24px;
  height: 64px;
  display: flex;
  align-items: center;
  position: relative;
  z-index: 100;
}

.app-header::before {
  content: '';
  position: absolute;
  top: 0;
  left: 0;
  right: 0;
  height: 1px;
  background: linear-gradient(90deg, rgba(255,255,255,0) 0%, rgba(255,255,255,0.4) 50%, rgba(255,255,255,0) 100%);
  pointer-events: none;
}

.header-content {
  display: flex;
  justify-content: space-between;
  align-items: center;
  width: 100%;
  max-width: 1920px;
  margin: 0 auto;
}

.logo {
  display: flex;
  align-items: center;
  gap: 12px;
  color: #FFFFFF;
}

.logo-text {
  font-size: 18px;
  font-weight: 600;
  letter-spacing: 0.5px;
}

.header-actions {
  display: flex;
  gap: 16px;
}

.header-btn {
  color: rgba(255, 255, 255, 0.9) !important;
  font-size: 14px;
  transition: all 0.3s ease;
}

.header-btn:hover {
  color: #FFFFFF !important;
  background: rgba(255, 255, 255, 0.1) !important;
}

.main-content {
  display: flex;
  flex: 1;
  overflow: hidden;
}

.sidebar {
  width: 360px;
  background: rgba(255, 255, 255, 0.98);
  border-right: 1px solid rgba(228, 231, 237, 0.8);
  display: flex;
  flex-direction: column;
  padding: 24px;
  box-sizing: border-box;
  overflow-y: auto;
  box-shadow: 2px 0 12px rgba(0, 0, 0, 0.03);
  border-radius: 8px 0 0 8px;
  margin: 8px 0 8px 8px;
}

.sidebar-tabs {
  display: flex;
  gap: 8px;
  margin-bottom: 24px;
  border-bottom: 1px solid #F0F2F5;
  padding-bottom: 12px;
  width: 100%;
}

.tab-btn {
  flex: 1 !important;
  justify-content: flex-start !important;
  gap: 6px !important;
  font-weight: 500 !important;
  color: #606266 !important;
  text-align: left !important;
  padding: 8px 12px !important;
  border-radius: 6px !important;
  border: none !important;
}

.tab-btn.active {
  color: #165DFF !important;
  border-bottom: 2px solid #165DFF !important;
  background: rgba(22, 93, 255, 0.05) !important;
}

.upload-area {
  margin-bottom: 20px;
  padding: 28px 24px;
  border: 1px dashed #DCDFE6;
  border-radius: 12px;
  text-align: center;
  transition: all 0.3s;
  background: linear-gradient(135deg, #F0F7FF 0%, #ECF5FF 100%);
  box-shadow: 0 2px 8px rgba(22, 93, 255, 0.05);
}

.upload-area:hover {
  border-color: #165DFF;
  background: linear-gradient(135deg, #ECF5FF 0%, #E8F3FF 100%);
  transform: translateY(-2px);
  box-shadow: 0 4px 12px rgba(22, 93, 255, 0.1);
}

.upload-progress {
  margin-bottom: 24px;
  width: 100%;
}

.progress-text {
  display: block;
  font-size: 12px;
  color: #909399;
  margin-top: 4px;
}

.empty-tip, .no-doc-tip, .no-content-tip {
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  padding: 60px 20px;
  color: #909399;
  width: 100%;
}

.empty-illustration, .no-doc-illustration {
  margin-bottom: 16px;
}

.empty-text, .no-doc-text, .no-content-text {
  font-size: 16px;
  font-weight: 500;
  margin-bottom: 8px;
  color: #606266;
}

.empty-desc, .no-doc-desc, .no-content-desc {
  font-size: 14px;
  color: #C0C6D0;
}

.doc-list {
  flex: 1;
  overflow-y: auto;
  width: 100%;
}

.doc-item {
  display: flex;
  align-items: center;
  gap: 12px;
  padding: 14px 16px;
  border-radius: 10px;
  cursor: pointer;
  transition: all 0.3s ease;
  margin-bottom: 8px;
  background: rgba(255, 255, 255, 0.8);
  width: 100%;
  box-sizing: border-box;
  justify-content: space-between;
}

.doc-item:hover {
  background: linear-gradient(135deg, #F0F7FF 0%, #ECF5FF 100%);
  transform: translateY(-2px);
  box-shadow: 0 4px 12px rgba(22, 93, 255, 0.08);
}

.doc-item.active {
  background: linear-gradient(135deg, #ECF5FF 0%, #E8F3FF 100%);
  border-left: 3px solid #165DFF;
}

.doc-icon {
  width: 40px;
  height: 40px;
  border-radius: 8px;
  background: linear-gradient(135deg, #F5F7FA 0%, #E4E8F0 100%);
  display: flex;
  align-items: center;
  justify-content: center;
  flex-shrink: 0;
  box-shadow: 0 2px 4px rgba(0, 0, 0, 0.05);
}

.doc-info {
  flex: 1;
  overflow: hidden;
  padding-right: 8px;
}

.doc-name {
  font-weight: 500;
  margin-bottom: 4px;
  white-space: nowrap;
  overflow: hidden;
  text-overflow: ellipsis;
  color: #303133;
}

.doc-meta {
  font-size: 12px;
  color: #909399;
  display: flex;
  gap: 12px;
}

/* 新增：删除按钮样式（优化视觉，抵消text类型的默认样式） */
.delete-btn {
  color: #F56C6C !important;
  opacity: 0.7;
  transition: all 0.2s;
  background: transparent !important;
  border: none !important;
  padding: 0 !important;
  margin-right: 4px !important;
}

.delete-btn:hover {
  opacity: 1;
  transform: scale(1.1);
  background: transparent !important;
}

.star-btn {
  flex-shrink: 0 !important;
  width: 32px !important;
  height: 32px !important;
  display: flex !important;
  align-items: center !important;
  justify-content: center !important;
}

.chat-panel {
  flex: 1;
  display: flex;
  flex-direction: column;
  background: transparent;
  overflow: hidden;
  margin: 8px 8px 8px 0;
  border-radius: 0 8px 8px 0;
}

.doc-info-card {
  padding: 20px 28px 10px;
  background: rgba(255, 255, 255, 0.95);
  border-bottom: 1px solid rgba(228, 231, 237, 0.5);
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.03);
  border-radius: 8px 8px 0 0;
}

.doc-info-header {
  display: flex;
  align-items: center;
  gap: 10px;
  margin-bottom: 10px;
}

.doc-title {
  font-weight: 600;
  font-size: 17px;
  color: #303133;
}

.doc-info-meta {
  display: flex;
  gap: 24px;
  font-size: 13px;
  color: #909399;
  margin-bottom: 16px;
}

.panel-tabs {
  display: flex;
  gap: 8px;
  border-top: 1px solid #F0F2F5;
  padding-top: 10px;
}

.panel-tab-btn {
  padding: 6px 16px !important;
  border-radius: 6px !important;
  border: none !important;
  font-weight: 500 !important;
  color: #606266 !important;
}

.panel-tab-btn.active {
  background: #165DFF !important;
  color: #FFFFFF !important;
}

.panel-content {
  flex: 1;
  overflow: hidden;
  display: flex;
  flex-direction: column;
}

.chat-content {
  flex: 1;
  padding: 28px;
  overflow-y: auto;
  background: transparent;
  padding-bottom: 220px;
}

.chat-messages {
  display: flex;
  flex-direction: column;
  gap: 24px;
  max-width: 100%;
}

.message {
  display: flex;
  gap: 16px;
  max-width: 85%;
  animation: fadeIn 0.4s ease;
}

@keyframes fadeIn {
  from { opacity: 0; transform: translateY(10px); }
  to { opacity: 1; transform: translateY(0); }
}

.message.assistant {
  align-self: flex-start;
}

.message.user {
  align-self: flex-end;
}

.message-avatar {
  width: 40px;
  height: 40px;
  border-radius: 50%;
  background: linear-gradient(135deg, #165DFF 0%, #0D47A1 100%);
  display: flex;
  align-items: center;
  justify-content: center;
  color: #FFFFFF;
  flex-shrink: 0;
  box-shadow: 0 2px 8px rgba(22, 93, 255, 0.2);
}

.message.user .message-avatar {
  background: linear-gradient(135deg, #67C23A 0%, #529B2E 100%);
}

.message-content {
  padding: 16px 20px;
  border-radius: 16px;
  background: rgba(255, 255, 255, 0.95);
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.05);
  line-height: 1.7;
  font-size: 15px;
  color: #303133;
}

/* ========== 新增：表格样式（核心修改） ========== */
.message-content table {
  border-collapse: collapse;
  width: 100%;
  margin: 12px 0;
  border: 1px solid #e2e8f0;
  border-radius: 6px;
  overflow: hidden;
}

.message-content th,
.message-content td {
  border: 1px solid #e2e8f0;
  padding: 8px 12px;
  text-align: center; /* 内容居中 */
  font-weight: normal; /* 取消表头加粗 */
}

.message-content th {
  background-color: #f8fafc; /* 表头浅背景 */
}
/* ========== 表格样式结束 ========== */

.message.assistant .message-content {
  border-top-left-radius: 6px;
}

.message.user .message-content {
  background: linear-gradient(135deg, #165DFF 0%, #0D47A1 100%);
  color: #FFFFFF;
  border-top-right-radius: 6px;
}

.message.typing .message-content {
  padding: 16px 20px;
}

.typing-dots {
  display: flex;
  gap: 6px;
}

.typing-dots span {
  width: 10px;
  height: 10px;
  border-radius: 50%;
  background-color: #909399;
  animation: typing 1.4s infinite ease-in-out both;
}

.typing-dots span:nth-child(2) {
  animation-delay: 0.2s;
}

.typing-dots span:nth-child(3) {
  animation-delay: 0.4s;
}

@keyframes typing {
  0% { transform: translateY(0); }
  50% { transform: translateY(-8px); }
  100% { transform: translateY(0); }
}

.doc-content {
  flex: 1;
  padding: 28px;
  overflow-y: auto;
  background: rgba(255, 255, 255, 0.6);
  border-radius: 0 0 8px 8px;
}

.doc-content-wrapper {
  max-width: 1000px;
  margin: 0 auto;
}

.doc-text {
  font-family: -apple-system, BlinkMacSystemFont, 'Segoe UI', Roboto, sans-serif;
  font-size: 15px;
  line-height: 1.8;
  color: #303133;
  white-space: pre-wrap;
  word-break: break-word;
  background: #FFFFFF;
  padding: 24px;
  border-radius: 12px;
  box-shadow: 0 2px 12px rgba(0, 0, 0, 0.04);
  margin: 0;
}

.chat-input-wrapper {
  position: fixed !important;
  bottom: 30px !important;
  left: 390px !important;
  right: 30px !important;
  padding: 20px 24px;
  background: rgba(255, 255, 255, 0.9) !important;
  backdrop-filter: blur(16px) !important;
  -webkit-backdrop-filter: blur(16px) !important;
  border-radius: 20px !important;
  box-shadow: 0 8px 32px rgba(0, 0, 0, 0.08) !important;
  border: 1px solid rgba(255, 255, 255, 0.7) !important;
  z-index: 9999 !important;
  box-sizing: border-box;
}

.input-container {
  max-width: 100%;
}

.custom-input {
  margin-bottom: 16px;
}

.custom-input .el-textarea__inner {
  border-radius: 14px !important;
  background: rgba(255, 255, 255, 0.95) !important;
  border: 1px solid rgba(22, 93, 255, 0.1) !important;
  padding: 16px 20px !important;
  font-size: 15px !important;
  line-height: 1.6 !important;
  transition: all 0.3s ease !important;
  min-height: 100px;
  color: #303133;
}

.custom-input .el-textarea__inner:hover {
  border-color: rgba(22, 93, 255, 0.3) !important;
  box-shadow: 0 4px 12px rgba(22, 93, 255, 0.08) !important;
}

.custom-input .el-textarea__inner:focus {
  border-color: #165DFF !important;
  box-shadow: 0 0 0 2px rgba(22, 93, 255, 0.15) !important;
  outline: none !important;
}

.input-actions {
  display: flex;
  justify-content: flex-end;
  width: 100%;
}

.send-btn {
  border-radius: 12px !important;
  padding: 12px 32px !important;
  background: linear-gradient(135deg, #165DFF 0%, #0D47A1 100%) !important;
  border: none !important;
  font-weight: 500 !important;
  font-size: 15px !important;
  transition: all 0.3s ease !important;
  box-shadow: 0 4px 12px rgba(22, 93, 255, 0.2);
}

.send-btn:hover {
  transform: translateY(-2px) !important;
  box-shadow: 0 8px 20px rgba(22, 93, 255, 0.3) !important;
}

.send-btn:disabled {
  background: #909399 !important;
  transform: none !important;
  box-shadow: none !important;
}

.input-tip {
  display: flex;
  justify-content: space-between;
  align-items: center;
  font-size: 12px;
  color: rgba(144, 147, 153, 0.8);
  margin-top: 12px;
}

@media (max-width: 1200px) {
  .sidebar {
    width: 320px;
  }
  .chat-input-wrapper {
    left: 350px !important;
  }
}
@media (max-width: 768px) {
  .main-content {
    flex-direction: column;
  }
  .sidebar {
    width: 100%;
    height: 40%;
    border-right: none;
    border-bottom: 1px solid #E4E7ED;
    margin: 8px;
    border-radius: 8px;
  }
  .chat-input-wrapper {
    left: 20px !important;
    right: 20px !important;
    bottom: 20px !important;
  }
  .chat-content {
    padding-bottom: 200px;
  }
  .chat-panel {
    margin: 0 8px 8px 8px;
    border-radius: 8px;
  }
  .doc-info-meta {
    flex-wrap: wrap;
    gap: 8px 24px;
  }
}
</style>
