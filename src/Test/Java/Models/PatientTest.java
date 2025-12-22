
package Models;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.lang.reflect.Field;

import static org.junit.jupiter.api.Assertions.*;

    class PatientTest {

        private Patient patient;

        @BeforeEach
        void setUp() {

            resetNextId();

            patient = new Patient(
                    "John",
                    "Doe",
                    Gender.MALE,
                    "0123456789",
                    "jdoe",
                    "password123"
            );
        }

        @AfterEach
        void tearDown() {
            resetNextId();
        }

        private void resetNextId() {
            try {
                Field field = Patient.class.getDeclaredField("nextId");
                field.setAccessible(true);
                field.setInt(null, 1); // Reset to 1
            } catch (Exception e) {
                throw new RuntimeException("Failed to reset Patient nextId", e);
            }
        }


        @Test
        void testConstructorAndIDGeneration() {
            // 1. Check ID generation for the first patient (created in setUp)
            assertEquals("PAT1", patient.getId(), "First patient ID should be PAT1");
            assertEquals("John", patient.getFirstName());
            assertEquals(0.0, patient.getBalance(), "Initial balance should be 0.0");
            assertEquals("", patient.getMedicalHistory(), "Initial medical history should be empty");
            assertEquals("0123456789", patient.getPhone());
            Patient patient2 = new Patient("Jane", "Doe", Gender.FEMALE, "9876543210", "jane", "pass");
            assertEquals("PAT2", patient2.getId(), "Second patient ID should be PAT2");
        }

        @Test
        void testMinusNextID() {
            Patient.minusNextID();
            Patient newPatient = new Patient("Test", "User", Gender.MALE, "000", "test", "pass");

            assertEquals("PAT1", newPatient.getId(), "ID should have rolled back to PAT1");
        }

        @Test
        void testSetBalance() {
            patient.setBalance(500.0);
            assertEquals(500.0, patient.getBalance(), 0.001);
        }

        @Test
        void testApplyPayment_Valid() {
            patient.setBalance(100.0);
            patient.applyPayment(40.0);
            assertEquals(60.0, patient.getBalance(), 0.001, "100 - 40 should be 60");
            patient.applyPayment(60.0);
            assertEquals(0.0, patient.getBalance(), 0.001, "60 - 60 should be 0");
        }

        @Test
        void testApplyPayment_ExceedingBalance() {
            patient.setBalance(50.0);

            patient.applyPayment(100.0);

            assertEquals(0.0, patient.getBalance(), "Balance should not go negative");
        }

        @Test
        void testApplyPayment_NegativeAmount() {
            Exception exception = assertThrows(IllegalArgumentException.class, () -> {
                patient.applyPayment(-50.0);
            });

            assertEquals("Amount must be >= 0", exception.getMessage());
        }

        @Test
        void testCompareTo() {
            Patient p1 = new Patient("Aaron", "Smith", Gender.MALE, "1", "u1", "p1"); // PAT2 (reset in setUp makes p1 PAT1, but p1 is 'patient')
            Patient pA = new Patient("Aaron", "Anderson", Gender.MALE, "1", "u1", "p1");
            Patient pB = new Patient("Bob", "Anderson", Gender.MALE, "1", "u2", "p2");

            Patient pC = new Patient("Aaron", "Zebra", Gender.MALE, "1", "u3", "p3");

            assertTrue(pA.compareTo(pC) < 0, "Anderson should come before Zebra");

            assertTrue(pA.compareTo(pB) < 0, "Aaron should come before Bob when last name is Anderson");
        }

        @Test
        void testToString() {
            String expected = "PAT1: John Doe";
            assertEquals(expected, patient.toString());
        }
    }



