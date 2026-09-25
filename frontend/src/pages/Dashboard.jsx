import { useEffect, useState } from 'react'
import { useNavigate } from 'react-router-dom'
import api from '../api/axios.js'
import { useAuth } from '../context/AuthContext.jsx'

export default function Dashboard() {
  const [account, setAccount] = useState(null)
  const [amount, setAmount] = useState('')
  const [message, setMessage] = useState('')
  const [error, setError] = useState('')
  const [loading, setLoading] = useState(true)

  const { logout } = useAuth()
  const navigate = useNavigate()

  async function loadAccount() {
    try {
      const res = await api.get('/account/me')
      setAccount(res.data)
    } catch (err) {
      setError('Melumat yuklenmedi')
    } finally {
      setLoading(false)
    }
  }

  useEffect(() => {
    loadAccount()
  }, [])

  async function handleDeposit() {
    await runTransaction('/account/deposit')
  }

  async function handleWithdraw() {
    await runTransaction('/account/withdraw')
  }

  async function runTransaction(url) {
    setError('')
    setMessage('')
    if (!amount || Number(amount) <= 0) {
      setError('Duzgun mebleg daxil edin')
      return
    }
    try {
      const res = await api.post(url, { amount: Number(amount) })
      setAccount(res.data)
      setAmount('')
      setMessage('Emeliyyat ugurla tamamlandi')
    } catch (err) {
      setError(err.response?.data?.message || 'Emeliyyat alinmadi')
    }
  }

  function handleLogout() {
    logout()
    navigate('/login')
  }

  if (loading) return <div className="dashboard-page">Yuklenir...</div>

  return (
    <div className="dashboard-page">
      <div className="dashboard-card">
        <div className="dashboard-header">
          <h1>Xos gelmisiniz, {account?.fullName}</h1>
          <button className="logout-btn" onClick={handleLogout}>
            Cixis
          </button>
        </div>

        <div className="balance-box">
          <span>Hesab nomresi: {account?.accountNumber}</span>
          <h2>{account?.balance?.toFixed(2)} AZN</h2>
        </div>

        {error && <div className="error">{error}</div>}
        {message && <div className="success">{message}</div>}

        <div className="transaction-form">
          <input
            type="number"
            step="0.01"
            placeholder="Mebleg"
            value={amount}
            onChange={(e) => setAmount(e.target.value)}
          />
          <div className="transaction-buttons">
            <button onClick={handleDeposit}>Pul yatir</button>
            <button onClick={handleWithdraw}>Pul cixar</button>
          </div>
        </div>
      </div>
    </div>
  )
}
