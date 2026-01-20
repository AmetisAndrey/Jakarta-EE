package dao;

import db.Db;
import model.User;
import org.mindrot.jbcrypt.BCrypt;

import java.sql.*;

public class UserDao {

    public boolean existsByUsername(String username) throws SQLException {
        String sql = "SELECT 1 FROM app_user WHERE username = ?";
        try (Connection c = Db.ds().getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setString(1, username);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next();
            }
        }
    }

    public boolean existsByEmail(String email) throws SQLException {
        String sql = "SELECT 1 FROM app_user WHERE email = ?";
        try (Connection c = Db.ds().getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setString(1, email);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next();
            }
        }
    }

    public User create(String username, String email, String rawPassword) throws SQLException {
        String hash = BCrypt.hashpw(rawPassword, BCrypt.gensalt(12));
        String sql = "INSERT INTO app_user(username, email, password_hash) VALUES(?,?,?) RETURNING id";

        try (Connection c = Db.ds().getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {

            ps.setString(1, username);
            ps.setString(2, email);
            ps.setString(3, hash);

            try (ResultSet rs = ps.executeQuery()) {
                rs.next();
                long id = rs.getLong(1);
                return new User(id, username, email);
            }
        }
    }

    public User authenticate(String usernameOrEmail, String rawPassword) throws SQLException {
        String sql = "SELECT id, username, email, password_hash FROM app_user WHERE username = ? OR email = ?";
        try (Connection c = Db.ds().getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {

            ps.setString(1, usernameOrEmail);
            ps.setString(2, usernameOrEmail);

            try (ResultSet rs = ps.executeQuery()) {
                if (!rs.next()) return null;

                String hash = rs.getString("password_hash");
                if (!BCrypt.checkpw(rawPassword, hash)) return null;

                return new User(
                        rs.getLong("id"),
                        rs.getString("username"),
                        rs.getString("email")
                );
            }
        }
    }
}
