/*
 * 
 */
package pennywise.service;

import pennywise.interfaces.IDataStorage;
import pennywise.model.User;


/**
 * The Class AuthenticationService.
 */
public class AuthenticationService {
    
    /** The storage. */
    private final IDataStorage storage;
    
    /** The current user. */
    private User currentUser;

    /**
     * Instantiates a new authentication service.
     *
     * @param storage the storage
     */
    public AuthenticationService(IDataStorage storage) {
        this.storage = storage;
    }

    /**
     * Login.
     *
     * @param userId the user id
     * @return true, if successful
     */
    public boolean login(String userId) {
        if (userId == null || userId.trim().isEmpty()) {
            return false;
        }
        User user = storage.loadUser(userId);
        if (user != null) {
            currentUser = user;
            return true;
        }
        return false;
    }

    /**
     * Register.
     *
     * @param userId the user id
     * @return true, if successful
     */
    public boolean register(String userId) {
        if (userId == null || userId.trim().isEmpty()) {
            return false;
        }
        if (storage.loadUser(userId) != null) {
            return false;
        }
        User newUser = new User(userId);
        return storage.saveUser(newUser);
    }

    /**
     * Gets the current user.
     *
     * @return the current user
     */
    public User getCurrentUser() {
        return currentUser;
    }

    /**
     * Logout.
     */
    public void logout() {
        currentUser = null;
    }
}
