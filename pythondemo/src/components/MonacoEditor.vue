<script setup>
import { ref, onMounted, onBeforeUnmount, watch, defineProps, defineEmits } from "vue";
import loader from "@monaco-editor/loader";
loader.config({ paths: { vs: '/vs' } });

const props = defineProps({
  value: String,
  language: {
    type: String,
    default: "python",
  },
  theme: {
    type: String,
    default: "vs-dark",
  },
  height: {
    type: [String, Number],
    default: 400,
  }
});

const emits = defineEmits(["update:value"]);

const editorContainer = ref(null);
let editorInstance = null;

onMounted(() => {
  loader.init().then((monaco) => {
    editorInstance = monaco.editor.create(editorContainer.value, {
      value: props.value || "",
      language: props.language,
      theme: props.theme,
      automaticLayout: true,
    });

    editorInstance.onDidChangeModelContent(() => {
      emits("update:value", editorInstance.getValue());
    });
  });
});

onBeforeUnmount(() => {
  if (editorInstance) {
    editorInstance.dispose();
  }
});

watch(
  () => props.language,
  (newLanguage) => {
    if (editorInstance) {
      loader.init().then((monaco) => {
        monaco.editor.setModelLanguage(editorInstance.getModel(), newLanguage);
      });
    }
  }
);

watch(
  () => props.value,
  (newValue) => {
    if (editorInstance && editorInstance.getValue() !== newValue) {
      editorInstance.setValue(newValue);
    }
  }
);
</script>

<template>
  <div
    ref="editorContainer"
    class="editor-container"
    :style="{ width: '100%', height: '100%' }"
  ></div>
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