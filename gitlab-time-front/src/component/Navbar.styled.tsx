import styled from 'styled-components';

const NavbarStyle = styled.div`
  width: 100%;
  background-color: lightsteelblue;
  border-bottom: 1px solid mediumpurple;
  padding-left: 50px;
  padding-bottom: 10px;
  padding-top: 10px;
`;

const NavbarBrand = styled.div`
  display: flex;
  flex-direction: column;
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
};
