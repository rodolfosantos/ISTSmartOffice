package eu.smartcampus.api;

import java.util.Set;

/**
 * @author Rodolfo Santos
 */
public interface UsersManagerAPI {

	
    /**
     * Add a new user to database
     * 
     * @param user a username id
     * @param pwd a secret key (which will be stored their digest)
     * @return true if the operation is executed, otherwise returns false (if username already exist)
     */
    public boolean addUser(String user, String pwd);

	/**
	 * Check the credentials of a given user 
	 * 
	 * @param user
	 * @param pwd
	 * @return true if the credentials are correct, otherwise returns false
	 */
	public boolean checkCredentials(String user, String pwd);

	/**
     * Remove a user
     * 
     * @param user a username id
     * @return true if the operation is executed, otherwise returns false (if username not exist)
     */
	public boolean removeUser(String user);

	
	/**
	 * Return a set of existent users in database (just for debugging)
	 * @return
	 */
	public Set<String> getUsersList();
}
