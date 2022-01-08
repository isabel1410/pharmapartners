import React, { Component } from 'react';
import {formatDate} from "../dashboard/Util";
class AdviceItem extends Component {
	constructor(props) {
		super();
		this.state = {
			allMeds: [],
			item: {},
			index: 0,
			active: false,
			health: [],
			isFirst: false
		}
		this.toggleAdvice = this.toggleAdvice.bind(this);
	}

	componentDidMount() {
		this.setState({
			allMeds: this.props.allMeds,
			item: this.props.item,
			index: this.props.index,
			health: this.props.health,
			isFirst: this.props.isFirst
		})
	}


	toggleAdvice() {
        const currentState = this.state.active;

		this.setState({
			isFirst: false,
			active: !currentState
		});

    };

	render() {
		let healthInfo = [];

		if (this.state.item.health) {
			let healthObj = JSON.parse(this.state.item.health);
			Object.keys(healthObj.healthValues).forEach((healthName, index) => {
				const medName = healthObj.healthValues[healthName].information.name;
				const medValue = healthObj.healthValues[healthName].value;
				const medUnit = healthObj.healthValues[healthName].information.measurement;
				healthInfo.push(<div className="mt-2" key={index}><div>{medName}:</div><div>{medValue} {medUnit}</div> <hr className="advice-hr"></hr></div>)
			});
		}

		return  <button
		className={`advice d-flex flex-column ${this.state.active || this.state.isFirst ? "advice-open" : ""}`}
		onClick={this.toggleAdvice} >
					<div className="w-100 d-flex advice-main">
						<div className="advice-index">{this.state.index + 1}</div>
						<div className="advice-text">
							{/* <div>Uw advies:</div> */}
							<div>{this.state.item.title ? this.state.item.title : "Het gebruik van uw medicatie is in orde. U hoeft geen actie te ondernemen"}</div>
							<div className={`more-info-button`}>{this.state.active || this.state.isFirst ? <div>Klik hier om te sluiten</div> : <div>Klik hier voor meer informatie</div>}</div>
						</div>
						
						<div className="date">{formatDate(this.state.item.date)}</div>
					</div>
					<div className={`advice-large ${this.state.active ? "advice-large-open" : ""} ${this.state.isFirst ? "advice-large-open" : ""}`}>
					<div className="advice-meds">
                    							<div className="more-info-header">Advies gebaseerd op de volgende medicatie(s):</div>
                    							<div>
                    								<ul>
                    									{this.state.allMeds.map((item, index) => <li key={index}>{item.medicationName} - {item.strength} {item.measuringUnit}</li>)}
                    								</ul>
                    							</div>
                    						</div>
                    						<div className="advice-description">
                    							<div className="more-info-header">Informatie voor zorgverleners:</div>
                    							<div>
                    								<ul>
                    									{this.state.allMeds.map((item, index) => <li key={index}>{item.medicationName}: {item.medicationAdvice.description}</li>)}
                    								</ul>
                    							</div>
                    						</div>
						<div className="advice-user-info">
							<div className="more-info-header">Uw gegevens over dit advies:</div>
							<div>
								{healthInfo.length > 0 ? healthInfo : <div>Geen informatie gevonden</div>}
							</div>
						</div>
					</div>
				</button>
	}
}

export default AdviceItem;
