
import java.awt.Color;
//import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.Timer;
//import javax.swing.UIManager;
//import javax.swing.UnsupportedLookAndFeelException;
import java.util.Random;


@SuppressWarnings("serial")
public class WaterRipples extends JPanel{

    private BufferedImage canvas;

    final static int WIDTH = 800, HEIGHT = 800;
    int cols;
    int rows;
    float[][] current,previous;

    float dampening = (float)0.99;

    public void init (int w, int h) {
        this.cols = w;
        this.rows = h;
        this.current = new float[this.cols][this.rows];
        this.previous = new float[this.cols][this.rows];

        canvas = new BufferedImage(this.cols, this.rows, BufferedImage.TYPE_INT_ARGB);

        //fillCanvas(Color.BLACK);

        //setup();
    }

    public int randInt(int max) {
        Random rand = new Random();
        return rand.nextInt(max);
    }
    public void randDrop() {
        if (randInt(5) == 0)
        this.previous[randInt(WIDTH)][randInt(HEIGHT)] = 255;
    }

    public WaterRipples(int w, int h) {
        init(w,h);

        Timer timer = new Timer(10, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                randDrop();
                draw();
                repaint();
            }
        });
        timer.start();
    }

    public void draw() {
        int var;
        for (int i=1; i<this.cols-1; i++) {
            for(int j=1; j<this.rows-1; j++) {

                this.current[i][j] = (
                    this.previous[i-1][j] + 
                    this.previous[i+1][j] + 
                    this.previous[i][j-1] + 
                    this.previous[i][j+1] ) / 2 - 
                    this.current[i][j];

                this.current[i][j] = this.current[i][j] * this.dampening;
                //if (var!=0) System.out.println(var);
                var = Math.round(this.current[i][j]*10);
                if (var>255 || var<0) {
                    if (var>255) {
                        var = 255;
                    } else {
                        var = 0;
                    }
                }
                
                colorPix(i,j, getColor(var));
            }
        }

        float[][] temp = this.previous;
        this.previous = this.current;
        this.current = temp;
    }

    public void colorPix(int x, int y, Color color) {
        int c = color.getRGB();
        canvas.setRGB(x, y, c);
    }

    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;
        //g2.scale(2,2);
        g2.drawImage(canvas, null, null);
    }

    public void fillCanvas(Color c) {
        int color = c.getRGB();
        for (int x = 0; x < canvas.getWidth(); x++) {
            for (int y = 0; y < canvas.getHeight(); y++) {
                canvas.setRGB(x, y, color);
            }
        }
        repaint();
    }

    public Color getColor (int x) {
        return new Color(x,x,x);
    }


    public static void main(String[] args) {

        JFrame frame = new JFrame("Water Ripples");

        WaterRipples game = new WaterRipples(WIDTH, HEIGHT);

        frame.add(game);
        frame.pack();
        frame.setVisible(true);
        
        frame.setSize(WIDTH,HEIGHT); 
        frame.setResizable(false);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

    }

}
