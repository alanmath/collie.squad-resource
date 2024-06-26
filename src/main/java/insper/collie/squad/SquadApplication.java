package insper.collie.squad;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;

@EnableFeignClients(basePackages = {
    "insper.collie.company",
    "insper.collie.account"
})
@EnableDiscoveryClient
@EnableCaching // habilita o uso de cache
@SpringBootApplication
public class SquadApplication {

    public static void main(String[] args) {
        SpringApplication.run(SquadApplication.class, args);
    }
}
