import { GroupingByField, Period, ReportParametersRequestDto, UserDetailsDto } from '../rest/rest-client';

import { RestApi } from '../rest/Api';
import { PeriodTranslation } from '../translation/PeriodTranslation';
import { GroupingTranslation } from '../translation/GroupingTranslation';
import { useCallback, useRef } from 'react';

export type DefaultValues = {
  grouping: { value: GroupingByField; label: string }[];
  user: { value?: number; label: string };
  project: { value?: number; label: string };
  period: { value: Period | undefined; label: string };
  dateFrom: Date;
  dateTo: Date;
  useSpentTime: boolean;
};

export type Options = {
  grouping: { value: GroupingByField; label: string }[];
  users: { value?: number; label: string }[];
  projects: { value?: number; label: string }[];
  periods: { value: Period | undefined; label: string }[];
};

export type OptionsDefaultValues = {
  defaultValues: DefaultValues;
  options: Options;
};

export const all = { value: undefined, label: '-- All' };

const getOptions = async (data: ReportParametersRequestDto, curUser: UserDetailsDto) => {
  const optionsResponse = (await RestApi.getOptions(data)).data;
  const users = [all, ...optionsResponse.users.map((userDto) => ({ value: userDto.id, label: userDto.name }))];
  const projects = [
    all,
    ...optionsResponse.projects.map((projectDto) => ({ value: projectDto.id, label: projectDto.name })),
  ];

  const options: Options = {
    grouping: [],
    users,
    projects,
    periods: [all],
  };

  options.periods = [
    all,
    ...(Object.keys(Period) as Period[]).map((p) => ({
      value: p,
      label: PeriodTranslation[p],
    })),
  ];

  options.grouping = [
    ...(Object.keys(GroupingByField) as GroupingByField[])
      .filter((g) => g !== GroupingByField.MONTH_SPENT)
      //.filter((g) =>  g !== GroupingByField.PERSON)
      .map((g) => ({
        value: g,
        label: GroupingTranslation[g],
      })),
  ];

  return options;
};

const fillDefaultValues = (
  defaultValues: DefaultValues,
  data: ReportParametersRequestDto,
  options: Options,
  curUser: UserDetailsDto
) => {
  // set default grouping
  data.grouping?.forEach((d) => {
    // if (!curUser.admin && d === GroupingByField.PERSON) {
    //   return;
    // }
    defaultValues.grouping.push({ value: d, label: GroupingTranslation[d] });
  });
  if (defaultValues.grouping.length === 0) {
    defaultValues.grouping.push({
      value: GroupingByField.PROJECT,
      label: GroupingTranslation[GroupingByField.PROJECT],
    });
  }

  // set default user
  if (data.userId !== undefined) {
    options.users.forEach((u) => {
      if (u.value === data.userId) {
        defaultValues.user = u;
      }
    });
  }

  //set default project
  if (data.projectId !== undefined) {
    options.projects.forEach((p) => {
      if (p.value === data.projectId) {
        defaultValues.project = p;
      }
    });
  }

  //set default start day
  if (data.dateFrom !== undefined) {
    defaultValues.dateFrom = new Date(data.dateFrom.year, data.dateFrom.month, data.dateFrom.day);
    //defaultValues.period = custom;
  }

  //set default end day
  if (data.dateTo !== undefined) {
    defaultValues.dateTo = new Date(data.dateTo.year, data.dateTo.month, data.dateTo.day);
    //defaultValues.period = custom;
  }

  //set default period
  if (data.dateFrom === undefined && data.dateTo === undefined && data.period !== undefined) {
    defaultValues.period = {
      value: data.period,
      label: data.period !== null ? PeriodTranslation[data.period] : 'Custom',
    };
  }

  defaultValues.useSpentTime = data.useSpentTime === true;
};

export const getDefaultDefaultValues = (curUser: UserDetailsDto) => {
  return {
    options: {
      grouping: [],
      users: [all],
      projects: [all],
      periods: [all],
    },
    defaultValues: {
      grouping: [],
      user: all,
      project: all,
      period: all,
      dateFrom: new Date(),
      dateTo: new Date(),
      useSpentTime: false,
    },
  };
};

export const useGetOptionsAndDefValues = (curUser: UserDetailsDto) => {
  const maxRequestNumber = useRef<number>(-1);
  const maxResponseNumber = useRef<number>(-1);
  const lastAcceptedResult = useRef<{ options: Options; defaultValues: DefaultValues }>();

  return useCallback(
    async (data: ReportParametersRequestDto) => {
      let requestNumber = ++maxRequestNumber.current;

      const options = await getOptions(data, curUser);

      const defaultValues: DefaultValues = getDefaultDefaultValues(curUser).defaultValues;
      fillDefaultValues(defaultValues, data, options, curUser);

      let res = { options: options, defaultValues: defaultValues };
      if (requestNumber > maxResponseNumber.current) {
        maxResponseNumber.current = requestNumber;
        lastAcceptedResult.current = res;
      } else {
        res = lastAcceptedResult.current as { options: Options; defaultValues: DefaultValues };
      }
      return res;
    },
    [curUser]
  );
};
