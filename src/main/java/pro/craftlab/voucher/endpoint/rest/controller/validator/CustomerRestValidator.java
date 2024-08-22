package pro.craftlab.voucher.endpoint.rest.controller.validator;

import java.util.function.Consumer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import pro.craftlab.voucher.endpoint.rest.model.Customer;
import pro.craftlab.voucher.repository.model.exception.BadRequestException;

@Component
public class CustomerRestValidator implements Consumer<Customer> {

  private final EmailRestValidator emailRestValidator;

  @Autowired
  public CustomerRestValidator(EmailRestValidator validationMail) {
    this.emailRestValidator = validationMail;
  }

  @Override
  public void accept(Customer customer) {
    StringBuilder sb = new StringBuilder();
    if (customer == null) {
      throw new BadRequestException("Customer is mandatory");
    } else {
      emailRestValidator.accept(customer);
      if (customer.getName() == null) {
        sb.append("Name is mandatory. ");
      }
    }
    String message = sb.toString();
    if (!message.isEmpty()) {
      throw new BadRequestException(message);
    }
  }
}
