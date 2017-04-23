package org.maratik.arena.calc.docs;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger.web.UiConfiguration;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

/**
 * Created by maratik.
 */
@Configuration
@EnableSwagger2
@EnableWebMvc
public class SwaggerConfiguration {
    private static final long REQUEST_TIMEOUT = 60_000;

    @Bean
    public Docket customDocket() {
        return new Docket(DocumentationType.SWAGGER_2)
                .apiInfo(apiInfo())
                .select()
                .apis(RequestHandlerSelectors.any())
                .paths(PathSelectors.any())
                .build()
                .useDefaultResponseMessages(false);
    }

    @Bean
    public UiConfiguration uiConfig() {
        return new UiConfiguration(null, "none", "alpha",
                "schema", UiConfiguration.Constants.DEFAULT_SUBMIT_METHODS,
                true, true, REQUEST_TIMEOUT);
    }

    private static ApiInfo apiInfo() {
        return new ApiInfoBuilder()
                .title("arena-calc")
                .build();
    }
}
