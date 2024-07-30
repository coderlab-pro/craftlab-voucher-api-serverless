package pro.craftlab.voucher.repository.model;

import jakarta.persistence.*;
import java.time.Instant;
import java.util.UUID;
import lombok.*;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
@Data
public class Voucher {

  @Id private String id;
  private String code;
  private Instant validationDatetime;
  private Instant creationDatetime;

  @PrePersist
  public void prePersist() {
    if (this.id == null) {
      this.id = UUID.randomUUID().toString();
    }
  }

  @ToString.Exclude @ManyToOne private Customer customer;
}
