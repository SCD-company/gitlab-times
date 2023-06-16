import React from 'react';
import DatePicker from 'react-datepicker';
import 'react-datepicker/dist/react-datepicker.css';

import { Styled as S } from './Filters.styled';

export interface DPickerProps {
  selected: Date;
  handleOnChange: (day: Date) => void;
}

export const DPicker: React.FC<DPickerProps> = ({ selected, handleOnChange }) => {
  return (
    <S.Select>
      <DatePicker dateFormat="dd/MM/yyyy" selected={selected} selectsEnd={false} onChange={handleOnChange} />
    </S.Select>
  );
};
