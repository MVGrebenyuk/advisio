package ru.advisio.core.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import ru.advisio.core.entity.base.BaseImagedEntity;
import ru.advisio.core.enums.CompanyType;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Table(name = "company")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
public class Company extends BaseImagedEntity {

    @NotBlank
    @Size(max = 100)
    @Column(nullable = false)
    private String cname;

    @Enumerated(value = EnumType.STRING)
    private CompanyType companyType;

    @OneToOne(mappedBy = "company", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private Details details;

    @JsonIgnore
    @OneToMany(mappedBy = "company")
    private List<Device> devices;

    @JsonIgnore
    @OneToMany(mappedBy = "company")
    private List<Tag> tags;

    @JsonIgnore
    @OneToMany(mappedBy = "company")
    private List<Template> tamplates;

    @JsonIgnore
    @ManyToMany
    @JoinTable(
            name = "company_images",
            joinColumns = @JoinColumn(name = "company_id"),
            inverseJoinColumns = @JoinColumn(name = "image_id")
    )
    private List<Image> images = new ArrayList<>();
}
