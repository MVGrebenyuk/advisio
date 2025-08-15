package ru.advisio.core.entity.products;

import jakarta.persistence.*;
import lombok.*;

import java.util.UUID;

@Entity
@Table(name = "product_attributes")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProductAttribute {

    @Id
    @GeneratedValue
    @Column(name = "id", updatable = false, nullable = false)
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id", nullable = false)
    private Product product;

    @Column(name = "tech_id", nullable = false, length = 100)
    private String techId;

    @Column(name = "tech_name", nullable = false, length = 100)
    private String techName;

    @Column(name = "sale_name", length = 150)
    private String saleName;

    @Column(name = "value", length = 10)
    private String value;
}
