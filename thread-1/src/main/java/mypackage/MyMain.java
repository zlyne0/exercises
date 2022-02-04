package mypackage;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

public class MyMain {

    public static void main(String args[]) throws ExecutionException, InterruptedException {
        CompletableFuture<String> completableFuture = CompletableFuture.failedFuture(new IllegalStateException("some my exceptions"));
        completableFuture = completableFuture.exceptionally(exception -> "catch exception");
        System.out.println("" + completableFuture.get());
    }
}
