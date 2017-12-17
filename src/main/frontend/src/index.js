import React from 'react';
import axios from 'axios';
import ReactDOM from 'react-dom';
import { BrowserRouter, Switch, Route, Link } from 'react-router-dom';

class LoginForm extends React.Component
{
	constructor(props) {
		super(props);
		console.log("history: " + props.history);
		this.state = {
				username: 'test',
				password: '',
				history: props.history
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
		  .then((response) => {
		    console.log("response: " + response);
		    if (response.data.token != undefined) {
		      localStorage.setItem('jwt_token', response.data.token);
		      console.log("logged in!");
		      this.state.history.push("/transactions");
		    } else {
		      console.log("login nok! wrong credentials?!");
		      this.state.history.push("/home");
		    }
		    
		    
		    
		    
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

const App = () => (
	<div>
		<Header/>
		<Main/>
	</div>
)

const Header = () => (
    <header>
      <nav>
        <ul>
          <li><Link to="/">Home</Link></li>
          <li><Link to="/login">Login</Link></li>
          <li><Link to="/transactions">Transactions</Link></li>
        </ul>
      
      </nav>
    </header>
    
)

const Home = () => (
    <div>
      <h1>Welcome to the BitcoinAdmin app!</h1>
    </div>
)

const TransactionsOverview = () => (
    <div>
      <h1>Transactions Overview!</h1>
    </div>
)

const Main = () => (
    <main>
      <Switch>
        <Route exact path="/" component={Home} />
        <Route exact path="/login" component={LoginForm} />
        <Route exact path="/transactions" component={TransactionsOverview} />
      </Switch>
    </main>		
)


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

ReactDOM.render((
	<BrowserRouter>
  	  <App/>
  	</BrowserRouter>
	),document.getElementById('root')
);