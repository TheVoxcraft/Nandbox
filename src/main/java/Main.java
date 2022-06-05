import Renderables.Rectangle;
import Renderables.Renderable;
import org.lwjgl.*;
import org.lwjgl.glfw.*;
import org.lwjgl.opengl.*;
import org.lwjgl.system.*;

import java.nio.*;

import static org.lwjgl.glfw.Callbacks.*;
import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.glfw.GLFW.glfwSetWindowTitle;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.system.MemoryStack.*;
import static org.lwjgl.system.MemoryUtil.*;

public class Main {

    private static final boolean FLAG_DEBUG = true;

    // The window handle
    private long window;
    boolean limitFPS = true;
    int MaxFPS = 60;

    public void run() {
        System.out.println("Hello LWJGL " + Version.getVersion() + "!");

        init();
        startMainLoop();

        // Free the window callbacks and destroy the window
        glfwFreeCallbacks(window);
        glfwDestroyWindow(window);

        // Terminate GLFW and free the error callback
        glfwTerminate();
        glfwSetErrorCallback(null).free();
    }

    private void init() {
        // Setup an error callback. The default implementation
        // will print the error message in System.err.
        GLFWErrorCallback.createPrint(System.err).set();

        // Initialize GLFW. Most GLFW functions will not work before doing this.
        if ( !glfwInit() )
            throw new IllegalStateException("Unable to initialize GLFW");

        // Configure GLFW
        glfwDefaultWindowHints(); // optional, the current window hints are already the default
        glfwWindowHint(GLFW_VISIBLE, GLFW_FALSE); // the window will stay hidden after creation
        glfwWindowHint(GLFW_RESIZABLE, GLFW_TRUE); // the window will be resizable

        // Create the window
        window = glfwCreateWindow(800, 600, "Hello World!", NULL, NULL);
        if ( window == NULL )
            throw new RuntimeException("Failed to create the GLFW window");

        // Setup a key callback. It will be called every time a key is pressed, repeated or released.
        glfwSetKeyCallback(window, (window, key, scancode, action, mods) -> {
            // Log every key press
            System.out.println("Key: " + key + " (" + scancode + ") Action: " + action + " Mods: " + mods);
            if ( key == GLFW_KEY_ESCAPE && action == GLFW_RELEASE ) glfwSetWindowShouldClose(window, true); // We will detect this in the rendering loop
        });

        glfwSetMouseButtonCallback(window, (window, button, action, mods) -> { // button -> mouse 3 and 4, action -> press and release, mods -> shift, ctrl, alt
            System.out.println("Mouse button: " + button + " (" + action + ") Mods: " + mods);
        });

        // Get the thread stack and push a new frame
        try ( MemoryStack stack = stackPush() ) {
            IntBuffer pWidth = stack.mallocInt(1); // int*
            IntBuffer pHeight = stack.mallocInt(1); // int*

            // Get the window size passed to glfwCreateWindow
            glfwGetWindowSize(window, pWidth, pHeight);

            // Get the resolution of the primary monitor
            GLFWVidMode vidMode = glfwGetVideoMode(glfwGetPrimaryMonitor());

            // Center the window
            glfwSetWindowPos(
                    window,
                    (vidMode.width() - pWidth.get(0)) / 2,
                    (vidMode.height() - pHeight.get(0)) / 2
            );
        } // the stack frame is popped automatically

        // Make the OpenGL context current
        glfwMakeContextCurrent(window);
        // Enable v-sync
        glfwSwapInterval(1);

        // Make the window visible
        glfwShowWindow(window);
    }

    private void startMainLoop() {
        Renderer renderer = new Renderer();
        Renderables.Rectangle rectangle = new Renderables.Rectangle(100, 100, 100, 100);
        rectangle.setColor(new float[] {1, 1, 1});
        renderer.add(rectangle);

        long proc_delta = 0;
        long total_delta = 0;
        initOpenGL();

        glfwSetWindowTitle(window, "Nandbox");

        // Run the rendering loop until the user has attempted to close
        // the window or has pressed the ESCAPE key.
        while ( !glfwWindowShouldClose(window) ) {
            // get nano timestamp
            long start = System.nanoTime();
            glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT); // clear the framebuffer

            inputLoop();
            updateLoop(rectangle);
            renderLoop(renderer);

            if(FLAG_DEBUG) {
                debugLoop(total_delta);
            }

            glfwSwapBuffers(window); // swap the color buffers

            long end_processing = System.nanoTime();
            proc_delta = end_processing - start;

            if(limitFPS) {
                long sleep_ms = (long) (((1000000000.0 / MaxFPS) - proc_delta) / 1000000.0);
                if(sleep_ms > 0) {
                    try {
                        Thread.sleep(sleep_ms);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }

            long end_loop = System.nanoTime();
            total_delta = end_loop - end_processing;

            // Poll for window events. The key callback above will only be
            // invoked during this call.
            glfwPollEvents();
        }
    }

    private void initOpenGL() {
        // This line is critical for LWJGL's interoperation with GLFW's
        // OpenGL context, or any context that is managed externally.
        // LWJGL detects the context that is current in the current thread,
        // creates the GLCapabilities instance and makes the OpenGL
        // bindings available for use.
        GL.createCapabilities();

        // Set the clear color
        glClearColor(185/255f, 215/255f, 225/255f, 0.0f);

        // get screen size from glfw
        IntBuffer width = BufferUtils.createIntBuffer(1);
        IntBuffer height = BufferUtils.createIntBuffer(1);
        glfwGetWindowSize(window, width, height);

        // set viewport
        glViewport(0, 0, width.get(0), height.get(0));

        // setup for 2d rendering
        glMatrixMode(GL_PROJECTION);
        glLoadIdentity(); // reset the projection matrix
        glOrtho(0, width.get(0), height.get(0), 0, 1, -1); // set orthographic projection

        // set modelview
        glMatrixMode(GL_MODELVIEW);
        glLoadIdentity(); // reset the modelview matrix
    }

    private void inputLoop() {

    }

    private void updateLoop(Rectangle rect) {
        DoubleBuffer xCord = BufferUtils.createDoubleBuffer(1);
        DoubleBuffer yCord = BufferUtils.createDoubleBuffer(1);
        glfwGetCursorPos(window, xCord, yCord);
        // set to mouse position
        rect.x = (int) xCord.get(0);
        rect.y = (int) yCord.get(0);
    }

    private void renderLoop(Renderer renderer) {
        renderer.render();
    }

    private void debugLoop(long dt) {
        double fps = 1e9 / dt;
        glfwSetWindowTitle(window, "Nandbox - FPS: " + String.format("%.0f", fps));
    }

    public static void main(String[] args) {
        new Main().run();
    }

}