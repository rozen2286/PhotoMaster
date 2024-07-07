import org.w3c.dom.Node;

import javax.swing.*;
import java.util.Stack;

/**
 * מחלקה זו מייצגת חלון גרפי עיקרי של התוכנית.
 */
public class Window extends JFrame {

    public static final int WIDTH = 1000;
    public static final int HEIGHT = 800;

    /**
     * יוצר אובייקט חדש של Screens.Window, מגדיר את גודל החלון ואת ההתנהגות הבסיסית שלו.
     */
    public Window() {

        this.setSize(WIDTH, HEIGHT);
        this.setDefaultCloseOperation(EXIT_ON_CLOSE);
        this.setResizable(false);
        this.setLocationRelativeTo(null);

        this.setTitle("PhotoMaster");
        this.setIconImage(new ImageIcon("resources/image/imageIcon.jpeg").getImage());

        this.add(new MainPanel());

        setVisible(true);
    }
}