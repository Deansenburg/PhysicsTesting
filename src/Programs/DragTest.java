package Programs;

public class DragTest {
    public static void main(String[] a)
    {
        System.out.println(Math.pow(-1d, 1d/5));

        double dC = 0.005;

        for (int velocity=1;velocity<10000;velocity+=100)
        {
            double drag = Math.pow(1d - (1d / velocity), 1d / dC);
            if (velocity < drag || true)
            {
                System.out.println("Velocity "+velocity+" Drag "+drag);
            }
        }
    }
}
