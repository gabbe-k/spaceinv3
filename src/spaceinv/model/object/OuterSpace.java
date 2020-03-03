package spaceinv.model.object;

import spaceinv.model.Positionable;

import static spaceinv.model.SI.*;

/*
    Used to check if projectiles from gun have left our world
 */
public class OuterSpace implements Positionable {

    @Override
    public double getX() {
        return 0;
    }

    @Override
    public double getY() {
        return 0;
    }

    @Override
    public double getWidth() {
        return GAME_WIDTH;
    }

    @Override
    public double getHeight() {
        return OUTER_SPACE_HEIGHT;
    }
}
