import styled from 'styled-components';

const Page = styled.div`
  display: flex;
  flex-direction: column;
  margin-inline: 50px;
  height: 100%;
  margin-top: 50px;
`;

const Label = styled.h2`
  display: flex;
  justify-content: center;
  padding-bottom: 3px;
  background: linear-gradient(to left, white, lavender, white);
`;

export const Styled = {
  Page,
  Label,
};
