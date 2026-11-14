import { useState, type FormEvent } from 'react'
import { Link, useLocation, useNavigate } from 'react-router'
import { useAuth } from '../auth/context'
import { AuthCard, Field, SubmitButton } from './authForm'

export function LoginPage() {
  const { login } = useAuth()
  const navigate = useNavigate()
  const location = useLocation()
  const [error, setError] = useState<string | null>(null)
  const [loading, setLoading] = useState(false)

  async function handleSubmit(event: FormEvent<HTMLFormElement>) {
    event.preventDefault()
    const form = new FormData(event.currentTarget)
    setError(null)
    setLoading(true)
    try {
      const session = await login({
        username: String(form.get('username')),
        password: String(form.get('password')),
      })
      const from = (location.state as { from?: string } | null)?.from
      navigate(from ?? (session.role === 'CLIENT' ? '/' : '/admin'), { replace: true })
    } catch (e) {
      setError((e as Error).message)
    } finally {
      setLoading(false)
    }
  }

  return (
    <AuthCard
      title="Ingresar"
      error={error}
      footer={
        <>
          ¿No tenés cuenta?{' '}
          <Link to="/registro" className="font-medium text-brand-600 hover:underline">
            Registrate
          </Link>
        </>
      }
    >
      <form onSubmit={handleSubmit} className="space-y-4">
        <Field label="Usuario" name="username" autoComplete="username" />
        <Field label="Contraseña" name="password" type="password" autoComplete="current-password" />
        <SubmitButton loading={loading}>Ingresar</SubmitButton>
      </form>
    </AuthCard>
  )
}
