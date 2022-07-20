package Structures;

import Threads.Clients;

public class RelativePoint {
    private static final float width = Clients.WIDTH;
    private static final float canvasWidth = Clients.CANVAS_WIDTH;
    private static final float canvasHeight = Clients.CANVAS_HEIGHT;
    private static final float posWIDTH = width / 2 - canvasWidth / 2;
    private float x;
    private float y;

    public RelativePoint(float x, float y) {
        this.x = (x - posWIDTH) / canvasWidth;
        this.y = (y - 8) / canvasHeight;
    }

    public float getProcessedX() {
        return x * canvasWidth + posWIDTH;
    }

    public void setX(float x) {
        this.x = x;
    }

    public float getProcessedY() {
        return y * canvasHeight + 8;
    }

    public void setY(float y) {
        this.y = y;
    }

    public float getX() {
        return x;
    }

    public float getY() {
        return y;
    }
}
