package mypackage;

import java.util.concurrent.Semaphore;

public class XXX {

    public static void main(String args[]) throws InterruptedException {

        Semaphore semaphore = new Semaphore(0);

        System.out.println("acquire");
        semaphore.acquire();
        System.out.println("end acquire");

    }

}
