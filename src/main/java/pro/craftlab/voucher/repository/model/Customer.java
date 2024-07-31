package pro.craftlab.voucher.repository.model;

import static jakarta.persistence.FetchType.EAGER;

import jakarta.persistence.*;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import java.util.HashSet;
import java.util.Set;
import lombok.*;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
@ToString
public class Customer {
  @Id private String id;
  private String name;
  private String mail;

  @OneToMany(mappedBy = "customer", cascade = CascadeType.ALL, orphanRemoval = true, fetch = EAGER)
  @ToString.Exclude
  @EqualsAndHashCode.Exclude
  private Set<Voucher> vouchers = new HashSet<>();
}
