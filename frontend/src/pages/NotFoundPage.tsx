import { Link } from 'react-router'

export function NotFoundPage() {
  return (
    <section className="py-16 text-center">
      <h1 className="text-2xl font-bold">Página no encontrada</h1>
      <Link to="/" className="mt-4 inline-block font-medium text-brand-600 hover:underline">
        Volver al menú
      </Link>
    </section>
  )
}
