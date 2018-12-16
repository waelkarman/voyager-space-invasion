package SoEproj;


public class EasyAlien extends Alien {

    private final String imagePath;
    private final String moveType;  // changes alien movements  
    private boolean goDown;         // to set boss helpers go up and down as thier boss
    

    public EasyAlien(int x, int y) {
        super(x, y, 1);             // 1 is the life
        SPACE = 1;
        points = 50;
        this.moveType = "";

        imagePath = "./src/main/java/SoEproj/Resource/WeakAlien.png";
        loadImage(imagePath);
        getImageDimensions();
    }

    public EasyAlien(int x, int y, String moveType, boolean goDown) {
        super(x, y, 1);
        SPACE = 1;
        points = 50;
        this.moveType = moveType;
        this.goDown = goDown;

        imagePath = "./src/main/java/SoEproj/Resource/WeakAlien.png";
        loadImage(imagePath);
        getImageDimensions();
    }


    public void move() {  
        if(moveType == "BossHelper") {
            if (x >= B_WIDTH - 65)
                x -= SPACE;
            
            if (goDown) {
                y += SPACE;
                if (y > B_HEIGHT - this.height)  // the alien touches the down board limit
                    goDown = false;
            } 
            else {
                y -= SPACE;
                if (y < B_SCORE_SPACE)
                    goDown = true;
            }
        }
        else{
            if (x < 0)
                setDying(true);
            x -= SPACE;
        }
    }
}
