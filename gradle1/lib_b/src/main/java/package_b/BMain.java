package package_b;

import org.apache.commons.math3.geometry.euclidean.twod.Vector2D;

public class BMain {

    public static void run() {
        System.out.println(BMain.class.getName() + " run in main");
        Vector2D vector2D = new Vector2D(1, 2);
        System.out.println(BMain.class.getName() + " vector2D " + vector2D);
    }

    public static void print(Vector2D point) {
        System.out.println("point " + point);
    }

    public static void main(String args[]) {
        run();
    }

}
