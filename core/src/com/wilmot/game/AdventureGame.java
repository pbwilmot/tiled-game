package com.wilmot.game;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

/**
 * Created by pbwilmot on 7/31/14.
 */
public class AdventureGame extends Game {

    private SpriteBatch batch;
    private BitmapFont font;

    @Override
    public void create() {
        this.batch = new SpriteBatch();
        this.font = new BitmapFont(); //Use LibGDX's default Arial font.
        this.setScreen(new MainMenuScreen(this));
    }

    public void render() {
        super.render();
    }

    public void dispose() {
        batch.dispose();
        font.dispose();
    }

    public SpriteBatch getBatch() {
        return batch;
    }

    public BitmapFont getFont() {
        return font;
    }

    public void setFont(BitmapFont font) {
        this.font = font;
    }
}
