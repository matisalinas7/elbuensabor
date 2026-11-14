import type { InputHTMLAttributes, ReactNode } from 'react'

// Piezas compartidas por login y registro

export function AuthCard({
  title,
  error,
  footer,
  children,
}: {
  title: string
  error: string | null
  footer: ReactNode
  children: ReactNode
}) {
  return (
    <div className="mx-auto max-w-md rounded-2xl border border-stone-200 bg-white p-6 sm:p-8">
      <h1 className="mb-6 text-2xl font-bold">{title}</h1>
      {error && (
        <p role="alert" className="mb-4 rounded-lg bg-red-50 px-3 py-2 text-sm text-red-700">
          {error}
        </p>
      )}
      {children}
      <p className="mt-6 text-center text-sm text-stone-600">{footer}</p>
    </div>
  )
}

export function Field({ label, ...input }: { label: string } & InputHTMLAttributes<HTMLInputElement>) {
  return (
    <label className="block">
      <span className="mb-1 block text-sm font-medium text-stone-700">{label}</span>
      <input
        required
        {...input}
        className="w-full rounded-lg border border-stone-300 px-3 py-2 outline-none focus:border-brand-500 focus:ring-2 focus:ring-brand-100"
      />
    </label>
  )
}

export function SubmitButton({ loading, children }: { loading: boolean; children: ReactNode }) {
  return (
    <button
      type="submit"
      disabled={loading}
      className="w-full rounded-lg bg-brand-500 py-2.5 font-semibold text-white hover:bg-brand-600 disabled:opacity-60"
    >
      {loading ? 'Un momento…' : children}
    </button>
  )
}
