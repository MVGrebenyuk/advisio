package ru.advisio.core.entity.products;

import jakarta.persistence.*;
import lombok.*;
import ru.advisio.core.entity.crm.Crm;

import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "products")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Product {

    @Id
    @GeneratedValue
    @Column(name = "id", updatable = false, nullable = false)
    private UUID id;

    @Column(name = "company_id", nullable = false)
    private UUID companyId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "crm_source", nullable = false)
    private Crm crmSource;

    @Column(name = "tech_id", nullable = false, length = 100)
    private String techId;

    @Column(name = "tech_name", nullable = false, length = 100)
    private String techName;

    @Column(name = "crm", nullable = false, length = 20)
    private String crm;

    @Column(name = "sale_name", length = 150)
    private String saleName;

    @Column(name = "value", length = 30)
    private String value;

    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ProductAttribute> attributes;

    @Transient
    private Boolean deleteMark = true;
}
