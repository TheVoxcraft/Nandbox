package Renderables;

public class TextField implements Renderable {
    public int x;
    public int y;
    private int width;
    private int height;
    private String text;

    TextField(int x, int y, String text) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.text = text;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        // TODO: calculate the width and height of the text
        this.text = text;
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

    @Override
    public void render() {
        return;
    }
}
