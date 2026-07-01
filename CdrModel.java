import java.io.Serializable;

// Custom Java object representing Call Detail Record (CDR) data
public class CdrModel implements Serializable {
    private String id;
    private String phoneNumber;
    private int duration;

    // Default constructor required for JSON serialization
    public CdrModel() {
    }

    // Constructor to initialize fields easily
    public CdrModel(String id, String phoneNumber, int duration) {
        this.id = id;
        this.phoneNumber = phoneNumber;
        this.duration = duration;
    }

    // Getters and Setters to read and write data
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getPhoneNumber() { return phoneNumber; }
    public void setPhoneNumber(String phoneNumber) { this.phoneNumber = phoneNumber; }

    public int getDuration() { return duration; }
    public void setDuration(int duration) { this.duration = duration; }

    // Method to print object data cleanly on the console
    @Override
    public String toString() {
        return "CdrModel{id='" + id + "', phoneNumber='" + phoneNumber + "', duration=" + duration + "}";
    }
}