package Models;

import java.time.LocalTime;

public enum AppointmentSlot {
    // Defines the standard start time for a 60-minute appointment
    SLOT_9_00_AM(LocalTime.of(9, 0)),
    SLOT_10_00_AM(LocalTime.of(10, 0)),
    SLOT_11_00_AM(LocalTime.of(11, 0)),
    SLOT_1_00_PM(LocalTime.of(13, 0)),
    SLOT_2_00_PM(LocalTime.of(14, 0)),
    SLOT_3_00_PM(LocalTime.of(15, 0)),
    SLOT_4_00_PM(LocalTime.of(16, 0)),
    SLOT_5_00_PM(LocalTime.of(17,0));

    private final LocalTime startTime;

    AppointmentSlot(LocalTime startTime) {
        this.startTime = startTime;
    }

    public LocalTime getStartTime() {
        return startTime;
    }
}
