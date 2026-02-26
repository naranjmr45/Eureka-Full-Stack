import axios from "axios";
const api = axios.create({
  baseURL: 'https://didactic-fiesta-5gpgwjjwrr4jh7rjg-8082.app.github.dev',
  timeout: 5000
});
export default api;