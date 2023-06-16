import { DateBoundaryDto, Period } from '../rest/rest-client';
import React, { useEffect, useState } from 'react';
import Select from 'react-select';
import { DPicker } from './DPicker';
import { DefaultValues, Options } from '../hook/useGetOptionsAndDefValues';
import { Styled as S } from './Filters.styled';

export interface PeriodSelectProps {
  setStartDay: (day: DateBoundaryDto | undefined) => void;
  setEndDay: (day: DateBoundaryDto | undefined) => void;
  setPeriod: (period: Period | undefined) => void;
  defaultValues: DefaultValues;
  options: Options;
}

export const PeriodSelect: React.FC<PeriodSelectProps> = ({
  setStartDay,
  setEndDay,
  setPeriod,
  defaultValues,
  options,
}) => {
  const [showDatePicker, setShowDatePicker] = useState(false);
  const [chosenFirstDate, setChosenFirstDate] = useState(new Date());
  const [chosenSecondDate, setChosenSecondDate] = useState(new Date());

  const [key, setKey] = useState<string>('');

  useEffect(() => {
    setPeriod(defaultValues.period.value);
    setShowDatePicker(defaultValues.period.value === Period.CUSTOM);
    setChosenFirstDate(defaultValues.dateFrom);
    setChosenSecondDate(defaultValues.dateTo);

    setKey(defaultValues.period.label);
  }, [defaultValues, setEndDay, setPeriod, setStartDay]);

  return (
    <>
      <S.FilterItem>
        <label> Period: </label>
        <S.Select>
          <Select
            defaultValue={defaultValues.period}
            options={options.periods}
            isSearchable={true}
            onChange={(e) => {
              if (e !== null) {
                let curDate = new Date();
                curDate.setHours(0, 0, 0, 0);
                setShowDatePicker(e.value === Period.CUSTOM);
                setChosenFirstDate(curDate);
                setChosenSecondDate(curDate);
                setStartDay(
                  e.value === Period.CUSTOM
                    ? { year: curDate.getFullYear(), month: curDate.getMonth(), day: curDate.getDate() }
                    : undefined
                );
                setEndDay(
                  e.value === Period.CUSTOM
                    ? { year: curDate.getFullYear(), month: curDate.getMonth(), day: curDate.getDate() }
                    : undefined
                );
                setPeriod(e.value);
              }
            }}
            key={key}
          />
        </S.Select>
      </S.FilterItem>
      {showDatePicker && (
        <>
          <S.FilterItem>
            <label>From: </label>
            <DPicker
              selected={chosenFirstDate}
              handleOnChange={(e) => {
                setStartDay({ year: e.getFullYear(), month: e.getMonth(), day: e.getDate() });
                setChosenFirstDate(e);
              }}
            />
          </S.FilterItem>
          <S.FilterItem>
            <label>To: </label>
            <DPicker
              selected={chosenSecondDate}
              handleOnChange={(e) => {
                setEndDay({ year: e.getFullYear(), month: e.getMonth(), day: e.getDate() });
                setChosenSecondDate(e);
              }}
            />
          </S.FilterItem>
        </>
      )}
    </>
  );
};
