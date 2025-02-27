var modules = []
const files = require.context('../', true, /\/Create.vue$/)
files.keys().forEach((itemPath) => {
  var file = files(itemPath).default || files(itemPath)
  modules.push(file)
})
export default modules
