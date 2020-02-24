package spaceinv.model;

public abstract class Movable implements Positionable {

    private double x;
    private double y;
    private double dx;
    private double dy;
    private final double width;
    private final double height;

    public Movable(double x, double y,double width, double height) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
    }

    protected void move() {
        x+=dx;
        y+=dy;
    }

    @Override
    public double getX() {
        return x;
    }
    @Override
    public double getY() {
        return y;
    }
    public double getDy() {
        return dy;
    }
    protected void setDy(double dy) {
        this.dy = dy;
    }
    protected double getDx() {
        return dx;
    }
    protected void setDx(double dx) {
        this.dx = dx;
    }
    @Override
    public double getWidth() {
        return width;
    }
    @Override
    public double getHeight() {
        return height;
    }
    protected void setX(double x) {
        this.x = x;
    }
    protected void setY(double y) {
        this.y = y;
    }
}
