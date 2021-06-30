package package_a;

import org.apache.commons.math3.geometry.euclidean.twod.Vector2D;
import package_b.BMain;

public class AMain {

    public static void main(String args[]) {
        System.out.println(AMain.class.getName() + " run in main");

        Vector2D point = new Vector2D(10, 10);

        System.out.println(AMain.class.getName() + " point: " + point);

        BMain.run();

    }

}
