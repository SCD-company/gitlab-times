import { Styled as S } from './Navbar.styled';
import { Button, Navbar as NavBar, NavItem } from 'react-bootstrap';

import React from 'react';
import { AuthUrl } from '../rest/Api';

export interface NavbarProps {
  isUserAuthenticated: boolean;
}

export const Navbar: React.FC<NavbarProps> = (props) => {
  return (
    <S.NavbarStyle>
      <NavBar>
        <S.NavbarBrand className="navbar-brand">
          <a href="https://scd-company.com.com/">
            <img src="logo-scd.svg" alt="SCD Company" />
          </a>
          <S.Headline>Gitlab time spent browser</S.Headline>
        </S.NavbarBrand>
        {props.isUserAuthenticated && (
          <S.LogoutButton>
            <NavItem>
              <a href={AuthUrl + 'logout'}>
                <Button variant="outline-light">Logout</Button>
              </a>
            </NavItem>
          </S.LogoutButton>
        )}

        {!props.isUserAuthenticated && (
          <S.LogoutButton>
            <NavItem>
              <a href={AuthUrl + 'login'}>
                <Button variant="outline-secondary">Login</Button>
              </a>
            </NavItem>
          </S.LogoutButton>
        )}
      </NavBar>
    </S.NavbarStyle>
  );
};
