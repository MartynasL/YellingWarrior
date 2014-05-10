package com.abunai.yellingwarrior.scene;

import java.util.Random;

import org.andengine.engine.camera.hud.HUD;
import org.andengine.entity.primitive.Rectangle;
import org.andengine.entity.shape.IAreaShape;
import org.andengine.entity.shape.Shape;
import org.andengine.entity.text.Text;
import org.andengine.entity.text.TextOptions;
import org.andengine.extension.physics.box2d.FixedStepPhysicsWorld;
import org.andengine.extension.physics.box2d.PhysicsFactory;
import org.andengine.util.HorizontalAlign;

import com.abunai.yellingwarrior.manager.ResourcesManager;
import com.abunai.yellingwarrior.object.Enemy;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.Manifold;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;

public class LygiuRezimas extends GameScene
{
	private Lygis currentLevel;
	private static int statistics;
	private int deaths = 0;
    private Text gameHUDLog3;
	
	
	public void setCurrentLevel(Lygis level)
	{
		currentLevel = level;
	}
	
	public Lygis getCurrentLevel()
	{
		return currentLevel;
	}
	
	public static int getStatistics()
	{
		return statistics;
	}
	
	@Override
    protected void createHUD()
    {
        gameHUD = new HUD();
        gameHUDLog1 = new Text(20, 420, resourcesManager.font, "Log1", 50, new TextOptions(HorizontalAlign.LEFT), vbom);  
        gameHUDLog1.setText("0");
        gameHUDLog2 = new Text(20, 360, resourcesManager.font, "Log2", 50, new TextOptions(HorizontalAlign.LEFT), vbom);  
        gameHUDLog2.setText("0");
        gameHUDLog3 = new Text(20, 0, resourcesManager.font, "Log2", 50, new TextOptions(HorizontalAlign.LEFT), vbom);  
        gameHUDLog3.setText("LEVEL 1");
        
        gameHUD.attachChild(gameHUDLog1);
        gameHUD.attachChild(gameHUDLog2);
        gameHUD.attachChild(gameHUDLog3);
        camera.setHUD(gameHUD);
    }
	
	@Override	
    protected void createPhysics()
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
                	deaths++;
                } else if (((String)body1.getUserData() == "player") && ((String)body2.getUserData() == "enemy_hitbox")) {
                	gameHUDLog1.setText("PLAYER_DEAD");
                	player.onDie();
                	deaths++;
                }
         		gameHUDLog2.setText("SCORE: " + score);
         		if (score > statistics)
         		{
         			statistics = score;
         		}
         		
         		switch(currentLevel.getDifficulty())
         		{
         			case 1:
         				if(score == 10)
         				{
         					setCurrentLevel(new Lygis(2));
         					score = 0;
         					deaths = 0;
         					gameHUDLog3.setText("LEVEL 2");
         					gameHUDLog2.setText("SCORE: " + score);
         				}
         				break;
         			case 2:
         				if(score == 10)
         				{
         					setCurrentLevel(new Lygis(3));
         					score = 0;
         					deaths = 0;
         					gameHUDLog3.setText("LEVEL 3");
         					gameHUDLog2.setText("SCORE: " + score);
         				}
         				break;
         			case 3:
         				if(score == 0 && deaths == 5)
         				{
         					setCurrentLevel(new Lygis(2));
         					score = 0;
         					deaths = 0;
         					gameHUDLog3.setText("LEVEL 2");
         					gameHUDLog2.setText("SCORE: " + score);
         				}
         				break;
         		}
	        }
	    });
    }
	
	@Override
	protected void generateEnemies() {
		int danger = 10;
		final Random generator = new Random();
		int result = generator.nextInt() % 100;
		if ((result <= danger) && (enemyList.size() < currentLevel.getEnemyNumber())) {
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
}
