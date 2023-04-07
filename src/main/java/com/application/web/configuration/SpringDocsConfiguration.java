package com.application.web.configuration;

import org.springdoc.core.SpringDocConfigProperties;
import org.springdoc.core.SpringDocConfiguration;
import org.springdoc.core.providers.ObjectMapperProvider;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Provides the capabilities to display the apiDoc in a preconfigured endpoint based on the following information of open-api
 * https://springdoc.org/faq.html#_what_is_a_proper_way_to_set_up_swagger_ui_to_use_provided_spec_yml
 * This apiDoc can be consulted through the endpoint: http://localhost:8080/swagger-ui/index.html
 */
@Configuration
public class SpringDocsConfiguration {

    @Bean
    SpringDocConfiguration springDocConfiguration() {
        return new SpringDocConfiguration();
    }

    @Bean
    public SpringDocConfigProperties springDocConfigProperties() {
        return new SpringDocConfigProperties();
    }

    @Bean
    ObjectMapperProvider objectMapperProvider(SpringDocConfigProperties springDocConfigProperties){
        return new ObjectMapperProvider(springDocConfigProperties);
    }
}

