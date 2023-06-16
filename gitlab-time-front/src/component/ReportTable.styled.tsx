import styled from 'styled-components';

const Table = styled.table`
  display: grid;
  margin-block: 25px;
`;

const Row = styled.tr<{ backgroundColor?: string }>`
  display: flex;
  flex-direction: row;
  width: 100%;
  font-weight: ${(props) => (props.backgroundColor ? props.backgroundColor : 'white')};

  &:hover {
    background-color: lavender;
  }
`;

const Cell = styled.th<{ actual: boolean; bold?: boolean }>`
  padding: 7px;
  width: 100%;
  font-weight: ${(props) => (props.bold ? 'bold' : 'normal')};
  border-bottom: 1px solid lightgray;
  text-align: left;
  color: ${(props) => (props.actual ? 'inherit' : '#AAAAAA')};
`;

export const Styled = {
  Table,
  Row,
  Cell,
};
