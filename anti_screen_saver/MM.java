import java.awt.AWTException;
import java.awt.MouseInfo;
import java.awt.Point;
import java.awt.Robot;

public class MM {
   long inactiveDuration = 20 * 1000;
   int tickSleepDuration = 3 * 1000;
   int xDelta = 4;
  
   Robot r;
   Point lastActivityPoint;
   long lastActivityTime;
  
   public MM() throws AWTException {
      r = new Robot();
   }
  
   public static void main(String[] args) throws Exception {
      MM mm = new MM();
      mm.mainLoop();
   }
  
   void mainLoop() {
      registerActivityPoint();
     
      boolean active = true;
     
      while (true) {
         sleep(tickSleepDuration);
        
         if (isActive()) {
            registerActivityPoint();
            if (!active) {
               System.out.println("detect activity");
            }
            active = true;
         } else {
            if (isExceededInactivityTimeout()) {
               if (active) {
                  System.out.println("detect lack of activity");
               }
               active = false;
               pretendMove();
            }
         }
      }
   }
  
   boolean isActive() {
      Point actualPoint = MouseInfo.getPointerInfo().getLocation();
      return !pointEq(actualPoint, lastActivityPoint);
   }

   private boolean pointEq(Point a, Point b) {
      return a.x == b.x && a.y == b.y;
   }
  
   boolean isExceededInactivityTimeout() {
      long actualTime = System.currentTimeMillis();
      return lastActivityTime + inactiveDuration < actualTime;
   }
  
   private void registerActivityPoint() {
      lastActivityTime = System.currentTimeMillis();
      lastActivityPoint = MouseInfo.getPointerInfo().getLocation();
   }
  
   private void pretendMove() {
      Point b = MouseInfo.getPointerInfo().getLocation();
      int x = (int) b.getX();
      int y = (int) b.getY();
     
      mouseMove(x + xDelta, y);
      mouseMove(x, y);
   }
  
   private void mouseMove(int x, int y) {
      r.mouseMove(x, y);
      sleep(50);
   }
  
   private void sleep(long millis) {
      try {
         Thread.sleep(millis);
      } catch (InterruptedException e) {
         e.printStackTrace();
      }
   }
}
