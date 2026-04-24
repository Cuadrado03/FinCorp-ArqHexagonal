package com.fincorp.domain.model;

import java.util.List;

public class CreditHistoryReport {
    private final String employeeName;
    private final List<Credit> previousCredits;
    private final Summary summary;
    private final int creditScore;

    public CreditHistoryReport(
            String employeeName,
            List<Credit> previousCredits,
            Summary summary,
            int creditScore
    ) {
        this.employeeName = employeeName;
        this.previousCredits = previousCredits;
        this.summary = summary;
        this.creditScore = creditScore;
    }

    public String getEmployeeName() {
        return employeeName;
    }

    public List<Credit> getPreviousCredits() {
        return previousCredits;
    }

    public Summary getSummary() {
        return summary;
    }

    public int getCreditScore() {
        return creditScore;
    }

    public static class Summary {
        private final int totalCredits;
        private final int approvedCredits;
        private final int rejectedCredits;

        public Summary(
                int totalCredits,
                int approvedCredits,
                int rejectedCredits
        ) {
            this.totalCredits = totalCredits;
            this.approvedCredits = approvedCredits;
            this.rejectedCredits = rejectedCredits;
        }

        public int getTotalCredits() {
            return totalCredits;
        }

        public int getApprovedCredits() {
            return approvedCredits;
        }

        public int getRejectedCredits() {
            return rejectedCredits;
        }
    }
}
