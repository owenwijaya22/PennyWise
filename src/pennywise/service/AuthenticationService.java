package pennywise.service;

import pennywise.interfaces.IDataStorage;
import pennywise.model.User;

public class AuthenticationService {
    private final IDataStorage storage;
    private User currentUser;

    public AuthenticationService(IDataStorage storage) {
        this.storage = storage;
    }

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

    public User getCurrentUser() {
        return currentUser;
    }

    public void logout() {
        currentUser = null;
    }
}
