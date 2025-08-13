package ru.advisio.core.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "template")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class Template {

    @Id
    private UUID id;

    private String url;

    private String name;

    @CreationTimestamp
    private LocalDateTime creationDt;

    @ManyToOne()
    @JoinColumn(name = "company_id")
    private Company company;

}
