package com.cg.domain.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;

import javax.persistence.*;
import java.math.BigDecimal;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Table(name="bills")
@Accessors(chain = true)
public class Bill extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "total_amount", precision = 12 , scale = 0 , nullable = false)
    private BigDecimal totalAmount;

    @OneToOne
    @JoinColumn(name = "order_id" , referencedColumnName = "id" , nullable = false)
    private Order order;

    @ManyToOne
    @JoinColumn(name ="staff_id" , referencedColumnName = "id" , nullable = false)
    private Staff staff;



}
