import { Filters } from './Filters';
import { Grouping } from './Grouping';
import { Styled as S } from '../page/ReportPage.styled';
import React, { useEffect, useState } from 'react';
import { all, DefaultValues, Options } from '../hook/useGetOptionsAndDefValues';
import { DateBoundaryDto, GroupingByField, Period, ReportParametersRequestDto } from '../rest/rest-client';
import { RestApi } from '../rest/Api';
import { CheckboxUseSpentAt } from './CheckboxUseSpentAt';

interface ReportFormProps {
  defaultValues: DefaultValues;
  options: Options;
  onFilter: (requestParams: ReportParametersRequestDto) => void;
}

function convertDate(curDate: Date): DateBoundaryDto {
  return { year: curDate.getFullYear(), month: curDate.getMonth(), day: curDate.getDate() };
}

export const ReportForm: React.FC<ReportFormProps> = (props) => {
  const defaultValues = props.defaultValues;

  const onFilter = props.onFilter;

  const [options, setOptions] = useState(props.options);

  const [grouping, setGrouping] = useState<GroupingByField[]>(
    props.defaultValues.grouping !== undefined ? defaultValues.grouping.map((g) => g.value) : []
  );

  const [userId, setUser] = useState<number | undefined>(defaultValues.user.value);
  const [projectId, setProject] = useState<number | undefined>(defaultValues.project.value);
  const [period, setPeriod] = useState<Period | undefined>(defaultValues.period.value);
  const [dateFrom, setDateFrom] = useState<DateBoundaryDto | undefined>(
    defaultValues.period.value === Period.CUSTOM ? convertDate(defaultValues.dateFrom) : undefined
  );
  const [dateTo, setDateTo] = useState<DateBoundaryDto | undefined>(
    defaultValues.period.value === Period.CUSTOM ? convertDate(defaultValues.dateTo) : undefined
  );
  const [usingSpentTime, setUsingSpentTime] = useState<boolean>(defaultValues.useSpentTime);

  useEffect(() => {
    const requestParams: ReportParametersRequestDto = {
      userId,
      projectId,
      period,
      dateFrom,
      dateTo,
      grouping,
      useSpentTime: usingSpentTime,
    };

    onFilter(requestParams);
    RestApi.getOptions(requestParams).then((response) => {
      const optionsResponse = response.data;
      setOptions((prevOptions) => ({
        ...prevOptions,
        users: [all, ...optionsResponse.users.map((userDto) => ({ value: userDto.id, label: userDto.name }))],
        projects: [
          all,
          ...optionsResponse.projects.map((projectDto) => ({
            value: projectDto.id,
            label: projectDto.name,
          })),
        ],
      }));
    });
  }, [dateFrom, dateTo, grouping, onFilter, period, projectId, userId, usingSpentTime]);

  return (
    <S.FiltersBlockGroup>
      <Filters
        defaultValues={defaultValues}
        options={options}
        setProject={setProject}
        setUser={setUser}
        setStartDay={setDateFrom}
        setEndDay={setDateTo}
        setPeriod={setPeriod}
      />

      <S.FiltersBlockGroupVertical>
        <Grouping setSelected={setGrouping} defaultValue={defaultValues.grouping} options={options.grouping} />

        <CheckboxUseSpentAt value={usingSpentTime} setValue={setUsingSpentTime} />
      </S.FiltersBlockGroupVertical>
    </S.FiltersBlockGroup>
  );
};
