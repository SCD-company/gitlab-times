import React, { useEffect, useState } from 'react';
import './App.css';
import { BrowserRouter, Redirect, Route, Switch } from 'react-router-dom';
import { ReportPage } from './page/ReportPage';
import 'bootstrap/dist/css/bootstrap.min.css';
import { Navbar } from './component/Navbar';
import { LoginPage } from './page/LoginPage';
import { RestApi } from './rest/Api';
import { UserDetailsDto } from './rest/rest-client';
import { useSetUrlParams } from './hook/useSetUrlParams';
import { loadParams, saveParams } from './SaveLoaderParams';
import { RenderAfterEffect } from './component/RenderAfterEffect';

const App: React.FC = () => {
  useEffect(() => {
    document.title = 'gitlab time';
  }, []);

  const [authUser, setAuthUser] = useState<UserDetailsDto>({
    authenticated: false,
    admin: false,
    id: -1,
    name: '',
  });
  const setUrl = useSetUrlParams();

  //check existing session
  useEffect(() => {
    RestApi.getUser()
      .then((response) => {
        setAuthUser({
          authenticated: response.data.authenticated,
          admin: response.data.admin,
          id: response.data.id,
          name: response.data.name,
        });
      })
      .catch((err) => {
        setAuthUser({
          authenticated: false,
          admin: false,
          id: -1,
          name: '',
        });
        console.log(err);
      });
  }, []);

  return (
    <div className="App">
      <Navbar isUserAuthenticated={authUser.authenticated} />
      <BrowserRouter>
        <Switch>
          <Route exact path="/">
            {authUser.authenticated ? <Redirect to="/report" /> : <Redirect to="/login" />}
          </Route>
          <Route path="/report">
            {authUser.authenticated ? (
              <RenderAfterEffect effect={() => loadParams(setUrl)} children={<ReportPage userInfo={authUser} />} />
            ) : (
              <RenderAfterEffect effect={saveParams} children={<Redirect from="/report" to="/login" />} />
            )}
          </Route>
          <Route path="/login">
            {authUser.authenticated ? <Redirect from="/login" to="/report" /> : <LoginPage />}
          </Route>

          {/*  Page Not Found*/ authUser.authenticated ? <Redirect to="/report" /> : <Redirect to="/login" />}
        </Switch>
      </BrowserRouter>
    </div>
  );
};

export default App;
