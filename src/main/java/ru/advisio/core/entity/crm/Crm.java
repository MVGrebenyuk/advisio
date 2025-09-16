package ru.advisio.core.entity.crm;

import jakarta.persistence.*;
import lombok.*;
import ru.advisio.core.enums.CrmType;

import java.util.UUID;
@Entity
@Table(name = "crm")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Crm {

    @Id
    @GeneratedValue
    @Column(name = "id", updatable = false, nullable = false)
    private UUID id;

    @Column(name = "company_id", nullable = false)
    private UUID companyId;

    @Enumerated(value = EnumType.STRING)
    private CrmType crmType;

    @Column(name = "name", nullable = false, length = 30)
    private String name;

    @OneToOne(mappedBy = "crm")
    @JoinColumn(name = "crm_id")
    private ConnectionCrm connectionCrm;

    @Column(name = "description")
    private String description;
}
