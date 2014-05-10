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

public class PasirinktiRezimaScene extends BaseScene implements IOnMenuItemClickListener {

	@Override
	public void createScene() 
	{
		createBackground();
		createPasirinktiRezimaChildScene();
		
	}
	
	private void createBackground()
	{
	    attachChild(new Sprite(0, 0, resourcesManager.background_region, vbom)
	    {
	        @Override
	        protected void preDraw(GLState pGLState, Camera pCamera) 
	        {
	            super.preDraw(pGLState, pCamera);
	            pGLState.enableDither();
	        }
	    });
	}
	
	private MenuScene pasirinktiRezimaChildScene;
	private final int MENU_ATGAL = 0;
	private final int LYGIU_REZIMAS = 1;
	private final int ISLIKIMO_REZIMAS = 2;
	
	private void createPasirinktiRezimaChildScene()
	{
		pasirinktiRezimaChildScene = new MenuScene(camera);
		pasirinktiRezimaChildScene.setPosition(0, 0);
		
		final IMenuItem lygiuRezimasMenuItem = new ScaleMenuItemDecorator(new SpriteMenuItem(LYGIU_REZIMAS, resourcesManager.play_lygiai_region, vbom), 1.2f, 1);
		pasirinktiRezimaChildScene.addMenuItem(lygiuRezimasMenuItem);
	    final IMenuItem islikimoRezimasMenuItem = new ScaleMenuItemDecorator(new SpriteMenuItem(ISLIKIMO_REZIMAS, resourcesManager.play_islikimas_region, vbom), 1.2f, 1);
		pasirinktiRezimaChildScene.addMenuItem(islikimoRezimasMenuItem);		
	    final IMenuItem atgalMenuItem = new ScaleMenuItemDecorator(new SpriteMenuItem(MENU_ATGAL, resourcesManager.atgal_region, vbom), 1.2f, 1);
	    pasirinktiRezimaChildScene.addMenuItem(atgalMenuItem);
	    
	    pasirinktiRezimaChildScene.buildAnimations();
	    pasirinktiRezimaChildScene.setBackgroundEnabled(false);
	    
	    islikimoRezimasMenuItem.setPosition(islikimoRezimasMenuItem.getX(), islikimoRezimasMenuItem.getY()+20);
	    atgalMenuItem.setPosition(atgalMenuItem.getX() - 250, atgalMenuItem.getY() + 150);
	    
	    pasirinktiRezimaChildScene.setOnMenuItemClickListener(this);	    
	    setChildScene(pasirinktiRezimaChildScene);
	}

	@Override
	public void onBackKeyPressed() 
	{
		SceneManager.getInstance().loadMenuScene(engine);	
	}

	@Override
	public SceneType getSceneType() 
	{
		return SceneType.SCENE_PASIRINKTI_REZIMA;
	}

	@Override
	public void disposeScene() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public boolean onMenuItemClicked(MenuScene pMenuScene, IMenuItem pMenuItem,
			float pMenuItemLocalX, float pMenuItemLocalY) {
		// TODO Auto-generated method stub
		switch(pMenuItem.getID())
		{
		case LYGIU_REZIMAS:
            SceneManager.getInstance().loadPasirinktiZaistiScene(engine);
            return true;
		case MENU_ATGAL:
			SceneManager.getInstance().loadMenuScene(engine);
			return true;
        default:
        	return false;
		}
	}

}
