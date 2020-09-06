import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.Random;

public class GameField extends JPanel implements ActionListener {

    public GameField(){
        setBackground(Color.BLACK);
        loadImages();
        initGame();
        addKeyListener(new FieldKeyListener());
        setFocusable(true);
    }

    public void loadImages(){
        // snake's heads
        ImageIcon ii_hU = new ImageIcon("head_snake_up.png");
        ImageIcon ii_hD = new ImageIcon("head_snake_down.png");
        ImageIcon ii_hL = new ImageIcon("head_snake_left.png");
        ImageIcon ii_hR = new ImageIcon("head_snake_right.png");

        headDURLSnake[0] = ii_hR.getImage();
        headDURLSnake[1] = ii_hL.getImage();
        headDURLSnake[2] = ii_hU.getImage();
        headDURLSnake[3] = ii_hD.getImage();

        // snake's tails
        ImageIcon ii_sU = new ImageIcon("tail_snake_up.png");
        ImageIcon ii_sD = new ImageIcon("tail_snake_down.png");
        ImageIcon ii_sL = new ImageIcon("tail_snake_left.png");
        ImageIcon ii_sR = new ImageIcon("tail_snake_right.png");

        tailDURLSnake[0] = ii_sL.getImage();
        tailDURLSnake[1] = ii_sR.getImage();
        tailDURLSnake[2] = ii_sD.getImage();
        tailDURLSnake[3] = ii_sU.getImage();

        //load fruit
        ImageIcon ii_fr1 = new ImageIcon("fruit1.png");
        ImageIcon ii_fr2 = new ImageIcon("fruit2.png");
        ImageIcon ii_fr3 = new ImageIcon("fruit3.png");
        ImageIcon ii_fr4 = new ImageIcon("fruit4.png");

        fruit = new ArrayList<>();
        fruit.add(ii_fr1.getImage());
        fruit.add(ii_fr2.getImage());
        fruit.add(ii_fr3.getImage());
        fruit.add(ii_fr4.getImage());

        ImageIcon ii_dt = new ImageIcon("body_snake.png");
        dot = ii_dt.getImage();

        // block
        ImageIcon ii_bl = new ImageIcon("block.png");
        block = ii_bl.getImage();
    }

    public void initGame(){
        isStop = false;
        snakeDots = 3;
        score = 0;
        numberSideHead = 0;

        directions[0] = false;
        directions[1] = false;
        directions[2] = true;
        directions[3] = false;

        timer.setDelay(DELAY);

        for(int i = 0; i < snakeDots; i++){
            x[i] = 48 - i * DOT_SIZE;
            y[i] = 48;
        }

        createBlocks();


        timer.start();
        createApple();
    }

    private void createBlocks(){
        xB[0] = 5 * DOT_SIZE;
        yB[0] = 5 * DOT_SIZE;

        xB[1] = 6 * DOT_SIZE;
        yB[1] = 5 * DOT_SIZE;

        xB[2] = 7 * DOT_SIZE;
        yB[2] = 5 * DOT_SIZE;

        xB[3] = 8 * DOT_SIZE;
        yB[3] = 5 * DOT_SIZE;

        xB[4] = 15 * DOT_SIZE;
        yB[4] = 15 * DOT_SIZE;

        xB[5] = 16 * DOT_SIZE;
        yB[5] = 15 * DOT_SIZE;

        xB[6] = 17 * DOT_SIZE;
        yB[6] = 15 * DOT_SIZE;

        xB[7] = 18 * DOT_SIZE;
        yB[7] = 15 * DOT_SIZE;
    }

    public void createApple(){
        while(true) {
            appleX = new Random().nextInt(20) * DOT_SIZE;
            appleY = new Random().nextInt(17) * DOT_SIZE;

            if (isNotUnderSnakeAndBlocks(appleX, appleY)) {
                break;
            }

        }
        idFruit = new Random().nextInt(fruit.size());
    }

    private boolean isNotUnderSnakeAndBlocks(int appleX, int appleY){
        for (int i = 0; i < snakeDots; i++) {
            if (x[i] == appleX && y[i] == appleY) {
                return false;
            }
        }

        for (int i = 0; i < xB.length; i++) {
            if (xB[i] == appleX && yB[i] == appleY) {
                return false;
            }
        }
        return true;
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        scoreEatFruit(g);
        keyEscForRestart(g);

        if (score != ALL_DOTS - xB.length) {
            if (!isGameOver) {
                g.drawImage(fruit.get(idFruit), appleX, appleY, this);

                for (int i = 0; i < snakeDots; i++) {
                    if (i == 0) {
                        g.drawImage(headDURLSnake[numberSideHead], x[i], y[i], this);

                    } else if (i == snakeDots - 1) {
                        g.drawImage(tailDURLSnake[numberSideTail], x[i], y[i], this);

                    } else {
                        g.drawImage(dot, x[i], y[i], this);
                    }
                }

                for (int i = 0; i < xB.length; i++) {
                    g.drawImage(block, xB[i], yB[i], this);
                }

            } else {
                String gameOver = "Game over";
                g.setColor(Color.RED);
                g.drawString(gameOver, WIDTH / 2 - 20, HEIGHT / 2);
            }
        } else {
            String gameOver = "You win!";
            g.setColor(Color.GREEN);
            g.drawString(gameOver, WIDTH / 2 - 20, HEIGHT / 2);
            timer.stop();
        }
    }

    private void scoreEatFruit(Graphics g) {
        String scorePoint = "Snake eat: " + score;
        g.setColor(Color.WHITE);
        g.drawString(scorePoint, 0, 10);
    }

    private void keyEscForRestart(Graphics g) {
        String escRestart = "ESC - restart";
        g.setColor(Color.WHITE);
        g.drawString(escRestart, WIDTH - 50, 10);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (!isGameOver){
            checkApple();
            checkCollisions();
            move();
        }
        repaint();
    }

    public void checkApple(){
        if (x[0] == appleX && y[0] == appleY){
            snakeDots ++;
            score++;
            createApple();
        }
    }

    public void checkCollisions(){
        for (int i = 0; i < xB.length; i++) {
            if (x[0] == xB[i] && y[0] == yB[i]) {
                isGameOver = true;
            }
        }

        for (int i = 1; i < snakeDots; i++) {
            if (x[0] == x[i] && y[0] == y[i]) {
                isGameOver = true;
            }
        }

        if (x[0] > WIDTH
                || x[0] < 0
                || y[0] > HEIGHT
                || y[0] < 0) {
            isGameOver = true;
        }
    }

    public void move(){
        for (int i = snakeDots; i > 0; i--){
            x[i] = x[i - 1];
            y[i] = y[i - 1];
        }

        if (directions[0]){
            x[0] -= DOT_SIZE;
        }
        if (directions[1]){
            y[0] -= DOT_SIZE;
        }
        if (directions[2]){
            x[0] += DOT_SIZE;
        }
        if (directions[3]){
            y[0] += DOT_SIZE;
        }


        switchDirectionMoveTail();
    }

    private void switchDirectionMoveTail (){
        int x1 = x[snakeDots - 1];
        int y1 = y[snakeDots - 1];
        int x2 = x[snakeDots - 2];
        int y2 = y[snakeDots - 2];


        if (x2 > x1) { numberSideTail = 0; }
        if (x2 < x1) { numberSideTail = 1; }
        if (y2 < y1) { numberSideTail = 2; }
        if (y2 > y1) { numberSideTail = 3; }
    }

    private final int WIDTH = 360;
    private final int HEIGHT = 336;
    private final int DOT_SIZE = 16;
    private final int ALL_DOTS = 400;
    private final int DELAY = 250;

    private int score;
    private Image dot;
    private ArrayList<Image> fruit;
    private final Image[] headDURLSnake = new Image[4];
    private final Image[] tailDURLSnake = new Image[4];
    private Image block;
    private int numberSideHead;
    private int numberSideTail = 0;


    private int appleX;
    private int appleY;
    private int idFruit;

    // Position for body snake
    private int[] x = new int[ALL_DOTS];
    private int[] y = new int[ALL_DOTS];
    private int snakeDots;

    // Position for block
    private int[] xB = new int[8];
    private int[] yB = new int[8];

    //Direction move snake
    private boolean[] directions = {false, false, false, false}; // left, up, right, down

    private Timer timer = new Timer(DELAY, this);
    private boolean isGameOver = false;
    private boolean isStop = false;

    class FieldKeyListener extends KeyAdapter{

        @Override
        public void keyPressed(KeyEvent e) {
            super.keyPressed(e);
            int key = e.getKeyCode();

            if (key == KeyEvent.VK_SPACE) {
                isStop = !isStop;
            }

            if (!isStop) {
                timer.start();
                if (lastKey != 0 && key != lastKey) {
                    timer.setDelay(DELAY);
                } else {
                    if (isCorrect2ClickMove(key)) {
                        timer.setDelay(DELAY - 150);
                    }
                }
            } else {
                timer.stop();
            }
            lastKey = key;
            switchDirectionMove(key);
        }

        private boolean isCorrect2ClickMove (int key) {
            for (int i = 0; i < directions.length; i++) {
                if (directions[i] && key == directionsKey[i]) {
                    return true;
                }
            }

            return false;
        }

        private void switchDirectionMove(int key){

            // moving
            if (key == KeyEvent.VK_LEFT || key == KeyEvent.VK_RIGHT) {
                if (key == KeyEvent.VK_LEFT && !directions[2]) {
                    directions[0] = true;
                    numberSideHead = 1;
                } else if (key == KeyEvent.VK_RIGHT && !directions[0]) {
                    directions[2] = true;
                    numberSideHead = 0;
                }

                directions[1] = false;
                directions[3] = false;
            }

            if (key == KeyEvent.VK_UP || key == KeyEvent.VK_DOWN) {
                if (key == KeyEvent.VK_UP && !directions[3]) {
                    directions[1] = true;
                    numberSideHead = 2;
                } else if (key == KeyEvent.VK_DOWN && !directions[1]) {
                    directions[3] = true;
                    numberSideHead = 3;
                }

                directions[2] = false;
                directions[0] = false;
            }

            // restart game
            if (key == KeyEvent.VK_ESCAPE){
                isGameOver = false;

                initGame();


            }
        }

        private int lastKey = 0;
        private int[] directionsKey = {37, 38, 39, 40}; // LEFT, UP, RIGHT, DOWN
    }
}

