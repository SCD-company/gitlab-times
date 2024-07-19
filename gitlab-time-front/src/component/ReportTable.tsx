import React, { useEffect, useState } from 'react';
import { Styled as S } from './ReportTable.styled';
import { GroupingByField, GroupingReportDto } from '../rest/rest-client';
import { GroupingTranslation } from '../translation/GroupingTranslation';

export interface ReportTableProps {
  tableData: GroupingReportDto[];
}

const getGrouping = (res: GroupingReportDto[], groups: GroupingByField[]): GroupingByField[] => {
  return !!res.length ? groups.concat(res[0].type, getGrouping(res[0]!.subGroup, groups)) : [];
};

export const ReportTable: React.FC<ReportTableProps> = ({ tableData }) => {
  const [groupings, setGroupings] = useState<GroupingByField[]>([]);

  useEffect(() => {
    setGroupings(getGrouping(tableData, []));
  }, [tableData]);

  const getRows: (data: GroupingReportDto[]) => JSX.Element[] = (data: GroupingReportDto[]) =>
    data.map((d) => (
      <>
        <S.Row>
          {groupings.map((column) => (
            <S.Cell actual={d.actual}>{column === d.type && d.names.join(' ')}</S.Cell>
          ))}
          <S.Cell actual={d.actual}>{d.time.toFixed(2)}</S.Cell>
        </S.Row>
        {getRows(d.subGroup)}
      </>
    ));

  const countTotalTime = (data: GroupingReportDto[]) => {
    return data.reduce((count, d) => count + d.time, 0).toFixed(2);
  };

  return (
    <S.Table>
      <S.Row>
        {groupings.map((group) => (
          <S.Cell actual bold>
            {GroupingTranslation[group]}
          </S.Cell>
        ))}
        <S.Cell actual bold>
          Time
        </S.Cell>
      </S.Row>
      {getRows(tableData)}
      <S.Row>
        {groupings.map((column) => (
          <S.Cell actual bold>
            {column === groupings[0] && 'Total Time'}
          </S.Cell>
        ))}
        <S.Cell actual bold>
          {countTotalTime(tableData)}
        </S.Cell>
      </S.Row>
    </S.Table>
  );
};
