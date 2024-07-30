package pro.craftlab.voucher.repository.model;

import static jakarta.persistence.FetchType.EAGER;

import jakarta.persistence.*;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import java.util.HashSet;
import java.util.Set;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Customer {
  @Id private String id;
  private String name;
  private String mail;

  @OneToMany(mappedBy = "customer", cascade = CascadeType.ALL, orphanRemoval = true, fetch = EAGER)
  private Set<Voucher> vouchers = new HashSet<>();
}
