package model;

import java.time.LocalDate;

/** Model class for one chama member (one row of the "members" table). */
public class Member {
    private int memberId;
    private String fullName;
    private String nationalId;
    private String phone;
    private String email;
    private LocalDate joinDate;
    private double monthlyPledge;
    private String status;

    public Member() {
    }

    public Member(String fullName, String nationalId, String phone, String email,
                  LocalDate joinDate, double monthlyPledge, String status) {
        this.fullName = fullName;
        this.nationalId = nationalId;
        this.phone = phone;
        this.email = email;
        this.joinDate = joinDate;
        this.monthlyPledge = monthlyPledge;
        this.status = status;
    }

    public Member(int memberId, String fullName, String nationalId, String phone, String email,
                  LocalDate joinDate, double monthlyPledge, String status) {
        this(fullName, nationalId, phone, email, joinDate, monthlyPledge, status);
        this.memberId = memberId;
    }

    public int getMemberId() { return memberId; }
    public void setMemberId(int memberId) { this.memberId = memberId; }

    public String getFullName() { return fullName; }
    public void setFullName(String fullName) { this.fullName = fullName; }

    public String getNationalId() { return nationalId; }
    public void setNationalId(String nationalId) { this.nationalId = nationalId; }

    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public LocalDate getJoinDate() { return joinDate; }
    public void setJoinDate(LocalDate joinDate) { this.joinDate = joinDate; }

    public double getMonthlyPledge() { return monthlyPledge; }
    public void setMonthlyPledge(double monthlyPledge) { this.monthlyPledge = monthlyPledge; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    @Override
    public String toString() {
        return memberId + " - " + fullName;
    }
}
