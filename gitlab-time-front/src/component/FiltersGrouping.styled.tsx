import styled from 'styled-components';

const Label = styled.div`
  display: inline-flex;

  hr {
    height: 1px;
    width: 100%;
    color: #282c34;
  }

  p {
    padding-top: 3px;
    margin-right: 10px;
  }
`;

const Container = styled.div`
  display: flex;
  flex-direction: column;
  height: 100%;
`;

const Item = styled.div`
  display: flex;
  justify-content: space-between;
  align-items: center;
  flex-wrap: wrap;
  margin-block: 10px;
  gap: 25px;
`;

const Select = styled.div`
  width: 260px;
  height: 40px;

  input {
    max-width: 260px;
    border: 1px solid lightgray;
    border-radius: 6px;
    padding: 6px;
    outline: none;
  }
`;

const FullWidthContainer = styled.div`
  display: flex;
  flex-direction: column;
  height: 100%;
  width: 100%;
`;

const Checkbox = styled.div`
  display: inline-flex;
  justify-content: left;
  align-items: center;
  flex-wrap: wrap;
  margin-block: 10px;
  gap: 10px;
  input {
    width: 16px;
    height: 16px;
    border: 1px solid lightgray;
    border-radius: 6px;
    padding: 6px;
    outline: none;
  }
`;

export const Styled = {
  Label,
  Container,
  Item,
  Select,
  Checkbox,
  FullWidthContainer,
};
