import javax.swing.*;

public class MainWindow extends JFrame {

    public MainWindow(){
        setTitle("Snake");
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setResizable(false);
        setSize(400, 400);
        setLocation(400, 400);

        add(new GameField());

        setVisible(true);
    }


    public static void main(String[] args) {
        MainWindow mWindow = new MainWindow();
    }
}
