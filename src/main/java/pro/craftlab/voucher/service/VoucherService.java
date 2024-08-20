package pro.craftlab.voucher.service;

import java.time.Instant;
import java.util.List;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import pro.craftlab.voucher.endpoint.rest.model.CreateVoucher;
import pro.craftlab.voucher.repository.CustomerRepository;
import pro.craftlab.voucher.repository.VoucherRepository;
import pro.craftlab.voucher.repository.function.VoucherCodeGenerator;
import pro.craftlab.voucher.repository.model.Customer;
import pro.craftlab.voucher.repository.model.Voucher;
import pro.craftlab.voucher.repository.model.exception.NotFoundException;

@Service
@AllArgsConstructor
public class VoucherService {
  private final VoucherRepository voucherRepository;
  private final CustomerRepository customerRepository;
  private final VoucherCodeGenerator voucherCodeGenerator;

  public Voucher generateVoucherCodeForCustomer(
      String idCustomer, List<CreateVoucher> createVouchers) {
    Customer customer =
        customerRepository
            .findById(idCustomer)
            .orElseThrow(() -> new NotFoundException("Customer not found :" + idCustomer));

    Instant now = Instant.now();
    CreateVoucher createVoucher =
        createVouchers.stream()
            .findFirst()
            .orElseThrow(() -> new NotFoundException("No vouchers find"));

    String code = voucherCodeGenerator.get();
    return voucherRepository.save(
        Voucher.builder()
            .code(code)
            .validationDatetime(createVoucher.getValidationDatetime())
            .creationDatetime(now)
            .customer(customer)
            .build());
  }
}
