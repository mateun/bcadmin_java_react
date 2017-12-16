package ttech.com.bitcoinapp.controllers;

import java.io.UnsupportedEncodingException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import ttech.com.bitcoinapp.TokenVerifyer;
import ttech.com.bitcoinapp.models.*;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.*;
import com.auth0.jwt.interfaces.Claim;
import com.auth0.jwt.interfaces.DecodedJWT;

@Controller
public class HelloController {
	
	@RequestMapping(value="/hi", method=RequestMethod.GET)
	@ResponseBody
	public String doWelcome() {
		String val = System.getenv("db_name");
		return "hello" + val;
	}
	
	@RequestMapping(value="/login", method=RequestMethod.GET)
	@ResponseBody
	public String login()
	{
		
		try 
		{
			Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
			String dbconnString = System.getenv("db_conn_string");
			Connection conn = DriverManager.getConnection(System.getenv("db_conn_string"));
			
			String sql = "SELECT * from mytrans";
			Statement stmt = conn.createStatement();
			ResultSet rs = stmt.executeQuery(sql);
			while(rs.next()) 
			{
				System.out.print("result row: ");
				System.out.println("id: " + rs.getInt(1));
			}
			
		} catch(Exception ex)
		{
			System.out.println("ex: " + ex);
			return "{\"success\": 0}";
		}
				
		
		try {
			Algorithm algorithmHS = Algorithm.HMAC256("ferkl");
			String token = JWT.create()
							.withIssuer("ttech")
							.withClaim("username", "franky")
							.withClaim("isAdmin", true)
							.withClaim("canCancelOrders", true)
							.sign(algorithmHS);
			
			return "{\"token\":\"" + token + "\"}";
			
		} catch (IllegalArgumentException | UnsupportedEncodingException e) {
			
			e.printStackTrace();
		}
		
		return "{\"success\":0}";
	}
	
	@RequestMapping(value="/user_standalone", method = RequestMethod.GET)
	@ResponseBody
	public User getUser()
	{
		User u = new User();
		u.setFirstName("tom");
		u.setLastName("miller100");
		return u;
	}
	
	@RequestMapping(value="/user/{id}", method = RequestMethod.GET)
	@ResponseBody
	public  User getUser(@PathVariable int id, @RequestHeader("Authorization") String auth)
	{
		if (TokenVerifyer.VerifyToken(auth)) 
		{
			User u = new User();
			u.setFirstName("tom");
			u.setLastName("miller");
			return u;
		}
		
		User u = new User();
		return u;
		
	}

}
