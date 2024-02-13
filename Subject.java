import java.awt.event.KeyEvent;

public interface Subject {
    public void attach(Observer o);
    public void detach(Observer o);
    public void notifyObservers(KeyEvent e);
}
