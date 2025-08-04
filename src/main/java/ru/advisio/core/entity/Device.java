package ru.advisio.core.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import ru.advisio.core.entity.base.BaseImagedEntity;
import ru.advisio.core.enums.DeviceType;
import ru.advisio.core.enums.Status;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "device")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
public class Device extends BaseImagedEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "account_id")
    private Account account;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "sp_id")
    private SalePoint salePoint;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "group_id")
    private Group group;

    @NotBlank
    @Size(max = 30)
    @Column(nullable = false)
    @Enumerated(value = EnumType.STRING)
    private DeviceType type;

    @NotNull
    @Column(nullable = false)
    private UUID serial;

    @NotBlank
    @Size(max = 10)
    @Column(nullable = false)
    @Enumerated(value = EnumType.STRING)
    private Status status;

    @ManyToMany
    @JoinTable(
            name = "device_images",
            joinColumns = @JoinColumn(name = "device_id"),
            inverseJoinColumns = @JoinColumn(name = "image_id")
    )
    private List<Image> images = new ArrayList<>();
}
