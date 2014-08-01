package com.wilmot.game;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Vector2;

public class TiledGame extends ApplicationAdapter implements InputProcessor {

    private TiledMap tiledMap;
    private OrthographicCamera camera;
    private OrthogonalTiledMapRenderer tiledMapRenderer;

    private Vector2 mPrevTouch;

    private float screenWidth;
    private float screenHeight;

    private float mapWidth;
    private float mapHeight;

    @Override
    public void create () {
        screenWidth = Gdx.graphics.getWidth();
        screenHeight = Gdx.graphics.getHeight();

        camera = new OrthographicCamera(screenWidth, screenHeight);
        camera.position.set(screenWidth / 2, screenHeight / 2, 0);
        camera.update();

        // Load map
        tiledMap = new TmxMapLoader().load("tile_map_1.tmx");
        tiledMapRenderer = new OrthogonalTiledMapRenderer(tiledMap, 2f);


        // This is a hack to find how big the tiled map is.
        final TiledMapTileLayer layer = (TiledMapTileLayer) tiledMap.getLayers().get(0);
        final int layerWidth = layer.getWidth();
        final int layerHeight = layer.getHeight();

        final float layerTileWidth = layer.getTileWidth() * tiledMapRenderer.getUnitScale();
        final float layerTileHeight = layer.getTileHeight() * tiledMapRenderer.getUnitScale ();

        mapWidth = layerWidth * layerTileWidth;
        mapHeight = layerHeight * layerTileHeight;

        Gdx.input.setInputProcessor(this);
    }

    @Override
    public void render () {
        Gdx.gl.glClearColor(1, 0, 0, 1);
        Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        /**
         * Ensure that the camera is only showing the map, nothing outside.
         */
        if (camera.position.x < screenWidth / 2) {
            camera.position.x = screenWidth / 2;
        }

        if (camera.position.x >= mapWidth - screenWidth / 2) {
            camera.position.x = mapWidth - screenWidth / 2;
        }

        if (camera.position.y < screenHeight / 2) {
            camera.position.y = screenHeight / 2;
        }

        if (camera.position.y >= mapHeight - screenHeight / 2) {
            camera.position.y = mapHeight - screenHeight / 2;
        }

        camera.update();
        tiledMapRenderer.setView(camera);
        tiledMapRenderer.render();
    }

    @Override
    public void resize (int width, int height) {
//        camera.viewportHeight = width;
//        camera.viewportWidth = height;
//        camera.update();
    }
    @Override
    public boolean keyDown(int keycode) {
        return false;
    }

    @Override
    public boolean keyUp(int keycode) {
        if(keycode == Input.Keys.LEFT)
            camera.translate(-32,0);
        if(keycode == Input.Keys.RIGHT)
            camera.translate(32,0);
        if(keycode == Input.Keys.UP)
            camera.translate(0,32);
        if(keycode == Input.Keys.DOWN)
            camera.translate(0,-32);
        if(keycode == Input.Keys.NUM_1)
            tiledMap.getLayers().get(0).setVisible(!tiledMap.getLayers().get(0).isVisible());
        if(keycode == Input.Keys.NUM_2)
            tiledMap.getLayers().get(1).setVisible(!tiledMap.getLayers().get(1).isVisible());
        return false;
    }

    @Override
    public boolean keyTyped(char character) {
        return false;
    }

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        if (mPrevTouch == null) {
            mPrevTouch = new Vector2(0, 0);
        }

        mPrevTouch.x = screenX;
        mPrevTouch.y = screenY;

        return true;
    }

    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        return false;
    }

    @Override
    public boolean touchDragged(int screenX, int screenY, int pointer) {
        camera.translate(new Vector2(mPrevTouch.x - screenX , screenY - mPrevTouch.y));

        mPrevTouch.x = screenX;
        mPrevTouch.y = screenY;

        return true;
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