package utils;

public class UserSession {
    private static String username;
    private static String role;

    public static void setUser(String name, String userRole) {
        username = name;
        role = userRole;
    }

    public static String getUsername() {
        return username;
    }

    public static String getRole() {
        return role;
    }

    public static void clearSession() {
        username = null;
        role = null;
    }
}
