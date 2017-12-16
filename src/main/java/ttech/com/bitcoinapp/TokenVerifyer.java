package ttech.com.bitcoinapp;

import java.io.UnsupportedEncodingException;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.Claim;
import com.auth0.jwt.interfaces.DecodedJWT;

public class TokenVerifyer {
	
	private static JWTVerifier _verifier;
	private static Algorithm _algorithmHS;
	
	private static Algorithm algorithm() 
	{
		if (_algorithmHS == null)
			try {
				_algorithmHS = Algorithm.HMAC256("ferkl");
			} catch (IllegalArgumentException | UnsupportedEncodingException e) {
				e.printStackTrace();
				return null;
			}
		
		return _algorithmHS;
		
	}
	
	private static JWTVerifier verifier() {
		if (_verifier == null) 
		{
			_verifier = JWT.require(algorithm())
		        .withIssuer("ttech")
		        .build();	
		}
		
		return _verifier;
	}
	
	public static String createToken(String username) 
	{
		return JWT.create()
		.withIssuer("ttech")
		.withClaim("username", username)
		.sign(TokenVerifyer.algorithm());
	}
	
	public static boolean verifyToken(String authHeader) 
	{
		
		if (authHeader.split(" ").length < 2) {
			return false;
		}
		
		String token = authHeader.split(" ")[1];
		
		System.out.println("token: " + token);
		try {
			    DecodedJWT jwt = verifier().verify(token);
			    Claim username = jwt.getClaim("username");
			    System.out.println("username: " + username.asString());
			
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
		
		return true;
		
	}
}


