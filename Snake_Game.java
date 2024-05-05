import javax.swing.*;

public class Snake_Game extends JFrame {
    Snake_Game() {
        super("Snake Game"); // This will give the title to the game.

        add(new Board()); // this will call the board class

        pack();
        // this will refresh the frame before it is displayed so that all the changes
        // will be displayed properly

        setSize(500, 500); // this will set the heigth and width of the frame

        setLocationRelativeTo(null); // this will set the location of the frame to the center of the screen
        // setLocation(100, 100);
        // this will set the location of the frame on the screen

        setResizable(false);

        // this will make the window visible ... actually it is already present on the
        // but we can't see it... this function will make it visible
    }

    public static void main(String[] args) {
        new Snake_Game().setVisible(true);
    }
}