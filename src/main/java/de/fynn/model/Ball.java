package de.fynn.model;

import org.lwjgl.opengl.GL11;

public class Ball
{
    private float x;
    private float y;
    private float speedX;
    private float speedY;
    private final float size;

    public Ball(float x, float y, float speedX, float speedY, float size)
    {
        this.x = x;
        this.y = y;
        this.speedX = speedX;
        this.speedY = speedY;
        this.size = size;
    }

    public void updatePosition(Player player)
    {
        x += speedX;
        y += speedY;
        checkCollision(player);
    }

    private void checkCollision(Player player)
    {
        if (x + size >= 1.0f || x - size <= -1.0f)
        {
            speedX = -speedX;
        }
        if (y + size >= 1.0f || y - size <= -1.0f)
        {
            speedY = -speedY;
        }

        // Kollision mit dem Spieler
        if (x - size < player.getX() + player.getWidth() && x + size > player.getX())
        {
            if (y + size > player.getY() - player.getHeight() / 2 && y - size < player.getY() + player.getHeight() / 2)
            {
                speedX = -speedX;
            }
        }
    }

    public void render()
    {
        GL11.glBegin(GL11.GL_QUADS);
        GL11.glVertex2f(x - size, y - size);
        GL11.glVertex2f(x + size, y - size);
        GL11.glVertex2f(x + size, y + size);
        GL11.glVertex2f(x - size, y + size);
        GL11.glEnd();
    }

    public float getX()
    {
        return x;
    }

    public float getY()
    {
        return y;
    }

    public float getSize()
    {
        return size;
    }
}
