package com.wilmot.game;

import com.badlogic.gdx.graphics.g2d.Animation;

/**
 * Created by pbwilmot on 8/1/14.
 */
public interface animatedMover {

    public void setLeftAnimation(Animation left);
    public void setUpAnimation(Animation up);
    public void setRightAnimation(Animation right);
    public void setDownAnimation(Animation down);

    public void moveLeft();
    public void moveUp();
    public void moveRight();
    public void moveDown();

}
