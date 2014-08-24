package com.wilmot.game;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Vector2;

/**
 * Created by pbwilmot on 8/1/14.
 */
public class SpriteCharacter extends Character implements animatedMover {

    private static final float DEFAULT_SPEED = 32;
    // arbitrarily small value for float comparison
    private static final float EPSILON = 1.0f;
    private Animation downAnimation, upAnimation, leftAnimation, rightAnimation;
    // TODO(pwilmot) store textures for each direction.
    // After moving in a direction set sprite texture to be the resting position in that direction

    private Sprite sprite;
    private Vector2 destination;
    private Vector2 moveVec;

    private Direction previousDirection = Direction.NONE;
    private float elapsedTime = 0;

    public SpriteCharacter(Sprite sprite) {
        super(DEFAULT_SPEED);
        this.sprite = sprite;
    }

    public SpriteCharacter(final Sprite sprite, float speed) {
        super(speed);
        this.sprite = sprite;
    }

    public void draw(Batch batch, float delta) {
        this.move(delta);
        Direction direction = this.getDirection();
        if (direction != this.previousDirection) {
            this.previousDirection = direction;
            System.out.println("Switch Direction " + direction);
            elapsedTime = 0;
        } else {
            elapsedTime += delta;
        }
        switch(direction) {
            case NORTH:
                if (upAnimation != null) {
                    batch.draw(upAnimation.getKeyFrame(elapsedTime, true), this.sprite.getX(), this.sprite.getY());
                    return;
                }
            case EAST:
                if (rightAnimation != null) {
                    batch.draw(rightAnimation.getKeyFrame(elapsedTime, true), this.sprite.getX(), this.sprite.getY());
                    return;
                }
            case SOUTH:
                if (downAnimation != null) {
                    batch.draw(downAnimation.getKeyFrame(elapsedTime, true), this.sprite.getX(), this.sprite.getY());
                    return;
                }
            case WEST:
                if (leftAnimation != null) {
                    batch.draw(leftAnimation.getKeyFrame(elapsedTime, true), this.sprite.getX(), this.sprite.getY());
                    return;
                }
            case NONE:
            default:
        }
        this.sprite.draw(batch);

    }
    // If destination came from a click
    // Make sure that it is unprojected by calling camera.unproject(clickCoordinates);
    public void setDestination(final Vector2 destination) {
        this.destination = destination;
    }

    public void move(float delta) {
        if (this.destination == null) {
            return;
        }
        // Dont use the bottom left.  Use the bottom center of the sprite
        float x = this.sprite.getX() + (this.sprite.getWidth() / 2);
        // check if there is work to do
        if(Math.abs(x - this.destination.x) > EPSILON ||
                Math.abs(this.sprite.getY() - this.destination.y) > EPSILON ) {
            // normalized vector in the direction to move
            moveVec = new Vector2(this.destination.x, this.destination.y)
                    .sub(x, this.sprite.getY()).nor();

            float newX = this.sprite.getX() + (this.speed * delta) * moveVec.x;
            float newY = this.sprite.getY() + (this.speed * delta) * moveVec.y;

            this.sprite.setPosition(newX, newY);
            return;
        }
        moveVec = null;
    }

    public Direction getDirection() {
        // Not moving so don't change direction
        if (this.moveVec == null) {
            return Direction.NONE; // null
        }

        if(Math.abs(this.moveVec.x) > Math.abs(this.moveVec.y)) {
            // X direction is bigger
            if (this.moveVec.x > 0) {
                // Dominated by positive X
                return Direction.EAST;
            }
            return Direction.WEST;
        }
        // Y direction is bigger
        if (this.moveVec.y > 0 ) {
            return Direction.NORTH;
        }
            return Direction.SOUTH;
    }

    @Override
    public void moveLeft() {

    }

    @Override
    public void moveUp() {

    }

    @Override
    public void moveRight() {

    }

    @Override
    public void moveDown() {

    }

    @Override
    public void setLeftAnimation(Animation left) {
        this.leftAnimation = left;
    }

    @Override
    public void setUpAnimation(Animation up) {
        this.upAnimation = up;
    }

    @Override
    public void setRightAnimation(Animation right) {
        this.rightAnimation = right;
    }

    @Override
    public void setDownAnimation(Animation down) {
        this.downAnimation = down;
    }
}
