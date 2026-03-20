public void login(String username, String password) {
    // Validate input
    if (username == null || password == null) {
        throw new IllegalArgumentException("Username and password cannot be null");
    }

    // Authenticate user
    User user = userService.authenticate(username, password);
    if (user == null) {
        throw new AuthenticationException("Invalid username or password");
    }

    // Set user session
    session.setAttribute("user", user);
}