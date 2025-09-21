package com.htttql.crmmodule.lead.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.htttql.crmmodule.common.config.CustomLocalTimeDeserializer;
import com.htttql.crmmodule.common.enums.AppointmentStatus;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalTime;

/**
 * DTO for simple Appointment creation/update
 * Simplified version - just basic reminder information
 * - Flexible references: leadId, customerId, or direct name/phone
 * - Simple date/time instead of complex scheduling
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class AppointmentRequest {

    // 👇 Flexible references - can be null, one, or both
    private Long leadId;                // Optional reference to lead
    private Long customerId;            // Optional reference to customer

    // 👇 Direct customer info for simple reminders
    private String customerName;        // Customer name for reminder
    private String customerPhone;       // Customer phone for reminder

    // 👇 Simple appointment date/time
    @NotNull(message = "Appointment date and time is required")
    private java.time.LocalDateTime appointmentDateTime;  // Date and time

    // 👇 Optional status and note
    private AppointmentStatus status;   // Default: SCHEDULED

    @Size(max = 500, message = "Notes must not exceed 500 characters")
    private String notes;               // Reminder note

    // 👇 Validation: at least one identifier or name/phone
    public boolean isValid() {
        return (leadId != null || customerId != null ||
                (customerName != null && customerPhone != null));
    }

    public String getValidationMessage() {
        if (isValid()) return "Valid";
        return "Either leadId, customerId, or both customerName and customerPhone must be provided";
    }

    // 👇 Smart validation for business logic
    public boolean hasCustomerOrLead() {
        return leadId != null || customerId != null;
    }

    public boolean needsDirectInfo() {
        return !hasCustomerOrLead();
    }

    public boolean hasCompleteInfo() {
        return hasCustomerOrLead() ||
               (customerName != null && customerPhone != null);
    }
}
