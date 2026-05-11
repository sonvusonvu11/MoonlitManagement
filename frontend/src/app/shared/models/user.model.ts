export interface User {
  userID: number;
  username: string;
  email: string;
  fullName: string;
  phone?: string;
  role: 'ADMIN' | 'STAFF' | 'CUSTOMER';
}

export interface LoginRequest {
  username: string;
  password: string;
}

export interface RegisterRequest {
  username: string;
  email: string;
  password: string;
  confirmPassword: string;
  fullName: string;
  phone?: string;
}

export interface LoginResponse {
  accessToken: string;
  refreshToken: string;
  tokenType: string;
  expiresInMs: number;
  user: User;
}

export interface UpdateProfileRequest {
  fullName: string;
  email: string;
  phone?: string;
  address?: string;
  dateOfBirth?: string;
}

export interface ChangePasswordRequest {
  oldPassword: string;
  newPassword: string;
}
