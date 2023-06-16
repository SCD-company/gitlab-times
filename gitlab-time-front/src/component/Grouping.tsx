import React from 'react';
import Select from 'react-select';
import { GroupingByField } from '../rest/rest-client';

import { Styled as S } from './Grouping.stiled';

export interface GroupingProps {
  options: { value: GroupingByField; label: string }[];
  defaultValue: { value: GroupingByField; label: string }[];
  setSelected: (value: GroupingByField[]) => void;
}

export const Grouping: React.FC<GroupingProps> = ({ options, defaultValue, setSelected }) => {
  return (
    <S.Container>
      <S.Label>
        <p>Grouping</p>
        <hr />
      </S.Label>
      <S.Item>
        <label>Select grouping:</label>
        <S.Select>
          <Select
            defaultValue={defaultValue}
            options={options}
            isMulti={true}
            isClearable={true}
            isSearchable={false}
            onChange={(e) => setSelected(e.map((el) => el.value))}
          />
        </S.Select>
      </S.Item>
    </S.Container>
  );
};
