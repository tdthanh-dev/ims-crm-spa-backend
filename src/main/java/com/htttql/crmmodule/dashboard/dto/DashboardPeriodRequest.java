package com.htttql.crmmodule.dashboard.dto;

import lombok.*;

import java.time.LocalDateTime;

/**
 * Dashboard Period Request DTO
 */
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class DashboardPeriodRequest {
    private LocalDateTime startDate;
    private LocalDateTime endDate;
    private String period; // "today", "week", "month", "year", "custom"
}
