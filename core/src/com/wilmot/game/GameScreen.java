package com.wilmot.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Vector2;

/**
 * Created by pbwilmot on 7/31/14.
 */
public class GameScreen implements Screen, InputProcessor {

    private final AdventureGame game;
    private final OrthographicCamera camera;
    private final OrthogonalTiledMapRenderer tiledMapRenderer;
    private final TiledMap tiledMap;

    private Vector2 prevTouch;

    // in pixels
    private float screenWidth;
    private float screenHeight;

    // in pixels
    private float mapWidth;
    private float mapHeight;


    public GameScreen(final AdventureGame game) {
        this.game = game;

        // create the camera
        this.screenWidth = Gdx.graphics.getWidth();
        this.screenHeight = Gdx.graphics.getHeight();
        System.out.println("width : " + screenWidth + " height : " + screenHeight);

        this.camera = new OrthographicCamera(this.screenWidth, this.screenHeight);
        this.camera.position.set(this.screenWidth / 2, this.screenHeight / 2, 0);
        this.camera.update();

        // Load tiled map
        this.tiledMap = new TmxMapLoader().load("tile_map_1.tmx");
        this.tiledMapRenderer = new OrthogonalTiledMapRenderer(this.tiledMap, 1.5f);

        // Get map size
        final TiledMapTileLayer layer = (TiledMapTileLayer) this.tiledMap.getLayers().get(0);
        final int layerWidth = layer.getWidth();
        final int layerHeight = layer.getHeight();

        final float layerTileWidth = layer.getTileWidth() * this.tiledMapRenderer.getUnitScale();
        final float layerTileHeight = layer.getTileHeight() * this.tiledMapRenderer.getUnitScale ();

        this.mapWidth = layerWidth * layerTileWidth;
        this.mapHeight = layerHeight * layerTileHeight;

        // Handle user inputs
        Gdx.input.setInputProcessor(this);
    }

    @Override
    public void render(float delta) {
        // clear the screen with a dark blue color. The
        // arguments to glClearColor are the red, green
        // blue and alpha component in the range [0,1]
        // of the color to be used to clear the screen.
        Gdx.gl.glClearColor(0, 0, 0.2f, 1);
        Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        validateMove();

        // tell the camera to update its matrices.
        this.camera.update();
        this.tiledMapRenderer.setView(this.camera);
        this.tiledMapRenderer.render();
    }

    /**
     * Ensure that the camera is only showing the map, nothing outside.
     */
    // TODO (pwilmot) this can be optimized since these are all constants
    private void validateMove() {
        if (this.camera.position.x < this.screenWidth / 2) {
            this.camera.position.x = this.screenWidth / 2;
        }

        if (this.camera.position.x >= this.mapWidth - this.screenWidth / 2) {
            this.camera.position.x = this.mapWidth - this.screenWidth / 2;
        }

        if (this.camera.position.y < this.screenHeight / 2) {
            this.camera.position.y = this.screenHeight / 2;
        }

        if (this.camera.position.y >= this.mapHeight - this.screenHeight / 2) {
            this.camera.position.y = this.mapHeight - this.screenHeight / 2;
        }
    }

    @Override
    public void resize(int width, int height) {
//        this.screenWidth = Gdx.graphics.getWidth();
//        this.screenHeight = Gdx.graphics.getHeight();
//
//        System.out.println("width : " + screenWidth + " height : " + screenHeight);
//
//        this.camera.position.set(this.screenWidth / 2, this.screenHeight / 2, 0);
//        this.camera.update();
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
        tiledMapRenderer.dispose();
        tiledMap.dispose();
    }

    @Override
    public boolean keyDown(int keycode) {
        return false;
    }

    @Override
    public boolean keyUp(int keycode) {
        if(keycode == Input.Keys.LEFT) {
            this.camera.translate(-32, 0);
        }
        if(keycode == Input.Keys.RIGHT) {
            this.camera.translate(32, 0);
        }
        if(keycode == Input.Keys.UP) {
            this.camera.translate(0, 32);
        }
        if(keycode == Input.Keys.DOWN) {
            this.camera.translate(0, -32);
        }
        if(keycode == Input.Keys.NUM_1) {
            this.tiledMap.getLayers().get(0).setVisible(
                    !this.tiledMap.getLayers().get(0).isVisible());
        }
        if(keycode == Input.Keys.NUM_2) {
            this.tiledMap.getLayers().get(1).setVisible(
                    !this.tiledMap.getLayers().get(1).isVisible());
        }
        return false;
    }

    @Override
    public boolean keyTyped(char character) {
        return false;
    }

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        if (this.prevTouch == null) {
            this.prevTouch = new Vector2(0, 0);
        }

        this.prevTouch.x = screenX;
        this.prevTouch.y = screenY;

        return false;
    }

    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        return false;
    }

    @Override
    public boolean touchDragged(int screenX, int screenY, int pointer) {
        this.camera.translate(new Vector2(this.prevTouch.x - screenX,
                screenY - this.prevTouch.y));

        this.prevTouch.x = screenX;
        this.prevTouch.y = screenY;

        return false;
    }

    @Override
    public boolean mouseMoved(int screenX, int screenY) {
        return false;
    }

    @Override
    public boolean scrolled(int amount) {
        return false;
    }
}
