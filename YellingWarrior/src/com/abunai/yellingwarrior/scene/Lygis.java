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
			case 2:
				enemyNumber = 3;
			case 3:
				enemyNumber = 6;
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
