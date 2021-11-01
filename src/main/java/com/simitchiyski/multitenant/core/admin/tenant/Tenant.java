package com.simitchiyski.multitenant.core.admin.tenant;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.data.domain.Persistable;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import static java.util.Objects.isNull;

@Getter
@Setter
@Entity
@ToString
@Table(name = "TENANT")
public class Tenant implements Persistable<Long> {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID", nullable = false)
    private Long id;

    @NotNull
    @Size(min = 3, max = 50)
    @Column(name = "NAME", length = 50, nullable = false)
    private String name;

    @JsonManagedReference
    @EqualsAndHashCode.Exclude
    @JoinColumn(name = "tenant_data_source_config_ID")
    @OneToOne(mappedBy = "tenant", cascade = CascadeType.ALL)
    private TenantDataSourceConfig tenantDataSourceConfig;

    @Override
    public boolean isNew() {
        return isNull(id);
    }
}
