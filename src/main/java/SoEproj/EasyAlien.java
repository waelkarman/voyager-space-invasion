package SoEproj;


public class EasyAlien extends Alien {

    private String imagePath = "./src/main/java/SoEproj/Resource/WeakAlien.png";
    private String moveType = "";   // initialization needed to avoid null pointer exception
    private boolean goDown;         // to set boss helpers go up and down as thier boss
    

    public EasyAlien(int x, int y) {
        super(x, y, 1);             // 1 is the life
        SPACE = 1;
        super.points = 50;

        loadImage(imagePath);
        getImageDimensions();
    }

    public EasyAlien(int x, int y, String moveType, boolean goDown) {
        super(x, y, 1);
        this.moveType = moveType;
        this.goDown = goDown;
        SPACE = 1;
        this.points = 50;

        loadImage(imagePath);
        getImageDimensions();
    }


    public void move() {  
        if(moveType == "BossHelper") {
            if (x >= INITIAL_X-65)
                x -= SPACE;
            
            if (goDown) {
                y += SPACE;
                if (y > 400)
                    goDown = false;
            } 
            else {
                y -= SPACE;
                if (y < 0)
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
