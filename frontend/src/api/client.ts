import axios from 'axios';

export interface Result<T> {
  code: number;
  message: string;
  data: T;
}

const client = axios.create({
  baseURL: '/api',
  timeout: 10000,
});

client.interceptors.response.use(
  (response) => {
    return response.data;
  },
  (error) => {
    if (error.response && error.response.status === 401) {
       // Redirect to login if not already there
       if (!window.location.pathname.startsWith('/login') && !window.location.pathname.startsWith('/register')) {
           window.location.href = '/login';
       }
    }
    return Promise.reject(error);
  }
);

export default client;
