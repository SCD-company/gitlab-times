import styled from 'styled-components';

const NavbarStyle = styled.div`
  width: 100%;
  background-color: lightsteelblue;
  border-bottom: 1px solid mediumpurple;
  padding-left: 50px;
  padding-bottom: 10px;
  padding-top: 10px;
`;

const Headline = styled.div`
  font-family: tahoma;
  font-weight: bold;
  padding-top: 27px;
  padding-left: 30px;
  font-size: 19px;
  color: rgb(53, 68, 146);
`;

const NavbarBrand = styled.div`
  display: flex;
  flex-direction: row;
  width: 100%;

  img {
    height: 80px;
    vertical-align: bottom;
    margin-left: -8px;
  }

  span {
    font-family: Verdana;
    font-size: 14px;
    color: #354492;
  }

  a {
    text-decoration: none;
  }

  p {
    padding: 0;
    margin-top: 10px;
    margin-bottom: 0;
    height: 15px;
    font-family: Tahoma;
    font-size: 16px;
  }
`;

const LogoutButton = styled.div`
  display: flex;
  position: absolute;
  right: 50px;
`;

export const Styled = {
  NavbarStyle,
  NavbarBrand,
  LogoutButton,
  Headline,
};
