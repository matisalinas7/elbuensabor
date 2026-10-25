import { Link, NavLink, Outlet } from 'react-router'
import { useAuth } from '../auth/context'

const ROLE_LABEL = { ADMIN: 'Administrador', EMPLOYEE: 'Empleado', CLIENT: 'Cliente' } as const

export function AdminLayout() {
  const { session, logout } = useAuth()

  return (
    <div className="min-h-screen md:flex">
      <aside className="border-b border-stone-200 bg-white md:w-60 md:shrink-0 md:border-r md:border-b-0">
        <div className="px-4 py-4">
          <Link to="/" className="text-lg font-bold text-brand-600">
            El Buen Sabor
          </Link>
          {session && <p className="text-xs text-stone-500">{ROLE_LABEL[session.role]}</p>}
        </div>
        <nav className="flex gap-1 px-2 pb-3 md:flex-col">
          <NavLink
            to="/admin"
            end
            className={({ isActive }) =>
              `rounded-lg px-3 py-2 text-sm font-medium ${
                isActive ? 'bg-brand-50 text-brand-700' : 'text-stone-700 hover:bg-stone-100'
              }`
            }
          >
            Inicio
          </NavLink>
        </nav>
        <div className="hidden px-4 pb-4 md:block">
          <button onClick={logout} className="text-sm text-stone-500 hover:text-brand-600">
            Cerrar sesión
          </button>
        </div>
      </aside>

      <main className="flex-1 px-4 py-6 md:px-8">
        <Outlet />
      </main>
    </div>
  )
}
