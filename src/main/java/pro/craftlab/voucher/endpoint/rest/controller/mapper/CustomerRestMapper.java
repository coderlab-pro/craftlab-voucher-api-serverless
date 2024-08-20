package pro.craftlab.voucher.endpoint.rest.controller.mapper;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;
import pro.craftlab.voucher.endpoint.rest.controller.validator.CustomerRestValidator;
import pro.craftlab.voucher.endpoint.rest.model.Customer;

@AllArgsConstructor
@Component
public class CustomerRestMapper {

  private final CustomerRestValidator customerRestValidator;

  public Customer toRest(pro.craftlab.voucher.repository.model.Customer domain) {
    return new Customer().id(domain.getId()).name(domain.getName()).mail(domain.getMail());
  }

  public pro.craftlab.voucher.repository.model.Customer toDomain(Customer rest) {
    // TODO : add CustomerRestValidator
    customerRestValidator.accept(rest);
    return pro.craftlab.voucher.repository.model.Customer.builder()
        .id(rest.getId())
        .name(rest.getName())
        .mail(rest.getMail())
        .build();
  }
}
