package dlsu.advcarc.view;

import javax.swing.*;
import java.awt.*;

/**
 * Created by Darren on 11/10/2015.
 */
public class MiniMipsFrame extends JFrame {

    public MiniMipsFrame(){
        super("MiniMips - Lam, Sapalo");
        setPreferredSize(new Dimension(800, 480));
        pack();
        setLocationRelativeTo(null);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setVisible(true);
    }
}
