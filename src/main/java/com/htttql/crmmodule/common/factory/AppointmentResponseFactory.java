package com.htttql.crmmodule.common.factory;

import com.htttql.crmmodule.lead.dto.AppointmentResponse;
import com.htttql.crmmodule.lead.entity.Appointment;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

/**
 * Appointment Response Factory - Simplified for basic reminders
 * Transforms appointment data for display purposes only
 */
@Component
@RequiredArgsConstructor
public class AppointmentResponseFactory {

    /**
     * Create appointment response for simple reminders
     */
    public AppointmentResponse createResponse(Appointment appointment) {
        return AppointmentResponse.builder()
                .apptId(appointment.getApptId())
                .leadId(appointment.getLeadId())
                .customerId(appointment.getCustomerId())
                .customerName(appointment.getCustomerName())
                .customerPhone(appointment.getCustomerPhone())
                .appointmentDateTime(appointment.getAppointmentDateTime())
                .status(appointment.getStatus())
                .note(appointment.getNote())
                .reminderSent(appointment.getReminderSent())
                .confirmedAt(appointment.getConfirmedAt())
                .cancelledReason(appointment.getCancelledReason())
                .createdAt(appointment.getCreatedAt())
                .updatedAt(appointment.getUpdatedAt())
                .build();
    }

    /**
     * Create minimal appointment response for list views
     */
    public AppointmentResponse createMinimalResponse(Appointment appointment) {
        return AppointmentResponse.builder()
                .apptId(appointment.getApptId())
                .customerName(appointment.getCustomerName() != null ?
                        maskCustomerName(appointment.getCustomerName()) : "Unknown")
                .appointmentDateTime(appointment.getAppointmentDateTime())
                .status(appointment.getStatus())
                .note(appointment.getNote())
                .build();
    }

    /**
     * Create customer-facing appointment response for reminders
     */
    public AppointmentResponse createCustomerResponse(Appointment appointment) {
        return AppointmentResponse.builder()
                .apptId(appointment.getApptId())
                .customerName(appointment.getCustomerName())
                .customerPhone(appointment.getCustomerPhone())
                .appointmentDateTime(appointment.getAppointmentDateTime())
                .status(appointment.getStatus())
                .note(appointment.getNote())
                .build();
    }

    // Helper methods

    /**
     * Mask customer name for privacy in list views
     */
    private String maskCustomerName(String fullName) {
        if (fullName == null || fullName.trim().isEmpty())
            return "Unknown";

        String[] parts = fullName.trim().split("\\s+");
        if (parts.length == 1) {
            return parts[0].substring(0, 1) + "***";
        }

        // Show first name + masked last name
        String firstName = parts[0];
        String lastName = parts[parts.length - 1];
        return firstName + " " + lastName.substring(0, 1) + "***";
    }
}
