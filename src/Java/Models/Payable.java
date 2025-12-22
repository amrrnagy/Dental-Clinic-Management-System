package Models;

// An interface for the Payment class
public interface Payable {

    String getId();
    String getPatientId();
    Patient getPayer();
}
