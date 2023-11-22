package slaughterhouse.shared.DistriputionData;

import slaughterhouse.shared.Package;
import slaughterhouse.shared.Pig;
import slaughterhouse.shared.PigPart;
import slaughterhouse.shared.Tray;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Types;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class DistriputionDatabase implements DistriputionData {

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

    @Override
    public PigPart createPigPart(int pigReg, String partName, double partWeight) {
        open();
        stmt = null;
        ResultSet resultSet = null;
        try {
            String query = """ 
                insert into slaughterHouse_distribution."pig_part"
                    (part_id, part_name, part_weight, pig_reg_number)
                values 
                    (default, ?,?,?)
                returning
                    part_id, part_name, part_weight, pig_reg_number;
                """;
            stmt = c.prepareStatement(query);
            stmt.setString(1, partName);
            stmt.setDouble(2, partWeight);
            stmt.setInt(3, pigReg);

            resultSet = stmt.executeQuery();

            if (resultSet.next()) {
                return pigPartFromResultSet(resultSet);
            } else {
                return null;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        } finally {
            close();
        }

    }

    @Override
    public PigPart readPigPart(int partNumber) {
        return null;
    }

    @Override
    public Collection<PigPart> readAllPigParts(int regNumber) {
        open();
        ResultSet rs = null;
        stmt = null;

        Collection<PigPart> ret = new ArrayList<>();
        try {

            String query = """
                SELECT *
                FROM slaughterHouse_distribution."pig_part"
                WHERE pig_reg_number = ?
                """;

            stmt = c.prepareStatement(query);
            stmt.setInt(1, regNumber);
            rs = stmt.executeQuery();
            while (rs.next()) {
                ret.add(pigPartFromResultSet(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        } finally {
            close(rs);
            close(stmt);
            close();
        }

        return ret;
    }

    @Override
    public Collection<PigPart> readAllWithoutTray() {
        open();
        ResultSet rs = null;
        stmt = null;

        Collection<PigPart> ret = new ArrayList<>();
        try {

            String query = """
                SELECT *
                FROM slaughterHouse_distribution."pig_part"
                WHERE tray_id IS NULL
                """;

            stmt = c.prepareStatement(query);
            rs = stmt.executeQuery();
            while (rs.next()) {
                ret.add(pigPartFromResultSet(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            close(rs);
            close(stmt);
            close();
        }

        return ret;
    }
    @Override
    public Collection<PigPart> readAllWithoutPackage() {
        open();
        ResultSet rs = null;
        stmt = null;

        Collection<PigPart> ret = new ArrayList<>();
        try {

            String query = """
                SELECT *
                FROM slaughterHouse_distribution."pig_part"
                WHERE package_id IS NULL
                """;

            stmt = c.prepareStatement(query);
            rs = stmt.executeQuery();
            while (rs.next()) {
                ret.add(pigPartFromResultSet(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            close(rs);
            close(stmt);
            close();
        }

        return ret;
    }

    @Override
    public int updatePigPart(PigPart pigPart, Tray tray, Package _package) {
        open();
        stmt = null;
        try {
            String query = """ 
                UPDATE slaughterHouse_distribution."pig_part"
                SET part_name = ?, part_weight = ?, pig_reg_number = ?, tray_id = ?, package_id = ?
                where                      
                pig_reg_number = ?;
                """;

            stmt = c.prepareStatement(query);
            stmt.setString(1, pigPart.getPartName());
            stmt.setDouble(2, pigPart.getPartWeight());
            if (tray != null)
                stmt.setInt(4, tray.getTrayId());
            else
                stmt.setNull(4, Types.INTEGER);
            if (_package != null)
                stmt.setInt(5, _package.getPackageId());
            else
                stmt.setNull(5, Types.INTEGER);
            stmt.setInt(6, pigPart.getPartId());

            stmt.execute();

            return 1;

        } catch (Exception e) {
            e.printStackTrace();
            return -1;
        } finally {
            close();
        }
    }

    @Override
    public void deletePigPart(int partNumber) {

    }

    @Override
    public Tray createTray(List<PigPart> pigParts, int maxWeight) {
        open();
        stmt = null;
        Tray newTray = null;
        ResultSet resultSet = null;
        try {
            String query = """ 
                insert into slaughterHouse_distribution."tray"
                    (tray_id, max_weight)
                values 
                    (default,?)
                returning
                    tray_id, max_weight;
                """;

            stmt = c.prepareStatement(query);
            stmt.setDouble(1, maxWeight);

            resultSet = stmt.executeQuery();

            if (resultSet.next()) {
                newTray = new Tray(
                    resultSet.getInt("tray_id"),
                    pigParts,
                    resultSet.getInt("max_weight")
                );

                for (PigPart part : pigParts) {
                    query = """
                        UPDATE slaughterHouse_distribution."pig_part"
                        SET tray_id = ?
                        where part_id = ?;
                        """;
                    stmt = c.prepareStatement(query);
                    stmt.setInt(1, newTray.getTrayId());
                    stmt.setInt(2, part.getPartId());
                    stmt.execute();
                }
            }
            return newTray;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        } finally {
            close();
        }

    }

    @Override
    public Tray readTray(int trayId) {
        return null;
    }

    @Override
    public Collection<Tray> readAllPigTrays() {
        return null;
    }

    @Override
    public int updateTray(Tray tray) {
        return 0;
    }

    @Override
    public void deleteTray(int trayId) {

    }

    @Override
    public Package createPackage(List<PigPart> pigParts, String packageType) {
        open();
        stmt = null;
        Package newPackage = null;
        ResultSet resultSet = null;
        try {
            String query = """ 
                insert into slaughterHouse_distribution."package"
                    (package_id, package_type)
                values 
                    (default,?)
                returning
                    package_id, package_type;
                """;

            stmt = c.prepareStatement(query);
            stmt.setString(1, packageType);

            resultSet = stmt.executeQuery();

            if (resultSet.next()) {
                newPackage = new Package(
                    resultSet.getInt("package_id"),
                    resultSet.getString("package_type"),
                    pigParts
                );

                for (PigPart part : pigParts) {
                    stmt = null;
                    query = """
                        UPDATE slaughterHouse_distribution."pig_part"
                        SET package_id = ?
                        where
                        pig_reg_number = ?;
                        """;
                    stmt = c.prepareStatement(query);
                    stmt.setInt(1, newPackage.getPackageId());
                    stmt.setInt(2, part.getPartId());
                    stmt.execute();
                }
            }
            return newPackage;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        } finally {
            close();
        }
    }

    @Override
    public Package readPackage(int packageId) {
        return null;
    }

    @Override
    public Collection<Package> readAllPackages() {
        return null;
    }

    @Override
    public Collection<Integer> readRegNumbersInPackage(int packageId) {
        open();

        ResultSet rs = null;
        stmt = null;

        Collection<Integer> ret = new ArrayList<>();
        try {

            String query = """
                SELECT DISTINCT pig_reg_number
                FROM slaughterHouse_distribution."pig_part"
                WHERE package_id = ?
                """;

            stmt = c.prepareStatement(query);
            stmt.setInt(1, packageId);
            rs = stmt.executeQuery();
            while (rs.next()) {
                ret.add(rs.getInt("pig_reg_number"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            close(rs);
            close(stmt);
            close();
        }

        return ret;
    }

    @Override
    public int updatePackage(Package _package) {
        return 0;
    }

    @Override
    public void deletePackage(int packageId) {

    }

    @Override
    public Collection<Integer> readPackagesFromRegNumber(int regNumber) {
        open();

        ResultSet rs = null;
        stmt = null;

        Collection<Integer> ret = new ArrayList<>();
        try {
            String query = """
                SELECT DISTINCT package_id
                FROM slaughterHouse_distribution."pig_part"
                WHERE pig_reg_number = ?
                """;

            stmt = c.prepareStatement(query);
            stmt.setInt(1, regNumber);
            rs = stmt.executeQuery();
            while (rs.next()) {
                ret.add(rs.getInt("package_id"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            close(rs);
            close(stmt);
            close();
        }

        return ret;
    }

    private static PigPart pigPartFromResultSet(ResultSet resultSet) throws SQLException {
        return new PigPart(
            resultSet.getInt("part_id"),
            resultSet.getInt("pig_reg_number"),
            resultSet.getInt("tray_id"),
            resultSet.getString("part_name"),
            resultSet.getDouble("part_weight")
        );
    }
}
