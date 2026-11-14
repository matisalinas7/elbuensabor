# 🍔 El Buen Sabor

Sistema de gestión para un local de comida rápida con delivery: catálogo de productos e insumos, pedidos, facturación y clientes, con acceso por roles (Admin, Empleado y Cliente).

> Nació como proyecto académico de la materia "Desarrollo de Software". El backend se siguió desarrollando después y el frontend se está rehaciendo desde cero.

## 📂 Estructura

```text
elbuensabor/
├── backend/    # API REST: Java 17, Spring Boot 3, Spring Security + JWT, H2
└── frontend/   # SPA: React 19, TypeScript, Vite, Tailwind
```

## 🚀 Cómo levantarlo

### Backend

```bash
cd backend
./gradlew bootRun        # Windows: .\gradlew.bat bootRun
```

La API queda en `http://localhost:8080`. Más detalles en [backend/README.md](backend/README.md).

### Frontend

```bash
cd frontend
npm install
npm run dev
```

Queda en `http://localhost:5173`. Más detalles (proxy al backend y usuarios de prueba) en [frontend/README.md](frontend/README.md).
