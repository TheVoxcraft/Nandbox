package Renderables;

import PrimitiveObjects.LogicGate;

public class RenderableLogicGate implements Renderable {
    public int x;
    public int y;
    private int width;
    private int height;

    private Rectangle rect;
    private TextField textField;

    private LogicGate logicGate;

    RenderableLogicGate(int x, int y, PrimitiveObjects.LogicGate logicGate) {
        this.x = x;
        this.y = y;
        this.width = 50;
        this.height = 50;
        this.logicGate = logicGate;

        rect = new Rectangle(x, y, width, height);
        //textField = new TextField(x + width + 5, y, 50, 50, logicGate.getName());
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
        rect.render();
        textField.render();
    }
}
