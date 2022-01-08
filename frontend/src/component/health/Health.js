import React, {Component} from 'react';

class Health extends Component {

    constructor(props) {
        super(props);
        this.state = {
            dataHealthInformationById: null,
            message: ""
        }
        this.handleSubmit = this.handleSubmit.bind(this)
        this.changeHealth = this.changeHealth.bind(this)
    }

    componentDidMount() {
        this.getHealthInformationById();
    }

    getHealthInformationById(){
        const id = 111;


        let url = new URL("http://localhost:8080/health/healthById")
        url.search = new URLSearchParams({
            id: id
        })

        //Get data
        fetch(url, {
            method: 'GET',
        })
            .then(result => result.json())
            .then(data => {
                console.log(data);
                this.setState({
                    dataHealthInformationById: data
                })
            })
            .catch(e => {
                console.log(e)
            })

        //If succes
        // this.setState({
        //     dataHealthInformationById: data
        // })
    }
    changeHealth(event){
        event.preventDefault();

        const heightInput = document.getElementById('checkHeight').value;
        const weightInput = document.getElementById('checkWeight').value;
        const highPressureInput = document.getElementById('checkTopPressure').value;
        const lowPressureInput = document.getElementById('checkLowPressure').value;
        const idInput = document.getElementById('id').value;

        let formData = new URLSearchParams();
        formData.append("height", heightInput);
        formData.append("weight", weightInput);
        formData.append("topPressure", highPressureInput);
        formData.append("lowPressure", lowPressureInput);
        formData.append("id", idInput);

        fetch("http://localhost:8080/health/change",{
            method: 'PUT',
            headers: {
                'Content-Type': 'application/x-www-form-urlencoded'
            },
            body: formData
        })
            .then(result => result.json())
            .then(data => {
                console.log(data)
                //window.location.reload()
                this.setState({
                    message: data.message
                })
            })
            .catch(e => {
                console.log(e)
            })
    }

    handleSubmit(event){
        event.preventDefault();

        const heightInput = document.getElementById('checkHeight').value;
        const weightInput = document.getElementById('checkWeight').value;
        const highPressureInput = document.getElementById('checkTopPressure').value;
        const lowPressureInput = document.getElementById('checkLowPressure').value;
        const idInput = document.getElementById('id').value;

        // const height = event.target.height.value;
        // const weight = event.target.weight.value;
        // const topPressure = event.target.toppressure.value;
        // const lowPressure = event.target.lowpressure.value;


        let formData = new URLSearchParams();
        formData.append("height", heightInput);
        formData.append("weight", weightInput);
        formData.append("topPressure", highPressureInput);
        formData.append("lowPressure", lowPressureInput);
        formData.append("id", idInput);

        fetch("http://localhost:8080/health/save",{
            method: 'POST',
            headers: {
                'Content-Type': 'application/x-www-form-urlencoded'
            },
            body: formData
        })
            .then(result => result.json())
            .then(data => {
                console.log(data)
                this.setState({
                    message: data.message
                })

            })
            .catch(e => {
                console.log(e)
            })
    }

    render()
    {
        const {dataHealthInformationById} = this.state;

        let id;
        let topPressure;
        let lowPressure;
        let height;
        let weight;

        if(dataHealthInformationById){
            id = dataHealthInformationById.healthId
            topPressure = dataHealthInformationById.bloodPressure.split("/")[0]
            lowPressure = dataHealthInformationById.bloodPressure.split("/")[1]
            height = dataHealthInformationById.height
            weight = dataHealthInformationById.weight
        }

            return (
                <form id="formtest">
                    <div className="form-group">
                        <div className="row">
                            <div className="col-lg-6 col-md-12 col-sm-12">
                                <label htmlFor="checkHeight" className="form-check-label"> Lengte (m) </label>
                                <input defaultValue={height && height} lang="en-US" placeholder="1.75" step="0.0001" type="number" className="form-control" id="checkHeight" name="height" max={2.5} min={1.0}/>
                                <div id="heightHelp" className="form-text text-info">Graag alleen getallen gebruiken. Gebruik ook punten in plaats van komma's.</div>
                            </div>
                        </div>
                    </div>
                    <div className="form-group">
                        <div className="row">
                            <div className="col-lg-6 col-md-12 col-sm-12">
                                <label htmlFor="checkWeight" className="form-check-label">Gewicht (kg) </label>
                                <input defaultValue={weight && weight} step="0.0001" type="number" placeholder="75" className="form-control" id="checkWeight" name="weight" max={150.0} min={5.0}/>
                                <div id="heightHelp" className="form-text text-info">Graag alleen getallen gebruiken. Gebruik ook punten in plaats van komma's.</div>
                            </div>
                        </div>
                    </div>
                    <div className="form-group ">
                        <div className="row">
                            <div className="col-lg-3 col-md-6 col-sm-6">
                                <label htmlFor="checkTopPressure" className="form-check-label">Boven druk </label>
                                <input defaultValue={topPressure && topPressure} type="number" placeholder="125" id="checkTopPressure" className="form-control" name="toppressure" max={200.0} min={60.0}/>
                                <div id="heightHelp" className="form-text text-info">U kunt hier minimaal 60 en maximaal 200 invullen.</div>
                            </div>
                            <div className="col-lg-3 col-md-6 col-sm-6">
                                <label htmlFor="checkLowPressure" className="form-check-label">Onder druk </label>
                                <input defaultValue={lowPressure && lowPressure} type="number" placeholder="125" id="checkLowPressure" className="form-control" name="lowpressure" max={200.0} min={60.0}/>
                                <div id="heightHelp" className="form-text text-info">U kunt hier minimaal 60 en maximaal 200 invullen.</div>
                            </div>
                        </div>
                    </div>
                    {!dataHealthInformationById &&
                        <div className="form-group">
                            <button type="submit" onClick={this.handleSubmit} className="btn btn-primary">Opslaan</button>
                            <input type="hidden" value={id && id} name="id" id="id" defaultValue={111}/>
                        </div>
                    }
                    {dataHealthInformationById &&
                    <div className="form-group">
                        <button type="submit" onClick={ this.changeHealth} className="btn btn-primary">Wijzig</button>
                        <input type="hidden" value={id && id} name="id" id="id"/>
                    </div>
                    }
                    <p className="text-primary">{this.state.message}</p>
                </form>
            )
        // }else{
        //     return (
        //         <div>
        //             <Spinner animation="border" role="status">
        //                 <span className="sr-only">Loading...</span>
        //             </Spinner>
        //         </div>
        //     );
        // }
    };
}
// function Health() {
// }

export default Health;
