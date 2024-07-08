package org.gnrd.uid;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties("uid.service")
public class ConfigProperties {
}
