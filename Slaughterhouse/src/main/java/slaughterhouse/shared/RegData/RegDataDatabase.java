package slaughterhouse.shared.RegData;

import org.springframework.stereotype.Component;
import slaughterhouse.shared.Pig;

import java.sql.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collection;

@Component
public class RegDataDatabase implements RegData {

    PreparedStatement stmt = null;
    Connection c = null;

    private void open() {
        try {
            Class.forName("org.postgresql.Driver");
            c = DriverManager.getConnection("jdbc:postgresql://localhost:5432/postgres", "postgres", "asdasd");
            System.out.println("Database open ok");
        } catch (SQLException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    private void close() {
        try {
            stmt.close();
            c.close();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public Pig create(double weight, String farm) {
        open();

        stmt = null;
        ResultSet resultSet = null;

        try {
            String query = """ 
                insert into slaughterHouse_registraion."pig"
                    (reg_number, weight, farm, date_of_reg)
                values 
                    (default, ?,?,?)
                returning
                    reg_number, weight, farm, date_of_reg;
                """;

            stmt = c.prepareStatement(query);
            stmt.setDouble(1, weight);
            stmt.setString(2, farm);
            stmt.setDate(3, java.sql.Date.valueOf(LocalDate.now()));

            resultSet = stmt.executeQuery();

            if (resultSet.next()) {
                return pigFromResultSet(resultSet);
            } else {
                return null;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        } finally {
            close(stmt);
            close();
        }

    }

    public Pig create(double weight, String farm, Date date) {
        open();
        stmt = null;
        ResultSet resultSet = null;

        try {
            String query = """ 
                insert into slaughterHouse_registraion."pig"
                (weight, farm, date_of_reg)
                values 
                (?,?,?);
                """;

            stmt = c.prepareStatement(query);
            stmt.setDouble(1, weight);
            stmt.setString(2, farm);
            stmt.setDate(2, date);

            resultSet = stmt.executeQuery();

            if (resultSet.next()) {
                return pigFromResultSet(resultSet);
            } else return null;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        } finally {
            close(stmt);
            close();
        }
    }

    public Pig read(int registrationNumber) {
        open();
        stmt = null;
        ResultSet resultSet = null;
        try {
            String query = """ 
                select * from slaughterHouse_registraion."pig"
                where                      
                reg_number = ?;
                """;

            stmt = c.prepareStatement(query);
            stmt.setInt(1, registrationNumber);

            resultSet = stmt.executeQuery();

            if (resultSet.next()) {
                return pigFromResultSet(resultSet);
            } else return null;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        } finally {
            close(stmt);
            close();
        }
    }

    public Collection<Pig> readAll() {
        open();
        stmt = null;
        ResultSet resultSet = null;

        try {
            String query = """ 
                select * from slaughterHouse_registraion."pig";""";

            stmt = c.prepareStatement(query);
            resultSet = stmt.executeQuery();

            Collection<Pig> returnPigs = new ArrayList<>();
            while (resultSet.next()) {
                returnPigs.add(
                    pigFromResultSet(resultSet)
                );
            }

            return returnPigs;

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        } finally {
            close(stmt);
            close();
        }

    }

    public Collection<Pig> readAllWithFarmAndDate(String farm, LocalDate date) {
        open();
        stmt = null;
        ResultSet resultSet = null;

        try {
            String query = """ 
                select * from slaughterHouse_registraion."pig" 
                where (?::varchar IS NULL OR farm = ?) 
                and   (?::date IS NULL OR date_of_reg = ?);
                """;

            stmt = c.prepareStatement(query);
            if (farm == null) stmt.setNull(1, Types.VARCHAR);
            else stmt.setString(1, farm);
            if (farm == null) stmt.setNull(2, Types.VARCHAR);
            else stmt.setString(2, farm);
            if (date == null) stmt.setNull(3, Types.DATE);
            else stmt.setDate(3, java.sql.Date.valueOf(date));
            if (date == null) stmt.setNull(4, Types.DATE);
            else stmt.setDate(4, java.sql.Date.valueOf(date));

            resultSet = stmt.executeQuery();

            Collection<Pig> returnPigs = new ArrayList<>();
            while (resultSet.next()) {
                returnPigs.add(
                    pigFromResultSet(resultSet)
                );
            }

            return returnPigs;

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        } finally {
            close(stmt);
            close();
        }
    }

    @Override
    public Collection<Pig> getReadyPigs() {
        open();
        ResultSet resultSet = null;

        try {
            String query = """
                SELECT * 
                FROM slaughterHouse_registraion."pig"
                WHERE date_of_split IS NULL
                """;

            stmt = c.prepareStatement(query);
            resultSet = stmt.executeQuery();

            Collection<Pig> returnPigs = new ArrayList<>();
            while (resultSet.next()) {
                returnPigs.add(
                    pigFromResultSet(resultSet)
                );
            }

            return returnPigs;
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        } finally {
            close();
            close(resultSet);
            close(stmt);
        }
    }
    public int update(Pig pig) {
        open();
        stmt = null;

        try {
            String query = """ 
                UPDATE slaughterHouse_registraion."pig" 
                SET weight = ?, farm = ?, date_of_reg = ?
                where                      
                reg_number = ?;
                """;

            stmt = c.prepareStatement(query);
            stmt.setDouble(1, pig.getWeight());
            stmt.setString(2, pig.getFarm());
            stmt.setDate(3, Date.valueOf(pig.getRegistrationDate()));
            stmt.setInt(4, pig.getRegistrationNumber());
            stmt.execute();

            return 1;
        } catch (Exception e) {
            e.printStackTrace();
            return -1;
        } finally {
            close(stmt);
            close();
        }
    }

    private static Pig pigFromResultSet(ResultSet resultSet) throws SQLException {
        return new Pig(
            resultSet.getDouble("weight"),
            resultSet.getInt("reg_number"),
            resultSet.getString("farm"),
            toLocalDate(resultSet.getDate("date_of_reg")),
            toLocalDate(resultSet.getDate("date_of_split"))
        );
    }

    public void delete(int registrationNumber) {
        open();
        stmt = null;

        try {
            String query = """ 
                DELETE from slaughterHouse_registraion."pig"
                where                      
                reg_number = ?;
                """;

            stmt = c.prepareStatement(query);
            stmt.setInt(1, registrationNumber);
            stmt.execute();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            close(stmt);
            close();
        }
    }

    private static void close(Statement stmt) {
        if (stmt != null) {
            try {
                stmt.close();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
    }

    private static void close(ResultSet resultSet) {
        if (resultSet != null) {
            try {
                resultSet.close();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
    }

    private static LocalDate toLocalDate(java.sql.Date date) {
        // NOTE: HVORFOR ER DET IKKE DEFAULT BEHAVIOUR!!!
        if (date == null) {
            return null;
        } else {
            return date.toLocalDate();
        }
    }
}
