module.exports = {
  content: ['src/set_game/*.cljs', 'public/*.html'],
  css: ['public/styles.css'],
  extractors: [
    {
      extractor: class {
        static extract(content) {
          return content.match(/[\w-:/]+(?<!:)/g) || []
        }
      },
      extensions: ['cljs', 'html']
    }
  ]
}
