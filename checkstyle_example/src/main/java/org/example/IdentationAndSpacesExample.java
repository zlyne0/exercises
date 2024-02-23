package org.example;

public class IdentationAndSpacesExample {
    public void conditionExample() {
      int zero = 0;
        int a = 0;
        int b=10;
        if (a==b){
            System.out.println("fail");
        }
        if(a!=b) {
            System.out.println("fail2");
        }
        if (a > b) System.out.println("fail");
    }
        public void someMethod() {
                System.out.println("out");
        }
public void someMethod2() {
System.out.printf("out2");
}


    public void withoutFinale(int arg1, int arg2, int arg3) {
        int z = arg1 + arg2 + arg3;
        arg1 = 0;
        System.out.println("z = " + z);
    }
}
