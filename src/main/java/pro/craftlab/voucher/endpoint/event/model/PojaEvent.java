package pro.craftlab.voucher.endpoint.event.model;

import static pro.craftlab.voucher.endpoint.event.EventStack.EVENT_STACK_1;

import java.io.Serializable;
import java.time.Duration;
import pro.craftlab.voucher.PojaGenerated;
import pro.craftlab.voucher.endpoint.event.EventStack;

@PojaGenerated
public abstract class PojaEvent implements Serializable {
  public abstract Duration maxConsumerDuration();

  private Duration randomConsumerBackoffBetweenRetries() {
    return Duration.ofSeconds(maxConsumerBackoffBetweenRetries().toSeconds());
  }

  public abstract Duration maxConsumerBackoffBetweenRetries();

  public final Duration randomVisibilityTimeout() {
    var eventHandlerInitMaxDuration = Duration.ofSeconds(90); // note(init-visibility)
    return Duration.ofSeconds(
        eventHandlerInitMaxDuration.toSeconds()
            + maxConsumerDuration().toSeconds()
            + randomConsumerBackoffBetweenRetries().toSeconds());
  }

  public EventStack getEventStack() {
    return EVENT_STACK_1;
  }

  public String getEventSource() {
    if (getEventStack().equals(EVENT_STACK_1)) return "pro.craftlab.voucher.event1";
    return "pro.craftlab.voucher.event2";
  }
}
