import tailwindcss from '@tailwindcss/vite'
import react from '@vitejs/plugin-react'
import { defineConfig, loadEnv } from 'vite'

// https://vite.dev/config/
export default defineConfig(({ mode }) => {
  const env = loadEnv(mode, process.cwd(), '')
  // En desarrollo el front llama a rutas relativas y Vite las reenvía al backend:
  // mismo origen, sin problemas de CORS
  const backend = env.BACKEND_URL || 'http://localhost:8080'

  return {
    plugins: [react(), tailwindcss()],
    server: {
      proxy: {
        '/auth': backend,
        '/elbuensabor': backend,
      },
    },
  }
})
