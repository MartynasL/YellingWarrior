package com.abunai.yellingwarrior.object;

import org.andengine.engine.camera.BoundCamera;
import org.andengine.entity.sprite.AnimatedSprite;
import org.andengine.extension.physics.box2d.PhysicsConnector;
import org.andengine.extension.physics.box2d.PhysicsFactory;
import org.andengine.extension.physics.box2d.PhysicsWorld;
import org.andengine.opengl.vbo.VertexBufferObjectManager;

import com.abunai.yellingwarrior.manager.ResourcesManager;
import com.abunai.yellingwarrior.scene.GameScene;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;

abstract public class Enemy extends AnimatedSprite {
	    // ---------------------------------------------
	    // CONSTRUCTOR
	    // ---------------------------------------------
	    
		public Enemy(float pX, float pY, VertexBufferObjectManager vbo, BoundCamera camera, PhysicsWorld physicsWorld)
		{
		    super(pX, pY, ResourcesManager.getInstance().enemy_region, vbo);
		    createPhysics(camera, physicsWorld);
		    currentAction = Enemy.Action.RUN_DOWN;
		    setAction(currentAction);
		}
	    
		
	    // ---------------------------------------------
	    // VARIABLES
	    // ---------------------------------------------
	     
	    private Body body;
	    private Body hitBox;
	    private Enemy.Action currentAction;
	    private int speed = 5;
	    
	    abstract public void onDie();
	    
	    private void createPhysics(final BoundCamera camera, PhysicsWorld physicsWorld)
	    {        
	    	///final int ptm = 32;
	    	
	    	this.setZIndex(0);
	    	
	    	hitBox = PhysicsFactory.createCircleBody(physicsWorld, 
	       			this.getX() + 50,
	       			this.getY() + 75,
		        	50,
		        	BodyType.DynamicBody, 
		        	GameScene.ENEMY_HITBOX_FIXTURE_DEF);
	       	hitBox.setUserData("enemy_hitbox");
	       	hitBox.setFixedRotation(true);
		     
	        
	        body = PhysicsFactory.createCircleBody(
	        		physicsWorld,  
	       			this.getX() + 100,
	       			this.getY() + 75,
	        		50,
	        		BodyType.DynamicBody, 
	        		GameScene.ENEMY_FIXTURE_DEF);
	        body.setUserData("enemy");
	        body.setFixedRotation(true);
	        
	       
	        
	        physicsWorld.registerPhysicsConnector(new PhysicsConnector(this, body, true, false)
	        {
	            @Override
	            public void onUpdate(float pSecondsElapsed)
	            {
	                super.onUpdate(pSecondsElapsed);
	                camera.onUpdate(0.1f);
	                
	                if (getY() >= ResourcesManager.CAMERA_HEIGHT)
	                {                    
	                    onDie();
	                } else {
	                	body.setLinearVelocity(body.getLinearVelocity().x, speed);
	                }
	                hitBox.setTransform(body.getPosition().x, body.getPosition().y, 0);
	            }
	        });
	    }

	    public enum Action {
	    	HALT, RUN_LEFT, RUN_RIGHT, RUN_DOWN
	    }
		
	    public void setAction(Enemy.Action action)
	    {
	    	switch (action) {
	    		case HALT:
	    			setActionHalt();
	    			break;
	    		case RUN_LEFT:
	    			setActionRunLeft();
	    			break;
	    		case RUN_RIGHT:
	    			setActionRunRight();
	    			break;
	    		case RUN_DOWN:
	    			setActionRunDown();
	    			break;
	    	}
	    }

	    /* not working */
		private void setActionRunRight() {
			body.setLinearVelocity(new Vector2(speed, body.getLinearVelocity().y));
			
			final long[] pFrameDurations = new long[] {100, 100};
	        
	        // void animate(final long[] pFrameDurations, final int pFirstTileIndex, final int pLastTileIndex, final boolean pLoop)
	        animate(pFrameDurations, 0, 1, true);
		}

		/* not working */
		private void setActionRunLeft() {
			body.setLinearVelocity(new Vector2(-speed, body.getLinearVelocity().y));
			
			final long[] pFrameDurations = new long[] {100, 100};
	        
	        // void animate(final long[] pFrameDurations, final int pFirstTileIndex, final int pLastTileIndex, final boolean pLoop)
	        animate(pFrameDurations, 0, 1, true);
		}
		
		private void setActionRunDown() {
			body.setLinearVelocity(new Vector2(0, speed));
			
			final long[] pFrameDurations = new long[] {200, 200, 200, 200};
	        
	        // void animate(final long[] pFrameDurations, final int pFirstTileIndex, final int pLastTileIndex, final boolean pLoop)
	        animate(pFrameDurations, 0, 3, true);
		}
		
		 /* not working */
		private void setActionHalt() {
			body.setLinearVelocity(new Vector2(0, 0));
			
	        stopAnimation();
		}
		
		public void setSpeed(int acceleration) {
			speed = acceleration;
		}
		
		public Body getBody() {
			return body;
		}
		
		public Body getHitBox() {
			return hitBox;
		}
	}