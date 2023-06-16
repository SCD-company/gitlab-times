import { Period } from '../rest/rest-client';

export const PeriodTranslation: { [key in Period]: string } = {
  CUR_MONTH: 'Current month',
  CUR_WEEK: 'Current week',
  LAST_MONTH: 'Last Month',
  LAST_WEEK: 'Last week',
  CUSTOM: 'Custom',
};
