import Cookies from "js-cookie"
import useUserStore from "@/store/modules/user"

const TokenKey = "Admin-Token"

export function getToken() {
  return Cookies.get(TokenKey)
}

export function setToken(token) {
  return Cookies.set(TokenKey, token)
}

export function removeToken() {
  return Cookies.remove(TokenKey)
}

export function getTenant() {
  return useUserStore().tenantId
}

export function isPlatform() {
  return !+useUserStore().tenantId
}
