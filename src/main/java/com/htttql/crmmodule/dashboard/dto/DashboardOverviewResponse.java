package com.htttql.crmmodule.dashboard.dto;

import lombok.*;

/**
 * Dashboard Overview Response DTO
 */
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class DashboardOverviewResponse {
    private Long totalCustomers;
    private Long totalStaff;
    private Long totalCases;
    private Long totalAppointments;
    private Long totalInvoices;
    private Long totalPayments;
    private Long newCustomersToday;
    private Long appointmentsToday;
    private Long invoicesToday;
    private Double revenueToday;
}
