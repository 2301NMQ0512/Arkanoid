package collidable;

import java.awt.Color;
import geometry.Point;
import geometry.Rectangle;

public class PatrollingBlock extends Block {


    private final double minX;
    private final double maxX;


    private double moveSpeed = 2.0; // Di chuyển 2 pixels mỗi khung hình


    public PatrollingBlock(Rectangle rectangle, Color color, int hitPoints, int moveRange) {

        super(rectangle, color, hitPoints);


        double startX = rectangle.getUpperLeft().getX();


        double halfRange = moveRange / 2.0; // (Chia cho 2.0 để đảm bảo là số double)


        this.minX = startX - halfRange;
        this.maxX = startX + halfRange;
    }


    @Override
    public void timePassed() {

        double currentX = this.rectangle.getUpperLeft().getX();
        double currentY = this.rectangle.getUpperLeft().getY();


        double newX = currentX + this.moveSpeed;


        if (newX < this.minX || newX > this.maxX) {

            this.moveSpeed *= -1;


            newX = currentX + this.moveSpeed;
        }


        this.rectangle = new Rectangle(
                new Point(newX, currentY),
                this.rectangle.getWidth(),
                this.rectangle.getHeight()
        );
    }

}