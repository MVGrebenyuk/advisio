package ru.advisio.core.entity.crm;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import ru.advisio.core.dto.crm.ConnectionType;
import ru.advisio.core.enums.CrmType;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "connection_crm")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ConnectionCrm {

    @Id
    @GeneratedValue
    @Column(name = "id", updatable = false, nullable = false)
    private UUID id;

    @OneToOne
    @JoinColumn(name = "crm_id", nullable = false)
    private Crm crm;

    @Column(name = "connection_name", nullable = false, length = 255)
    private String connectionName;

    @Column(name = "crm_type", nullable = false, length = 50)
    @Enumerated(value = EnumType.STRING)
    private CrmType crmType;

    @Column(name = "connection_type", nullable = false, length = 50)
    @Enumerated(value = EnumType.STRING)
    private ConnectionType connectionType;

    @Column(name = "connection_url", nullable = false, length = 255)
    private String connectionUrl;

    @Column(name = "login", length = 255)
    private String login;

    @Column(name = "password", length = 255)
    private String password;

    @Column(name = "token", length = 255)
    private String token;

    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
}
