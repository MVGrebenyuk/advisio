package ru.advisio.core.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "image")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Image {

    @Id
    @NotNull
    private UUID id;

    @JsonIgnore
    @ManyToMany(mappedBy = "images", fetch = FetchType.LAZY)
    private List<Account> accounts = new ArrayList<>();

    @JsonIgnore
    @ManyToMany(mappedBy = "images", fetch = FetchType.LAZY)
    private List<SalePoint> salePoints = new ArrayList<>();

    @JsonIgnore
    @ManyToMany(mappedBy = "images", fetch = FetchType.LAZY)
    private List<Group> devGroups = new ArrayList<>();

    @JsonIgnore
    @ManyToMany(mappedBy = "images", fetch = FetchType.LAZY)
    private List<Device> devices = new ArrayList<>();

    @NotNull
    @Column(columnDefinition = "text", nullable = false)
    private String image;
}