package de.fynn.model;

import org.lwjgl.opengl.GL11;

public class Player
{
    private float x;
    private float y;
    private final float width;
    private final float height;
    private final float speed;

    public Player(float x, float y, float width, float height, float speed)
    {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.speed = speed;
    }

    public void updatePosition(boolean moveUp, boolean moveDown)
    {
		if(moveUp && y + height / 2 <= 1.0f)
		{
			y += speed;
		}

		if(moveDown && y - height / 2 >= -1.0f)
		{
			y -= speed;
		}
	}

    public void render()
    {
        GL11.glBegin(GL11.GL_QUADS);
        GL11.glVertex2f(x, y - height / 2);
        GL11.glVertex2f(x + width, y - height / 2);
        GL11.glVertex2f(x + width, y + height / 2);
        GL11.glVertex2f(x, y + height / 2);
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

    public float getWidth()
    {
        return width;
    }

    public float getHeight()
    {
        return height;
    }
}
