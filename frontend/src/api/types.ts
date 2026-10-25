// Espejo de las entidades y DTOs del backend (backend/src/main/java/com/peso/elBuenSabor)

export type Role = 'ADMIN' | 'EMPLOYEE' | 'CLIENT'

export type EstadoPedido =
  | 'PENDIENTE_PAGO'
  | 'PAGADO'
  | 'PREPARACION'
  | 'PENDIENTE_ENTREGA'
  | 'EN_CAMINO'
  | 'CANCELADO'
  | 'NOTA_CREDITO'
  | 'COMPLETADO'

export type TipoEnvio = 'DELIVERY' | 'TAKE_AWAY'
export type FormaPago = 'EFECTIVO' | 'MERCADO_PAGO'

export interface Base {
  id: number
  fechaAlta: string | null
  fechaModificacion: string | null
  fechaBaja: string | null
}

export interface RubroArticulo extends Base {
  denominacion: string
}

export interface ArticuloInsumo extends Base {
  nombre: string
  descripcion: string
  urlImagen: string | null
  precioCompra: number
  stockActual: number
  stockMinimo: number
  unidadMedida: string
}

export interface DetalleArticuloManufacturado extends Base {
  cantidad: number
  unidadMedida: string
  articuloInsumo: ArticuloInsumo
}

export interface ArticuloManufacturado extends Base {
  denominacion: string
  descripcion: string
  tiempoEstimadoCocina: number
  precioVenta: number
  costo: number | null
  urlImagen: string | null
  rubroArticulo: RubroArticulo | null
  detalleArticulosManufacturados: DetalleArticuloManufacturado[]
}

export interface Cliente {
  id: number
  nombre: string
  apellido: string | null
  telefono: string | null
  email: string | null
}

export interface Me {
  id: number
  username: string
  firstname: string | null
  lastname: string
  role: Role
  cliente: Cliente | null
}

export interface AuthResponse {
  token: string
}

export interface LoginRequest {
  username: string
  password: string
}

export interface RegisterRequest extends LoginRequest {
  firstname: string
  lastname: string
  country?: string
}
