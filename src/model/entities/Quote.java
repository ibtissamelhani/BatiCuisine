package model.entities;

import java.time.LocalDate;

public class Quote {

    private int id;
    private Double estimatedAmount;
    private LocalDate validityDate;
    private LocalDate issueDate;
    private Boolean isAccepted;

    public Quote() {
    }

    public Quote(Double estimatedAmount, LocalDate validityDate, LocalDate issueDate, Boolean isAccepted) {
        this.estimatedAmount = estimatedAmount;
        this.validityDate = validityDate;
        this.issueDate = issueDate;
        this.isAccepted = isAccepted;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Double getEstimatedAmount() {
        return estimatedAmount;
    }

    public void setEstimatedAmount(Double estimatedAmount) {
        this.estimatedAmount = estimatedAmount;
    }

    public LocalDate getValidityDate() {
        return validityDate;
    }

    public void setValidityDate(LocalDate validityDate) {
        this.validityDate = validityDate;
    }

    public LocalDate getIssueDate() {
        return issueDate;
    }

    public void setIssueDate(LocalDate issueDate) {
        this.issueDate = issueDate;
    }

    public Boolean getAccepted() {
        return isAccepted;
    }

    public void setAccepted(Boolean accepted) {
        isAccepted = accepted;
    }
}
