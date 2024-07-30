package pro.craftlab.voucher.endpoint.rest.controller.mapper;

import org.springframework.stereotype.Component;
import pro.craftlab.voucher.endpoint.rest.model.Voucher;

@Component
public class VoucherRestMapper {

  public Voucher toRest(pro.craftlab.voucher.repository.model.Voucher domain) {
    return new Voucher()
        .id(domain.getId())
        .code(domain.getCode())
        .validationDatetime(domain.getValidationDatetime())
        .creationDatetime(domain.getCreationDatetime());
  }
}
