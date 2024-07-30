package pro.craftlab.voucher.repository.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;
import java.sql.Date;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Voucher {

    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id private String id;
    private String code;
    private Date validation;
    private Date expiration;

    @ManyToOne
    private Customer customer;
}
