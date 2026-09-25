import { useState } from 'react'
import { Link, useNavigate } from 'react-router-dom'
import api from '../api/axios.js'
import { useAuth } from '../context/AuthContext.jsx'

export default function Register() {
  const [fullName, setFullName] = useState('')
  const [email, setEmail] = useState('')
  const [password, setPassword] = useState('')
  const [error, setError] = useState('')
  const [loading, setLoading] = useState(false)

  const { login } = useAuth()
  const navigate = useNavigate()

  async function handleSubmit(e) {
    e.preventDefault()
    setError('')
    setLoading(true)
    try {
      const res = await api.post('/auth/register', { fullName, email, password })
      login(res.data.token, res.data.fullName)
      navigate('/dashboard')
    } catch (err) {
      setError(err.response?.data?.message || 'Qeydiyyat zamani xeta bas verdi')
    } finally {
      setLoading(false)
    }
  }

  return (
    <div className="auth-page">
      <form className="auth-card" onSubmit={handleSubmit}>
        <h1>Qeydiyyat</h1>

        {error && <div className="error">{error}</div>}

        <label>Ad Soyad</label>
        <input value={fullName} onChange={(e) => setFullName(e.target.value)} required />

        <label>Email</label>
        <input type="email" value={email} onChange={(e) => setEmail(e.target.value)} required />

        <label>Sifre</label>
        <input
          type="password"
          value={password}
          onChange={(e) => setPassword(e.target.value)}
          minLength={6}
          required
        />

        <button type="submit" disabled={loading}>
          {loading ? 'Gozleyin...' : 'Qeydiyyatdan kec'}
        </button>

        <p className="switch-link">
          Artiq hesabin var? <Link to="/login">Daxil ol</Link>
        </p>
      </form>
    </div>
  )
}
