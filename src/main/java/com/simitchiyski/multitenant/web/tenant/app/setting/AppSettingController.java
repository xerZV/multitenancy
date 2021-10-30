package com.simitchiyski.multitenant.web.tenant.app.setting;

import com.simitchiyski.multitenant.core.tenant.app.setting.AppSetting;
import com.simitchiyski.multitenant.core.tenant.app.setting.AppSettingRepository;
import com.simitchiyski.multitenant.web.tenant.app.setting.dto.AppSettingCreateDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
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
    private final AppSettingRepository appSettingRepository;

    @GetMapping
    public ResponseEntity<List<AppSetting>> tenantAppSettings() {
        return ok(appSettingRepository.findAll());
    }

    @PostMapping
    @Transactional
    public ResponseEntity<AppSetting> tenantAppSettings(@Valid @NotNull @RequestBody AppSettingCreateDto createDto) {
        final AppSetting entity = new AppSetting();
        entity.setKey(createDto.getKey());
        entity.setValue(createDto.getValue());
        entity.setType(createDto.getType());

        return ok(appSettingRepository.save(entity));
    }
}
