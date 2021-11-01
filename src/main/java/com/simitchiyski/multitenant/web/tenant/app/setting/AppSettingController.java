package com.simitchiyski.multitenant.web.tenant.app.setting;

import com.simitchiyski.multitenant.core.tenant.app.setting.AppSettingService;
import com.simitchiyski.multitenant.web.tenant.app.setting.dto.AppSettingCreateDto;
import com.simitchiyski.multitenant.web.tenant.app.setting.dto.AppSettingDto;
import com.simitchiyski.multitenant.web.tenant.app.setting.mapper.AppSettingMapper;
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
@RequestMapping("/tenant/app/settings")
public class AppSettingController {
    private final AppSettingMapper appSettingMapper;
    private final AppSettingService appSettingService;

    @GetMapping
    public ResponseEntity<List<AppSettingDto>> tenantAppSettings() {
        return ok(appSettingMapper.toDto(appSettingService.findAll()));
    }

    @PostMapping
    public ResponseEntity<AppSettingDto> tenantAppSettings(final @Valid @NotNull @RequestBody AppSettingCreateDto createDto) {
        return ok(appSettingMapper.toDto(appSettingService.create(createDto)));
    }
}
