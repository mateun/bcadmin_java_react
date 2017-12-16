import React from 'react';
import axios from 'axios';
import ReactDOM from 'react-dom';

class LoginForm extends React.Component
{
	constructor(props) {
		super(props);
		this.state = {
				username: 'test',
				password: ''
		}
		console.log("in ctr: " + this.state.username);
		
		this.handleUsername = this.handleUsername.bind(this);
		this.handlePassword = this.handlePassword.bind(this);
		this.handleSubmit = this.handleSubmit.bind(this);
	}
	
	handleSubmit(event) {
		console.log("logging in! " + this.state.username + ":" + this.state.password);
		
		axios.post('/BitCoinAdmin/signin2', {
		    userName: this.state.username,
		    password: this.state.password
		  })
		  .then(function (response) {
		    console.log("response: " + response);
		    localStorage.setItem('jwt_token', response.data.token);
		  })
		  .catch(function (error) {
		    console.log("error: " + error);
		  });
		
		event.preventDefault();
	}
	
	handleUsername(event) {
		this.setState({username: event.target.value});
	}
	
	handlePassword(event) {
		this.setState({password: event.target.value});
	}
	
	render() {
		
		return (
			<div>
				<input type="text" value={this.state.username} onChange = {this.handleUsername} />
				<input type="password" value={this.state.password} onChange = {this.handlePassword} />
				<button onClick={this.handleSubmit}> Login</button>
			
			</div>
		);
		
	}
}

class Fetcher extends React.Component {
	constructor(props) {
		super(props);
		
		this.state = {
				transactions: [],
				username : ""
		};
	}
	
	componentDidMount() {
		console.log("did mount!!");
		axios.get(`http://localhost:8080/BitCoinAdmin/user_standalone`)
	      .then(res => {
	    	const user = res.data;
	        console.log("user: " + user.firstName);
	        const ts = [];
	        this.setState({transactions: ts,
	        	username: user.firstName });
	        
	        console.log("user from state: " + this.state.username);
	      });
	}
	
	render() {
		return (
				<div>
					<h2>hello from component! {this.state.username}</h2>
				</div>
		);
	}
}

ReactDOM.render(
  <LoginForm/>,
  document.getElementById('root')
);