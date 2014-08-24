package com.wilmot.game;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

/**
 * Created by pbwilmot on 8/5/14.
 *
 * This is an 8 directional action
 */
public class IsometricAction {

    private final Animation northAnimation, northEastAnimation, eastAnimation, southEastAnimation, southAnimation, southWestAnimation, westAnimation, northWestAnimation;
    private final float animationSpeed;
    private float elapsedTime;
    private Direction previousDirection = Direction.NONE;

    public IsometricAction(Texture animationTexture, final int cols, final float animationSpeed) {
        this.animationSpeed = animationSpeed;
        // This is an 8 directional action so rows must be 8
        final int rows = 8;

        // Get the width of an element
        final int elementWidth = animationTexture.getWidth() / cols;
        // Get the height of an element
        final int elementHeight = animationTexture.getHeight() / rows;
        // Load all elements into a 2d array
        final TextureRegion[][] tmp = TextureRegion.split(animationTexture, elementWidth, elementHeight);

        northAnimation = new Animation(this.animationSpeed, tmp[0]);
        northEastAnimation = new Animation(this.animationSpeed, tmp[1]);
        eastAnimation = new Animation(this.animationSpeed, tmp[2]);
        southEastAnimation = new Animation(this.animationSpeed, tmp[3]);
        southAnimation = new Animation(this.animationSpeed, tmp[4]);
        southWestAnimation = new Animation(this.animationSpeed, tmp[5]);
        westAnimation = new Animation(this.animationSpeed, tmp[6]);
        northWestAnimation = new Animation(this.animationSpeed, tmp[7]);
    }

    public void draw(final Batch batch, float delta, final Direction direction, int x, int y) {
        if (this.previousDirection != direction) {
            this.elapsedTime = 0;
            this.previousDirection = direction;
        }

        this.elapsedTime += delta;

        switch(direction) {
            case NORTH:
                renderAnimation(batch, northAnimation, elapsedTime, x, y);
                return;
            case NORTH_EAST:
                renderAnimation(batch, northEastAnimation, elapsedTime, x, y);
                return;
            case EAST:
                renderAnimation(batch, eastAnimation, elapsedTime, x, y);
                return;
            case SOUTH_EAST:
                renderAnimation(batch, southEastAnimation, elapsedTime, x, y);
                return;
            case SOUTH:
                renderAnimation(batch, southAnimation, elapsedTime, x, y);
                return;
            case SOUTH_WEST:
                renderAnimation(batch, southWestAnimation, elapsedTime, x, y);
                return;
            case WEST:
                renderAnimation(batch, westAnimation, elapsedTime, x, y);
                return;
            case NORTH_WEST:
                renderAnimation(batch, northWestAnimation, elapsedTime, x, y);
                return;
            case NONE:

            default:
        }
    }

    private void renderAnimation(final Batch batch, final Animation animation, float elapsedTime, int x, int y) {
        batch.draw(animation.getKeyFrame(elapsedTime, true), x, y);
    }
}
