package com.htttql.crmmodule.dashboard.dto;

import lombok.*;

import java.util.List;
import java.util.Map;

/**
 * Receptionist Dashboard Response DTO
 */
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class ReceptionistDashboardResponse {
    private Map<String, Object> todayAppointmentsSummary;
    private List<Map<String, Object>> todayAppointmentsDetail;
    private List<Map<String, Object>> todayTasks;
    private List<Map<String, Object>> todayCases;
    private List<Map<String, Object>> recentActivities;
}
