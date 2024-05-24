package de.fynn.model;

import org.lwjgl.opengl.GL11;

public class Ball
{
    private float x;
    private float y;
    private float speedX;
    private float speedY;
    private final float size;

    private static final float MAX_VERTICAL_SPEED = 0.03f;

    public Ball(float x, float y, float speedX, float speedY, float size)
    {
        this.x = x;
        this.y = y;
        this.speedX = speedX;
        this.speedY = speedY;
        this.size = size;
    }

    public void updatePosition(Player leftPlayer, Player rightPlayer)
    {
        x += speedX;
        y += speedY;
        checkCollision(leftPlayer, rightPlayer);
    }

    private void checkCollision(Player leftPlayer, Player rightPlayer)
    {
        if(x + size >= 1.0f || x - size <= -1.0f)
        {
            speedX = -speedX;
        }
        if(y + size >= 1.0f || y - size <= -1.0f)
        {
            speedY = -speedY;
        }

        // Collision with left player
        if(x - size < leftPlayer.getX() + leftPlayer.getWidth() && x + size > leftPlayer.getX())
        {
            bounceOffPlayer(leftPlayer);
        }

        // Collision with right player
        if (x + size > rightPlayer.getX() + rightPlayer.getWidth() && x - size < rightPlayer.getX())
        {
            bounceOffPlayer(rightPlayer);
        }
    }

    private void bounceOffPlayer(Player player)
    {
        if(y + size > player.getY() - player.getHeight() / 2 && y - size < player.getY() + player.getHeight() / 2)
        {
            speedX = -speedX;

            float hitPosition = (y - player.getY()) / (player.getHeight() / 2);
            speedY = hitPosition * MAX_VERTICAL_SPEED;
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
