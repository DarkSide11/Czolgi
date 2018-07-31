package com.czolgi.game;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.utils.Logger;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

import aurelienribon.bodyeditor.BodyEditorLoader;

public class Czolgi extends ApplicationAdapter {

	private static final float SCENE_WIDTH = 12.80f; // 12.8 metres wide
	private static final float SCENE_HEIGHT = 7.20f; // 7.2 metres high

	public Viewport viewport;
	public Vector3 point = new Vector3();
	Texture groundTex;
	// General Box2D

	Box2DDebugRenderer debugRenderer;
	BodyDef defaultDynamicBodyDef;
	World world;

	Body groundBody;

	SpriteBatch batch;
	Texture img;
	public int imgXpos;
	public int imgYpos;
	public Logger logger = new Logger("TAG", Logger.DEBUG);

	// Ball
	CircleShape circle, circle2, circle3;
	FixtureDef circleFixtureDef, circleFixtureDef2, circleFixtureDef3;

	@Override
	public void create() {

		TankInputAdapter inputProcessor = new TankInputAdapter(this);
		
		groundTex = new Texture(Gdx.files.internal("data/box2D/Podloze.png"));
		viewport = new FitViewport(SCENE_WIDTH, SCENE_HEIGHT);
		// Center camera to get (0,0) as the origin of the Box2D world
		viewport.getCamera().position.set(viewport.getCamera().position.x + SCENE_WIDTH * 0.5f,
				viewport.getCamera().position.y + SCENE_HEIGHT * 0.5f, 0);
		viewport.getCamera().update();

		batch = new SpriteBatch();

		// Create Physics World
		world = new World(new Vector2(0, -10), true);
		Gdx.input.setInputProcessor(inputProcessor);
		// Instantiate DebugRenderer for rendering shapes
		debugRenderer = new Box2DDebugRenderer();

		// Creates a ground to avoid objects falling forever
		createGround();

		// Default Body Definition
		defaultDynamicBodyDef = new BodyDef();
		defaultDynamicBodyDef.type = BodyType.DynamicBody;

		batch = new SpriteBatch();
		

		// Shape for circles
		circle = new CircleShape();
		// 0.1 meter for radius
		circle.setRadius(0.1f);
		// Fixture definition for our shapes
		circleFixtureDef = new FixtureDef();
		circleFixtureDef.shape = circle;
		circleFixtureDef.density = 0.8f;
		circleFixtureDef.friction = 0.4f;
		circleFixtureDef.restitution = 0.5f;

	}

	@Override
	public void render() {

		Gdx.gl.glClearColor(0, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		world.step(1 / 60f, 6, 2);
		
		
		batch.begin();
		batch.setProjectionMatrix(viewport.getCamera().combined);
//		batch.draw(
//				groundTex, 
//				groundBody.getPosition().x , groundBody.getPosition().y,
//				SCENE_WIDTH/2, SCENE_HEIGHT/2, 
//				SCENE_WIDTH, SCENE_HEIGHT, 
//				1f, 1f, 
//				0, 
//				0, 0, 
//				groundTex.getWidth(), groundTex.getHeight(), 
//				false, false);
		
		
		batch.draw(
				groundTex, 
				groundBody.getPosition().x - (SCENE_WIDTH*.5f), groundBody.getPosition().y - (SCENE_HEIGHT*.5f),
				SCENE_WIDTH*.5f, SCENE_HEIGHT*.5f, 
				SCENE_WIDTH, SCENE_HEIGHT, 
				1f, 1f, 
				groundBody.getAngle() * MathUtils.radDeg, 
				0, 0, 
				groundTex.getWidth(), groundTex.getHeight(), 
				false, false);
		batch.end();
		
		debugRenderer.render(world, viewport.getCamera().combined);

		
	}
	@Override
	public void resize(int width, int height) {
		viewport.update(width, height);
	}


	@Override
	public void dispose() {
		batch.dispose();
		img.dispose();
		debugRenderer.dispose();
		circle.dispose();
		world.dispose();

	}

	private void createGround() {
		// Instantiate the loader with the created JSON data
		BodyEditorLoader loader = new BodyEditorLoader(Gdx.files.internal("data/box2D/MapaCzolgi.json"));

		// Create the glass body definition and place it in within the world
		BodyDef bd = new BodyDef();
		bd.type = BodyType.StaticBody;
		bd.position.set(0, 0f);

		// Set physics properties
		FixtureDef fd = new FixtureDef();
		fd.density = 1;
		fd.friction = 0.5f;
		fd.restitution = 0.05f;

		// Create the glass body
		groundBody = world.createBody(bd);

		// Magic happens here!! Glass fixture is generated automatically by the loader.
		loader.attachFixture(groundBody, "PoglozeCzolgi", fd,SCENE_WIDTH );
	}

	public void createCircle(float x, float y) {
		defaultDynamicBodyDef.position.set(x, y);

		Body body = world.createBody(defaultDynamicBodyDef);

		body.createFixture(circleFixtureDef);
	}

}