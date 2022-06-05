import Renderables.Renderable;

import java.util.ArrayList;
import java.util.List;

public class Renderer {

    List<Renderable> renderables = new ArrayList<>();

    Renderer() {

    }

    public void add(Renderable renderable) {
        renderables.add(renderable);
    }

    public void render() {
        for (Renderable renderable : renderables) {
            renderable.render();
        }
    }
}
