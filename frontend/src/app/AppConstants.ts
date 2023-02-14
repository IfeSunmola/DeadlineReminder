export const API_URL = "http://localhost:8080";
export const VERIFY_CODE_LENGTH = 5
export const SUCCESS = "Success";
export const INCORRECT = "Incorrect";
export const EXPIRED = "Expired";
export const INVALID_REQUEST = "InvalidRequest";
// TODO: Check if db can handle encrypted password of this long
export const MIN_PASSWORD_LENGTH = 8
export const MAX_PASSWORD_LENGTH = 250
export const MAX_NICKNAME_LENGTH = 10
export const AUTH_TOKEN = "AuthToken"
// login stuff
export const LOGIN_NEEDED = "loginNeeded"
export const LOGIN_NEEDED_MESSAGE = "Login is needed for that page"
export const DISABLED_ACCOUNT = "Account is disabled"
export const DISABLED_ACCOUNT_MESSAGE = "Verification is needed to access your account"
export const INVALID_CREDENTIALS = "Invalid username or password"
// Logout stuff
export const NORMAL_LOGOUT = "normalLogout"
export const NORMAL_LOGOUT_MESSAGE = "Logout Successful"
// logged in before
export const INVALID_SESSION = "invalidSession";
export const INVALID_SESSION_MESSAGE = "Your session is invalid. Please login again."