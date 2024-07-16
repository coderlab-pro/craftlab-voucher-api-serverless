package pro.craftlab.voucher.endpoint;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import pro.craftlab.voucher.conf.FacadeIT;
import pro.craftlab.voucher.service.CustomerService;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class CustomerServiceIT extends FacadeIT {
    @Autowired CustomerService subject;

    @Test
    void read_customers() {
        var actual = subject.getCustomers(Pageable.ofSize(500));

        assertEquals(0, actual.size());
    }
}
