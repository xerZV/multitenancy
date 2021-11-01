package com.simitchiyski.multitenant.web.tenant.app.setting.dto;

import lombok.Value;

@Value
public class AppSettingDto {
    Long id;
    String key;
    String value;
    String type;
}
