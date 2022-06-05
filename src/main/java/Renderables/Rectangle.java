package Renderables;

import static org.lwjgl.opengl.GL11.*;

public class Rectangle implements Renderable {
    public int x;
    public int y;
    public int width;
    public int height;
    float[] color;

    public Rectangle(int x, int y, int width, int height) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
    }

    @Override
    public int getX() {
        return x;
    }

    @Override
    public int getY() {
        return y;
    }

    @Override
    public int getWidth() {
        return width;
    }

    @Override
    public int getHeight() {
        return height;
    }

    public void setColor(float[] color) {
        this.color = color;
    }

    @Override
    public void render() {
        glPushMatrix();
        glTranslatef(x, y, 0);
        glBegin(GL_QUADS);
        glColor3fv(color);
        glVertex2f(-width/2f, -height/2f);
        glVertex2f(width/2f, -height/2f);
        glVertex2f(width/2f, height/2f);
        glVertex2f(-width/2f, height/2f);
        glEnd();
        glPopMatrix();
    }
}
