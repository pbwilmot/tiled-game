package com.wilmot.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;

/**
 * Created by pbwilmot on 7/31/14.
 */
public class MainMenuScreen implements Screen {

    final AdventureGame game;
    final OrthographicCamera camera;

    public MainMenuScreen(final AdventureGame game) {
        super();
        this.game = game;
        this.camera = new OrthographicCamera();

        // TODO(pwilmot) what size should go here?
        this.camera.setToOrtho(false, 800, 480);
    }

    @Override
    public void render(float delta) {

        Gdx.gl.glClearColor(0, 0, 0.2f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        this.camera.update();
        this.game.getBatch().setProjectionMatrix(camera.combined);

        this.game.getBatch().begin();
        this.game.getFont().draw(this.game.getBatch(), "Welcome to a magical Adventure!!! ", 100, 150);
        this.game.getFont().draw(this.game.getBatch(), "Tap anywhere to begin!", 100, 100);
        this.game.getBatch().end();

        if (Gdx.input.isTouched()) {
            System.out.println("Switch to game screen");
            game.setScreen(new GameScreen(game));
            dispose();
        }
    }

    @Override
    public void resize(int width, int height) {

    }

    @Override
    public void show() {

    }

    @Override
    public void hide() {

    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void dispose() {

    }
}
