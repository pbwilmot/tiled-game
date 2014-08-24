package com.wilmot.game;

import com.badlogic.gdx.math.Vector2;

/**
 * Created by pbwilmot on 8/1/14.
 */
public class Character {

    protected float speed;
    protected Vector2 position;

    public Character() {

    }

    public Character(float speed) {
        this.speed = speed;
    }

    public float getSpeed() {
        return this.speed;
    }

    public void setSpeed(float speed) {
        this.speed = speed;
    }

    public Vector2 getPosition() { return this.position; }

    public void setPosition(Vector2 position) {
        this.position = position;
    }

    public void setXPosition(float x) {
        this.position.x = x;
    }

    public void setYPosition(float y) {
        this.position.y = y;
    }
}
