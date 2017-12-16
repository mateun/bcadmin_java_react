package ttech.com.bitcoinapp.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;

import java.io.UnsupportedEncodingException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import org.jasypt.util.digest.*;
import org.jasypt.util.password.BasicPasswordEncryptor;
import org.jasypt.util.password.StrongPasswordEncryptor;

import ttech.com.bitcoinapp.TokenVerifyer;
import ttech.com.bitcoinapp.dal.DBConnectionProvider;
import ttech.com.bitcoinapp.models.*;

@Controller
public class CustomerController {
	
	
	@RequestMapping(value="/transactions", method = RequestMethod.GET)
	@ResponseBody
	public List<BCTransaction> getTransactions(@RequestHeader("Authorization") String auth)
	{
		// TODO load transactions from database
		if (!TokenVerifyer.verifyToken(auth))
		{
			return new ArrayList<>();
		}
		
		
		return null;
	}
	
	
	@RequestMapping(value="/foobar", method = RequestMethod.POST)
	@ResponseBody
	public String signup(@RequestParam String foo) {

		Connection conn = null;

		try {
			conn = DBConnectionProvider.dbConnection();

			/*
			 * String sql =
			 * "INSERT into customer(first_name, last_name, user_name, password) VALUES(?, ?, ?, ?)"
			 * ; PreparedStatement stmt = conn.prepareStatement(sql); stmt.setString(1,
			 * customer.getFirstName()); stmt.setString(2, customer.getLastName());
			 * stmt.setString(3, customer.getUserName()); stmt.setString(4,
			 * encryptedPassword);
			 * 
			 * int rowsAffected = stmt.executeUpdate();
			 * 
			 * stmt.close();
			 */

		} catch (Exception ex) {
			System.out.println("ex: " + ex);
			return "{\"exception\":\"" + ex + "\"}";
		} finally {
			try {
				conn.close();
			} catch (Exception ex) {
				return "close error";
			}
		}

		return "{\"val\": 234}";

	}

	@RequestMapping(value = "/signup", method = RequestMethod.POST)
	@ResponseBody
	public String signup(@RequestBody Customer customer) {
		String password = customer.getPassword();
		StrongPasswordEncryptor basicPasswordEncryptor = new StrongPasswordEncryptor();
		String encryptedPassword = basicPasswordEncryptor.encryptPassword(password);
		System.out.println("encrypted password: " + encryptedPassword);

		Connection conn = null;

		try {
			
			conn = DBConnectionProvider.dbConnection();

			String sql = "INSERT into customer(first_name, last_name, user_name, password) VALUES(?, ?, ?, ?)";
			PreparedStatement stmt = conn.prepareStatement(sql);
			stmt.setString(1, customer.getFirstName());
			stmt.setString(2, customer.getLastName());
			stmt.setString(3, customer.getUserName());
			stmt.setString(4, encryptedPassword);

			stmt.close();

		} catch (Exception ex) {
			System.out.println("ex: " + ex);
			return "{\"success\": 0}";
		} finally {
			try {
				conn.close();
			} catch (Exception ex) {
			}
		}

		return "{\"result\": \"ok\"}";
	}
	
	
	@RequestMapping(value = "/signin2", method = RequestMethod.POST)
	@ResponseBody
	public String signin(@RequestBody Customer user) {
		System.out.println("username: " + user.getUserName());

		Connection conn = null;

		try {
			Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
			String dbConnString = System.getenv("db_conn_string");
			conn = DriverManager.
					getConnection(dbConnString);

			StrongPasswordEncryptor passwordEncryptor = new StrongPasswordEncryptor();
			String encryptedPassword = passwordEncryptor.encryptPassword(user.getPassword());
			System.out.println("encrypted provided pw: " + encryptedPassword);

			String sql = "SELECT password from customer where user_name = ?";
			PreparedStatement stmt = conn.prepareStatement(sql);
			stmt.setString(1, user.getUserName());

			ResultSet rs = stmt.executeQuery();
			if (rs.next()) {
				String dbpw = rs.getString(1);
				if (passwordEncryptor.checkPassword(user.getPassword(), dbpw)) {
					System.out.println("login ok");

					try {
						String token = TokenVerifyer.createToken(user.getUserName());

						return "{\"token\":\"" + token + "\"}";

					} catch (Exception e) {

						e.printStackTrace();
					} finally {
						try {
							conn.close();
						} catch (SQLException e) {

						}
					}

				} else {
					System.out.println("login FAILED!");
				}
			}

		} catch (Exception ex) {
			System.out.println("ex: " + ex);
			return "{\"success\": \"" + ex.getMessage() + "\"}";
		}

		return "{\"success\":0}";
	}
	

	@RequestMapping(value = "/signin", method = RequestMethod.POST)
	@ResponseBody
	public String signin(@RequestParam String username, @RequestParam String password) {
		System.out.println("username: " + username);

		Connection conn = null;

		try {
			Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
			String dbConnString = System.getenv("db_conn_string");
			conn = DriverManager.getConnection(dbConnString);

			StrongPasswordEncryptor passwordEncryptor = new StrongPasswordEncryptor();
			String encryptedPassword = passwordEncryptor.encryptPassword(password);
			System.out.println("encrypted provided pw: " + encryptedPassword);

			String sql = "SELECT password from customer where user_name = ?";
			PreparedStatement stmt = conn.prepareStatement(sql);
			stmt.setString(1, username);

			ResultSet rs = stmt.executeQuery();
			if (rs.next()) {
				String dbpw = rs.getString(1);
				if (passwordEncryptor.checkPassword(password, dbpw)) {
					System.out.println("login ok");

					try {
						String token = TokenVerifyer.createToken(username);

						return "{\"token\":\"" + token + "\"}";

					} catch (Exception e) {

						e.printStackTrace();
					} finally {
						try {
							conn.close();
						} catch (SQLException e) {

						}
					}

				} else {
					System.out.println("login FAILED!");
				}
			}

		} catch (Exception ex) {
			System.out.println("ex: " + ex);
			
			return "{\"success\": \"" + ex + "\"}";
		}

		return "{\"success\":0}";
	}

}
