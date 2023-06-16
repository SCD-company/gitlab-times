import React from 'react';
import { Styled as S } from './LoginPage.styled';
import { AuthUrl } from '../rest/Api';

export const LoginPage: React.FC = () => (
  <S.Panel>
    Welcome to the spent time browser for Gitlab.
    <br />
    Please <a href={AuthUrl + 'login'}>login</a>
  </S.Panel>
);
