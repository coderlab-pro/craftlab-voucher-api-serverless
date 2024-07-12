package pro.craftlab.voucher.conf;

import org.springframework.test.context.DynamicPropertyRegistry;
import pro.craftlab.voucher.PojaGenerated;

@PojaGenerated
public class BucketConf {

  void configureProperties(DynamicPropertyRegistry registry) {
    registry.add("aws.s3.bucket", () -> "dummy-bucket");
  }
}
