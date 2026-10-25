import { Link, Outlet } from 'react-router'
import { useAuth } from '../auth/context'

export function StoreLayout() {
  const { session, me, logout } = useAuth()
  const isStaff = session?.role === 'ADMIN' || session?.role === 'EMPLOYEE'

  return (
    <div className="min-h-screen flex flex-col">
      <header className="border-b border-stone-200 bg-white">
        <div className="mx-auto flex max-w-6xl items-center justify-between gap-4 px-4 py-3">
          <Link to="/" className="text-xl font-bold text-brand-600">
            El Buen Sabor
          </Link>

          <nav className="flex items-center gap-4 text-sm">
            {isStaff && (
              <Link to="/admin" className="font-medium text-stone-700 hover:text-brand-600">
                Panel
              </Link>
            )}
            {session ? (
              <>
                <span className="text-stone-500">Hola, {me?.firstname ?? session.username}</span>
                <button onClick={logout} className="font-medium text-stone-700 hover:text-brand-600">
                  Salir
                </button>
              </>
            ) : (
              <>
                <Link to="/login" className="font-medium text-stone-700 hover:text-brand-600">
                  Ingresar
                </Link>
                <Link
                  to="/registro"
                  className="rounded-lg bg-brand-500 px-3 py-1.5 font-medium text-white hover:bg-brand-600"
                >
                  Crear cuenta
                </Link>
              </>
            )}
          </nav>
        </div>
      </header>

      <main className="mx-auto w-full max-w-6xl flex-1 px-4 py-8">
        <Outlet />
      </main>
    </div>
  )
}
