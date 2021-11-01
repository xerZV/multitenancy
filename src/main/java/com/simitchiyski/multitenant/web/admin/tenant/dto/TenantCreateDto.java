package com.simitchiyski.multitenant.web.admin.tenant.dto;

import lombok.Value;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Value
public class TenantCreateDto {
    @NotBlank
    @Size(min = 3, max = 50)
    String name;
}
