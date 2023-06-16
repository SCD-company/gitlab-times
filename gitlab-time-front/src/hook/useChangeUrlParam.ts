import { useCallback } from 'react';

export const useChangeUrlParam = () => {
  return useCallback((search: URLSearchParams, param: string, newParam?: string | string[]) => {
    if (search.has(param)) search.delete(param);

    if (newParam !== undefined)
      if (!Array.isArray(newParam)) search.set(param, newParam);
      else
        newParam.forEach((p) => {
          search.append(param, p);
        });
  }, []);
};
