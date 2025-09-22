# Sequence Diagrams for CRM Module

Dưới đây là 5 sequence diagram mô tả các luồng chính trong hệ thống CRM. Các sơ đồ được viết bằng Mermaid và có thể render trực tiếp trên GitHub hoặc công cụ hỗ trợ Mermaid (như VS Code extensions). Bạn có thể copy code Mermaid vào trình soạn thảo để xem.

## 1. Tạo Lead và Chuyển đổi thành Customer
```mermaid
sequenceDiagram
    participant User as User/External System
    participant LeadController
    participant LeadService
    participant LeadRepository
    participant CustomerService
    participant CustomerRepository

    User->>LeadController: POST /api/leads (fullName, phone, source)
    activate LeadController
    LeadController->>LeadService: createLead(request)
    activate LeadService
    LeadService->>LeadRepository: save(lead)
    activate LeadRepository
    LeadRepository-->>LeadService: leadEntity
    deactivate LeadRepository
    LeadService-->>LeadController: LeadResponse
    deactivate LeadService
    LeadController-->>User: response (lead ID, status "New")
    deactivate LeadController

    note over User: Sau đó, cập nhật status
    User->>LeadController: PUT /api/leads/{id}/status (status="Converted")
    activate LeadController
    LeadController->>LeadService: updateLeadStatus(id, request)
    activate LeadService
    LeadService->>CustomerService: convertToCustomer(leadData)
    activate CustomerService
    CustomerService->>CustomerRepository: save(customer)
    activate CustomerRepository
    CustomerRepository-->>CustomerService: customerEntity
    deactivate CustomerRepository
    CustomerService-->>LeadService: response
    deactivate CustomerService
    LeadService-->>LeadController: LeadResponse
    deactivate LeadService
    LeadController-->>User: response (status "Converted")
    deactivate LeadController
```

## 2. Đặt và Xác nhận Lịch hẹn
```mermaid
sequenceDiagram
    participant Customer as Customer/User
    participant AppointmentController
    participant AppointmentService
    participant AppointmentRepository
    participant CustomerRepository
    participant StaffService

    Customer->>AppointmentController: POST /api/appointments (customerId, staffId, appointmentDateTime, serviceId)
    activate AppointmentController
    AppointmentController->>AppointmentService: createAppointment(request)
    activate AppointmentService
    AppointmentService->>CustomerRepository: findById(customerId)
    activate CustomerRepository
    CustomerRepository-->>AppointmentService: customerEntity
    deactivate CustomerRepository
    AppointmentService->>StaffService: checkAvailability(staffId)
    activate StaffService
    StaffService-->>AppointmentService: availabilityStatus
    deactivate StaffService
    AppointmentService->>AppointmentRepository: save(appointment)
    activate AppointmentRepository
    AppointmentRepository-->>AppointmentService: appointmentEntity
    deactivate AppointmentRepository
    AppointmentService-->>AppointmentController: AppointmentResponse
    deactivate AppointmentService
    AppointmentController-->>Customer: response (appointment ID, status "Pending")
    deactivate AppointmentController

    note over Customer: Xác nhận appointment
    Customer->>AppointmentController: PUT /api/appointments/{id}/status (status="Confirmed", notes)
    activate AppointmentController
    AppointmentController->>AppointmentService: updateAppointmentStatus(id, status, notes)
    activate AppointmentService
    AppointmentService->>AppointmentRepository: save(updatedAppointment)
    activate AppointmentRepository
    AppointmentRepository-->>AppointmentService: updatedEntity
    deactivate AppointmentRepository
    AppointmentService-->>AppointmentController: response
    deactivate AppointmentService
    AppointmentController-->>Customer: response
    deactivate AppointmentController
```

## 3. Tạo Hóa đơn và Thanh toán
```mermaid
sequenceDiagram
    participant Staff as Staff/User
    participant InvoiceController
    participant InvoiceService
    participant InvoiceRepository
    participant CustomerService
    participant PaymentController
    participant PaymentService
    participant PaymentRepository

    Staff->>InvoiceController: POST /api/invoices (customerId, serviceIds, amounts)
    activate InvoiceController
    InvoiceController->>InvoiceService: createInvoice(request)
    activate InvoiceService
    InvoiceService->>CustomerService: refreshCustomerTier(customerId)
    activate CustomerService
    CustomerService-->>InvoiceService: tierUpdated
    deactivate CustomerService
    InvoiceService->>InvoiceRepository: save(invoice)
    activate InvoiceRepository
    InvoiceRepository-->>InvoiceService: invoiceEntity
    deactivate InvoiceRepository
    InvoiceService-->>InvoiceController: InvoiceResponse
    deactivate InvoiceService
    InvoiceController-->>Staff: response (invoice ID, status "Pending")
    deactivate InvoiceController

    note over Staff: Xử lý thanh toán
    Staff->>PaymentController: POST /api/payments (invoiceId, paymentMethod, amount)
    activate PaymentController
    PaymentController->>PaymentService: createPayment(request)
    activate PaymentService
    PaymentService->>PaymentRepository: save(payment)
    activate PaymentRepository
    PaymentRepository-->>PaymentService: paymentEntity
    deactivate PaymentRepository
    PaymentService->>InvoiceService: updateInvoiceStatus(invoiceId, "Paid")
    activate InvoiceService
    InvoiceService-->>PaymentService: statusUpdated
    deactivate InvoiceService
    PaymentService-->>PaymentController: PaymentResponse
    deactivate PaymentService
    PaymentController-->>Staff: response
    deactivate PaymentController
```

## 4. Đăng nhập và Lấy Dữ liệu Dashboard
```mermaid
sequenceDiagram
    participant User as User
    participant AuthController
    participant SecurityContextService
    participant DashboardController
    participant DashboardService
    participant ICustomerService
    participant ILeadService
    participant IInvoiceService

    User->>AuthController: POST /api/auth/login (username, password)
    activate AuthController
    AuthController->>SecurityContextService: authenticate(credentials)
    activate SecurityContextService
    SecurityContextService-->>AuthController: JWT Token
    deactivate SecurityContextService
    AuthController-->>User: token
    deactivate AuthController

    note over User: Sử dụng token để truy cập dashboard
    User->>DashboardController: GET /api/dashboard/overview (Bearer token)
    activate DashboardController
    DashboardController->>SecurityContextService: getCurrentStaffId(token)
    activate SecurityContextService
    SecurityContextService-->>DashboardController: staffId
    deactivate SecurityContextService
    DashboardController->>DashboardService: getDashboardOverview()
    activate DashboardService
    DashboardService->>ICustomerService: getCustomerStats()
    activate ICustomerService
    ICustomerService-->>DashboardService: customerStats
    deactivate ICustomerService
    DashboardService->>ILeadService: getLeadStats()
    activate ILeadService
    ILeadService-->>DashboardService: leadStats
    deactivate ILeadService
    DashboardService->>IInvoiceService: getRevenueStats()
    activate IInvoiceService
    IInvoiceService-->>DashboardService: revenueStats
    deactivate IInvoiceService
    DashboardService-->>DashboardController: aggregatedData (Map<String, Object>)
    deactivate DashboardService
    DashboardController-->>User: response
    deactivate DashboardController
```

## 5. Tạo và Cập nhật Task trong Audit
```mermaid
sequenceDiagram
    participant Staff as Staff
    participant AuditController
    participant TaskService
    participant TaskRepository
    participant AuditService

    Staff->>AuditController: POST /api/audit/tasks (title, description, assigneeId, dueDate)
    activate AuditController
    AuditController->>TaskService: createTask(request)
    activate TaskService
    TaskService->>TaskRepository: save(task)
    activate TaskRepository
    TaskRepository-->>TaskService: taskEntity
    deactivate TaskRepository
    TaskService->>AuditService: log("Task created")
    activate AuditService
    AuditService-->>TaskService: logResponse
    deactivate AuditService
    TaskService-->>AuditController: TaskResponse
    deactivate TaskService
    AuditController-->>Staff: response (task ID, status "Pending")
    deactivate AuditController

    note over Staff: Cập nhật status
    Staff->>AuditController: PUT /api/audit/tasks/{id}/status (status="In Progress", notes)
    activate AuditController
    AuditController->>TaskService: updateTaskStatus(id, status)
    activate TaskService
    TaskService->>TaskRepository: save(updatedTask)
    activate TaskRepository
    TaskRepository-->>TaskService: updatedEntity
    deactivate TaskRepository
    TaskService->>AuditService: log("Task status updated")
    activate AuditService
    AuditService-->>TaskService: logResponse
    deactivate AuditService
    TaskService-->>AuditController: response
    deactivate TaskService
    AuditController-->>Staff: response
    deactivate AuditController
```

### Hướng dẫn sử dụng:
- **Render trên GitHub:** GitHub tự động render Mermaid nếu bạn dùng fenced code blocks như ```mermaid.
- **Công cụ khác:** Copy code vào Mermaid editor (mermaid.live), VS Code với extension Mermaid, hoặc diagrams.net (import từ Mermaid).
- **Tùy chỉnh:** Nếu cần thêm details (e.g., alt fragments cho errors), cho tôi biết để update!

Nếu bạn muốn file riêng cho từng diagram hoặc chỉnh sửa, cứ nói nhé! 😊
