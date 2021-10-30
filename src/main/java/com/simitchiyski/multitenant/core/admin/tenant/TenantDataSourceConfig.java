package com.simitchiyski.multitenant.core.admin.tenant;

import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Getter
@Setter
@Entity
@ToString
@Table(name = "tenant_datasource")
public class TenantDataSourceConfig {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID", nullable = false)
    private Long id;

    @ToString.Exclude
    @JsonBackReference
    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "TENANT_ID", referencedColumnName = "id", foreignKey = @ForeignKey(name = "TENANT_DS_FK"))
    private Tenant tenant;

    @NotNull
    @Column(name = "DRIVER_CLASS_NAME", nullable = false)
    private String driverClassName;

    @NotNull
    @Column(name = "URL", nullable = false)
    private String url;

    @NotNull
    @Column(name = "USERNAME", nullable = false)
    private String username;

    @NotNull
    @Column(name = "PASSWORD", nullable = false)
    private String password;
}
