package pro.craftlab.voucher.repository.model;

import jakarta.persistence.*;
import java.time.Instant;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Voucher {

  @Id private String id;
  private String code;
  private Instant validation;
  private Instant expiration;
  private Instant creationDatetime;

  @PrePersist
  public void prePersist() {
    if (this.id == null) {
      this.id = UUID.randomUUID().toString();
    }
  }

  @ManyToOne private Customer customer;
}
