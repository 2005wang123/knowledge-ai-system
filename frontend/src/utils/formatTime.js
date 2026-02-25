/**
 * 格式化时间为 YYYY-MM-DD HH:mm 格式
 * @param {Date|String|Number} time - 时间值
 * @returns {String} 格式化后的时间
 */
export const formatTime = (time) => {
  if (!time) return '未知时间'
  const date = new Date(time)
  const year = date.getFullYear()
  const month = String(date.getMonth() + 1).padStart(2, '0')
  const day = String(date.getDate()).padStart(2, '0')
  const hours = String(date.getHours()).padStart(2, '0')
  const minutes = String(date.getMinutes()).padStart(2, '0')
  return `${year}-${month}-${day} ${hours}:${minutes}`
}