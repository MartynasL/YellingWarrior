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

public class MainMenuScene extends BaseScene implements IOnMenuItemClickListener  {

	@Override
	public SceneType getSceneType()
	{
	    return SceneType.SCENE_MENU;
	}
	
	@Override
	public void onBackKeyPressed()
	{
	    System.exit(0);
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
	public void createScene()
	{
	    createBackground();
	    createMenuChildScene();
	}
	
	private MenuScene menuChildScene;
	private final int MENU_PLAY = 0;
	private final int MENU_OPTIONS = 1;
	private final int MENU_ATSILIEPIMAI = 2;
	private final int MENU_PARDUOTUVE = 3;
	private final int MENU_REZULTATAI = 4;
	private final int MENU_ISEITI = 5;

	public boolean onMenuItemClicked(MenuScene pMenuScene, IMenuItem pMenuItem, float pMenuItemLocalX, float pMenuItemLocalY)
	{
	    switch(pMenuItem.getID())
	    {
	        case MENU_PLAY:
	            //Load Game Scene!
//	            SceneManager.getInstance().loadGameScene(engine);
	        	SceneManager.getInstance().loadPasirinktiRezimaScene(engine);
	        	return true;
	        case MENU_OPTIONS:
	            return true;
	        case MENU_ATSILIEPIMAI:
	            return true;
	        case MENU_PARDUOTUVE:
	            return true;
	        case MENU_REZULTATAI:
	        	SceneManager.getInstance().loadRezultataiScene(engine);
	            return true;
	        case MENU_ISEITI:
	        	System.exit(0);
	        default:
	            return false;
	    }
	}

	
	@Override
	public void disposeScene() {
		// TODO Auto-generated method stub
		
	}
	
	private void createMenuChildScene()
	{
	    menuChildScene = new MenuScene(camera);
	    menuChildScene.setPosition(0, 0);
	    
	    final IMenuItem playMenuItem = new ScaleMenuItemDecorator(new SpriteMenuItem(MENU_PLAY, resourcesManager.play_region, vbom), 1.2f, 1);
	    final IMenuItem optionsMenuItem = new ScaleMenuItemDecorator(new SpriteMenuItem(MENU_OPTIONS, resourcesManager.options_region, vbom), 1.2f, 1);
	    final IMenuItem atsiliepimaiMenuItem = new ScaleMenuItemDecorator(new SpriteMenuItem(MENU_ATSILIEPIMAI, resourcesManager.atsiliepimai_region, vbom), 1.2f, 1);
	    final IMenuItem parduotuveMenuItem = new ScaleMenuItemDecorator(new SpriteMenuItem(MENU_PARDUOTUVE, resourcesManager.parduotuve_region, vbom), 1.2f, 1);
	    final IMenuItem rezultataiMenuItem = new ScaleMenuItemDecorator(new SpriteMenuItem(MENU_REZULTATAI, resourcesManager.rezultatai_region, vbom), 1.2f, 1);
	    final IMenuItem iseitiMenuItem = new ScaleMenuItemDecorator(new SpriteMenuItem(MENU_ISEITI, resourcesManager.iseiti_region, vbom), 1.2f, 1);
	    
	    menuChildScene.addMenuItem(playMenuItem);
	    menuChildScene.addMenuItem(optionsMenuItem);
	    menuChildScene.addMenuItem(atsiliepimaiMenuItem);
	    menuChildScene.addMenuItem(parduotuveMenuItem);
	    menuChildScene.addMenuItem(rezultataiMenuItem);
	    menuChildScene.addMenuItem(iseitiMenuItem);
	    
	    menuChildScene.buildAnimations();
	    menuChildScene.setBackgroundEnabled(false);
	    
	   // playMenuItem.setPosition(playMenuItem.getX(), playMenuItem.getY() - 110);
	   // optionsMenuItem.setPosition(optionsMenuItem.getX(), optionsMenuItem.getY() + 10);
	    
	    menuChildScene.setOnMenuItemClickListener(this);
	    
	    setChildScene(menuChildScene);
	}
}
