package SoEproj;


/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

public class EasyAlien extends Alien {

    private String imagePath = ".\\src\\main\\java\\SoEproj\\Resource\\WeakAlien.png";
    private String moveType = "";   // initialization needed to avoid null pointer exception
    private boolean goDown;         // to set boss helpers go up and down as thier boss

    public EasyAlien(int x, int y) {
        super(x, y, 1);
        SPACE = 1;

        loadImage(imagePath);
        getImageDimensions();
    }

    public EasyAlien(int x, int y, String moveType, boolean goDown) {
        super(x, y, 1);
        this.moveType = moveType;
        this.goDown = goDown;
        SPACE = 1;

        loadImage(imagePath);
        getImageDimensions();
    }


    public void move() {  
        switch(moveType){
            case "BossHelper":
                if (x >= INITIAL_X-65)
                    x -= SPACE;
                
                if (goDown) {
                    y += SPACE;
                    if (y > 400)
                        goDown = false;
                
                } else {
                    y -= SPACE;
                    if (y < 0)
                        goDown = true;
                }
                break;

            default:
                if (x < 0)
                    setDying(true);
    
                x -= SPACE;
        }
    }
}
