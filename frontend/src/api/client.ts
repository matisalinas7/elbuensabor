const BASE_URL = import.meta.env.VITE_API_URL ?? ''
const TOKEN_KEY = 'ebs.token'

export const tokenStorage = {
  get: () => localStorage.getItem(TOKEN_KEY),
  set: (token: string) => localStorage.setItem(TOKEN_KEY, token),
  clear: () => localStorage.removeItem(TOKEN_KEY),
}

export class ApiError extends Error {
  readonly status: number

  constructor(status: number, message: string) {
    super(message)
    this.status = status
  }
}

// Avisa a la app que la sesión ya no sirve (token vencido o inválido)
export const UNAUTHORIZED_EVENT = 'ebs:unauthorized'

type RequestOptions = Omit<RequestInit, 'body'> & { body?: unknown }

export async function api<T>(path: string, { body, headers, ...init }: RequestOptions = {}): Promise<T> {
  const token = tokenStorage.get()
  const response = await fetch(`${BASE_URL}${path}`, {
    ...init,
    headers: {
      ...(body !== undefined && { 'Content-Type': 'application/json' }),
      ...(token && { Authorization: `Bearer ${token}` }),
      ...headers,
    },
    body: body !== undefined ? JSON.stringify(body) : undefined,
  })

  if (!response.ok) {
    if (response.status === 401 && token) {
      window.dispatchEvent(new Event(UNAUTHORIZED_EVENT))
    }
    throw new ApiError(response.status, await errorMessage(response))
  }

  if (response.status === 204) return undefined as T
  const text = await response.text()
  return (text ? JSON.parse(text) : undefined) as T
}

// El backend responde los errores como {"Error": "..."}
async function errorMessage(response: Response) {
  try {
    const data = await response.json()
    if (typeof data?.Error === 'string') return data.Error
  } catch {
    // cuerpo vacío o no JSON
  }
  if (response.status === 401) return 'Tenés que iniciar sesión'
  if (response.status === 403) return 'No tenés permiso para hacer esto'
  return 'Ocurrió un error, intentá de nuevo más tarde'
}
