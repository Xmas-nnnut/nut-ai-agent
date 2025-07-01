import MarkdownIt from 'markdown-it'
import hljs from 'highlight.js'

// 配置markdown解析器
const md = new MarkdownIt({
  html: false, // 不允许HTML标签
  xhtmlOut: false,
  breaks: true, // 将换行符转换为<br>
  linkify: true, // 自动识别链接
  typographer: true, // 启用一些语言中性的替换和引号美化
  highlight: function (str, lang) {
    if (lang && hljs.getLanguage(lang)) {
      try {
        return '<pre class="hljs"><code>' +
               hljs.highlight(str, { language: lang, ignoreIllegals: true }).value +
               '</code></pre>'
      } catch (__) {}
    }

    return '<pre class="hljs"><code>' + md.utils.escapeHtml(str) + '</code></pre>'
  }
})

// 自定义链接渲染，让链接在新窗口打开
const defaultRender = md.renderer.rules.link_open || function(tokens, idx, options, env, renderer) {
  return renderer.renderToken(tokens, idx, options)
}

md.renderer.rules.link_open = function (tokens, idx, options, env, renderer) {
  const aIndex = tokens[idx].attrIndex('target')
  
  if (aIndex < 0) {
    tokens[idx].attrPush(['target', '_blank'])
  } else {
    tokens[idx].attrs[aIndex][1] = '_blank'
  }
  
  return defaultRender(tokens, idx, options, env, renderer)
}

// 导出渲染函数
export function renderMarkdown(text) {
  if (!text) return ''
  return md.render(text)
}

export default md 