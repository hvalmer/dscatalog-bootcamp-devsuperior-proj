import React from 'react';
import {BrowserRouter, Switch, Route } from 'react-router-dom';
import Admin from './pages/Admin';
import Home from './pages/Home';
import Catalog from './pages/Catalog';
import Navbar from './core/components/Navbar';


const Routes = () => (

    <BrowserRouter>
        <Navbar/>
        <Switch>
            <Route path="/" exact>
                <Home/>
            </Route>
            <Route path="/catalog">
                <Catalog/>
            </Route>
            <Route path="/admin">
                <Admin/>
            </Route>
        </Switch>
    </BrowserRouter>
);

export default Routes;