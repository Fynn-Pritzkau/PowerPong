package de.fynn.model;

import org.lwjgl.glfw.GLFW;
import org.lwjgl.glfw.GLFWErrorCallback;
import org.lwjgl.glfw.GLFWVidMode;
import org.lwjgl.opengl.GL;
import org.lwjgl.opengl.GL11;
import org.lwjgl.system.MemoryStack;

import java.nio.IntBuffer;

import static org.lwjgl.glfw.Callbacks.glfwFreeCallbacks;
import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11C.glClearColor;
import static org.lwjgl.system.MemoryStack.stackPush;
import static org.lwjgl.system.MemoryUtil.NULL;

public class Main
{
    public static final int WINDOW_WIDTH = 800;
    public static final int WINDOW_HEIGHT = 600;

    private static boolean moveUpRight = false;
    private static boolean moveDownRight = false;

    private static boolean moveUpLeft = false;
    private static boolean moveDownLeft = false;

    private double lastUpdateTime;

    private long window;

    private Ball ball;

    private Player player;
    private Player player2;

    // Game timing variables
    private static final int TARGET_UPS = 60; // updates per second
    private static final double UPDATE_INTERVAL = 1.0 / TARGET_UPS;

    public static void main(String[] args)
    {
        new Main().run();
    }

    public void run()
    {
        try
        {
            init();
            loop();

            // Free the window callbacks and destroy the window
            glfwFreeCallbacks(window);
            glfwDestroyWindow(window);
        }
        finally
        {
            // Terminate GLFW and free the error callback
            glfwTerminate();
            glfwSetErrorCallback(null).free();
        }
    }

    private void init ()
    {
        // Set up an error callback
        GLFWErrorCallback.createPrint(System.err).set();

        // Initialize GLFW
        if (!GLFW.glfwInit())
        {
            throw new IllegalStateException("Unable to initialize GLFW");
        }

        // Configure GLFW
        GLFW.glfwDefaultWindowHints();
        GLFW.glfwWindowHint(GLFW.GLFW_VISIBLE, GLFW.GLFW_FALSE);
        GLFW.glfwWindowHint(GLFW.GLFW_RESIZABLE, GLFW.GLFW_TRUE);

        // Create the window
        window = GLFW.glfwCreateWindow(WINDOW_WIDTH, WINDOW_HEIGHT, "Power Pong", 0, 0);

        if(window == NULL)
        {
            throw new RuntimeException("Failed to create the GLFW window");
        }

        try(MemoryStack stack = stackPush())
        {
            IntBuffer pWidth = stack.mallocInt(1); // int*
            IntBuffer pHeight = stack.mallocInt(1); // int*

            // Get the window size passed to glfwCreateWindow
            glfwGetWindowSize(window, pWidth, pHeight);

            // Get the resolution of the primary monitor
            GLFWVidMode vidmode = glfwGetVideoMode(glfwGetPrimaryMonitor());

            // Center the window
            glfwSetWindowPos(
                    window,
                    (vidmode.width() - pWidth.get(0)) / 2,
                    (vidmode.height() - pHeight.get(0)) / 2
            );
        } // the stack frame is popped automatically

        // Make the OpenGL context current
        glfwMakeContextCurrent(window);

        // Enable v-sync
        glfwSwapInterval(1);

        // Make the window visible
        glfwShowWindow(window);

        // Initialize OpenGL bindings
        GL.createCapabilities();

        // Set the clear color
        glClearColor(0.0f, 0.0f, 0.0f, 0.0f);

        ball = new Ball(0.0f, 0.0f, 0.02f, 0.02f, 0.02f);

        player = new Player(-0.95f, 0.0f, 0.05f, 0.3f, 0.02f);

        //todo Why the fuck must be the positioning 5 off at 0.9 and not 0.95?
        player2 = new Player(0.9f, 0.0f, 0.05f, 0.3f, 0.02f);

        // Initialize timing
        lastUpdateTime = glfwGetTime();
    }

    private void update ()
    {
        // Set up key callbacks
        GLFW.glfwSetKeyCallback(window, (windowHandle, key, scancode, action, mods) ->
        {
            if(key == GLFW.GLFW_KEY_W && (action == GLFW.GLFW_PRESS || action == GLFW.GLFW_REPEAT))
            {
                moveUpLeft = true;
            }
            else if(key == GLFW.GLFW_KEY_W && action == GLFW.GLFW_RELEASE)
            {
                moveUpLeft = false;
            }

            if(key == GLFW.GLFW_KEY_S && (action == GLFW.GLFW_PRESS || action == GLFW.GLFW_REPEAT))
            {
                moveDownLeft = true;
            }
            else if(key == GLFW.GLFW_KEY_S && action == GLFW.GLFW_RELEASE)
            {
                moveDownLeft = false;
            }

            if(key == GLFW.GLFW_KEY_UP && (action == GLFW.GLFW_PRESS || action == GLFW.GLFW_REPEAT))
            {
                moveUpRight = true;
            }
            else if (key == GLFW.GLFW_KEY_UP && action == GLFW.GLFW_RELEASE)
            {
                moveUpRight = false;
            }

            if(key == GLFW.GLFW_KEY_DOWN && (action == GLFW.GLFW_PRESS || action == GLFW.GLFW_REPEAT))
            {
                moveDownRight = true;
            }
            else if(key == GLFW.GLFW_KEY_DOWN && action == GLFW.GLFW_RELEASE)
            {
                moveDownRight = false;
            }
        });

        player.updatePosition(moveUpLeft, moveDownLeft);
        player2.updatePosition(moveUpRight, moveDownRight);
        ball.updatePosition(player, player2);
    }

    private void render ()
    {
        // Clear the framebuffer
        GL11.glClear(GL11.GL_COLOR_BUFFER_BIT | GL11.GL_DEPTH_BUFFER_BIT);

        player.render();

        player2.render();

        ball.render();

        // Swap the color buffers
        glfwSwapBuffers(window);
    }

    private void loop ()
    {
        while (!glfwWindowShouldClose(window))
        {
            // Poll for window events. The key callback above will only be
            // invoked during this call.
            glfwPollEvents();

            double currentTime = glfwGetTime();
            double elapsedTime = currentTime - lastUpdateTime;

            if(elapsedTime >= UPDATE_INTERVAL)
            {
                update();

                lastUpdateTime = currentTime;
            }

            render();
        }
    }
}
