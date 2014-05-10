package com.abunai.yellingwarrior.scene;

import java.util.Random;

import com.abunai.yellingwarrior.manager.ResourcesManager;
import com.abunai.yellingwarrior.object.Enemy;

public class LygiuRezimas extends GameScene
{
	Lygis currentLevel;
	int statistics;
	
	public void setStatistics(int statistics)
	{
		this.statistics = statistics;
	}
	
	public void setCurrentLevel(Lygis level)
	{
		currentLevel = level;
	}
	
	public Lygis getCurrentLevel()
	{
		return currentLevel;
	}
	
	public int getStatistics()
	{
		return statistics;
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
