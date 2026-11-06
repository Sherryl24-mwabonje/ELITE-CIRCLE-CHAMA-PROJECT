
package dao;

import db.DBConnection;
import model.Contribution;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class ContributionDAO {

    // CREATE: Add a contribution
    public int addContribution(Contribution c) throws SQLException {

        String sql = "INSERT INTO contributions " +
                "(member_id, amount, contribution_date, " +
                "payment_method, reference_code) " +
                "VALUES (?, ?, ?, ?, ?)";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(
                     sql, Statement.RETURN_GENERATED_KEYS)) {

            ps.setInt(1, c.getMemberId());
            ps.setDouble(2, c.getAmount());
            ps.setDate(3, Date.valueOf(c.getContributionDate()));
            ps.setString(4, c.getPaymentMethod());
            ps.setString(5, c.getReferenceCode());

            ps.executeUpdate();

            try (ResultSet keys = ps.getGeneratedKeys()) {
                return keys.next() ? keys.getInt(1) : -1;
            }
        }
    }

    // READ: Get all contributions
    public List<Contribution> getAllContributions()
            throws SQLException {

        List<Contribution> list = new ArrayList<>();

        String sql = "SELECT c.contribution_id, c.member_id, " +
                "m.full_name, c.amount, c.contribution_date, " +
                "c.payment_method, c.reference_code " +
                "FROM contributions c " +
                "JOIN members m ON c.member_id = m.member_id " +
                "ORDER BY c.contribution_date DESC, " +
                "c.contribution_id DESC";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {

                list.add(new Contribution(
                        rs.getInt("contribution_id"),
                        rs.getInt("member_id"),
                        rs.getString("full_name"),
                        rs.getDouble("amount"),
                        rs.getDate("contribution_date").toLocalDate(),
                        rs.getString("payment_method"),
                        rs.getString("reference_code")
                ));
            }
        }

        return list;
    }

    // UPDATE: Edit a contribution
    public boolean updateContribution(Contribution c)
            throws SQLException {

        String sql = "UPDATE contributions SET member_id=?, " +
                "amount=?, contribution_date=?, payment_method=?, " +
                "reference_code=? WHERE contribution_id=?";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, c.getMemberId());
            ps.setDouble(2, c.getAmount());
            ps.setDate(3, Date.valueOf(c.getContributionDate()));
            ps.setString(4, c.getPaymentMethod());
            ps.setString(5, c.getReferenceCode());
            ps.setInt(6, c.getContributionId());

            return ps.executeUpdate() > 0;
        }
    }

    // DELETE: Remove a contribution
    public boolean deleteContribution(int contributionId)
            throws SQLException {

        String sql = "DELETE FROM contributions " +
                "WHERE contribution_id=?";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, contributionId);

            return ps.executeUpdate() > 0;
        }
    }

    // Calculate total contributions
    public double getTotalContributions() throws SQLException {

        String sql = "SELECT COALESCE(SUM(amount), 0) " +
                "FROM contributions";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            return rs.next() ? rs.getDouble(1) : 0;
        }
    }
}

