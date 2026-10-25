import { createBrowserRouter } from 'react-router'
import { RequireRole } from './auth/RequireRole'
import { AdminLayout } from './layouts/AdminLayout'
import { StoreLayout } from './layouts/StoreLayout'
import { AdminHomePage } from './pages/AdminHomePage'
import { LoginPage } from './pages/LoginPage'
import { MenuPage } from './pages/MenuPage'
import { NotFoundPage } from './pages/NotFoundPage'
import { RegisterPage } from './pages/RegisterPage'

export const router = createBrowserRouter([
  {
    // Tienda: pública, el cliente arma su pedido acá
    element: <StoreLayout />,
    children: [
      { index: true, element: <MenuPage /> },
      { path: 'login', element: <LoginPage /> },
      { path: 'registro', element: <RegisterPage /> },
      { path: '*', element: <NotFoundPage /> },
    ],
  },
  {
    // Panel de gestión: solo personal del local
    path: 'admin',
    element: (
      <RequireRole roles={['ADMIN', 'EMPLOYEE']}>
        <AdminLayout />
      </RequireRole>
    ),
    children: [{ index: true, element: <AdminHomePage /> }],
  },
])
