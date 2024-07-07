import javax.swing.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

public class Control implements MouseListener {

    private final MainPanel panel;

    public Control(MainPanel panel) {
        this.panel = panel;
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        if (e.getX() > MainPanel.IMAGE_X && e.getX() < MainPanel.IMAGE_WIDTH + MainPanel.IMAGE_X && e.getY() > MainPanel.IMAGE_Y && e.getY() < MainPanel.IMAGE_HEIGHT + MainPanel.IMAGE_Y) {
            if (SwingUtilities.isLeftMouseButton(e)) {
                panel.addPoint(e.getX(), e.getY());
            } else if (SwingUtilities.isRightMouseButton(e)) {
                panel.removePoint(e.getX(), e.getY());
            }
        }
    }

    @Override
    public void mousePressed(MouseEvent e) {

    }

    @Override
    public void mouseReleased(MouseEvent e) {

    }

    @Override
    public void mouseEntered(MouseEvent e) {

    }

    @Override
    public void mouseExited(MouseEvent e) {

    }
}
