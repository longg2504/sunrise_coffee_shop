package com.cg.domain.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Entity
@Getter
@Setter
@Table(name="products")
@Accessors(chain = true)
public class Product extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;

    @Column(precision = 10, scale = 0, nullable = false)
    private BigDecimal price;

    @ManyToOne
    @JoinColumn(name="unit_id" , referencedColumnName = "id" , nullable = false)
    private Unit unit;

    @ManyToOne
    @JoinColumn(name="category_id" , referencedColumnName = "id" , nullable = false)
    private Category category;

    @OneToOne
    @JoinColumn(name = "product_avatar_id",referencedColumnName = "id" ,  nullable = false)
    private Avatar productAvatar;

    @OneToMany(mappedBy = "product")
    private List<OrderDetail> orderDetails;


}
