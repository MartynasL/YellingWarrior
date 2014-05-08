package com.abunai.yellingwarrior.object;

import org.andengine.engine.camera.BoundCamera;
import org.andengine.entity.sprite.AnimatedSprite;
import org.andengine.extension.physics.box2d.PhysicsConnector;
import org.andengine.extension.physics.box2d.PhysicsFactory;
import org.andengine.extension.physics.box2d.PhysicsWorld;
import org.andengine.opengl.vbo.VertexBufferObjectManager;

import android.util.Log;

import com.abunai.yellingwarrior.manager.ResourcesManager;
import com.abunai.yellingwarrior.manager.VoiceInputManager;
import com.abunai.yellingwarrior.scene.GameScene;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;

public abstract class Player extends AnimatedSprite
{
    // ---------------------------------------------
    // CONSTRUCTOR
    // ---------------------------------------------
    
	public Player(float pX, float pY, VertexBufferObjectManager vbo, BoundCamera camera, PhysicsWorld physicsWorld)
	{
	    super(pX, pY, ResourcesManager.getInstance().player_region, vbo);
	    createPhysics(camera, physicsWorld);
	    currentAction = Player.Action.HALT;
	    setAction(currentAction);
	}
    
	
    // ---------------------------------------------
    // VARIABLES
    // ---------------------------------------------
     
    private Body body;
    private Body hitBox;
    private Player.Action currentAction;
    private int speed = 5;
    
    public abstract void onDie();
    
    private void createPhysics(final BoundCamera camera, PhysicsWorld physicsWorld)
    {        
    	//int ptm = 32;
    	
    	this.setZIndex(1);
    	
       	hitBox = PhysicsFactory.createCircleBody(
       			physicsWorld, 
       			this.getX() + 50,
       			this.getY() + 75,
       			75,
       			BodyType.DynamicBody, 
       			GameScene.PLAYER_HITBOX_FIXTURE_DEF);
   	
        hitBox.setUserData("player_hitbox_inactive");
        hitBox.setActive(false);
        hitBox.setFixedRotation(true); 
 
        body = PhysicsFactory.createCircleBody(
    			physicsWorld,  
       			this.getX() + 50,
       			this.getY() + 75,
    			50, 
    			BodyType.DynamicBody, 
    			GameScene.PLAYER_FIXTURE_DEF);
	
        body.setUserData("player");
        body.setFixedRotation(true);
        physicsWorld.registerPhysicsConnector(new PhysicsConnector(this, this.body, true, false)
        {
        	@Override
            public void onUpdate(float pSecondsElapsed)
            {
                super.onUpdate(pSecondsElapsed);
                
                camera.onUpdate(0.1f);
                           
                hitBox.setTransform(body.getPosition().x, body.getPosition().y, 0);
            }
        });
        
    }

    public enum Action {
    	HALT, HIT
    }
	
    public void setAction(Player.Action action)
    {
    	switch (action) {
    		case HALT:
    			setActionHalt();
    			break;
    		case HIT:
    			setActionHit();
    			break;
    	}
    }
    
    private void setActionHit() {
    	body.setLinearVelocity(new Vector2(0, body.getLinearVelocity().y));
		
		final long[] pFrameDurations = new long[] {75, 50, 50, 75};
        animate(pFrameDurations, 12, 15, false);
    }

	private void setActionHalt() {
		body.setLinearVelocity(new Vector2(0, body.getLinearVelocity().y));
		
		final long[] pFrameDurations = new long[] {200, 200, 200, 200};
        animate(pFrameDurations, 0, 3, true);
	}
	
	public Action getAction() {
		return currentAction;
	}
	public void setSpeed(int speed) {
		this.speed = speed;
	}
	
	public Body getBody() {
		return body;
	}
	
	public Body getHitBox() {
		return hitBox;
	}
}