package com.htttql.crmmodule.lead.service.impl;

import com.htttql.crmmodule.common.enums.AppointmentStatus;
import com.htttql.crmmodule.common.exception.ResourceNotFoundException;
import com.htttql.crmmodule.core.repository.ICustomerRepository;
import com.htttql.crmmodule.lead.dto.AppointmentRequest;
import com.htttql.crmmodule.lead.dto.AppointmentResponse;
import com.htttql.crmmodule.lead.entity.Appointment;
import com.htttql.crmmodule.lead.repository.IAppointmentRepository;
import com.htttql.crmmodule.lead.repository.ILeadRepository;
import com.htttql.crmmodule.lead.service.IAppointmentService;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.*;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;


@Service
@RequiredArgsConstructor
public class AppointmentServiceImpl implements IAppointmentService {

    private final IAppointmentRepository appointmentRepository;
    private final ILeadRepository leadRepository;
    private final ICustomerRepository customerRepository;

    // 👉 Mapper nội bộ - simplified for simple reminders
    private AppointmentResponse toResponse(Appointment appointment) {
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

    @Override
    public Page<AppointmentResponse> getAllAppointments(Pageable pageable) {
        return appointmentRepository.findAll(pageable).map(this::toResponse);
    }

    @Override
    public AppointmentResponse getAppointmentById(Long id) {
        Appointment appointment = appointmentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Appointment not found with id: " + id));
        return toResponse(appointment);
    }

    @Override
    public Page<AppointmentResponse> getAppointmentsByCustomerId(Long customerId, Pageable pageable) {
        return appointmentRepository.findByCustomerIdOrderByAppointmentDateTimeDesc(customerId, pageable)
                .map(this::toResponse);
    }

    @Override
    public AppointmentResponse createAppointment(AppointmentRequest request) {
        // 👉 Validate request
        if (!request.isValid()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, request.getValidationMessage());
        }

        // 👉 Get customer info with priority: Customer > Lead > Request
        String customerName = request.getCustomerName();
        String customerPhone = request.getCustomerPhone();

        // 1️⃣ Ưu tiên 1: Customer (nếu có customerId)
        if (request.getCustomerId() != null) {
            var customer = customerRepository.findById(request.getCustomerId())
                    .orElseThrow(() -> new ResourceNotFoundException("Customer not found with id: " + request.getCustomerId()));
            customerName = customer.getFullName();
            customerPhone = customer.getPhone();
        }
        // 2️⃣ Ưu tiên 2: Lead (nếu không có customerId nhưng có leadId)
        else if (request.getLeadId() != null) {
            var lead = leadRepository.findById(request.getLeadId())
                    .orElseThrow(() -> new ResourceNotFoundException("Lead not found with id: " + request.getLeadId()));
            customerName = lead.getFullName();
            customerPhone = lead.getPhone();
        }
        // 3️⃣ Ưu tiên 3: Request data (nếu cả customerId và leadId đều null)

        Appointment appointment = Appointment.builder()
                .leadId(request.getLeadId())
                .customerId(request.getCustomerId())
                .customerName(customerName)      // 👈 Use resolved name
                .customerPhone(customerPhone)   // 👈 Use resolved phone
                .appointmentDateTime(request.getAppointmentDateTime())
                .status(request.getStatus() != null ? request.getStatus() : AppointmentStatus.SCHEDULED)
                .note(request.getNotes())
                .build();

        return toResponse(appointmentRepository.save(appointment));
    }

    @Override
    public AppointmentResponse updateAppointment(Long id, AppointmentRequest request) {
        Appointment appointment = appointmentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Appointment not found with id: " + id));

        // 👉 Get customer info with priority: Customer > Lead > Request
        String customerName = request.getCustomerName();
        String customerPhone = request.getCustomerPhone();

        // 1️⃣ Ưu tiên 1: Customer (nếu có customerId)
        if (request.getCustomerId() != null) {
            var customer = customerRepository.findById(request.getCustomerId())
                    .orElseThrow(() -> new ResourceNotFoundException("Customer not found with id: " + request.getCustomerId()));
            customerName = customer.getFullName();
            customerPhone = customer.getPhone();
        }
        // 2️⃣ Ưu tiên 2: Lead (nếu không có customerId nhưng có leadId)
        else if (request.getLeadId() != null) {
            var lead = leadRepository.findById(request.getLeadId())
                    .orElseThrow(() -> new ResourceNotFoundException("Lead not found with id: " + request.getLeadId()));
            customerName = lead.getFullName();
            customerPhone = lead.getPhone();
        }
        // 3️⃣ Ưu tiên 3: Request data (nếu cả customerId và leadId đều null)

        // 👉 Update fields
        appointment.setLeadId(request.getLeadId());
        appointment.setCustomerId(request.getCustomerId());
        appointment.setCustomerName(customerName);      // 👈 Use resolved name
        appointment.setCustomerPhone(customerPhone);   // 👈 Use resolved phone
        appointment.setAppointmentDateTime(request.getAppointmentDateTime());
        appointment.setStatus(request.getStatus() != null ? request.getStatus() : appointment.getStatus());
        appointment.setNote(request.getNotes());

        return toResponse(appointmentRepository.save(appointment));
    }

    @Override
    public void deleteAppointment(Long id) {
        if (!appointmentRepository.existsById(id)) {
            throw new ResourceNotFoundException("Appointment not found with id: " + id);
        }
        appointmentRepository.deleteById(id);
    }

    @Override
    public AppointmentResponse updateAppointmentStatus(Long id, String status) {
        return updateAppointmentStatus(id, status, null, null);
    }

    @Override
    public AppointmentResponse updateAppointmentStatus(Long id, String status, String reason, String notes) {
        Appointment appointment = appointmentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Appointment not found with id: " + id));

        AppointmentStatus appointmentStatus = AppointmentStatus.valueOf(status.toUpperCase());
        appointment.setStatus(appointmentStatus);

        // Update confirmedAt if status is CONFIRMED
        if (appointmentStatus == AppointmentStatus.CONFIRMED) {
            appointment.setConfirmedAt(java.time.LocalDateTime.now());
        }

        // Update cancelledReason if status is CANCELLED and reason is provided
        if (appointmentStatus == AppointmentStatus.CANCELLED && reason != null) {
            appointment.setCancelledReason(reason);
        }

        // Update notes if provided
        if (notes != null) {
            appointment.setNote(notes);
        }

        return toResponse(appointmentRepository.save(appointment));
    }

}
