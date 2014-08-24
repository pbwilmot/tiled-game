package com.wilmot.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.Plane;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.math.collision.Ray;

/**
 * Created by pbwilmot on 7/31/14.
 */
public class GameScreen implements Screen, InputProcessor {

    private final AdventureGame game;
    private final OrthographicCamera camera;
    private final OrthogonalTiledMapRenderer tiledMapRenderer;
    private final TiledMap tiledMap;

    // in pixels
    private float screenWidth;
    private float screenHeight;

    // in tiles
    private int mapWidth;
    private int mapHeight;
    
    // in pixels
    private float mapPixelWidth;
    private float mapPixelHeight;
    
    // tiles dimensions in pixels
    private float mapTileWidth;
    private float mapTileHeight;

    private SpriteCharacter character;
//    private Sprite sprite;

    private TextureRegion[] downFrames, upFrames, leftFrames, rightFrames;
    private Animation downAnimation, upAnimation, leftAnimation, rightAnimation;
    private float elapsedTime = 0;
    // For Testing
    ShapeRenderer renderer;

    public GameScreen(final AdventureGame game) {
    	renderer = new ShapeRenderer();
    	
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
        this.tiledMapRenderer = new OrthogonalTiledMapRenderer(this.tiledMap, 1.5f, this.game.getBatch());

        // Get map size
        final TiledMapTileLayer layer = (TiledMapTileLayer) this.tiledMap.getLayers().get(0);
        this.mapWidth = layer.getWidth();
        this.mapHeight = layer.getHeight();
        this.mapTileWidth = layer.getTileWidth() * this.tiledMapRenderer.getUnitScale();
        this.mapTileHeight = layer.getTileHeight() * this.tiledMapRenderer.getUnitScale();
        
//        final float layerTileWidth = this.mapTileWidth * this.tiledMapRenderer.getUnitScale();
//        final float layerTileHeight = this.mapTileHeight * this.tiledMapRenderer.getUnitScale();

        this.mapPixelWidth = mapWidth * this.mapTileWidth;
        this.mapPixelHeight = mapHeight * this.mapTileWidth;
        

        // Handle user inputs
        Gdx.input.setInputProcessor(this);

        DwarfSoldier soldier = new DwarfSoldier();

        // Load Sprites
        final Texture walkSheet = new Texture(Gdx.files.internal("ashes_of_immortality_2_sprites.png"));
        final int cols = 12;
        final int rows = 8;
        final int elementWidth = walkSheet.getWidth()/cols;
        final int elementHeight = walkSheet.getHeight()/rows;
        final TextureRegion[][] tmp = TextureRegion.split(walkSheet, elementWidth, elementHeight);

        Sprite sprite = new Sprite(tmp[0][1].getTexture(), elementWidth, elementHeight);
        sprite.setPosition(300, 300);
        this.character = new SpriteCharacter(sprite);

        // walk down - row 1
        downFrames = new TextureRegion[3];
        downFrames[0] = tmp[0][0];
        downFrames[1] = tmp[0][1];
        downFrames[2] = tmp[0][2];
        // walk left - row 2
        leftFrames = new TextureRegion[3];
        leftFrames[0] = tmp[1][0];
        leftFrames[1] = tmp[1][1];
        leftFrames[2] = tmp[1][2];
        // walk right - row 3
        rightFrames = new TextureRegion[3];
        rightFrames[0] = tmp[2][0];
        rightFrames[1] = tmp[2][1];
        rightFrames[2] = tmp[2][2];
        // walk up - row 4
        upFrames = new TextureRegion[3];
        upFrames[0] = tmp[3][0];
        upFrames[1] = tmp[3][1];
        upFrames[2] = tmp[3][2];

        final float animationSpeed = 0.1f;
        downAnimation = new Animation(animationSpeed, downFrames);
        upAnimation = new Animation(animationSpeed, upFrames);
        leftAnimation = new Animation(animationSpeed, leftFrames);
        rightAnimation = new Animation(animationSpeed, rightFrames);

        this.character.setDownAnimation(downAnimation);
        this.character.setUpAnimation(upAnimation);
        this.character.setLeftAnimation(leftAnimation);
        this.character.setRightAnimation(rightAnimation);
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

        this.game.getBatch().begin();
        this.elapsedTime += delta;
        
        this.character.draw(this.game.getBatch(), delta);
        this.game.getBatch().end();
        
        this.renderer.setProjectionMatrix(camera.combined);
		this.renderer.begin(ShapeType.Line);
		renderer.setColor(1, 0, 0, 1);
		// width
		for (int i = 0; i < this.mapWidth; i++) {
			renderer.line(i * mapTileWidth, 0, i * mapTileWidth, mapPixelHeight);
		}
		
		for (int j = 0; j < this.mapHeight; j++) {
			renderer.line(0, j * mapTileHeight, mapPixelWidth, j * mapTileHeight);
		}
		
		renderer.end();
    }

    /**
     * Ensure that the camera is only showing the map, nothing outside.
     */
    // TODO (pwilmot) this can be optimized since these are all constants
    private void validateMove() {
        if (this.camera.position.x < this.screenWidth / 2) {
            this.camera.position.x = this.screenWidth / 2;
        }

        if (this.camera.position.x >= this.mapPixelWidth - this.screenWidth / 2) {
            this.camera.position.x = this.mapPixelWidth - this.screenWidth / 2;
        }

        if (this.camera.position.y < this.screenHeight / 2) {
            this.camera.position.y = this.screenHeight / 2;
        }

        if (this.camera.position.y >= this.mapPixelHeight - this.screenHeight / 2) {
            this.camera.position.y = this.mapPixelHeight - this.screenHeight / 2;
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
		renderer.dispose();
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

    
    private Vector2 getTileCenter(int x, int y) {
    	System.out.println("Tile x: " + x + ", y: " + y);
    	float pixX = (mapTileWidth * x) + (mapTileWidth / 2);
    	float pixY = (mapTileHeight * y) + (mapTileHeight / 2);
    	System.out.println("Tile pix x: " + pixX + ", y: " + pixY);
    	
    	return new Vector2(pixX, pixY);
    }
    
    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        // Move sprite
        Vector3 clickCoordinates = new Vector3(screenX,screenY,0);
        Vector3 position = camera.unproject(clickCoordinates);
        int x = (int) Math.floor((int)position.x / this.mapTileWidth);
        int y = (int)Math.floor((int)position.y / this.mapTileHeight);
        // make sure a valid tile was clicked
        if (x >= 0 && x < this.mapWidth && y >= 0 && y < mapHeight) {
        	// TODO(pwilmot) make sure this is a walkable tile - check for collisions        	
        	this.character.setDestination(getTileCenter(x,y));
        }
        return true;
    }


    final Plane xyPlane = new Plane(new Vector3(0, 0, 1), 0);
    final Vector3 intersection = new Vector3();
    final Vector3 curr = new Vector3();
    final Vector3 last = new Vector3(-1, -1, -1);
    final Vector3 delta = new Vector3();
    
    @Override
    public boolean touchDragged(int screenX, int screenY, int pointer) {
    	Ray pickRay = camera.getPickRay(screenX, screenY);
        Intersector.intersectRayPlane(pickRay, xyPlane, curr);
        if (!(last.x == -1 && last.y == -1 && last.z == -1)) {
                pickRay = camera.getPickRay(last.x, last.y);
                Intersector.intersectRayPlane(pickRay, xyPlane, delta);
                delta.sub(curr);
                camera.position.add(delta.x, delta.y, 0);
        }
        last.set(screenX, screenY, 0);
        return true;
    }
    
    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
    	last.set(-1, -1, -1);
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
