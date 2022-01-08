import React, { Component } from 'react';
import Swal from 'sweetalert2';

class AccountInfo extends Component {
	constructor(props) {
		super();
		this.state = {
			active: false,
			user: {}
		}

		this.editEmail = this.editEmail.bind(this);
		this.cancelEdit = this.cancelEdit.bind(this);
		this.updateEmail = this.updateEmail.bind(this);
	}

	static getDerivedStateFromProps(nextProps){
		return {user: nextProps.user};
	}

	editEmail(){
		this.setState({
			active: true
		})
	}

	cancelEdit(){
		this.setState({
			active: false
		})
	}

	updateEmail(e){
		e.preventDefault();

		let email = e.target.email.value;

		let formData = new URLSearchParams();
		formData.append('personId', 41);
		formData.append('email', email);

		const Toast = Swal.mixin({
			toast: true,
			position: 'top',
			showConfirmButton: false,
			timer: 1500,
			timerProgressBar: true
		});

		fetch("http://localhost:8080/account/change", {
			method: 'PUT',
			mode: 'cors',
			headers: {
				'Content-Type': 'application/x-www-form-urlencoded',
			},
			body: formData
		})
		.then(result => result.status === 200 ? result.json() : Promise.reject(result))
		.then(data => {
			if (data) {
				Toast.fire({
					icon: 'success',
					title: data.message
				});
				this.cancelEdit();
			}
			
		})
		.catch(result => result.json())
		.then(data => {
			if(data) {
				Toast.fire({
					icon: 'warning',
					title: data.message
				});
			}
		})
		
	}

	render() {
		return <div>
			<div className="account-header">Account</div>
			<div>
				<form onSubmit={this.updateEmail}>
					{this.state.active &&
					<div className="input-container">
						<label htmlFor="email">Email</label>
						<input id="email" name="email" defaultValue={this.state.user.email}/>
					</div>
					}
					{!this.state.active &&
					<div className="input-container">
						<label htmlFor="email">Email</label>
						<input id="email" name="email" disabled defaultValue={this.state.user.email}/>
					</div>
					}
					<div className="input-container">
						<label htmlFor="voornaam">Voornaam</label>
						<input id="voornaam" name="voornaam" disabled defaultValue={this.state.user.firstname}/>
					</div>
					<div className="input-container">
						<label htmlFor="achternaam">Achternaam</label>
						<input id="achternaam" name="achternaam" disabled defaultValue={this.state.user.lastname}/>
					</div>
					<div className="input-container">
						<label htmlFor="geboortedatum">Geboortedatum</label>
						<input id="geboortedatum" name="geboortedatum" disabled defaultValue={this.state.user.dateOfBirth}/>
					</div>

					{this.state.active &&
						<div className="btn-group account-buttons">
							<button type="submit" className="btn btn-primary">Opslaan</button>
							<button type="button" onClick={this.cancelEdit} className="btn btn-danger">Annuleren</button>
						</div>
					}
					{!this.state.active &&
						<div className="btn-group account-buttons">
							<button onClick={this.editEmail} type="button" className="btn btn-primary">Verander email</button>
						</div>
					}
				</form>
			</div>
		</div>
	}
}

export default AccountInfo;
