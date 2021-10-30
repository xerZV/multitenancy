package com.simitchiyski.multitenant.web.admin.tenant.dto;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Getter
@Setter
public class TenantCreateDto {

    @NotBlank
    @Size(min = 3, max = 50)
    private String name;
}
