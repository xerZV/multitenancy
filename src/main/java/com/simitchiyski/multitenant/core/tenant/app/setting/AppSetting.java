package com.simitchiyski.multitenant.core.tenant.app.setting;

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
@Table(name = "APP_SETTING")
public class AppSetting implements Persistable<Long> {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID", nullable = false)
    private Long id;

    @NotNull
    @Size(min = 3, max = 50)
    @Column(name = "key", length = 50, nullable = false)
    private String key;

    @NotNull
    @Size(min = 3, max = 50)
    @Column(name = "value", length = 50, nullable = false)
    private String value;

    @NotNull
    @Size(min = 3, max = 100)
    @Column(name = "type", length = 100, nullable = false)
    private String type;

    @Override
    public boolean isNew() {
        return isNull(id);
    }
}
