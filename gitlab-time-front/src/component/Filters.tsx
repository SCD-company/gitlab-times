import React, { useEffect, useState } from 'react';
import Select from 'react-select';
import { DateBoundaryDto, Period } from '../rest/rest-client';
import { DefaultValues, Options } from '../hook/useGetOptionsAndDefValues';
import { PeriodSelect } from './PeriodSelect';

import { Styled as S } from './Filters.styled';

export interface FiltersProps {
  setUser: (id: number | undefined) => void;
  setProject: (id: number | undefined) => void;
  setStartDay: (day: DateBoundaryDto | undefined) => void;
  setEndDay: (day: DateBoundaryDto | undefined) => void;
  setPeriod: (period: Period | undefined) => void;
  defaultValues: DefaultValues;
  options: Options;
}

export const Filters: React.FC<FiltersProps> = ({
  setUser,
  setProject,
  setStartDay,
  setEndDay,
  setPeriod,
  defaultValues,
  options,
}) => {
  const [key, setKey] = useState<{ userKey: string; projectKey: string }>({ userKey: 'user', projectKey: 'project' });

  // set default values
  useEffect(() => {
    setKey({ userKey: 'user' + defaultValues.user.value, projectKey: 'project' + defaultValues.project.value });
  }, [defaultValues, setProject, setUser]);

  return (
    <S.Container>
      <S.Label>
        <p>Filters</p>
        <hr />
      </S.Label>

      <S.FilterItem>
        <label>Person:</label>
        <S.Select>
          <Select
            defaultValue={defaultValues.user}
            options={options.users}
            isSearchable={true}
            key={key.userKey}
            onChange={(e) => setUser(e !== null ? e.value : undefined)}
          />
        </S.Select>
      </S.FilterItem>

      <S.FilterItem>
        <label>Project:</label>
        <S.Select>
          <Select
            defaultValue={defaultValues.project}
            options={options.projects}
            isSearchable={true}
            key={key.projectKey}
            onChange={(e) => setProject(e !== null ? e.value : undefined)}
          />
        </S.Select>
      </S.FilterItem>
      <PeriodSelect
        setStartDay={setStartDay}
        setEndDay={setEndDay}
        setPeriod={setPeriod}
        defaultValues={defaultValues}
        options={options}
      />
    </S.Container>
  );
};
