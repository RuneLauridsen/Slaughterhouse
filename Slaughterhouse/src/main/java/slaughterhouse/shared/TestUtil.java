package slaughterhouse.shared;

import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class TestUtil {
    // NOTE(rune): main() er kun for convenience. Bliver ikke kaldt fra nogle steder,
    // men kan køres manuelt hvis man vil resette database, uden af køre en unit test.
    public static void main(String[] args) {
        resetSqlDatabase();
    }

    public static void resetSqlDatabase() {
        try {
            Class.forName("org.postgresql.Driver");
            Connection connection = DriverManager.getConnection("jdbc:postgresql://localhost:5432/postgres", "postgres", "asdasd");

            URL sqlUlr = TestUtil.class.getResource("/Slagtherhouse.sql");
            String sql = Files.readString(Path.of(sqlUlr.toURI()));

            PreparedStatement stmt = connection.prepareStatement(sql);
            stmt.execute();
            connection.close();

            System.out.println("Database reset");
        } catch (ClassNotFoundException | SQLException | IOException | URISyntaxException e) {
            throw new RuntimeException(e);
        }
    }
}
