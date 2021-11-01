package com.simitchiyski.multitenant.web.admin.tenant;

import com.simitchiyski.multitenant.core.admin.tenant.TenantService;
import com.simitchiyski.multitenant.web.admin.tenant.dto.TenantCreateDto;
import com.simitchiyski.multitenant.web.admin.tenant.dto.TenantDto;
import com.simitchiyski.multitenant.web.admin.tenant.mapper.TenantMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.List;

import static org.springframework.http.ResponseEntity.ok;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/admin/tenants")
public class TenantController {
    private final TenantMapper tenantMapper;
    private final TenantService tenantService;

    @GetMapping
    public ResponseEntity<List<TenantDto>> tenants() {
        return ok(tenantMapper.toDto(tenantService.findAll()));
    }

    @PostMapping
    public ResponseEntity<TenantDto> createTenant(final @NotNull @Valid @RequestBody TenantCreateDto dto) {
        return ok(tenantMapper.toDto(tenantService.create(dto)));
    }
}
