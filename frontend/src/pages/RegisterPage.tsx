import { useState, type FormEvent } from 'react'
import { Link, useNavigate } from 'react-router'
import { useAuth } from '../auth/context'
import { AuthCard, Field, SubmitButton } from './authForm'

export function RegisterPage() {
  const { register } = useAuth()
  const navigate = useNavigate()
  const [error, setError] = useState<string | null>(null)
  const [loading, setLoading] = useState(false)

  async function handleSubmit(event: FormEvent<HTMLFormElement>) {
    event.preventDefault()
    const form = new FormData(event.currentTarget)
    setError(null)
    setLoading(true)
    try {
      await register({
        firstname: String(form.get('firstname')),
        lastname: String(form.get('lastname')),
        username: String(form.get('username')),
        password: String(form.get('password')),
      })
      navigate('/', { replace: true })
    } catch (e) {
      setError((e as Error).message)
    } finally {
      setLoading(false)
    }
  }

  return (
    <AuthCard
      title="Crear cuenta"
      error={error}
      footer={
        <>
          ¿Ya tenés cuenta?{' '}
          <Link to="/login" className="font-medium text-brand-600 hover:underline">
            Ingresá
          </Link>
        </>
      }
    >
      <form onSubmit={handleSubmit} className="space-y-4">
        <div className="grid gap-4 sm:grid-cols-2">
          <Field label="Nombre" name="firstname" autoComplete="given-name" />
          <Field label="Apellido" name="lastname" autoComplete="family-name" />
        </div>
        <Field label="Usuario" name="username" autoComplete="username" />
        <Field label="Contraseña" name="password" type="password" autoComplete="new-password" minLength={4} />
        <SubmitButton loading={loading}>Crear cuenta</SubmitButton>
      </form>
    </AuthCard>
  )
}
