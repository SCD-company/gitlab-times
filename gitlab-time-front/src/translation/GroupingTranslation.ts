import { GroupingByField } from '../rest/rest-client';

export const GroupingTranslation: { [key in GroupingByField]: string } = {
  PERSON: 'Person',
  ISSUE: 'Issue',
  PROJECT: 'Project',
  MONTH: 'Month',
  MONTH_SPENT: 'Month',
};
