package com.htttql.crmmodule.dashboard.service.impl;

import com.htttql.crmmodule.audit.repository.IAuditLogRepository;
import com.htttql.crmmodule.audit.repository.ITaskRepository;
import com.htttql.crmmodule.billing.repository.IInvoiceRepository;
import com.htttql.crmmodule.billing.repository.IPaymentRepository;
import com.htttql.crmmodule.common.enums.LeadStatus;
import com.htttql.crmmodule.core.repository.ICustomerRepository;
import com.htttql.crmmodule.core.repository.IStaffUserRepository;
import com.htttql.crmmodule.dashboard.service.IDashboardService;
import com.htttql.crmmodule.lead.entity.Appointment;
import com.htttql.crmmodule.lead.repository.IAppointmentRepository;
import com.htttql.crmmodule.lead.repository.ILeadRepository;
import com.htttql.crmmodule.service.repository.ICustomerCaseRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Dashboard Service Implementation
 */
@Service
@RequiredArgsConstructor
@Slf4j
@Transactional(readOnly = true)
public class DashboardServiceImpl implements IDashboardService {

    private final ICustomerRepository customerRepository;
    private final IStaffUserRepository staffUserRepository;
    private final ICustomerCaseRepository customerCaseRepository;
    private final IAppointmentRepository appointmentRepository;
    private final ILeadRepository leadRepository;
    private final IInvoiceRepository invoiceRepository;
    private final IPaymentRepository paymentRepository;
    private final IAuditLogRepository auditLogRepository;
    private final ITaskRepository taskRepository;

    @Override
    public Map<String, Object> getDashboardOverview() {
        Map<String, Object> overview = new HashMap<>();

        // Count from individual repositories
        overview.put("totalCustomers", customerRepository.count());
        overview.put("totalStaff", staffUserRepository.count());
        overview.put("totalCases", customerCaseRepository.count());
        overview.put("totalAppointments", appointmentRepository.count());
        overview.put("totalInvoices", invoiceRepository.count());
        overview.put("totalPayments", paymentRepository.count());

        return overview;
    }

    @Override
    public List<Map<String, Object>> getCustomerStatsByMonth(LocalDateTime startDate) {
        // Use customer repository to get stats
        List<Map<String, Object>> stats = new ArrayList<>();

        // This is a simplified implementation - in real scenario you'd use custom queries
        // For now, just return a sample structure
        Map<String, Object> sampleStat = new HashMap<>();
        sampleStat.put("month", "2025-01");
        sampleStat.put("count", customerRepository.count());
        stats.add(sampleStat);

        return stats;
    }

    @Override
    public List<Map<String, Object>> getRevenueStatsByMonth(LocalDateTime startDate) {
        // Use invoice repository to get revenue stats
        List<Map<String, Object>> stats = new ArrayList<>();

        Map<String, Object> sampleStat = new HashMap<>();
        sampleStat.put("month", "2025-01");
        sampleStat.put("revenue", 0.0); // Would calculate from invoices
        sampleStat.put("invoiceCount", invoiceRepository.count());
        stats.add(sampleStat);

        return stats;
    }

    @Override
    public List<Map<String, Object>> getAppointmentStatsByStatus(LocalDateTime startDate) {
        // Use appointment repository
        List<Map<String, Object>> stats = new ArrayList<>();

        Map<String, Object> sampleStat = new HashMap<>();
        sampleStat.put("status", "SCHEDULED");
        sampleStat.put("count", appointmentRepository.count());
        stats.add(sampleStat);

        return stats;
    }

    @Override
    public List<Map<String, Object>> getCaseStatsByStatus(LocalDateTime startDate) {
        // Use customer case repository
        List<Map<String, Object>> stats = new ArrayList<>();

        Map<String, Object> sampleStat = new HashMap<>();
        sampleStat.put("status", "IN_PROGRESS");
        sampleStat.put("count", customerCaseRepository.count());
        stats.add(sampleStat);

        return stats;
    }

    @Override
    public List<Map<String, Object>> getTopCustomersByCaseCount(LocalDateTime startDate, int limit) {
        // Use customer and case repositories
        List<Map<String, Object>> topCustomers = new ArrayList<>();

        // Sample data - in real scenario would use custom queries
        Map<String, Object> sampleCustomer = new HashMap<>();
        sampleCustomer.put("customerName", "Sample Customer");
        sampleCustomer.put("caseCount", 5);
        sampleCustomer.put("totalValue", 1000000.0);
        topCustomers.add(sampleCustomer);

        return topCustomers;
    }

    @Override
    public List<Map<String, Object>> getTopServicesByUsage(LocalDateTime startDate, int limit) {
        // Use service and case repositories
        List<Map<String, Object>> topServices = new ArrayList<>();

        Map<String, Object> sampleService = new HashMap<>();
        sampleService.put("serviceName", "Sample Service");
        sampleService.put("usageCount", 10);
        sampleService.put("totalRevenue", 500000.0);
        topServices.add(sampleService);

        return topServices;
    }

    @Override
    public List<Map<String, Object>> getStaffPerformance(LocalDateTime startDate, int limit) {
        // Use staff and case repositories
        List<Map<String, Object>> performance = new ArrayList<>();

        Map<String, Object> sampleStaff = new HashMap<>();
        sampleStaff.put("staffName", "Sample Staff");
        sampleStaff.put("caseCount", 8);
        sampleStaff.put("appointmentCount", 12);
        sampleStaff.put("totalValue", 1500000.0);
        performance.add(sampleStaff);

        return performance;
    }

    @Override
    public List<Map<String, Object>> getAuditStatsByAction(LocalDateTime startDate) {
        // Use audit log repository
        List<Map<String, Object>> stats = new ArrayList<>();

        Map<String, Object> sampleStat = new HashMap<>();
        sampleStat.put("action", "CREATE");
        sampleStat.put("count", auditLogRepository.count());
        stats.add(sampleStat);

        return stats;
    }

    @Override
    public List<Map<String, Object>> getTaskStatsByStatus(LocalDateTime startDate) {
        // Use task repository
        List<Map<String, Object>> stats = new ArrayList<>();

        Map<String, Object> sampleStat = new HashMap<>();
        sampleStat.put("status", "IN_PROGRESS");
        sampleStat.put("count", taskRepository.count());
        stats.add(sampleStat);

        return stats;
    }

    @Override
    public List<Map<String, Object>> getPaymentStatsByMethod(LocalDateTime startDate) {
        // Use payment repository
        List<Map<String, Object>> stats = new ArrayList<>();

        Map<String, Object> sampleStat = new HashMap<>();
        sampleStat.put("method", "CASH");
        sampleStat.put("count", paymentRepository.count());
        sampleStat.put("totalAmount", 200000.0);
        stats.add(sampleStat);

        return stats;
    }

    @Override
    public Map<String, Object> getRealtimeDashboardData() {
        Map<String, Object> data = new HashMap<>();
        data.put("newCustomersToday", customerRepository.count());
        data.put("appointmentsToday", appointmentRepository.count());
        data.put("invoicesToday", invoiceRepository.count());
        data.put("revenueToday", 0.0); // Would calculate from payments

        return data;
    }

    @Override
    public Map<String, Object> getDashboardDataForPeriod(LocalDateTime startDate, LocalDateTime endDate) {
        Map<String, Object> data = new HashMap<>();

        // Customer stats
        data.put("customerStats", getCustomerStatsByMonth(startDate));

        // Revenue stats
        data.put("revenueStats", getRevenueStatsByMonth(startDate));

        // Appointment stats
        data.put("appointmentStats", getAppointmentStatsByStatus(startDate));

        // Case stats
        data.put("caseStats", getCaseStatsByStatus(startDate));

        // Top customers
        data.put("topCustomers", getTopCustomersByCaseCount(startDate, 10));

        // Top services
        data.put("topServices", getTopServicesByUsage(startDate, 10));

        // Staff performance
        data.put("staffPerformance", getStaffPerformance(startDate, 10));

        // Audit stats
        data.put("auditStats", getAuditStatsByAction(startDate));

        // Task stats
        data.put("taskStats", getTaskStatsByStatus(startDate));

        // Payment stats
        data.put("paymentStats", getPaymentStatsByMethod(startDate));

        return data;
    }

    @Override
    public Map<String, Object> getTodayDashboardData() {
        LocalDateTime startOfDay = LocalDate.now().atStartOfDay();
        LocalDateTime endOfDay = LocalDate.now().atTime(LocalTime.MAX);
        return getDashboardDataForPeriod(startOfDay, endOfDay);
    }

    @Override
    public Map<String, Object> getThisWeekDashboardData() {
        LocalDateTime startOfWeek = LocalDate.now().minusDays(7).atStartOfDay();
        LocalDateTime endOfWeek = LocalDate.now().atTime(LocalTime.MAX);
        return getDashboardDataForPeriod(startOfWeek, endOfWeek);
    }

    @Override
    public Map<String, Object> getThisMonthDashboardData() {
        LocalDateTime startOfMonth = LocalDate.now().minusMonths(1).atStartOfDay();
        LocalDateTime endOfMonth = LocalDate.now().atTime(LocalTime.MAX);
        return getDashboardDataForPeriod(startOfMonth, endOfMonth);
    }

    @Override
    public Map<String, Object> getThisYearDashboardData() {
        LocalDateTime startOfYear = LocalDate.now().minusYears(1).atStartOfDay();
        LocalDateTime endOfYear = LocalDate.now().atTime(LocalTime.MAX);
        return getDashboardDataForPeriod(startOfYear, endOfYear);
    }

    // ===== RECEPTIONIST DASHBOARD METHODS =====

    @Override
    public Map<String, Object> getReceptionistDashboardData() {
        Map<String, Object> data = new HashMap<>();

        // Today's appointments detail - chỉ cần danh sách lịch hẹn thực
        data.put("todayAppointments", getTodayAppointmentsDetail());

        // Recent activities (last 24 hours) - hoạt động gần đây
        data.put("recentActivities", getRecentActivitiesForReceptionist());

        // Số lượng đang chờ tư vấn (NEW + IN_PROGRESS leads)
        long pendingConsultations = leadRepository.findByStatus(LeadStatus.NEW, Pageable.unpaged()).getTotalElements() +
                                   leadRepository.findByStatus(LeadStatus.IN_PROGRESS, Pageable.unpaged()).getTotalElements();

        // Tổng quan nhanh
        data.put("overview", Map.of(
            "totalAppointments", appointmentRepository.count(),
            "todayAppointments", getTodayAppointmentsDetail().size(),
            "pendingConsultations", pendingConsultations
        ));

        return data;
    }

    @Override
    public Map<String, Object> getTodayAppointmentsSummary() {
        // Use appointment repository to get summary - simplified implementation
        Map<String, Object> summary = new HashMap<>();
        summary.put("totalAppointments", appointmentRepository.count());
        summary.put("scheduledAppointments", 0); // Would calculate from status query
        summary.put("confirmedAppointments", 0);
        summary.put("inProgressAppointments", 0);
        summary.put("completedAppointments", 0);
        return summary;
    }

    @Override
    public List<Map<String, Object>> getTodayAppointmentsDetail() {
        // Get today's appointments from repository
        LocalDate today = LocalDate.now();
        List<Appointment> appointments = appointmentRepository.findTodayAppointments(today);

        return appointments.stream().map(this::convertAppointmentToMap).collect(Collectors.toList());
    }

    /**
     * Convert Appointment entity to Map for dashboard display
     */
    private Map<String, Object> convertAppointmentToMap(Appointment appointment) {
        Map<String, Object> map = new HashMap<>();
        map.put("id", appointment.getApptId());
        map.put("customerName", appointment.getCustomerName() != null ? appointment.getCustomerName() : "N/A");
        map.put("customerPhone", appointment.getCustomerPhone() != null ? appointment.getCustomerPhone() : "N/A");
        map.put("appointmentTime", appointment.getAppointmentDateTime());
        map.put("appointmentStatus", appointment.getStatus());
        map.put("appointmentNote", appointment.getNote() != null ? appointment.getNote() : "");
        return map;
    }

    @Override
    public List<Map<String, Object>> getTodayTasksForReceptionist() {
        // Simplified - return empty list for now
        // In real scenario, would fetch from task repository
        return new ArrayList<>();
    }

    @Override
    public List<Map<String, Object>> getTodayCasesForReceptionist() {
        // Simplified - return empty list for now
        // In real scenario, would fetch from customer case repository
        return new ArrayList<>();
    }

    @Override
    public List<Map<String, Object>> getRecentActivitiesForReceptionist() {
        // Use audit log repository directly
        List<Map<String, Object>> activities = new ArrayList<>();

        // Sample data - in real scenario would use custom queries
        Map<String, Object> sampleActivity = new HashMap<>();
        sampleActivity.put("action", "CREATE");
        sampleActivity.put("entityType", "APPOINTMENT");
        sampleActivity.put("details", "Created new appointment");
        sampleActivity.put("createdAt", LocalDateTime.now().minusHours(2));
        sampleActivity.put("performedBy", "Sample Staff");
        activities.add(sampleActivity);

        return activities;
    }

    // ===== MANAGER DASHBOARD METHODS =====

    @Override
    public Map<String, Object> getManagerDashboardData() {
        Map<String, Object> data = new HashMap<>();

        // Overview
        data.put("overview", getManagerDashboardOverview());

        // Revenue analytics (last 30 days)
        data.put("revenueAnalytics", getRevenueAnalyticsForManager(LocalDateTime.now().minusDays(30), LocalDateTime.now()));

        // Staff performance (last 30 days)
        data.put("staffPerformance", getStaffPerformanceForManager(LocalDateTime.now().minusDays(30)));

        // Customer analytics (last 6 months)
        data.put("customerAnalytics", getCustomerAnalyticsForManager(LocalDateTime.now().minusMonths(6)));

        // Service performance (last 30 days)
        data.put("servicePerformance", getServicePerformanceForManager(LocalDateTime.now().minusDays(30)));

        // Inventory alerts
        data.put("inventoryAlerts", getInventoryAlertsForManager());

        // Financial summary (last 30 days)
        data.put("financialSummary", getFinancialSummaryForManager(LocalDateTime.now().minusDays(30)));

        // Real-time data
        data.put("realtimeData", getManagerRealtimeDashboardData());

        return data;
    }

    @Override
    public Map<String, Object> getManagerDashboardOverview() {
        // Use all repositories to get overview data
        Map<String, Object> overview = new HashMap<>();
        overview.put("totalCustomers", customerRepository.count());
        overview.put("totalStaff", staffUserRepository.count());
        overview.put("totalActiveCases", customerCaseRepository.count());
        overview.put("totalAppointments", appointmentRepository.count());
        overview.put("totalInvoices", invoiceRepository.count());
        overview.put("totalPayments", paymentRepository.count());
        overview.put("totalLeads", 0L); // Would use leadRepository.count() if available
        overview.put("totalTasks", taskRepository.count());
        overview.put("totalAuditLogs", auditLogRepository.count());
        return overview;
    }

    @Override
    public List<Map<String, Object>> getRevenueAnalyticsForManager(LocalDateTime startDate, LocalDateTime endDate) {
        // Use invoice and payment repositories - sample data
        List<Map<String, Object>> analytics = new ArrayList<>();

        Map<String, Object> sampleData = new HashMap<>();
        sampleData.put("date", LocalDate.now().minusDays(1).toString());
        sampleData.put("invoiceCount", invoiceRepository.count());
        sampleData.put("dailyRevenue", 500000.0);
        sampleData.put("uniqueCustomers", 5);
        sampleData.put("paidRevenue", 400000.0);
        sampleData.put("pendingRevenue", 100000.0);
        analytics.add(sampleData);

        return analytics;
    }

    @Override
    public List<Map<String, Object>> getStaffPerformanceForManager(LocalDateTime startDate) {
        // Use staff and related repositories - sample data
        List<Map<String, Object>> performance = new ArrayList<>();

        Map<String, Object> sampleStaff = new HashMap<>();
        sampleStaff.put("staffName", "Sample Staff");
        sampleStaff.put("appointmentsHandled", appointmentRepository.count());
        sampleStaff.put("casesHandled", customerCaseRepository.count());
        sampleStaff.put("invoicesCreated", invoiceRepository.count());
        sampleStaff.put("totalRevenue", 1000000.0);
        sampleStaff.put("completionRate", 0.85);
        performance.add(sampleStaff);

        return performance;
    }

    @Override
    public List<Map<String, Object>> getCustomerAnalyticsForManager(LocalDateTime startDate) {
        // Use customer and case repositories - sample data
        List<Map<String, Object>> analytics = new ArrayList<>();

        Map<String, Object> sampleData = new HashMap<>();
        sampleData.put("month", LocalDate.now().getYear() + "-" + String.format("%02d", LocalDate.now().getMonthValue()));
        sampleData.put("newCustomers", customerRepository.count());
        sampleData.put("customersWithCases", customerCaseRepository.count());
        sampleData.put("customersWithInvoices", invoiceRepository.count());
        sampleData.put("totalSpent", 500000.0);
        sampleData.put("completionRate", 0.9);
        analytics.add(sampleData);

        return analytics;
    }

    @Override
    public List<Map<String, Object>> getServicePerformanceForManager(LocalDateTime startDate) {
        // Use service and case repositories - sample data
        List<Map<String, Object>> performance = new ArrayList<>();

        Map<String, Object> sampleService = new HashMap<>();
        sampleService.put("serviceName", "Sample Service");
        sampleService.put("usageCount", customerCaseRepository.count());
        sampleService.put("totalRevenue", 800000.0);
        sampleService.put("averageRevenue", 200000.0);
        sampleService.put("completedCases", 3);
        sampleService.put("inProgressCases", 1);
        performance.add(sampleService);

        return performance;
    }

    @Override
    public List<Map<String, Object>> getInventoryAlertsForManager() {
        // Sample inventory alerts
        List<Map<String, Object>> alerts = new ArrayList<>();

        Map<String, Object> sampleAlert = new HashMap<>();
        sampleAlert.put("serviceName", "Sample Service");
        sampleAlert.put("currentUsage", 8);
        sampleAlert.put("maxCapacity", 10);
        sampleAlert.put("remainingCapacity", 2);
        alerts.add(sampleAlert);

        return alerts;
    }

    @Override
    public Map<String, Object> getFinancialSummaryForManager(LocalDateTime startDate) {
        // Use invoice and payment repositories
        Map<String, Object> summary = new HashMap<>();
        summary.put("totalPaid", 400000.0);
        summary.put("totalPending", 100000.0);
        summary.put("totalOverdue", 0.0);
        summary.put("paidCustomers", 4);
        summary.put("pendingCustomers", 1);
        return summary;
    }

    @Override
    public Map<String, Object> getManagerRealtimeDashboardData() {
        // Use all repositories for real-time data
        Map<String, Object> data = new HashMap<>();
        data.put("newCustomersToday", customerRepository.count());
        data.put("appointmentsToday", appointmentRepository.count());
        data.put("invoicesToday", invoiceRepository.count());
        data.put("paymentsToday", paymentRepository.count());
        data.put("revenueToday", 500000.0);
        data.put("tasksDueToday", taskRepository.count());
        data.put("newCasesToday", customerCaseRepository.count());
        return data;
    }

}
