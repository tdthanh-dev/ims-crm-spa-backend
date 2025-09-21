package com.htttql.crmmodule.lead.entity;

import com.htttql.crmmodule.common.config.SchemaConstants;
import com.htttql.crmmodule.common.entity.BaseEntity;
import com.htttql.crmmodule.common.enums.AppointmentStatus;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalTime;

/**
 * Appointment entity for simple reminders
 * Simplified version - just basic reminder information
 * No complex relationships, just name, phone, date, note for reminder purposes
 */
@Entity
@Table(name = "appointment", schema = SchemaConstants.LEAD_SCHEMA, indexes = {
        @Index(name = "idx_appt_date", columnList = "appointment_date"),
        @Index(name = "idx_appt_phone", columnList = "customer_phone"),
        @Index(name = "idx_appt_status", columnList = "status"),
        @Index(name = "idx_appt_customer", columnList = "customer_id"),
        @Index(name = "idx_appt_lead", columnList = "lead_id")
})
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Appointment extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "appt_seq")
    @SequenceGenerator(name = "appt_seq", sequenceName = SchemaConstants.LEAD_SCHEMA
            + ".appointment_seq", allocationSize = 1)
    @Column(name = "appt_id")
    private Long apptId;

    // 👇 Flexible references - can be null, one, or both
    @Column(name = "lead_id")
    private Long leadId;        // Optional reference to lead

    @Column(name = "customer_id")
    private Long customerId;    // Optional reference to customer

    // 👇 Basic info for reminder (stored directly, no FK needed)
    @Column(name = "customer_name", length = 200)
    private String customerName;    // Customer name for reminder

    @Column(name = "customer_phone", length = 20)
    private String customerPhone;   // Customer phone for reminder

    // 👇 Simple appointment date and time
    @Column(name = "appointment_date_time", nullable = false, columnDefinition = "TIMESTAMPTZ")
    private java.time.LocalDateTime appointmentDateTime;  // Date and time

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false, length = 20)
    @Builder.Default
    private AppointmentStatus status = AppointmentStatus.SCHEDULED;

    @Column(name = "note", columnDefinition = "TEXT")
    private String note;  // Reminder note

    // 👇 Simple tracking
    @Column(name = "reminder_sent")
    @Builder.Default
    private Boolean reminderSent = false;

    @Column(name = "confirmed_at", columnDefinition = "TIMESTAMPTZ")
    private java.time.LocalDateTime confirmedAt;

    @Column(name = "cancelled_reason", columnDefinition = "TEXT")
    private String cancelledReason;

    // 👇 Helper methods for display
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
        return appointmentDateTime != null && appointmentDateTime.toLocalDate().equals(java.time.LocalDate.now());
    }

    public boolean isUpcoming() {
        return appointmentDateTime != null && appointmentDateTime.isAfter(java.time.LocalDateTime.now());
    }

    public boolean isPast() {
        return appointmentDateTime != null && appointmentDateTime.isBefore(java.time.LocalDateTime.now());
    }

    @PrePersist
    @PreUpdate
    private void validateAppointment() {
        // Basic validation - name and phone should be provided if no IDs
        if (leadId == null && customerId == null) {
            if (customerName == null || customerName.trim().isEmpty()) {
                throw new IllegalArgumentException("Customer name is required when no lead/customer ID is provided");
            }
            if (customerPhone == null || customerPhone.trim().isEmpty()) {
                throw new IllegalArgumentException("Customer phone is required when no lead/customer ID is provided");
            }
        }

        // Normalize phone if provided
        if (customerPhone != null) {
            customerPhone = customerPhone.replaceAll("[^0-9]", "");
        }
    }
}
