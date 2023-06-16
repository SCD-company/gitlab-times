import { AxiosRestApplicationClient } from './rest-client';
import axios from 'axios';

const instance = axios.create({ withCredentials: true });

instance.interceptors.response.use(
  (response) => response,
  (error) => {
    const status = error.response?.status;

    const url = error.response?.config.url;

    if (status === 500) {
      alert(
        'Internal server error while trying to access ' + url + ' \n\nwith data \n\n' + error.response?.config.data
      );
    } else if (status === 400) {
      alert(
        'The server has considered the request as bad, while trying to access ' +
          url +
          ' \n\nwith data \n\n' +
          error.response?.config.data
      );
    }

    return Promise.reject(error);
  }
);

const API_ENDPOINT = import.meta.env.VITE_API_ENDPOINT;

export const RestApi = new AxiosRestApplicationClient(API_ENDPOINT || '/api', instance);
export const AuthUrl = API_ENDPOINT + 'auth/';
