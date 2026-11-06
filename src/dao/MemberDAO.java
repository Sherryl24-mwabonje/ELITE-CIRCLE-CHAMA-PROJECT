package dao;

import db.DBConnection;
import model.Member;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class MemberDAO {

    public int addMember(Member m) throws SQLException {
        String sql = "INSERT INTO members (full_name, national_id, phone, email, "
                   + "join_date, monthly_pledge, status) VALUES (?, ?, ?, ?, ?, ?, ?)";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, m.getFullName());
            ps.setString(2, m.getNationalId());
            ps.setString(3, m.getPhone());
            ps.setString(4, m.getEmail());
            ps.setDate(5, Date.valueOf(m.getJoinDate()));
            ps.setDouble(6, m.getMonthlyPledge());
            ps.setString(7, m.getStatus());
            ps.executeUpdate();
            try (ResultSet keys = ps.getGeneratedKeys()) {
                return keys.next() ? keys.getInt(1) : -1;
            }
        }
    }

    public List<Member> getAllMembers() throws SQLException {
        List<Member> list = new ArrayList<>();
        String sql = "SELECT * FROM members ORDER BY member_id";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                list.add(new Member(
                    rs.getInt("member_id"),
                    rs.getString("full_name"),
                    rs.getString("national_id"),
                    rs.getString("phone"),
                    rs.getString("email"),
                    rs.getDate("join_date").toLocalDate(),
                    rs.getDouble("monthly_pledge"),
                    rs.getString("status")));
            }
        }
        return list;
    }

    public boolean updateMember(Member m) throws SQLException {
        String sql = "UPDATE members SET full_name=?, national_id=?, phone=?, email=?, "
                   + "join_date=?, monthly_pledge=?, status=? WHERE member_id=?";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, m.getFullName());
            ps.setString(2, m.getNationalId());
            ps.setString(3, m.getPhone());
            ps.setString(4, m.getEmail());
            ps.setDate(5, Date.valueOf(m.getJoinDate()));
            ps.setDouble(6, m.getMonthlyPledge());
            ps.setString(7, m.getStatus());
            ps.setInt(8, m.getMemberId());
            return ps.executeUpdate() > 0;
        }
    }

    public boolean deleteMember(int memberId) throws SQLException {
        String sql = "DELETE FROM members WHERE member_id=?";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, memberId);
            return ps.executeUpdate() > 0;
        }
    }
}