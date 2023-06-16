import styled from 'styled-components';
import { Styled as S } from './Page.Styled';

const Page = S.Page;

const Label = S.Label;

const FiltersBlockGroup = styled.div`
  display: flex;
  flex-direction: row;
  justify-content: space-between;
  align-items: flex-start;
  margin-bottom: 25px;
  gap: 20px;
  height: 320px;
`;

const FiltersBlockGroupVertical = styled.div`
  display: flex;
  flex-direction: column;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 25px;
  gap: 60px;
`;

const ButtonsGroup = styled.div`
  width: 100%;
  display: flex;
  justify-content: flex-end;
  gap: 15px;
`;

export const Styled = {
  Page,
  FiltersBlockGroup,
  ButtonsGroup,
  Label,
  FiltersBlockGroupVertical,
};
