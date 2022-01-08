import React, { Component } from 'react';
import {BrowserRouter as Router, Route, Switch} from 'react-router-dom';
import './App.css';

import Register from '../register/Register';
import Login from '../login/Login';
import Nav from '../nav/Nav'
// import Home from '../home/Home'
import Dashboard from '../dashboard/Dashboard';
import SelectMedication from "../selectMedication/SelectMedication";
import Health from "../health/Health";
import Account from "../account/Account";
import Advice from '../advice/Advice';
import Error from "../error/Error";

class App extends Component {
	constructor(props) {
		super();
		this.state = {
			loggedIn: localStorage.getItem('pharma-loggedIn'),
			canLogin: true,
			loginData: ""
		}
	}

	componentDidMount() {
		const loggedIn = localStorage.getItem('pharma-loggedIn');
		this.setState({loggedIn: loggedIn, canLogin: false})
	}

	render() {
		return (
			<div className="App d-flex flex-column">
				<Router>
					<main className="content">
						<Switch>
							<Route path="/" exact>
								<Login loggedIn={this.state.loggedIn} onResult={loginData => {if(loginData.jwt){this.setState({loggedIn: true})}this.setState({loginData})}} />
							</Route>
							<Route path="/register" exact component={Register} />
							<Route path="/dashboard" exact loggedIn={this.state.loggedIn} component={Dashboard} />
							<Route path="/selectMedication" exact component={SelectMedication}/>
							<Route path="/health" exact component={Health}/>
							<Route path="/account" exact component={Account}/>
							<Route path="/advice" exact component={Advice}/>
							<Route path="*" component={Error} />
						</Switch>
					</main>

					{this.state.loggedIn ? <Nav loginStatus={this.state.loggedIn} /> : ''}
					
					
				</Router>
			</div>
		);
	}
}

export default App;
