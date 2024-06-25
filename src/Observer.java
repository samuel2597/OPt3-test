import java.util.List;

public interface Observer {
    void update(String message);
    List<String> getNotifications();
    void clearNotifications();
}
