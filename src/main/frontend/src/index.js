import React from 'react';
import axios from 'axios';
import ReactDOM from 'react-dom';
import { BrowserRouter, Switch, Route, Link, Redirect } from 'react-router-dom';

class LoginForm extends React.Component
{
	constructor(props) {
		super(props);
		console.log("history: " + props.navihist);
		this.state = {
				username: 'test',
				password: '',
				history: props.navihist
		}
		console.log("in ctr: " + this.state.username);
		
		this.handleUsername = this.handleUsername.bind(this);
		this.handlePassword = this.handlePassword.bind(this);
		this.handleSubmit = this.handleSubmit.bind(this);
	}
	
	handleSubmit(event) {
	  console.log("logging in! " + this.state.username + ":" + this.state.password);
	  
		
		
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
				<button onClick={() => this.props.onLoginTry(this.state.username, this.state.password)}> Login</button>
			
			</div>
		);
		
	}
}

class LoginState extends React.Component {
  constructor(props) {
    super(props);
    this.state = { loggedIn : false};
  }
  
  render() {
    return (
        <div>
          <div>{this.state.loggedIn}</div>
        </div>
    )
  }
}
  
class App extends React.Component {
  constructor(props) {
    super(props);
    this.state = { 
        userLoggedIn : false,
        history: props.history
        };
    
    this.handleLoginTry = this.handleLoginTry.bind(this);
    console.log("props history: " + props.history);
    
  }
  
  handleLoginTry(username, password) {
    console.log("event bubbled up!");
    
    axios.post('/signin2', {
      userName: username,
      password: password
    })
    .then((response) => {
      console.log("response: " + response);
      if (response.data.token != undefined) {
        localStorage.setItem('jwt_token', response.data.token);
        console.log("logged in!");
        this.setState({userLoggedIn : true });
        this.state.history.push("/transactions");
      } else {
        console.log("login nok! wrong credentials?!");
        this.setState({userLoggedIn : false });
        this.state.history.push("/");
      }
      
    })
    .catch(function (error) {
      console.log("error: " + error);
      
    });
    
    
  }
  
  render() {
   return (
       <div>
         <Header userLoggedIn={ this.state.userLoggedIn }/>
         <Main handleLoginTry={ this.handleLoginTry } />
       </div>
   ) 
    
  }
	
}

class LogoutForm extends React.Component {
  
  render() {
    localStorage.removeItem("jwt_token");
    return <Redirect to="/" />;
  }
}

class Header extends React.Component {
    constructor(props) {
      super(props);
    }
    
    render() {
      
      var loginPath = "/login";
      var pathLabel = "Login";
      if (this.props.userLoggedIn ) {
        loginPath = "/logout";
        pathLabel = "Logout";
      }
      
      console.log("loginPath: " + loginPath);
      
      return (
        <header>
          <nav>
            <ul>
              <li><Link to="/">Home _ changed</Link></li>
              <li><Link to={loginPath}>{pathLabel}</Link></li>
              <li><Link to="/transactions">Transactions</Link></li>
            </ul>
          
          </nav>
        </header>
      )
    }        
}

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

class Main extends React.Component {
  constructor(props) {
    super(props);
    this.setState = { 
        userLoggedIn : false,
    };
  }
  
  
  
  render() {
    
    return (
      <main>
        <Switch>
          <Route exact path="/" component={Home} />
          <Route exact path="/login" render= {(history) => (
              <LoginForm onLoginTry={this.props.handleLoginTry} navihist={history} />
           )} 
          />
          <Route exact path="/logout" component={LogoutForm} />
          <Route exact path="/transactions" component={TransactionsOverview} />
        </Switch>
      </main>   
    
    
    )
    
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

ReactDOM.render((
	<BrowserRouter>
  	  <Route component={App} />
  	</BrowserRouter>
	),document.getElementById('root')
);