package SoEproj;

import java.util.Timer;
import java.util.TimerTask;
import java.util.Scanner;
import java.awt.Toolkit;
 
public class ResettingShip {
    Timer timer;
    SpaceShip sp;
    String ammo;

    public ResettingShip(int seconds,SpaceShip s, String Ammotype) {
        this.sp = s;
        this.ammo = Ammotype;
        timer = new Timer();
        timer.schedule(new RemindTask(), seconds * 1000);
    }
 
    class RemindTask extends TimerTask {
        public void run() {
            sp.resetMissileType(ammo);
        }
    }

}
