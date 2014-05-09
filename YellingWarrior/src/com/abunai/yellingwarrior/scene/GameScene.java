package com.abunai.yellingwarrior.scene;

import java.util.ArrayList;
import java.util.Random;

import org.andengine.engine.camera.hud.HUD;
import org.andengine.engine.handler.timer.ITimerCallback;
import org.andengine.engine.handler.timer.TimerHandler;
import org.andengine.entity.primitive.Rectangle;
import org.andengine.entity.scene.IOnSceneTouchListener;
import org.andengine.entity.scene.Scene;
import org.andengine.entity.scene.background.Background;
import org.andengine.entity.shape.Shape;
import org.andengine.entity.text.Text;
import org.andengine.entity.text.TextOptions;
import org.andengine.extension.physics.box2d.FixedStepPhysicsWorld;
import org.andengine.extension.physics.box2d.PhysicsFactory;
import org.andengine.extension.physics.box2d.PhysicsWorld;
import org.andengine.input.sensor.acceleration.AccelerationData;
import org.andengine.input.sensor.acceleration.IAccelerationListener;
import org.andengine.input.touch.TouchEvent;
import org.andengine.util.HorizontalAlign;
import org.andengine.util.color.Color;
import org.andengine.entity.shape.IAreaShape;

import android.util.Log;

import com.abunai.yellingwarrior.base.BaseScene;
import com.abunai.yellingwarrior.manager.ResourcesManager;
import com.abunai.yellingwarrior.manager.SceneManager;
import com.abunai.yellingwarrior.manager.SceneManager.SceneType;
import com.abunai.yellingwarrior.manager.VoiceInputManager;
import com.abunai.yellingwarrior.object.Enemy;
import com.abunai.yellingwarrior.object.Player;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.Manifold;

public class GameScene extends BaseScene implements IAccelerationListener, IOnSceneTouchListener, ITimerCallback
{	
	/* The categories. */
	public static final short CATEGORYBIT_WALL = 1;
	public static final short CATEGORYBIT_GROUND = 2;
	public static final short CATEGORYBIT_PLAYER = 4;
	public static final short CATEGORYBIT_ENEMY = 8;
	public static final short CATEGORYBIT_PLAYER_HITBOX = 16;
	public static final short CATEGORYBIT_ENEMY_HITBOX = 32;

	/* And what should collide with what. */
	public static final short MASKBITS_WALL = CATEGORYBIT_ENEMY 
			+ CATEGORYBIT_PLAYER;
	public static final short MASKBITS_GROUND = CATEGORYBIT_PLAYER;
	public static final short MASKBITS_PLAYER = CATEGORYBIT_WALL
			+ CATEGORYBIT_GROUND
			+ CATEGORYBIT_PLAYER
			+ CATEGORYBIT_ENEMY
			+ CATEGORYBIT_ENEMY_HITBOX;
	public static final short MASKBITS_ENEMY = CATEGORYBIT_WALL
			+ CATEGORYBIT_ENEMY 
			+ CATEGORYBIT_PLAYER
			+ CATEGORYBIT_PLAYER_HITBOX;
	public static final short MASKBITS_PLAYER_HITBOX = CATEGORYBIT_ENEMY;
	public static final short MASKBITS_ENEMY_HITBOX = CATEGORYBIT_PLAYER;

	public static final FixtureDef WALL_FIXTURE_DEF = PhysicsFactory.createFixtureDef(0, 0, 0, false, CATEGORYBIT_WALL, MASKBITS_WALL, (short)0);
	public static final FixtureDef GROUND_FIXTURE_DEF = PhysicsFactory.createFixtureDef(0, 0, 0, false, CATEGORYBIT_GROUND, MASKBITS_GROUND, (short)0);
	public static final FixtureDef PLAYER_FIXTURE_DEF = PhysicsFactory.createFixtureDef(0, 0, 0, false, CATEGORYBIT_PLAYER, MASKBITS_PLAYER, (short)0);
	public static final FixtureDef ENEMY_FIXTURE_DEF = PhysicsFactory.createFixtureDef(0, 0, 0, false, CATEGORYBIT_ENEMY, MASKBITS_ENEMY, (short)0);
	public static final FixtureDef PLAYER_HITBOX_FIXTURE_DEF = PhysicsFactory.createFixtureDef(0, 0, 0, false, CATEGORYBIT_PLAYER_HITBOX, MASKBITS_PLAYER_HITBOX, (short)0);
	public static final FixtureDef ENEMY_HITBOX_FIXTURE_DEF = PhysicsFactory.createFixtureDef(0, 0, 0, false, CATEGORYBIT_ENEMY_HITBOX, MASKBITS_ENEMY_HITBOX, (short)0);

	
    protected PhysicsWorld physicsWorld;
	private Player player;
    private boolean gameOverDisplayed = false;
    private HUD gameHUD;
    private Text gameHUDLog1;
    private Text gameHUDLog2;
    private int score = 0;

	protected ArrayList<Enemy> enemyList = new ArrayList<Enemy>();
    TimerHandler timerHandler;

	
	@Override
	public void createScene()
	{
	    VoiceInputManager.getInstance().prepareManager();
	    VoiceInputManager.getInstance().startShout();
	    
	    createBackground();
	    createHUD();
	    createPhysics();
	    
	    setOnSceneTouchListener(this);
	    
        player = new Player(480 - ResourcesManager.PLAYER_TEXTURE_WIDTH, 480 - ResourcesManager.PLAYER_TEXTURE_HEIGHT + 10, vbom, camera, physicsWorld)
        {
        	@Override
        	public void onDie()
        	{
                //SceneManager.getInstance().loadMenuScene(engine);
        	}
        };

		this.attachChild(player);
        try {
        	engine.enableAccelerationSensor(activity, this);	
        }
        catch(Exception e) {
        	Log.e("ErrorEnableAccelerationSensor", e.toString());
        }

		timerHandler = new TimerHandler(0.1f, true, this);

	    this.registerUpdateHandler(timerHandler);
	}
	

	
    private void createBackground()
    {
    	setBackground(new Background(Color.WHITE));
    }

    private void createHUD()
    {
        gameHUD = new HUD();
        gameHUDLog1 = new Text(20, 420, resourcesManager.font, "Log1", 50, new TextOptions(HorizontalAlign.LEFT), vbom);  
        gameHUDLog1.setText("0");
        gameHUDLog2 = new Text(20, 360, resourcesManager.font, "Log2", 50, new TextOptions(HorizontalAlign.LEFT), vbom);  
        gameHUDLog2.setText("0");
        
        gameHUD.attachChild(gameHUDLog1);
        gameHUD.attachChild(gameHUDLog2);
        camera.setHUD(gameHUD);
    }
    
    private int time = 0;
	
	@Override
	public void onTimePassed(TimerHandler pTimerHandler) {
        ResourcesManager.getInstance().activity.runOnUpdateThread(new Runnable(){
        	
        
            @Override
            public void run() {
            	if (time > 0) {
            		if (time == 1) {
    		        	player.getHitBox().setActive(true);
    		        	player.setAction(Player.Action.HIT);
            		} 
            		time++;
            		
            	}
            	if (time == 4) {
            		player.setAction(Player.Action.HALT);
		        	player.getHitBox().setUserData("player_hitbox_inactive");

                	player.getHitBox().setActive(false);
            		time = 0;
            	}
            	generateEnemies();
                for (Object o : enemyList.toArray()) {
                	if (o instanceof Enemy) {
                		Enemy enemy = (Enemy) o;
                    	Body body = enemy.getBody();
                    	if (body.getUserData() == "garbage") {
                    		destroyEnemy(enemy);
                            enemyList.remove(enemy);

                    	}         	
                	}
                }
                int amp = VoiceInputManager.getInstance().getShoutSound();
                Log.e("VoiceInputManager", "amp = " + amp);
                if (amp >= 30000) {

		        	player.getHitBox().setUserData("player_hitbox_active");
		        	time = 1;
		        	
                }
            }
        });		
        
        this.sortChildren(true);
	}

    private void createPhysics()
    {
    	physicsWorld = new FixedStepPhysicsWorld(60, new Vector2(0, 0), false);
        registerUpdateHandler(physicsWorld);
        
        final Shape left = new Rectangle(0, 0, 20, ResourcesManager.CAMERA_HEIGHT, vbom);
        final Shape right = new Rectangle(ResourcesManager.CAMERA_WIDTH - 20, 0, 20, ResourcesManager.CAMERA_HEIGHT, vbom);
        final Shape bottom = new Rectangle(20, ResourcesManager.CAMERA_HEIGHT - 20, ResourcesManager.CAMERA_WIDTH - 20, ResourcesManager.CAMERA_HEIGHT, vbom);
        final Shape top = new Rectangle(20, 0, ResourcesManager.CAMERA_WIDTH - 20, 20, vbom);
        Body body;
        body = PhysicsFactory.createBoxBody(this.physicsWorld, (IAreaShape) left, BodyType.StaticBody, WALL_FIXTURE_DEF);
        body.setUserData("wall");
        body = PhysicsFactory.createBoxBody(this.physicsWorld, (IAreaShape) right, BodyType.StaticBody, WALL_FIXTURE_DEF);
        body.setUserData("wall");
        body = PhysicsFactory.createBoxBody(this.physicsWorld, (IAreaShape) bottom, BodyType.StaticBody, GROUND_FIXTURE_DEF);
        body.setUserData("ground");
        body = PhysicsFactory.createBoxBody(this.physicsWorld, (IAreaShape) top, BodyType.StaticBody, WALL_FIXTURE_DEF);
        body.setUserData("ceiling");

        this.registerUpdateHandler(this.physicsWorld);
		
		this.physicsWorld.setContactListener(new ContactListener() {

	        @Override
	        public void preSolve(Contact contact, Manifold oldManifold) {
	        }

	        @Override
	        public void postSolve(Contact contact, ContactImpulse impulse) {            
	        }

	        @Override
	        public void endContact(Contact contact) {
	        }
	        @Override
	        public void beginContact(Contact contact) {
	        	Body body1 = contact.getFixtureA().getBody();
                Body body2 = contact.getFixtureB().getBody();
               
                if (((String)body1.getUserData() == "player") && ((String)body2.getUserData() == "enemy")) {
                } else if (((String)body1.getUserData() == "enemy") && ((String)body2.getUserData() == "player")) {
                } else if (((String)body1.getUserData() == "player_hitbox_active") && ((String)body2.getUserData() == "enemy")) {
                	gameHUDLog1.setText("ENEMY_DEAD");

                	body2.setUserData("garbage");
                	score++;
                } else if (((String)body1.getUserData() == "enemy") && ((String)body2.getUserData() == "player_hitbox_active")) {
                	gameHUDLog1.setText("ENEMY_DEAD");

                	body1.setUserData("garbage");
                	score++;
                } else if (((String)body1.getUserData() == "enemy_hitbox") && ((String)body2.getUserData() == "player")) {
                	gameHUDLog1.setText("PLAYER_DEAD");
                	player.onDie();
                } else if (((String)body1.getUserData() == "player") && ((String)body2.getUserData() == "enemy_hitbox")) {
                	gameHUDLog1.setText("PLAYER_DEAD");
                	player.onDie();
                }
         		gameHUDLog2.setText("SCORE: " + score);
	        }
	    });
    }

    @Override
    public void onBackKeyPressed()
    {
        SceneManager.getInstance().loadMenuScene(engine);
    }
    
    public boolean onSceneTouchEvent(Scene pScene, TouchEvent pSceneTouchEvent)
    {
        if (pSceneTouchEvent.isActionDown())
        {    

        	player.getHitBox().setUserData("player_hitbox_active");
        	time = 1;
        }
        return false;
    }
   
    private void displayGameOverText()
    {
        gameHUDLog1.setText("GAME OVER");
        timerHandler = null;
    }

    
	@Override
	public void onAccelerationAccuracyChanged(AccelerationData pAccelerationData) {
	}

	@Override
	public void onAccelerationChanged(AccelerationData pAccelerationData) {
		player.getBody().setLinearVelocity(pAccelerationData.getX(), 0);
	}
	
	
	protected void generateEnemies() {
		int danger = 10;
		final Random generator = new Random();
		int result = generator.nextInt() % 100;
		if ((result <= danger) && (enemyList.size() < 2)) {
        	final Enemy enemy = new Enemy(Math.abs(generator.nextInt() % (ResourcesManager.CAMERA_WIDTH - ResourcesManager.ENEMY_TEXTURE_WIDTH)), 0, vbom, camera, physicsWorld) {
        		@Override
        		public void onDie() {
        			this.getBody().setUserData("garbage");
        		}
        	};
        	attachChild(enemy);
            enemy.setCullingEnabled(true);
  			enemyList.add(enemy);
		}
	}
	
    @Override
    public SceneType getSceneType()
    {
        return SceneType.SCENE_GAME;
    }

	private void destroyEnemy(final Enemy enemy)
    {
            physicsWorld.unregisterPhysicsConnector(physicsWorld.getPhysicsConnectorManager().findPhysicsConnectorByShape(enemy));
            physicsWorld.destroyBody(enemy.getBody());
            physicsWorld.destroyBody(enemy.getHitBox());
            enemy.detachSelf();
    }	
	
    @Override
    public void disposeScene()
    {
    	VoiceInputManager.getInstance().stopShout();
        camera.setHUD(null);
        camera.setCenter(400, 240);
        camera.setChaseEntity(null);
        ResourcesManager.getInstance().activity.runOnUpdateThread(new Runnable(){

            @Override
            public void run() {
                for (Object o : enemyList.toArray()) {
                	if (o instanceof Enemy) {
                		Enemy enemy = (Enemy) o;
                    	Body body = enemy.getBody();
                    	if (body.getUserData() == "garbage") {
                    		destroyEnemy(enemy);
                            enemyList.remove(enemy);	                    		
                    	}              	
                	}
                }
            }
        });
        System.gc();
    }


}