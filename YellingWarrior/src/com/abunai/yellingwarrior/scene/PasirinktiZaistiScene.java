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

public class PasirinktiZaistiScene extends BaseScene implements IOnMenuItemClickListener {

	@Override
	public void createScene() 
	{
		createBackground();
		createPasirinktiZaistiChildScene();
		
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
	
	private MenuScene pasirinktiZaistiChildScene;
	private final int MENU_ATGAL = 0;
	private final int NAUJAS_ZAIDIMAS = 1;
	private final int TESTI = 2;
	
	private void createPasirinktiZaistiChildScene()
	{
		pasirinktiZaistiChildScene = new MenuScene(camera);
		pasirinktiZaistiChildScene.setPosition(0, 0);
		
		final IMenuItem naujasZaidimasMenuItem = new ScaleMenuItemDecorator(new SpriteMenuItem(NAUJAS_ZAIDIMAS, resourcesManager.naujas_zaidimas_region, vbom), 1.2f, 1);
		pasirinktiZaistiChildScene.addMenuItem(naujasZaidimasMenuItem);
	    final IMenuItem testiMenuItem = new ScaleMenuItemDecorator(new SpriteMenuItem(TESTI, resourcesManager.testi_region, vbom), 1.2f, 1);
		pasirinktiZaistiChildScene.addMenuItem(testiMenuItem);		
	    final IMenuItem atgalMenuItem = new ScaleMenuItemDecorator(new SpriteMenuItem(MENU_ATGAL, resourcesManager.atgal_region, vbom), 1.2f, 1);
	    pasirinktiZaistiChildScene.addMenuItem(atgalMenuItem);
	    
	    pasirinktiZaistiChildScene.buildAnimations();
	    pasirinktiZaistiChildScene.setBackgroundEnabled(false);
	    
	    testiMenuItem.setPosition(testiMenuItem.getX(), testiMenuItem.getY()+20);
	    atgalMenuItem.setPosition(atgalMenuItem.getX() - 250, atgalMenuItem.getY() + 150);
	    
	    pasirinktiZaistiChildScene.setOnMenuItemClickListener(this);	    
	    setChildScene(pasirinktiZaistiChildScene);
	}

	@Override
	public void onBackKeyPressed() 
	{
		SceneManager.getInstance().loadPasirinktiRezimaScene(engine);	
	}

	@Override
	public SceneType getSceneType() 
	{
		return SceneType.SCENE_PASIRINKTI_ZAISTI;
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
		case NAUJAS_ZAIDIMAS:
            SceneManager.getInstance().loadLygiuRezimasScene(engine);
            return true;
		case MENU_ATGAL:
			SceneManager.getInstance().loadPasirinktiRezimaScene(engine);
			return true;
        default:
        	return false;
		}
	}

}
