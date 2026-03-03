import axios from "axios";

const api = axios.create({
  baseURL: 'https://didactic-fiesta-5gpgwjjwrr4jh7rjg-8082.app.github.dev',
  timeout: 5000
});

api.interceptors.request.use((config) => {
  const token = localStorage.getItem('user-token');

  if (token) {
    config.headers.Authorization = `Bearer ${token}`;
  }
  return config;
},
(error) => {
  return Promise.reject(error);
})

api.interceptors.response.use(
  (response) => response,
  (error) => {
    if (error.response && error.response.status === 401) {
      // Clear token and redirect to login if session expires
      localStorage.removeItem('user-token');
    }
    return Promise.reject(error);
  }
);
export default api;