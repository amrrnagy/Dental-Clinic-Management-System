package Models;

import java.time.LocalDateTime;

public interface Payable {

    String getId();
    double getAmount();
    PaymentMethod getMethod();
    LocalDateTime getDateTime();

}
