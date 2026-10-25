import { useAuth } from '../auth/context'

export function AdminHomePage() {
  const { me, session } = useAuth()

  return (
    <section>
      <h1 className="text-2xl font-bold">Panel de gestión</h1>
      <p className="mt-2 text-stone-600">Bienvenido, {me?.firstname ?? session?.username}.</p>
    </section>
  )
}
