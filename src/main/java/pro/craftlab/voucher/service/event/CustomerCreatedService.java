package pro.craftlab.voucher.service.event;

import java.util.function.Consumer;
import org.springframework.stereotype.Service;
import pro.craftlab.voucher.endpoint.event.model.CustomerCreated;

@Service
public class CustomerCreatedService implements Consumer<CustomerCreated> {

  @Override
  public void accept(CustomerCreated customerCreated) {}
}
