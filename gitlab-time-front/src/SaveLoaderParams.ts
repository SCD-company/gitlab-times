import { getUrlParams } from './page/getUrlParams';
import { ReportParametersRequestDto } from './rest/rest-client';

export const loadParams = (setUrl: (data: ReportParametersRequestDto) => void) => {
  const search =
    window.location.search === '' ? (localStorage.getItem('paramsInMemory') as string) : window.location.search;
  localStorage.removeItem('paramsInMemory');
  setUrl(getUrlParams(new URLSearchParams(search)));
};

export const saveParams = () => {
  if (window.location.pathname.toLowerCase() === '/report' && window.location.search !== '') {
    localStorage.setItem('paramsInMemory', window.location.search);
  }
};
