import { createContext, useContext, useState } from 'react'

const AuthContext = createContext(null)

export function AuthProvider({ children }) {
  const [token, setToken] = useState(localStorage.getItem('token'))
  const [fullName, setFullName] = useState(localStorage.getItem('fullName'))

  const login = (newToken, newFullName) => {
    localStorage.setItem('token', newToken)
    localStorage.setItem('fullName', newFullName)
    setToken(newToken)
    setFullName(newFullName)
  }

  const logout = () => {
    localStorage.removeItem('token')
    localStorage.removeItem('fullName')
    setToken(null)
    setFullName(null)
  }

  return (
    <AuthContext.Provider value={{ token, fullName, login, logout }}>
      {children}
    </AuthContext.Provider>
  )
}

export function useAuth() {
  return useContext(AuthContext)
}
