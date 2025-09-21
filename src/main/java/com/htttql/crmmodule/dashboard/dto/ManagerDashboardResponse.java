package com.htttql.crmmodule.dashboard.dto;

import lombok.*;

import java.util.List;
import java.util.Map;

/**
 * Manager Dashboard Response DTO
 */
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class ManagerDashboardResponse {
    private Map<String, Object> overview;
    private List<Map<String, Object>> revenueAnalytics;
    private List<Map<String, Object>> staffPerformance;
    private List<Map<String, Object>> customerAnalytics;
    private List<Map<String, Object>> servicePerformance;
    private List<Map<String, Object>> inventoryAlerts;
    private Map<String, Object> financialSummary;
    private Map<String, Object> realtimeData;
}
