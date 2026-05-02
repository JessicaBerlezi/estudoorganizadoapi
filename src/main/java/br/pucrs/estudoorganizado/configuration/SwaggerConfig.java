package br.pucrs.estudoorganizado.configuration;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.servers.Server;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        Server server = new Server();
        server.setUrl("https://estudoorganizadoapi.up.railway.app");


        return new OpenAPI()
                .servers(List.of(server))
                .info(new Info()
                        .title("Estudo Organizado API")
                        .version("v1")
                        .description("Documentação dos endpoints de estudo e revisão"));
    }
}
