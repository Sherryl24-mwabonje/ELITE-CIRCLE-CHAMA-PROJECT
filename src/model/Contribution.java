
package model;

import java.time.LocalDate;

/**
 * Model class for one contribution.
 */
public class Contribution {

    // Variables (attributes)
    private int contributionId;
    private int memberId;
    private double amount;
    private LocalDate contributionDate;
    private String paymentMethod;
    private String referenceCode;
    private String memberName;

    // Empty constructor
    public Contribution() {
    }

    // Constructor used when adding a contribution
    public Contribution(int memberId, double amount,
            LocalDate contributionDate, String paymentMethod,
            String referenceCode) {

        this.memberId = memberId;
        this.amount = amount;
        this.contributionDate = contributionDate;
        this.paymentMethod = paymentMethod;
        this.referenceCode = referenceCode;
    }

    // Constructor used when reading from the database
    public Contribution(int contributionId, int memberId,
            String memberName, double amount,
            LocalDate contributionDate, String paymentMethod,
            String referenceCode) {

        this(memberId, amount, contributionDate,
                paymentMethod, referenceCode);

        this.contributionId = contributionId;
        this.memberName = memberName;
    }

    // Getters and setters
    public int getContributionId() {
        return contributionId;
    }

    public void setContributionId(int contributionId) {
        this.contributionId = contributionId;
    }

    public int getMemberId() {
        return memberId;
    }

    public void setMemberId(int memberId) {
        this.memberId = memberId;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public LocalDate getContributionDate() {
        return contributionDate;
    }

    public void setContributionDate(LocalDate contributionDate) {
        this.contributionDate = contributionDate;
    }

    public String getPaymentMethod() {
        return paymentMethod;
    }

    public void setPaymentMethod(String paymentMethod) {
        this.paymentMethod = paymentMethod;
    }

    public String getReferenceCode() {
        return referenceCode;
    }

    public void setReferenceCode(String referenceCode) {
        this.referenceCode = referenceCode;
    }

    public String getMemberName() {
        return memberName;
    }

    public void setMemberName(String memberName) {
        this.memberName = memberName;
    }
}


