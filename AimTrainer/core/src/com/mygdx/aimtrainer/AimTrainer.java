package com.mygdx.aimtrainer;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

/*
* This is a simple little aim training app, created with love by Darren Finch.
* You probably found this project through my tutorial. If so, thanks so much for reading it, and enjoy the game!
* */
public class AimTrainer extends ApplicationAdapter
{
	private SpriteBatch batch;
	private Texture targetTexture;
	private Rectangle targetRect;

	private Timer gameTimer;
	private int secondsLeft;
	private final int seconds = 30;

	private int score;
	private boolean gameOver = false;

	private BitmapFont font;

	private Random rng;

	@Override
	public void create ()
	{
		batch = new SpriteBatch();
		targetTexture = new Texture("target.png");
		targetRect = new Rectangle((Gdx.graphics.getWidth() / 2) - targetTexture.getWidth() * 2,
				(Gdx.graphics.getHeight() / 2) - targetTexture.getHeight() * 2,
				targetTexture.getWidth() * 2, targetTexture.getHeight() * 2);
		rng = new Random();
		gameTimer = new Timer();
		font = new BitmapFont();
		font.setColor(Color.WHITE);
		font.getData().setScale(5);

		startGame();
		startTimer();
	}

	@Override
	public void render ()
	{
		if(secondsLeft <= 0)
		{
			gameOver = true;
		}

		//Clearing the background
		Gdx.gl.glClearColor(0.5f, 0.5f, 1, 0.5f);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

		batch.begin();
		if(!gameOver)
		{
			batch.draw(targetTexture, targetRect.x, targetRect.y, targetRect.width, targetRect.height);
			font.draw(batch, "Seconds Left: " + secondsLeft, 150, Gdx.graphics.getHeight() - 150);
			font.draw(batch, "Your score: " + score, 150, 150);

			if(Gdx.input.justTouched())
			{
				Vector2 touchPosition = new Vector2(Gdx.input.getX(), Gdx.graphics.getHeight() - Gdx.input.getY());
				Rectangle touchedRect = new Rectangle(touchPosition.x, touchPosition.y, 1, 1);
				if(Intersector.overlaps(touchedRect, targetRect))
				{
					changeTargetPosition();
					score++;
				}
			}
		}
		else
		{
			if(Gdx.input.justTouched())
				startGame();
			font.draw(batch, "Game Over! Your score was: " + score, 150, Gdx.graphics.getHeight() - 150);
		}
		batch.end();
	}

	private void changeTargetPosition()
	{
		targetRect.setPosition(rng.nextInt(Gdx.graphics.getWidth() - (int) targetRect.width) + targetRect.height / 2, rng.nextInt(Gdx.graphics.getHeight() - (int) targetRect.height) + targetRect.height / 2);
	}

	private void startGame()
	{
		score = 0;
		gameOver = false;
		secondsLeft = seconds;
		targetRect = new Rectangle((Gdx.graphics.getWidth() / 2) - targetTexture.getWidth() * 2,
				(Gdx.graphics.getHeight() / 2) - targetTexture.getHeight() * 2,
				targetTexture.getWidth() * 2, targetTexture.getHeight() * 2);
	}

	private void startTimer()
	{
		secondsLeft = seconds;
		gameTimer.scheduleAtFixedRate(new TimerTask()
		{
			@Override
			public void run()
			{
				if(!gameOver)
					secondsLeft--;
			}
		}, 0, 1000);
	}

	@Override
	public void dispose ()
	{
		batch.dispose();
	}
}
