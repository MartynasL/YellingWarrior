package com.abunai.yellingwarrior.manager;

import org.andengine.engine.Engine;
import org.andengine.engine.handler.timer.ITimerCallback;
import org.andengine.engine.handler.timer.TimerHandler;
import org.andengine.ui.IGameInterface.OnCreateSceneCallback;

import com.abunai.yellingwarrior.base.BaseScene;
import com.abunai.yellingwarrior.scene.GameScene;
import com.abunai.yellingwarrior.scene.LoadingScene;
import com.abunai.yellingwarrior.scene.Lygis;
import com.abunai.yellingwarrior.scene.LygiuRezimas;
import com.abunai.yellingwarrior.scene.MainMenuScene;
import com.abunai.yellingwarrior.scene.RezultataiScene;
import com.abunai.yellingwarrior.scene.SplashScene;

public class SceneManager
{
    //---------------------------------------------
    // SCENES
    //---------------------------------------------
    
    private BaseScene splashScene;
    private BaseScene menuScene;
    private BaseScene gameScene;
    private BaseScene loadingScene;
    private BaseScene rezultataiScene;
    
    //---------------------------------------------
    // VARIABLES
    //---------------------------------------------
    
    private static final SceneManager INSTANCE = new SceneManager();
    
    private SceneType currentSceneType = SceneType.SCENE_SPLASH;
    
    private BaseScene currentScene;
    
    private Engine engine = ResourcesManager.getInstance().engine;
    
    public enum SceneType
    {
        SCENE_SPLASH,
        SCENE_MENU,
        SCENE_GAME,
        SCENE_LOADING,
        SCENE_REZULTATAI,
    }
    
    //---------------------------------------------
    // CLASS LOGIC
    //---------------------------------------------
    
    public void setScene(BaseScene scene)
    {
        engine.setScene(scene);
        currentScene = scene;
        currentSceneType = scene.getSceneType();
    }
    
    public void setScene(SceneType sceneType)
    {
        switch (sceneType)
        {
            case SCENE_MENU:
                setScene(menuScene);
                break;
            case SCENE_GAME:
                setScene(gameScene);
                break;
            case SCENE_SPLASH:
                setScene(splashScene);
                break;
            case SCENE_LOADING:
                setScene(loadingScene);
                break;
            case SCENE_REZULTATAI:
            	setScene(rezultataiScene);
            	break;
            default:
                break;
        }
    }
    
    //---------------------------------------------
    // GETTERS AND SETTERS
    //---------------------------------------------
    
    public static SceneManager getInstance()
    {
        return INSTANCE;
    }
    
    public SceneType getCurrentSceneType()
    {
        return currentSceneType;
    }
    
    public BaseScene getCurrentScene()
    {
        return currentScene;
    }
    
    public void createSplashScene(OnCreateSceneCallback pOnCreateSceneCallback)
    {
        ResourcesManager.getInstance().loadSplashScreen();

        ResourcesManager.getInstance().loadGameResources();
        splashScene = new SplashScene();
        currentScene = splashScene;
        pOnCreateSceneCallback.onCreateSceneFinished(splashScene);
    }
    
    public void createMenuScene()
    {
        ResourcesManager.getInstance().loadMenuResources();
        menuScene = new MainMenuScene();
        loadingScene = new LoadingScene();
        ResourcesManager.getInstance().loadRezultataiResources();
        rezultataiScene = new RezultataiScene();
        SceneManager.getInstance().setScene(menuScene);
        disposeSplashScene();
    }
    
    private void disposeSplashScene()
    {
        ResourcesManager.getInstance().unloadSplashScreen();
        splashScene.disposeScene();
        splashScene = null;
    }
    
    public void loadGameScene(final Engine mEngine)
    {
        setScene(loadingScene);
        ResourcesManager.getInstance().unloadMenuTextures();
        mEngine.registerUpdateHandler(new TimerHandler(0.1f, new ITimerCallback() 
        {
            public void onTimePassed(final TimerHandler pTimerHandler) 
            {
                mEngine.unregisterUpdateHandler(pTimerHandler);
                // game textures now loaded only one along with slpash scene (lame fix)

                LygiuRezimas lygiuRezimas = new LygiuRezimas();
                lygiuRezimas.setCurrentLevel(new Lygis(3));
                gameScene = lygiuRezimas;
                setScene(gameScene);
            }
        }));
    }
    
    public void loadMenuScene(final Engine mEngine)
    {
        setScene(loadingScene);
        
        mEngine.registerUpdateHandler(new TimerHandler(0.1f, new ITimerCallback() 
        {
            public void onTimePassed(final TimerHandler pTimerHandler) 
            {
                mEngine.unregisterUpdateHandler(pTimerHandler);
                if (gameScene != null) {

                	gameScene.disposeScene();
                	gameScene = null;

                    ResourcesManager.getInstance().loadMenuTextures();
                }
                else if (rezultataiScene != null)
                {
                	ResourcesManager.getInstance().unloadRezultataiTextures();

                    ResourcesManager.getInstance().loadMenuTextures();
                }
                setScene(menuScene);
            }
        }));
    }

	public void loadRezultataiScene(final Engine mEngine)
	{
        setScene(loadingScene);
        ResourcesManager.getInstance().unloadMenuTextures();
        mEngine.registerUpdateHandler(new TimerHandler(0.1f, new ITimerCallback() 
        {
            public void onTimePassed(final TimerHandler pTimerHandler) 
            {
            	mEngine.unregisterUpdateHandler(pTimerHandler);
            	ResourcesManager.getInstance().unloadMenuTextures();

                ResourcesManager.getInstance().loadRezultataiTextures();
                setScene(rezultataiScene);
            }
        }));
    }
}