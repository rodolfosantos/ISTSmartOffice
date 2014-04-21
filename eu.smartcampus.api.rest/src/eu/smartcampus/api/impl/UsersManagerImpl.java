package eu.smartcampus.api.impl;

/**
 * @author Rodolfo Santos
 */

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import eu.smartcampus.api.IUserManagerService;

public class UsersManagerImpl implements IUserManagerService {

	private static IUserManagerService instance = null;
	private Map<String, String> users;

	private UsersManagerImpl() {
		users = new ConcurrentHashMap<String, String>();
		this.addUser("admin", "admin");
		this.addUser("admin2", "admin");
		this.addUser("admin3", "admin");
		this.addUser("user", "user");
		this.addUser("user2", "user");
		this.addUser("user3", "user");
	}

	public static synchronized IUserManagerService getInstance() {
		if (instance == null) {
			instance = new UsersManagerImpl();
		}
		return instance;
	}

	public boolean addUser(String user, String pwd) {
		if (this.checkUserName(user))
			return false;
		users.put(user.toLowerCase(), hash(pwd));
		return true;
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
	
	private boolean checkUserName(String user) {
		return users.containsKey(user.toLowerCase());
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
