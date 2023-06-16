import { useCallback } from 'react';
import { DateBoundaryDto, Period, ReportParametersRequestDto } from '../rest/rest-client';

export const useSetUrlParams = () => {
  const setParam = useCallback((search: URLSearchParams, param: string, newParam?: string | string[]) => {
    if (search.has(param)) search.delete(param);

    if (newParam !== undefined) {
      if (!Array.isArray(newParam)) {
        search.set(param, newParam);
      } else {
        newParam.forEach((p) => search.append(param, p));
      }
    }
  }, []);

  return useCallback(
    (data: ReportParametersRequestDto) => {
      const urlSearchParams = new URLSearchParams(window.location.search);

      setParam(urlSearchParams, 'projectId', data.projectId?.toString());
      setParam(urlSearchParams, 'userId', data.userId?.toString());
      setParam(
        urlSearchParams,
        'dateFrom',
        data.period === Period.CUSTOM ? timestampToDateString(data.dateFrom) : undefined
      );
      setParam(
        urlSearchParams,
        'dateTo',
        data.period === Period.CUSTOM ? timestampToDateString(data.dateTo) : undefined
      );
      setParam(urlSearchParams, 'period', data.period !== Period.CUSTOM ? data.period : undefined);
      setParam(
        urlSearchParams,
        'grouping',
        data.grouping?.map((g) => g.toString())
      );
      setParam(urlSearchParams, 'useSpentTime', data.useSpentTime === true ? String(true) : undefined);

      let newUrl =
        window.location.protocol +
        '//' +
        window.location.host +
        window.location.pathname +
        '?' +
        urlSearchParams.toString();
      window.history.pushState({ path: newUrl }, '', newUrl);
    },
    [setParam]
  );
};

const timestampToDateString = (timestamp: DateBoundaryDto | undefined) => {
  if (!timestamp) {
    return undefined;
  }
  return new Date(timestamp.year, timestamp.month, timestamp.day).toDateString();
};
