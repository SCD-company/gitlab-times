import { Button } from 'react-bootstrap';
import React, { useCallback, useEffect, useMemo, useState } from 'react';
import { GroupingReportDto, Period, ReportParametersRequestDto, UserDetailsDto } from '../rest/rest-client';
import { RestApi } from '../rest/Api';
import { useDownloadFile } from '../hook/useDownloadFile';
import { useSetUrlParams } from '../hook/useSetUrlParams';
import { getUrlParams } from './getUrlParams';
import { OptionsDefaultValues, useGetOptionsAndDefValues } from '../hook/useGetOptionsAndDefValues';

import { Styled as S } from './ReportPage.styled';
import { ReportTable } from '../component/ReportTable';
import { ReportForm } from '../component/ReportForm';

export interface ReportPageProps {
  userInfo: UserDetailsDto;
}

export const ReportPage: React.FC<ReportPageProps> = ({ userInfo }) => {
  const paramsFromUrl = useMemo<ReportParametersRequestDto>(() => {
    const res = getUrlParams(new URLSearchParams(window.location.search));
    if (JSON.stringify(res) === JSON.stringify({})) {
      res.period = Period.LAST_MONTH;
    }
    return res;
  }, []);
  const [optionsAndDefaultValues, setOptionsAndDefaultValues] = useState<OptionsDefaultValues>();

  const [reportTableData, setReportTableData] = useState<GroupingReportDto[]>([]);
  const [reportTableKey, setReportTableKey] = useState<string>('');

  // callbacks
  const getOptionsAndDefValues = useGetOptionsAndDefValues(userInfo);
  const downloadPdf = useDownloadFile('application/pdf');
  const downloadCsv = useDownloadFile('text/csv');
  const setUrl = useSetUrlParams();

  useEffect(() => {
    const params = paramsFromUrl;

    getOptionsAndDefValues(params).then(setOptionsAndDefaultValues);
  }, [getOptionsAndDefValues, paramsFromUrl, userInfo]);

  const [reportParams, setReportParams] = useState<ReportParametersRequestDto>(paramsFromUrl);

  const onFilter = useCallback(
    (requestParams) => {
      setReportParams(requestParams);
      setUrl(requestParams);

      if (
        requestParams.dateFrom &&
        requestParams.dateTo &&
        new Date(requestParams.dateFrom.year, requestParams.dateFrom.month, requestParams.dateFrom.day).getTime() >=
          new Date(requestParams.dateTo.year, requestParams.dateTo.month, requestParams.dateTo.day).getTime()
      ) {
        setReportTableData([]);
        setReportTableKey(new Date().toDateString());
      } else {
        RestApi.getReportWithGrouping(requestParams).then((response) => {
          setReportTableData(response.data);
          setReportTableKey(new Date().toDateString());
        });
      }
    },
    [setUrl]
  );

  const getFile = useCallback(() => {
    RestApi.getCsvReport(reportParams).then((response) => downloadCsv(response.data.fileName, response.data.content));
  }, [downloadCsv, reportParams]);

  const getPdfFile = useCallback(() => {
    RestApi.getPdfReport(reportParams).then((response) => downloadPdf(response.data.fileName, response.data.content));
  }, [downloadPdf, reportParams]);

  return (
    <S.Page>
      {optionsAndDefaultValues !== undefined && (
        <ReportForm
          defaultValues={optionsAndDefaultValues.defaultValues}
          options={optionsAndDefaultValues.options}
          onFilter={onFilter}
        />
      )}
      <hr />
      <S.ButtonsGroup>
        <Button onClick={getFile} disabled={reportTableData.length === 0}>
          {' '}
          Download as .csv{' '}
        </Button>
        <Button onClick={getPdfFile} disabled={reportTableData.length === 0}>
          Download as .pdf
        </Button>
      </S.ButtonsGroup>
      <ReportTable tableData={reportTableData} key={reportTableKey} />
    </S.Page>
  );
};
