package com.fincorp.domain.service;

import com.fincorp.domain.model.Credit;
import com.fincorp.domain.model.CreditHistoryReport;
import java.util.List;

public class CreditHistoryAnalyzer {

    public CreditHistoryReport.Summary buildSummary(List<Credit> credits) {
        int totalCredits = credits.size();
        int approvedCredits = (int) credits.stream().filter(c -> "APROBADO".equalsIgnoreCase(c.getStatus())).count();
        int rejectedCredits = totalCredits - approvedCredits;

        return new CreditHistoryReport.Summary(
                totalCredits,
                approvedCredits,
                rejectedCredits
        );
    }

    public int calculateScore(List<Credit> credits, CreditHistoryReport.Summary summary) {
        if (credits.isEmpty()) {
            return 500;
        }

        double approvedRate = (double) summary.getApprovedCredits() / summary.getTotalCredits();
        return (int) Math.round(300 + (approvedRate * 550));
    }
}
