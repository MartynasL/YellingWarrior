package com.abunai.yellingwarrior.scene;

public class Lygis 
{
	private int enemyNumber;
	private int difficulty;
	
	public Lygis(int difficulty)
	{
		this.difficulty = difficulty;
		switch(difficulty)
		{
			case 1:
				enemyNumber = 1;
				break;
			case 2:
				enemyNumber = 3;
				break;
			case 3:
				enemyNumber = 6;
				break;
		}
	}
	
	public int getEnemyNumber()
	{
		return enemyNumber;
	}
	
	public int getDifficulty()
	{
		return difficulty;
	}
	
	public void setDifficulty(int difficulty)
	{
		this.difficulty = difficulty;
	}
}
