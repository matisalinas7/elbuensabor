# 🍔 El Buen Sabor – Frontend

SPA para la tienda (menú y pedidos de clientes) y el panel de gestión (admin y empleados) de El Buen Sabor.

**Stack:** React 19, TypeScript, Vite, React Router, TanStack Query, Tailwind CSS.

## 🛠️ Cómo levantarlo

Necesita Node 20+ y el [backend](../backend) corriendo.

```bash
cd frontend
npm install
npm run dev
```

Queda en `http://localhost:5173`. En desarrollo, Vite reenvía `/auth` y `/elbuensabor` al backend (por defecto `http://localhost:8080`).
Si el backend corre en otro puerto, crear `frontend/.env.local` (ver `.env.example`):

```bash
BACKEND_URL=http://localhost:8081
```

### Usuarios de prueba

El backend crea estos usuarios al arrancar con una base vacía:

| Usuario | Contraseña | Rol |
| :--- | :--- | :--- |
| `admin` | `admin123` | Administrador |
| `empleado` | `empleado123` | Empleado |

Los clientes se registran desde la propia tienda.

## 📂 Estructura

```text
src/
├── api/        # Cliente HTTP (token, errores) y tipos de las entidades del backend
├── auth/       # Sesión: token, rol, perfil (/me) y guard de rutas por rol
├── layouts/    # Layout de la tienda y del panel de gestión
├── pages/      # Pantallas
├── router.tsx  # Rutas: tienda (/) y panel (/admin)
└── main.tsx
```

## 📜 Scripts

| Comando | Qué hace |
| :--- | :--- |
| `npm run dev` | Servidor de desarrollo |
| `npm run build` | Chequeo de tipos + build de producción en `dist/` |
| `npm run lint` | Lint con oxlint |
| `npm run preview` | Sirve el build de producción |
