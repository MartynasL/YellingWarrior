package com.abunai.yellingwarrior.scene;

import org.andengine.entity.scene.menu.MenuScene;
import org.andengine.entity.scene.menu.MenuScene.IOnMenuItemClickListener;
import org.andengine.entity.scene.menu.item.IMenuItem;
import org.andengine.entity.scene.menu.item.SpriteMenuItem;
import org.andengine.entity.scene.menu.item.decorator.ScaleMenuItemDecorator;
import org.andengine.entity.sprite.Sprite;
import org.andengine.entity.text.Text;
import org.andengine.opengl.util.GLState;
import org.andengine.engine.camera.Camera;

import com.abunai.yellingwarrior.base.BaseScene;
import com.abunai.yellingwarrior.manager.SceneManager;
import com.abunai.yellingwarrior.manager.SceneManager.SceneType;

public class RezultataiScene extends BaseScene implements IOnMenuItemClickListener  {
	
	//private static int highestScore = 0;
	public static Text score;

	@Override
	public SceneType getSceneType()
	{
	    return SceneType.SCENE_REZULTATAI;
	}
	
	@Override
	public void onBackKeyPressed()
	{
		SceneManager.getInstance().loadMenuScene(engine);
	}
	
	private void createBackground()
	{
	
	    attachChild(new Sprite(0, 0, resourcesManager.rezultatai_background_region, vbom)
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
		if (pMenuItem.getID() == 0)
		{
			SceneManager.getInstance().loadMenuScene(engine);
			return true;
		}
		else
			return false;
	}

	@Override
	public void createScene() 
	{
		createBackground();
	    createRezultataiChildScene();
	    attachChild(new Text(50, 200, resourcesManager.font2, "Daugiausiai taðkø:", vbom));
	    //score = new Text(50, 230, resourcesManager.font2, Integer.toString(highestScore), vbom);
	    score = new Text(50, 230, resourcesManager.font2, "0000000000", vbom);
	    attachChild(score);
	}

	@Override
	public void disposeScene() {
		// TODO Auto-generated method stub
		
	}
	
	private MenuScene rezultataiChildScene;
	private final int MENU_ATGAL = 0;
	
	private void createRezultataiChildScene()
	{
		rezultataiChildScene = new MenuScene(camera);
		rezultataiChildScene.setPosition(0, 0);
	    
	    final IMenuItem atgalMenuItem = new ScaleMenuItemDecorator(new SpriteMenuItem(MENU_ATGAL, resourcesManager.atgal_region, vbom), 1.2f, 1);
	    
	    rezultataiChildScene.addMenuItem(atgalMenuItem);
	    
	    rezultataiChildScene.buildAnimations();
	    rezultataiChildScene.setBackgroundEnabled(false);
	    
	    atgalMenuItem.setPosition(atgalMenuItem.getX() - 250, atgalMenuItem.getY() + 200);
	    
	    rezultataiChildScene.setOnMenuItemClickListener(this);
	    
	    setChildScene(rezultataiChildScene);
	}

	//public static int getHighestScore() {
	//	return highestScore;
	//}

	//public static void setHighestScore(int highestScore) {
	//	RezultataiScene.highestScore = highestScore;
	//}
}