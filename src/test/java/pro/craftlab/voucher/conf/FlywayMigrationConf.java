package pro.craftlab.voucher.conf;

import org.springframework.test.context.DynamicPropertyRegistry;

public class FlywayMigrationConf {

  void configureProperties(DynamicPropertyRegistry registry) {
    registry.add("spring.flyway.locations", () -> "classpath:/db/migration,classpath:/db/testdata");
  }
}
