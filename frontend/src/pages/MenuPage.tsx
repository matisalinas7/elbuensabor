import { useQuery } from '@tanstack/react-query'
import { api } from '../api/client'
import type { ArticuloManufacturado } from '../api/types'

const precio = new Intl.NumberFormat('es-AR', { style: 'currency', currency: 'ARS' })

export function MenuPage() {
  const { data: productos, isPending, error } = useQuery({
    queryKey: ['articulosmanufacturados'],
    queryFn: () => api<ArticuloManufacturado[]>('/elbuensabor/v1/articulosmanufacturados'),
  })

  return (
    <section>
      <h1 className="mb-6 text-2xl font-bold">Nuestro menú</h1>

      {isPending && <p className="text-stone-500">Cargando menú…</p>}
      {error && <p className="text-red-600">{error.message}</p>}
      {productos?.length === 0 && <p className="text-stone-500">Todavía no hay productos cargados.</p>}

      <ul className="grid gap-4 sm:grid-cols-2 lg:grid-cols-3">
        {productos?.map((producto) => (
          <li key={producto.id} className="rounded-xl border border-stone-200 bg-white p-4">
            <p className="text-xs font-medium uppercase tracking-wide text-brand-600">
              {producto.rubroArticulo?.denominacion}
            </p>
            <h2 className="font-semibold">{producto.denominacion}</h2>
            <p className="mt-1 text-sm text-stone-600">{producto.descripcion}</p>
            <p className="mt-3 font-bold">{precio.format(producto.precioVenta)}</p>
          </li>
        ))}
      </ul>
    </section>
  )
}
