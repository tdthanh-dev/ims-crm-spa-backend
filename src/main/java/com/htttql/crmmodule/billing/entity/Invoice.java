package com.htttql.crmmodule.billing.entity;

import com.htttql.crmmodule.common.config.SchemaConstants;
import com.htttql.crmmodule.common.entity.BaseEntity;
import com.htttql.crmmodule.common.enums.InvoiceStatus;
import com.htttql.crmmodule.core.entity.Customer;
import com.htttql.crmmodule.core.entity.StaffUser;
import com.htttql.crmmodule.service.entity.CustomerCase;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "invoice", schema = SchemaConstants.BILLING_SCHEMA, indexes = {
        @Index(name = "idx_invoice_case", columnList = "case_id"),
        @Index(name = "idx_invoice_customer", columnList = "customer_id"),
        @Index(name = "idx_invoice_status", columnList = "status"),
        @Index(name = "idx_invoice_created", columnList = "created_at DESC"),
        @Index(name = "idx_invoice_paid_at", columnList = "paid_at")
})
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Invoice extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "invoice_seq")
    @SequenceGenerator(name = "invoice_seq", sequenceName = SchemaConstants.BILLING_SCHEMA
            + ".invoice_seq", allocationSize = 1)
    @Column(name = "invoice_id")
    private Long invoiceId;
    //Hồ sơ khách hàng
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "case_id", unique = true, foreignKey = @ForeignKey(name = "fk_invoice_case"))
    private CustomerCase customerCase;
    //Khách hàng
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "customer_id", nullable = false, foreignKey = @ForeignKey(name = "fk_invoice_customer"))
    private Customer customer;

    //Nhân viên
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", foreignKey = @ForeignKey(name = "fk_invoice_user"))
    private StaffUser staffUser; 
    //Tổng tiền
    @Column(name = "total_amount", nullable = false, precision = 12, scale = 2)
    private BigDecimal totalAmount;

    //Trạng thái
    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false, length = 20)
    @Builder.Default
    private InvoiceStatus status = InvoiceStatus.DRAFT;

    //Ngày thanh toán
    @Column(name = "paid_at", columnDefinition = "TIMESTAMPTZ")
    private LocalDateTime paidAt;

    //Số hóa đơn
    @Column(name = "invoice_number", unique = true, length = 50)
    private String invoiceNumber;

    //Ngày đến hạn
    @Column(name = "due_date")
    private LocalDateTime dueDate;

    //Ghi chú
    @Column(name = "notes", columnDefinition = "TEXT")
    private String notes;

    //Thanh toán
    @OneToMany(mappedBy = "invoice", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private List<Payment> payments = new ArrayList<>();

    //Tạo hóa đơn
    @PrePersist
    protected void onCreate() {
        super.onCreate();
        if (invoiceNumber == null) {
            // Generate invoice number format: INV-YYYYMMDD-XXXXX
            invoiceNumber = String.format("INV-%tF-%05d",
                    LocalDateTime.now(),
                    System.currentTimeMillis() % 100000);
        }
    }

    //Cập nhật hóa đơn
    @PreUpdate
    protected void onUpdate() {
        super.onUpdate();
    }

    public BigDecimal getTotalPaid() {
        return payments.stream()
                .map(Payment::getAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    public BigDecimal getBalanceDue() {
        return totalAmount.subtract(getTotalPaid());
    }
}
