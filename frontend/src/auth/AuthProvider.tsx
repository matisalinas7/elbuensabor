import { useQuery, useQueryClient } from '@tanstack/react-query'
import { useCallback, useEffect, useMemo, useState, type ReactNode } from 'react'
import { api, tokenStorage, UNAUTHORIZED_EVENT } from '../api/client'
import type { AuthResponse, LoginRequest, Me, RegisterRequest } from '../api/types'
import { AuthContext, sessionFromToken, type AuthContextValue } from './context'

export function AuthProvider({ children }: { children: ReactNode }) {
  const queryClient = useQueryClient()
  const [session, setSession] = useState(() => {
    const current = sessionFromToken(tokenStorage.get())
    if (!current) tokenStorage.clear()
    return current
  })

  const { data: me } = useQuery({
    queryKey: ['me', session?.username],
    queryFn: () => api<Me>('/elbuensabor/v1/me'),
    enabled: session !== null,
  })

  const logout = useCallback(() => {
    tokenStorage.clear()
    setSession(null)
    queryClient.clear()
  }, [queryClient])

  // Cualquier 401 con token (vencido, usuario borrado) cierra la sesión
  useEffect(() => {
    window.addEventListener(UNAUTHORIZED_EVENT, logout)
    return () => window.removeEventListener(UNAUTHORIZED_EVENT, logout)
  }, [logout])

  const startSession = useCallback((token: string) => {
    const next = sessionFromToken(token)
    if (!next) throw new Error('El servidor devolvió un token inválido')
    tokenStorage.set(token)
    setSession(next)
    return next
  }, [])

  const login = useCallback(
    async (data: LoginRequest) => {
      const { token } = await api<AuthResponse>('/auth/login', { method: 'POST', body: data })
      return startSession(token)
    },
    [startSession],
  )

  const register = useCallback(
    async (data: RegisterRequest) => {
      const { token } = await api<AuthResponse>('/auth/register', { method: 'POST', body: data })
      return startSession(token)
    },
    [startSession],
  )

  const value = useMemo<AuthContextValue>(
    () => ({ session, me, login, register, logout }),
    [session, me, login, register, logout],
  )

  return <AuthContext value={value}>{children}</AuthContext>
}
