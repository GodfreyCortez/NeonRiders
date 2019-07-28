//Godfrey C
package newneon;

import java.awt.BorderLayout;
import javax.swing.*;


public class Neon_Window extends JFrame //this will add all the components and give settings
{ 
    private ImageIcon icon = new ImageIcon("assets/Neon Riders Logo.jpg"); //Grab the icon image
    public Neon_Window() {
        Game_Loop game = new Game_Loop();
        this.add (game, BorderLayout.CENTER);
        this.setSize(1440,900); 
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); 
        this.setTitle("Neon Riders"); 
        this.setIconImage(icon.getImage()); 
        //this.setExtendedState(JFrame.MAXIMIZED_BOTH); //this will make the window go full screen
        //this.setUndecorated(true); //removes the border of the window
        this.setResizable(false); 
        this.setLocationRelativeTo(null); 
        this.setVisible(true); 
        game.runGameLoop();
    }
    public static void main(String[] args) {
        Neon_Window window = new Neon_Window(); //create instance of our window
    }
} 
