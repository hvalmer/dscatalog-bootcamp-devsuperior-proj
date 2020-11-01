import React from 'react';
import {BrowserRouter, Switch, Route } from 'react-router-dom';
import Admin from './pages/Admin';
import Home from './pages/Home';
import Catalog from './pages/Catalog';
import ProducDetails from './pages/Catalog/components/ProductDetails';
import Navbar from './core/components/Navbar';


const Routes = () => (

    <BrowserRouter>
        <Navbar/>
        <Switch>
            <Route path="/" exact>
                <Home/>
            </Route>
            <Route path="/products" exact>
                <Catalog/>
            </Route>
            <Route path="/products/:productId">
                <ProducDetails/>
            </Route>
            <Route path="/admin">
                <Admin/>
            </Route>
        </Switch>
    </BrowserRouter>
);

export default Routes;