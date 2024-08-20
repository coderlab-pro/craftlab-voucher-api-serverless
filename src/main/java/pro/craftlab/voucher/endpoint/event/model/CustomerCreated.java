package pro.craftlab.voucher.endpoint.event.model;

import java.time.Duration;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;
import pro.craftlab.voucher.repository.model.Customer;

@NoArgsConstructor
@AllArgsConstructor
@Builder
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
