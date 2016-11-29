/**
 * Created by Mateusz on 09.03.2016.
 */
import javax.swing.JFrame;

public class TuringMachine implements Runnable
{
    public static void main(String[] args)
    {
        new TuringMachine();
        new Thread().start();
    }
    public TuringMachine()
    {
        Frame frame = new Frame();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
    }
    public void run()
    {
        System.out.println("out");
    }
}

