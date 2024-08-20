package pro.craftlab.voucher.endpoint.event.model.customer;

import java.time.Duration;
import lombok.*;
import pro.craftlab.voucher.endpoint.event.model.PojaEvent;
import pro.craftlab.voucher.repository.model.Customer;

@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
@Data
@EqualsAndHashCode(callSuper = false)
@ToString
public class CustomerCreated extends PojaEvent {
  private Customer customer;

  @Override
  public Duration maxConsumerDuration() {
    return Duration.ofMinutes(1L);
  }

  @Override
  public Duration maxConsumerBackoffBetweenRetries() {
    return Duration.ofMinutes(5L);
  }
}
