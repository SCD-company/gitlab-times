import { DateBoundaryDto, GroupingByField, Period, ReportParametersRequestDto } from '../rest/rest-client';

function convertDate(curDate: Date): DateBoundaryDto {
  return { year: curDate.getFullYear(), month: curDate.getMonth(), day: curDate.getDate() };
}

export const getUrlParams = (urlSearchParams: URLSearchParams) => {
  const paramValueSetters: { [key: string]: (data: ReportParametersRequestDto, params: URLSearchParams) => void } = {
    grouping: (data, params) => (data.grouping = params.getAll('grouping') as GroupingByField[]),
    period: (data, params) => (data.period = params.get('period') as Period),
    userId: (data, params) => (data.userId = Number.parseInt(params.get('userId')!)),
    projectId: (data, params) => (data.projectId = Number.parseInt(params.get('projectId')!)),
    dateFrom: (data, params) => (data.dateFrom = convertDate(new Date(params.get('dateFrom')!))),
    dateTo: (data, params) => (data.dateTo = convertDate(new Date(params.get('dateTo')!))),
    useSpentTime: (data, params) => (data.useSpentTime = params.get('useSpentTime') === 'true'),
  };

  const res = Object.entries(paramValueSetters)
    .filter(([param]) => urlSearchParams.has(param))
    .reduce((data: ReportParametersRequestDto, [_, setter]) => {
      setter(data, urlSearchParams);
      return data;
    }, {});
  if (res.dateFrom !== undefined || res.dateTo !== undefined) {
    res.period = Period.CUSTOM;
  }
  return res;
};
