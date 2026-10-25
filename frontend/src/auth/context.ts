import { createContext, useContext } from 'react'
import type { LoginRequest, Me, RegisterRequest, Role } from '../api/types'

export interface Session {
  username: string
  role: Role
}

export interface AuthContextValue {
  session: Session | null
  // Perfil completo de /me; puede tardar en llegar aunque ya haya sesión
  me: Me | undefined
  login: (data: LoginRequest) => Promise<Session>
  register: (data: RegisterRequest) => Promise<Session>
  logout: () => void
}

export const AuthContext = createContext<AuthContextValue | null>(null)

export function useAuth() {
  const value = useContext(AuthContext)
  if (!value) throw new Error('useAuth tiene que usarse dentro de <AuthProvider>')
  return value
}

// El payload del JWT trae sub (username), role y exp; no hace falta verificar la firma acá,
// el backend la valida en cada request
export function sessionFromToken(token: string | null): Session | null {
  if (!token) return null
  try {
    const payload = JSON.parse(atob(token.split('.')[1].replace(/-/g, '+').replace(/_/g, '/')))
    if (typeof payload.exp === 'number' && payload.exp * 1000 < Date.now()) return null
    return { username: payload.sub, role: payload.role }
  } catch {
    return null
  }
}
