package Client;

public class RelativePoint {
    private static final float width = Clients.CANVAS_WIDTH;
    private static final float height = Clients.CANVAS_HEIGHT;
    private float x;
    private float y;

    public RelativePoint(float x, float y) {
        this.x = (x - width) / width;
        this.y = (y - height) / height;
    }

    public float getX() {
        return x;
    }

    public void setX(float x) {
        this.x = x;
    }

    public float getY() {
        return y;
    }

    public void setY(float y) {
        this.y = y;
    }
}
