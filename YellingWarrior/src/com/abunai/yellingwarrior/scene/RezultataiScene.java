package com.abunai.yellingwarrior.scene;

import org.andengine.entity.scene.menu.MenuScene;
import org.andengine.entity.scene.menu.MenuScene.IOnMenuItemClickListener;
import org.andengine.entity.scene.menu.item.IMenuItem;
import org.andengine.entity.scene.menu.item.SpriteMenuItem;
import org.andengine.entity.scene.menu.item.decorator.ScaleMenuItemDecorator;
import org.andengine.entity.sprite.Sprite;
import org.andengine.opengl.util.GLState;
import org.andengine.engine.camera.Camera;

import com.abunai.yellingwarrior.base.BaseScene;
import com.abunai.yellingwarrior.manager.SceneManager;
import com.abunai.yellingwarrior.manager.SceneManager.SceneType;

public class RezultataiScene extends BaseScene implements IOnMenuItemClickListener  {

	@Override
	public SceneType getSceneType()
	{
	    return SceneType.SCENE_MENU;
	}
	
	@Override
	public void onBackKeyPressed()
	{
		SceneManager.getInstance().loadMenuScene(engine);
	}
	
	private void createBackground()
	{
	    attachChild(new Sprite(0, 0, resourcesManager.menu_background_region, vbom)
	    {
	        @Override
	        protected void preDraw(GLState pGLState, Camera pCamera) 
	        {
	            super.preDraw(pGLState, pCamera);
	            pGLState.enableDither();
	        }
	    });
	}

	@Override
	public boolean onMenuItemClicked(MenuScene pMenuScene, IMenuItem pMenuItem,
			float pMenuItemLocalX, float pMenuItemLocalY) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void createScene() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void disposeScene() {
		// TODO Auto-generated method stub
		
	}
}