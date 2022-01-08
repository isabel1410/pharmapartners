import React, { Component } from 'react';
import './Dashboard.css';
import { withRouter } from "react-router-dom";

import * as TiIcons from "react-icons/ti";
import TextField from '@material-ui/core/TextField';
import Autocomplete from '@material-ui/lab/Autocomplete';
import Swal from 'sweetalert2';
import {Spinner, Card} from 'react-bootstrap';

import {generateAdvice, getAdvice, prepareAdvice} from "./Util";

class Dashboard extends Component {
	
    //Create constructor
    constructor(props) {
		super();
		this.adviceEnum = {
			RED: 'red',
			YELLOW: 'yellow',
			GREEN: 'green',
			GREY: 'grey',
		}

        this.state = {
            loadingMedicationList: false,
            dataMedicationList: null,
            loadingSelectedMedication: false,
			dataSelectedMedication: {},
			loadingAdvice: false,
			dataAdvice: null,
			adviceStatus: this.adviceEnum.GREY,
        };

		this.selectMedication = this.selectMedication.bind(this);
		this.advicePreparation = this.advicePreparation.bind(this);
		this.adviceGetter = this.adviceGetter.bind(this);
		this.redirectAdvice = this.redirectAdvice.bind(this);
    }

    //Run when loading is true
    componentDidMount() {
		document.title = 'Overzicht';
		console.log(localStorage.getItem('pharma-loggedIn'))
		if (localStorage.getItem('pharma-loggedIn') === 'false' || localStorage.getItem('pharma-loggedIn') === null) {
			this.props.history.push("/");
			return;
		}

        this.getAllMedication();
		this.getAllSelectedMedication();

		//Get advice
		this.adviceGetter();

		let lastCheckDate = localStorage.getItem('pharma-lastAdviceCheck');

		if (lastCheckDate) {
			lastCheckDate = new Date(lastCheckDate);

			if (this.isToday(lastCheckDate)) {
				console.log('today')
				let x = localStorage.getItem('pharma-adviceStatus') ? localStorage.getItem('pharma-adviceStatus') : this.adviceEnum.GREY
				this.setState({
					adviceStatus: x
				})
			} else {
				this.advicePreparation();
			}
		}
	}
	
	isToday(someDate) {
		const today = new Date()
		return someDate.getDate() === today.getDate() &&
		  someDate.getMonth() === today.getMonth() &&
		  someDate.getFullYear() === today.getFullYear()
	  }

	/**Get all MedicationDetails**/
	getAllMedication() {
		fetch("http://localhost:8080/medication/allMedicationDetails")
		.then(result => result.json())
		.then(data => {
			this.setState({
				loadingMedicationList: true,
				dataMedicationList: data
			})
		});
	}

	/**Get all selected MedicationDetails**/
	getAllSelectedMedication(){
		fetch("http://localhost:8080/medication/allSelectedMedicationDetails")
		.then(result => result.json())
		.then(data => {
			this.setState({
				loadingSelectedMedication: true,
				dataSelectedMedication: data
			})
		});
	}

	selectMedication(event, value){
		if(value == null){
			return;
		}

		//Open popup
		Swal.fire({
			title: `${value.medication.medicationName} - ${value.strength} ${value.measurementUnit.measurementUnit} (${value.dosisForm.dosisForm})`,
			text: 'Hoeveel neemt u hier per dag van in?',
			input: 'range',
			imageUrl: value.picture.url,
			imageAlt: value.picture.description,
			inputAttributes: {
				min: .5,
				max: 8,
				step: .5,
			},
			inputValue: 1,
			showCancelButton: true,
			cancelButtonText: "Annuleren",
			confirmButtonText: "Toevoegen",
			customClass: {
				confirmButton: 'popup-confirm',
				cancelButton: 'popup-cancel',
				image: 'medication-image'
			},
			validationMessage: 'Voer een heel getal in.',
			allowOutsideClick: false
		})
		.then(data => {
			if (data.isConfirmed) {
				let intakeFrequency = data.value;
				
				const selectedMedication = new URLSearchParams();
				selectedMedication.append('personId', 41);
				selectedMedication.append('medicationDetailId', value.medicationDetailsId);
				selectedMedication.append("intakeFrequency", intakeFrequency);
		
				fetch("http://localhost:8080/person/selectMedication", {
					method: 'POST',
					headers: {
						'Content-Type': 'application/x-www-form-urlencoded',
					},
					body: selectedMedication
				})
				.then(result => result.json())
				.then(data => {
					if(data){
						this.setState({
							message: data.message
						});
						
						document.querySelector('.MuiAutocomplete-clearIndicator').click();
						this.getAllSelectedMedication();

						//Get new data
						this.getAllMedication();
						this.advicePreparation();
					}
				});
			} else {
				document.querySelector('.MuiAutocomplete-clearIndicator').click();
				return;
			}
		})


	}

	deleteMedication(selectedMedication){
		Swal.fire({
			title: 'Wilt u deze medicatie verwijderen?',
			showCancelButton: true,
			confirmButtonText: `Verwijderen`,
			cancelButtonText: 'Annuleren',
			customClass: {
				confirmButton: 'popup-confirm',
				cancelButton: 'popup-cancel'
			},
		})
		.then((result) => {
			if (result.isConfirmed) {

				fetch("http://localhost:8080/person/deleteMed/" + selectedMedication.id, {
					method: 'DELETE',
					headers: {
						'Content-Type': 'application/x-www-form-urlencoded',
					},
				})
				.then(result => result.json())
				.then(data => {
					if(data){
						this.getAllSelectedMedication();
					}

					// const Toast = Swal.mixin({
					// 	toast: true,
					// 	position: 'top-right',
					// 	showConfirmButton: false,
					// 	timer: 2500,
					// 	timerProgressBar: true,
					// });
					//
					// Toast.fire({
					// 	icon: 'success',
					// 	title: data.message
					// });
					//Get new data
					this.getAllMedication();

					this.advicePreparation();
				});
			}
		})
	}

	medCheck(values) {
		let x = '';

		let length = values.keyValues.length;

		if(length > 0){
			for(var i = 0; i < length; i++){
				let name = values.keyValues[i].information.name;
				let measurement = values.keyValues[i].information.measurement;
				let value = values.keyValues[i].value;

				console.log(name);

				x += `<div id="medChange" class="medChange-popup mt-4">
					<div class="medChange-popup-name">${name}:</div> 
					<div class="medChange-popup-data">
						<input value=${value} type="number" disabled>
						(${measurement})
					</div>
				  </div>`;
			}

			Swal.fire({
				title: 'Kloppen onderstaande gegevens?',
				html: x,
				showCloseButton: false,
				showCancelButton: true,
				focusConfirm: false,
				confirmButtonText: 'Ja',
				confirmButtonAriaLabel: 'Ja',
				cancelButtonText: 'Nee',
				cancelButtonAriaLabel: 'Nee',
				allowOutsideClick: false,
				customClass: {
					confirmButton: 'popup-confirm',
					cancelButton: 'popup-cancel'
				},
				hideClass: {
					popup: '',
				}
			})
				.then(result => {
					if (result.isConfirmed) {
						//Generate advice
						this.adviceGeneration(values);
					} else {
						this.medChange(values);
					}

					localStorage.setItem('pharma-lastAdviceCheck', new Date().toString())
				})
		}else{
			this.adviceGeneration(values);
		}
	}

	medChange(values){
		let x = '';

		let length = values.keyValues.length;

		for(let i = 0; i < length; i++){
			const name = values.keyValues[i].information.name;
			const measurement = values.keyValues[i].information.measurement;
			const value = values.keyValues[i].value;
			const keyValue = values.keyValues[i].keyValue;

			console.log(name);

			x += `<div id="medChange" class="medChange-popup mt-4">
					<div class="medChange-popup-name">${name}:</div> 
					<div class="medChange-popup-data">
						<input id=swal-input${i} name=${keyValue} value=${value} type="number">
						(${measurement})
					</div>
				  </div>`;
		}

		Swal.fire({
			title: 'Kloppen onderstaande gegevens?',
			html: x,
			showCloseButton: false,
			showCancelButton: false,
			focusConfirm: false,
			confirmButtonText: 'Opslaan',
			confirmButtonAriaLabel: 'Opslaan',
			cancelButtonAriaLabel: 'Annuleren',
			customClass: {
				confirmButton: 'popup-confirm',
			},
			showClass: {
				popup: '',
				icon: ''
			},
			hideClass: {
				popup: '',
			},
			allowOutsideClick: false,
			preConfirm: function() {
				return new Promise((resolve, reject) => {
					// get your inputs using their placeholder or maybe add IDs to them
					let obj = {};

					for(let i = 0; i < length; i++){
						let x = document.getElementById("swal-input" + i);

						obj[x.name] = x.value;
					}

					resolve({
						obj: obj
					});
				});
			}
		})
		.then(result => {
			if (result.isConfirmed) {
				//Generate advice
				const data = result.value.obj;


				for(let x in data){
					for(let i = 0; i < length; i++){
						if (x == values.keyValues[i].keyValue) {
							values.keyValues[i].value = data[x];
						}
					}
				}

				this.adviceGeneration(values);
			} else {
				let medChanges = document.getElementsByClassName('medChange');
				console.log(medChanges);
			}

			localStorage.setItem('pharma-lastAdviceCheck', new Date().toString())
		})
	}

	advicePreparation(){
		prepareAdvice(41).then(data => {
			console.log(data);

			this.medCheck(data);

			//Set checked date
			localStorage.setItem('pharma-lastAdviceCheck', new Date().toString())
		})
	}

	adviceGeneration(values){
		generateAdvice(41, values).then(data => {
			console.log(data);

			this.adviceGetter();

			const Toast = Swal.mixin({
				toast: true,
				position: 'top-right',
				showConfirmButton: false,
				timer: 2500,
				timerProgressBar: true
			});

			Toast.fire({
				icon: 'success',
				title: "Advies opgehaald"
			});
		})
	}

	//Call this when something changed in the data.
	adviceGetter(){
		getAdvice(41).then(data => {
			console.log("data");
			console.log(data);

			if(data.Advice){
				this.setState({
					dataAdvice: data.Advice,
					adviceStatus: data.Advice.status
				});
			}
		})

	}


	getAdvice() {
		this.setState({
			adviceStatus: this.adviceEnum.GREEN
		})
		localStorage.setItem('pharma-adviceStatus', this.adviceEnum.GREEN)
		localStorage.setItem('pharma-lastAdviceCheck', new Date().toString())
	}

	redirectAdvice(){
		this.props.history.push("/advice");
	}
	
	render() {
		if (this.state.loadingMedicationList && this.state.loadingSelectedMedication) {
			const { dataMedicationList } = this.state;
			const { dataSelectedMedication } = this.state;
			const{ dataAdvice } = this.state;

			let adviceClass;
			let adviceText;
			let adviceSmall;

			if(this.state.dataSelectedMedication.length === 0){
				adviceClass = 'advice-grey'
				adviceText = 'Voeg hieronder uw medicatie toe om een advies te krijgen'
				adviceSmall = ""
			}else{
				switch(this.state.adviceStatus) {
					case "RED":
						adviceClass = 'advice-red'
						adviceText = dataAdvice.title
						adviceSmall = "Klik hier voor meer informatie"
						break;
					case "GREEN":
						adviceClass = 'advice-green'
						adviceText =  dataAdvice.title ? dataAdvice.title : "Het gebruik van uw medicatie is in orde. U hoeft geen actie te ondernemen"
						adviceSmall = "Klik hier voor meer informatie"
						break;
					default:
						adviceClass = 'advice-grey'
						adviceText = 'Voeg hieronder uw medicatie toe om een advies te krijgen'
						adviceSmall = ""
						break;
				}
			}

			return <div>
				<div className={`active-advice ${adviceClass}`} onClick={this.redirectAdvice}>
					<div>{adviceText}</div>
					<div className="active-advice-small">{adviceSmall}</div>
				</div>
				<div className="advice-selector">
					{/*<button className="btn btn-primary advice-button mb-3" onClick={this.advicePreparation}>Advies genereren</button>*/}
					<Autocomplete
						id="MedicationListInput"
						ref={this.autocomplete}
						options={dataMedicationList}
						getOptionLabel={(option) => `${option.medication.medicationName} - ${option.strength} ${option.measurementUnit.measurementUnit} (${option.dosisForm.dosisForm})`}
						style={{ width: "100%" }}
						onChange={this.selectMedication}
						renderOption={(option) => (
							<React.Fragment>
								<input type="hidden" value="2"></input>
								<div>{`${option.medication.medicationName} - ${option.strength} ${option.measurementUnit.measurementUnit} (${option.dosisForm.dosisForm})`}</div>
							</React.Fragment>
						)}
						clearOnBlur
						renderInput={(params) => <TextField {...params} label="Zoek uw medicatie" variant="outlined" />}
					/>

					<div className="mb-3">
						{dataSelectedMedication.map((selectedMedication, index) => {
							let id = selectedMedication.personMedicationId
							let medicationName = selectedMedication.medicationDetails.medication.medicationName
							let medicationStrength = selectedMedication.medicationDetails.strength
							let measurement = selectedMedication.medicationDetails.measurementUnit.measurementUnit
							let dosis = selectedMedication.medicationDetails.dosisForm.dosisForm
							let frequency = selectedMedication.intakeFrequency
							return (
								<Card body key={index}>
									<div className="d-flex align-items-center">
										<div>
											<Card.Title className="medication-name">
												{medicationName} {medicationStrength}{measurement}
											</Card.Title>
											{dosis} - {frequency} keer per dag
										</div>
										<button className="removeMed ml-auto" aria-label="Medicatie verwijderen">
											<TiIcons.TiDelete size={50} onClick={() => this.deleteMedication({id})} className="deleteIcon"/>
										</button>
									</div>
								</Card>
							);
						})}
					</div>
				</div>
			</div>
		} else {
			return (
				<div>
					<Spinner animation="border" role="status">
						<span className="sr-only">Loading...</span>
					</Spinner>
				</div>
			);
		}
	}
	
}


export default withRouter(Dashboard);
