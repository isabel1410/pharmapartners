import React, { Component } from 'react';
import { withRouter } from "react-router-dom";
import AccountInfo from './AccountInfo';

import './Account.css';
class Account extends Component {
	constructor(props){
		super()

		this.state = {
			user: {},
			loaded: false
		}

		this.logout = this.logout.bind(this);
	}

	componentDidMount() {
		document.title = 'Account';
		if (localStorage.getItem('pharma-loggedIn') === 'false' || localStorage.getItem('pharma-loggedIn') === null) {
			this.props.history.push("/");
			return;
		}

		fetch("http://localhost:8080/account/41")
		.then(result => result.json())
		.then(data => {
			this.setState({
				user: {
					email: data.account.email,
					firstname: data.account.person.firstname,
					lastname: data.account.person.lastname,
					dateOfBirth: data.account.person.dateOfBirth
				},
				loaded: true
			})
		})
		
	}

	
	logout() {
		localStorage.setItem('pharma-loggedIn', 'false')
	}

	render() {
		return <div className="account-container">
			<AccountInfo user={this.state.user} />
			<div className="btn btn-danger logout-button" onClick={this.logout}>Uitloggen</div>
		</div>
	}
}

export default withRouter(Account);
