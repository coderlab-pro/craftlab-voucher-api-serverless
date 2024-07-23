package pro.craftlab.voucher.endpoint.rest.controller.mapper;

import org.springframework.stereotype.Component;
import pro.craftlab.voucher.endpoint.rest.model.Customer;

@Component
public class CustomerRestMapper {
  public Customer toRest(pro.craftlab.voucher.repository.model.Customer domain) {
    return new Customer().id(domain.getId()).name(domain.getName()).mail(domain.getMail());
  }

  public pro.craftlab.voucher.repository.model.Customer toDomain(Customer rest) {
    return pro.craftlab.voucher.repository.model.Customer.builder()
        .id(rest.getId())
        .name(rest.getName())
        .mail(rest.getMail())
        .build();
  }
}
