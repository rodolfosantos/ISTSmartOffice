package eu.smartcampus.api.rest;

/**
 * @author Rodolfo Santos
 */

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class UsersManager {

	private static UsersManager instance = null;
	private Map<String, String> users;

	private UsersManager() {
		users = new HashMap<String, String>();
		this.addUser("admin", "admin");
		this.addUser("admin2", "admin");
	}

	public static synchronized UsersManager getInstance() {
		if (instance == null) {
			instance = new UsersManager();
		}
		return instance;
	}

	public boolean addUser(String user, String pwd) {
		if (this.checkUserName(user))
			return false;
		users.put(user.toLowerCase(), hash(pwd));
		return true;
	}

	private boolean checkUserName(String user) {
		return users.containsKey(user.toLowerCase());
	}

	public boolean checkCredentials(String user, String pwd) {

		if (!this.checkUserName(user))
			return false;
		String tmp = users.get(user.toLowerCase());
		return hash(pwd).equals(tmp);
	}

	public boolean removeUser(String user) {
		if (!this.checkUserName(user))
			return false;
		users.remove(user.toLowerCase());
		return true;
	}

	public Set<String> getUsersList() {
		return users.keySet();
	}


	private String hash(String pwd) {
		MessageDigest m;
		try {
			m = MessageDigest.getInstance("SHA");
			m.reset();
			m.update(pwd.getBytes());
			byte[] digest = m.digest();
			BigInteger bigInt = new BigInteger(1, digest);
			String hashtext = bigInt.toString(16);
			while (hashtext.length() < 32) {
				hashtext = "0" + hashtext;
			}
			return hashtext;

		} catch (NoSuchAlgorithmException e) {
			return null;
		}
	}
}
