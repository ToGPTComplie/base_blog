import client, { Result } from './client';

export interface UserLoginRequest {
  username: string;
  password?: string;
}

export interface UserRegisterRequest {
  username: string;
  password?: string;
  nickname?: string;
  email?: string;
  mobile?: string;
}

export interface UserResponse {
  username: string;
  nickname?: string;
  id?: number;
}

export const login = (data: UserLoginRequest) => {
  return client.post<any, Result<string>>('/auth/login', data);
};

export const register = (data: UserRegisterRequest) => {
  return client.post<any, Result<UserResponse>>('/auth/register', data);
};

export const testLogin = () => {
  return client.post<any, Result<UserResponse>>('/auth/testLogin');
};
