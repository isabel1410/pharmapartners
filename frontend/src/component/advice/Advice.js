import React, { Component } from 'react';
import { withRouter } from "react-router-dom";
import AdviceItem from '../adviceItem/AdviceItem';
import {Spinner} from 'react-bootstrap';


import './Advice.css'
class Advice extends Component {
	constructor(props){
		super()

		this.state = {
			userId: 23,
			adviceList: [],
			loaded: false
		}
		this.focusHeader = React.createRef();
	}

	componentDidMount() {
		if (localStorage.getItem('pharma-loggedIn') === 'false' || localStorage.getItem('pharma-loggedIn') === null) {
			this.props.history.push("/");
			return;
		}
		if (this.state.loaded) {
			this.focusHeader.current.focus();
		}
		document.title = 'Adviezen';

		fetch('http://localhost:8080/advice/history/41')
		.then(result => result.json())
		.then(data => {
			if (data) {
				this.setState({
					adviceList: data.advices.reverse(),
					loaded: true
				})
			}
		})
	}

	render() {
		let x = [];
			this.state.adviceList.forEach((item, index) => {
				let allMeds = [];
			
				item.medications.forEach(med => {
					allMeds.push(med)
				})

				x.push(<AdviceItem
					key={index}
					item={item}
					allMeds={allMeds}
					health={item.health ? JSON.parse(item.health) : []}
					index={index}
					isFirst={index === 0}
					/>)
			})
		if (this.state.loaded) {

			return <div className="h-100">
				<div className="d-flex advice-header" >
					<div className="advice-index advice-header-item">Nr.</div>
					<div className="advice-text advice-header-item">Advies</div>
					<div className="date advice-header-item">Datum</div>
				</div>
				<div className="advices" ref={this.focusHeader}>{x}</div>
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

export default withRouter(Advice);
