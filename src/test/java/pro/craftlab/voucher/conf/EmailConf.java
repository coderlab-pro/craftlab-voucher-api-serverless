package pro.craftlab.voucher.conf;

import org.springframework.test.context.DynamicPropertyRegistry;
import pro.craftlab.voucher.PojaGenerated;

@PojaGenerated
public class EmailConf {

  void configureProperties(DynamicPropertyRegistry registry) {
    registry.add("aws.ses.source", () -> "dummy-ses-source");
  }
}
