package com.htttql.crmmodule.dashboard.dto;

import lombok.*;

import java.util.List;
import java.util.Map;

/**
 * Dashboard Stats Response DTO
 */
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class DashboardStatsResponse {
    private DashboardOverviewResponse overview;
    private List<Map<String, Object>> customerStats;
    private List<Map<String, Object>> revenueStats;
    private List<Map<String, Object>> appointmentStats;
    private List<Map<String, Object>> caseStats;
    private List<Map<String, Object>> topCustomers;
    private List<Map<String, Object>> topServices;
    private List<Map<String, Object>> staffPerformance;
    private List<Map<String, Object>> auditStats;
    private List<Map<String, Object>> taskStats;
    private List<Map<String, Object>> paymentStats;
    private Map<String, Object> realtimeData;
}
