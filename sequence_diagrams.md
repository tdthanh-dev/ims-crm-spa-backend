# Sequence Diagrams for CRM Module

Dưới đây là các sequence diagram mô tả các luồng chính trong hệ thống CRM, dựa trên source code thực tế. Các sơ đồ được viết bằng Mermaid và có thể render trực tiếp trên GitHub hoặc công cụ hỗ trợ Mermaid. Tôi đã xóa các sơ đồ cũ và tạo lại dựa trên luồng chính xác từ source BE.

## 1. Tạo Lead (Public API, Check SĐT để xác định khách cũ/mới)
```mermaid
sequenceDiagram
    participant User as User/External System
    participant LeadController
    participant LeadCoordinatorService
    participant LeadAntiSpamService
    participant LeadValidator
    participant LeadRepository

    User->>LeadController: POST /api/leads (fullName, phone, note)
    activate LeadController
    LeadController->>LeadCoordinatorService: createLead(request)
    activate LeadCoordinatorService
    LeadCoordinatorService->>LeadAntiSpamService: validateAndReserve(request, context)
    activate LeadAntiSpamService
    LeadAntiSpamService->>LeadValidator: getCustomerIdByPhone(phone)
    activate LeadValidator
    LeadValidator-->>LeadAntiSpamService: existingCustomerId (null if new)
    deactivate LeadValidator
    LeadAntiSpamService-->>LeadCoordinatorService: validationResult (valid, existingCustomerId)
    deactivate LeadAntiSpamService
    LeadCoordinatorService->>LeadRepository: save(lead)
    activate LeadRepository
    LeadRepository-->>LeadCoordinatorService: leadEntity
    deactivate LeadRepository
    LeadCoordinatorService-->>LeadController: LeadResponse
    deactivate LeadCoordinatorService
    LeadController-->>User: response (lead ID, status "New", isExistingCustomer)
    deactivate LeadController
```

## 2. Đăng nhập sinh Token
```mermaid
sequenceDiagram
    participant User as User
    participant AuthController
    participant AuthService
    participant AuthenticationManager
    participant StaffUserRepository
    participant JwtUtils

    User->>AuthController: POST /api/auth/login (username, password)
    activate AuthController
    AuthController->>AuthService: login(request)
    activate AuthService
    AuthService->>AuthenticationManager: authenticate(credentials)
    activate AuthenticationManager
    AuthenticationManager-->>AuthService: authentication
    deactivate AuthenticationManager
    AuthService->>StaffUserRepository: findByEmailOrPhone(username)
    activate StaffUserRepository
    StaffUserRepository-->>AuthService: user
    deactivate StaffUserRepository
    AuthService->>JwtUtils: generateToken(username, claims)
    activate JwtUtils
    JwtUtils-->>AuthService: accessToken
    deactivate JwtUtils
    AuthService->>JwtUtils: generateRefreshToken(username)
    activate JwtUtils
    JwtUtils-->>AuthService: refreshToken
    deactivate JwtUtils
    AuthService-->>AuthController: JwtResponse (accessToken, refreshToken, userInfo)
    deactivate AuthService
    AuthController-->>User: response
    deactivate AuthController
```

## 3. Tạo Khách hàng
```mermaid
sequenceDiagram
    participant User as User
    participant CustomerController
    participant CustomerService
    participant CustomerRepository
    participant TierRepository

    User->>CustomerController: POST /api/customers (fullName, phone, email, address)
    activate CustomerController
    CustomerController->>CustomerService: createCustomer(request)
    activate CustomerService
    CustomerService->>CustomerRepository: existsByPhone(phone)
    activate CustomerRepository
    CustomerRepository-->>CustomerService: false (if new)
    deactivate CustomerRepository
    CustomerService->>TierRepository: findByCode("REGULAR")
    activate TierRepository
    TierRepository-->>CustomerService: defaultTier
    deactivate TierRepository
    CustomerService->>CustomerRepository: save(customer)
    activate CustomerRepository
    CustomerRepository-->>CustomerService: customerEntity
    deactivate CustomerRepository
    CustomerService-->>CustomerController: CustomerResponse
    deactivate CustomerService
    CustomerController-->>User: response (customer ID, tier)
    deactivate CustomerController
```

## 4. Tạo Lịch hẹn
```mermaid
sequenceDiagram
    participant User as User
    participant AppointmentController
    participant AppointmentService
    participant AppointmentRepository
    participant CustomerRepository
    participant LeadRepository

    User->>AppointmentController: POST /api/appointments (customerId, leadId, appointmentDateTime, serviceId, notes)
    activate AppointmentController
    AppointmentController->>AppointmentService: createAppointment(request)
    activate AppointmentService
    alt if customerId provided
        AppointmentService->>CustomerRepository: findById(customerId)
        activate CustomerRepository
        CustomerRepository-->>AppointmentService: customerEntity
        deactivate CustomerRepository
    else if leadId provided
        AppointmentService->>LeadRepository: findById(leadId)
        activate LeadRepository
        LeadRepository-->>AppointmentService: leadEntity
        deactivate LeadRepository
    end
    AppointmentService->>AppointmentRepository: save(appointment)
    activate AppointmentRepository
    AppointmentRepository-->>AppointmentService: appointmentEntity
    deactivate AppointmentRepository
    AppointmentService-->>AppointmentController: AppointmentResponse
    deactivate AppointmentService
    AppointmentController-->>User: response (appointment ID, status "SCHEDULED")
    deactivate AppointmentController
```

## 5. Tạo Hồ sơ Điều trị (Customer Case)
```mermaid
sequenceDiagram
    participant User as User
    participant CustomerCaseController
    participant CustomerCaseService
    participant CustomerCaseRepository
    participant CustomerRepository

    User->>CustomerCaseController: POST /api/cases (customerId, serviceId, description, notes)
    activate CustomerCaseController
    CustomerCaseController->>CustomerCaseService: createCase(request)
    activate CustomerCaseService
    CustomerCaseService->>CustomerRepository: findById(customerId)
    activate CustomerRepository
    CustomerRepository-->>CustomerCaseService: customerEntity
    deactivate CustomerRepository
    CustomerCaseService->>CustomerCaseRepository: save(case)
    activate CustomerCaseRepository
    CustomerCaseRepository-->>CustomerCaseService: caseEntity
    deactivate CustomerCaseRepository
    CustomerCaseService-->>CustomerCaseController: CaseResponse
    deactivate CustomerCaseService
    CustomerCaseController-->>User: response (case ID, status)
    deactivate CustomerCaseController
```

## 6. Upload Ảnh Trước Sau (Case Photos)
```mermaid
sequenceDiagram
    participant User as User
    participant CasePhotoController
    participant CasePhotoService
    participant CasePhotoRepository
    participant CustomerCaseRepository

    User->>CasePhotoController: POST /api/cases/{caseId}/photos (beforeImage, afterImage, notes)
    activate CasePhotoController
    CasePhotoController->>CasePhotoService: uploadPhotos(caseId, request)
    activate CasePhotoService
    CasePhotoService->>CustomerCaseRepository: findById(caseId)
    activate CustomerCaseRepository
    CustomerCaseRepository-->>CasePhotoService: caseEntity
    deactivate CustomerCaseRepository
    CasePhotoService->>CasePhotoRepository: save(photo)
    activate CasePhotoRepository
    CasePhotoRepository-->>CasePhotoService: photoEntity
    deactivate CasePhotoRepository
    CasePhotoService-->>CasePhotoController: PhotoResponse
    deactivate CasePhotoService
    CasePhotoController-->>User: response (photo IDs, URLs)
    deactivate CasePhotoController
```

## 7. Thanh toán
```mermaid
sequenceDiagram
    participant User as User
    participant PaymentController
    participant PaymentService
    participant PaymentRepository
    participant InvoiceService
    participant InvoiceRepository

    User->>PaymentController: POST /api/payments (invoiceId, paymentMethod, amount)
    activate PaymentController
    PaymentController->>PaymentService: createPayment(request)
    activate PaymentService
    PaymentService->>PaymentRepository: save(payment)
    activate PaymentRepository
    PaymentRepository-->>PaymentService: paymentEntity
    deactivate PaymentRepository
    PaymentService->>InvoiceService: updateInvoiceStatus(invoiceId, "Paid")
    activate InvoiceService
    InvoiceService->>InvoiceRepository: update(invoice)
    activate InvoiceRepository
    InvoiceRepository-->>InvoiceService: updatedInvoice
    deactivate InvoiceRepository
    InvoiceService-->>PaymentService: statusUpdated
    deactivate InvoiceService
    PaymentService-->>PaymentController: PaymentResponse
    deactivate PaymentService
    PaymentController-->>User: response (payment ID, status)
    deactivate PaymentController
```

### Hướng dẫn sử dụng:
- **Render trên GitHub:** GitHub tự động render Mermaid nếu bạn dùng fenced code blocks như ```mermaid.
- **Công cụ khác:** Copy code vào Mermaid editor (mermaid.live), VS Code với extension Mermaid, hoặc diagrams.net (import từ Mermaid).
- **Tùy chỉnh:** Nếu cần thêm details (e.g., alt fragments cho errors), cho tôi biết để update!

Nếu bạn muốn file riêng cho từng diagram hoặc chỉnh sửa, cứ nói nhé! 😊
