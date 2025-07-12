<script>
import * as monaco from 'monaco-editor';

export default {
  name: 'MonacoEditor',
  props: {
    value: {
      type: String,
      default: ''
    },
    language: {
      type: String,
      default: 'python'
    },
    theme: {
      type: String,
      default: 'vs-dark'
    },
    fontSize: {
      type: Number,
      default: 16
    }
  },
  mounted() {
    this.editor = monaco.editor.create(this.$refs.editorContainer, {
      value: this.value,
      language: this.language,
      theme: this.theme,
      fontSize: this.fontSize,
      automaticLayout: true
    });
    this.editor.onDidChangeModelContent(() => {
      this.$emit('input', this.editor.getValue());
      this.$emit('update:value', this.editor.getValue()); // 兼容 v-model
    });
  },
  watch: {
    value(newVal) {
      if (this.editor && this.editor.getValue() !== newVal) {
        this.editor.setValue(newVal);
      }
    },
    language(newLang) {
      if (this.editor) {
        monaco.editor.setModelLanguage(this.editor.getModel(), newLang);
      }
    },
    theme(newTheme) {
      if (this.editor) {
        monaco.editor.setTheme(newTheme);
      }
    },
    fontSize(newFontSize) {
      if (this.editor) {
        this.editor.updateOptions({ fontSize: newFontSize });
      }
    }
  },
  beforeUnmount() {
    if (this.editor) {
      this.editor.dispose();
    }
  }
};
</script>

<template>
  <div ref="editorContainer" style="width: 100%; height: 400px;"></div>
</template>

<style scoped>
.editor-container {
  width: 100%;
  height: 100%;
  min-height: 400px;
  background: #f8f9fa;
  border-radius: 12px;
  box-shadow: 0 2px 8px rgba(0,0,0,0.08);
  border: 1.5px solid #e0e7ef;
  overflow: hidden;
  transition: box-shadow 0.2s;
  display: flex;
  flex-direction: column;
}
.editor-container:focus-within {
  box-shadow: 0 4px 16px rgba(37,99,235,0.15);
  border-color: #2563eb;
}
</style> 