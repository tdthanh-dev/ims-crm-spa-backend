package com.htttql.crmmodule.lead.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.htttql.crmmodule.common.enums.AppointmentStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

/**
 * Appointment Response DTO - Simple reminder information
 * Simplified version for basic appointment reminders
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class AppointmentResponse {

    private Long apptId;

    // 👇 Flexible references
    private Long leadId;                // Optional reference to lead
    private Long customerId;            // Optional reference to customer

    // 👇 Basic customer info for reminder
    private String customerName;        // Customer name
    private String customerPhone;       // Customer phone

    // 👇 Simple appointment date/time
    private java.time.LocalDateTime appointmentDateTime;  // Date and time

    // 👇 Status and note
    private AppointmentStatus status;   // Appointment status
    private String note;                // Reminder note

    // 👇 Tracking fields
    private Boolean reminderSent;       // Has reminder been sent
    private LocalDateTime confirmedAt;  // When was it confirmed
    private String cancelledReason;     // Why was it cancelled

    // 👇 Metadata
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    // 👇 Computed fields for display
    public String getDisplayDate() {
        return appointmentDateTime != null ? appointmentDateTime.toLocalDate().toString() : "N/A";
    }

    public String getDisplayTime() {
        return appointmentDateTime != null ? appointmentDateTime.toLocalTime().toString() : "N/A";
    }

    public String getDisplayStatus() {
        if (status == null) return "Unknown";
        return switch (status) {
            case SCHEDULED -> "Đã lên lịch";
            case CONFIRMED -> "Đã xác nhận";
            case NO_SHOW -> "Không đến";
            case DONE -> "Hoàn thành";
            case CANCELLED -> "Đã hủy";
        };
    }

    public boolean isToday() {
        return appointmentDateTime != null &&
               appointmentDateTime.toLocalDate().equals(LocalDate.now());
    }

    public boolean isUpcoming() {
        return appointmentDateTime != null &&
               appointmentDateTime.isAfter(LocalDateTime.now());
    }

    public boolean isPast() {
        return appointmentDateTime != null &&
               appointmentDateTime.isBefore(LocalDateTime.now());
    }

    public String getAppointmentDateTime() {
        return appointmentDateTime != null ? appointmentDateTime.toString() : "N/A";
    }
}