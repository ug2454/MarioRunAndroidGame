package com.myg;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.Rectangle;

import java.util.ArrayList;
import java.util.Random;

public class MyGdxGame extends ApplicationAdapter {
    SpriteBatch batch;
    Texture background;
    Texture[] man;
    Texture dizzy;
    int manState = 0;
    int pause = 0;
    float gravity = 0.2f;
    float velocity = 0;
    int manY = 0;
    int score = 0;
    int gameState = 0;
    Random random;
    Rectangle manRectangle;
    BitmapFont bitmapFont;
    ArrayList<Integer> coinsX = new ArrayList<>();
    ArrayList<Integer> coinsY = new ArrayList<>();
    ArrayList<Integer> bombsX = new ArrayList<>();
    ArrayList<Integer> bombsY = new ArrayList<>();
    ArrayList<Rectangle> coinRectangle = new ArrayList<>();
    ArrayList<Rectangle> bombRectangle = new ArrayList<>();


    Texture coin;
    int coinCount;

    Texture bomb;
    int bombCount;


    @Override
    public void create() {
        batch = new SpriteBatch();
        background = new Texture("bg.png");
        man = new Texture[4];
        man[0] = new Texture("frame-1.png");
        man[1] = new Texture("frame-2.png");
        man[2] = new Texture("frame-3.png");
        man[3] = new Texture("frame-4.png");
        manY = Gdx.graphics.getHeight() / 2;
        coin = new Texture("coin.png");
        random = new Random();
        bomb = new Texture("bomb.png");
        bitmapFont = new BitmapFont();
        bitmapFont.setColor(Color.WHITE);
        bitmapFont.getData().setScale(10);
        dizzy = new Texture("dizzy-1.png");

    }

    public void makeCoin() {
        float height = random.nextFloat() * Gdx.graphics.getHeight();
        coinsY.add((int) height);
        coinsX.add(Gdx.graphics.getWidth());

    }

    public void makeBomb() {
        float height = random.nextFloat() * Gdx.graphics.getHeight();
        bombsY.add((int) height);
        bombsX.add(Gdx.graphics.getWidth());
    }

    @Override
    public void render() {
        batch.begin();
        batch.draw(background, 0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());

        if (gameState == 1) {//game is live
//coin
            if (coinCount < 80) {
                coinCount++;
            } else {
                coinCount = 0;
                makeCoin();
            }
            coinRectangle.clear();
            for (int i = 0; i < coinsX.size(); i++) {
                batch.draw(coin, coinsX.get(i), coinsY.get(i));
                coinsX.set(i, coinsX.get(i) - 10);
                coinRectangle.add(new Rectangle(coinsX.get(i), coinsY.get(i), coin.getWidth(), coin.getHeight()));
            }


            //bomb
            if (bombCount < 500) {
                bombCount++;
            } else {
                bombCount = 0;
                makeBomb();
            }

            bombRectangle.clear();
            for (int i = 0; i < bombsX.size(); i++) {
                batch.draw(bomb, bombsX.get(i), bombsY.get(i));
                bombsX.set(i, bombsX.get(i) - 10);
                bombRectangle.add(new Rectangle(bombsX.get(i), bombsY.get(i), bomb.getWidth(), bomb.getHeight()));
            }
            if (Gdx.input.isTouched()) {
                velocity = -10;
            }
            if (pause < 8) {
                pause++;
            } else {
                pause = 0;
                if (manState < 3) {
                    manState++;
                } else {
                    manState = 0;
                }
            }
            velocity += gravity;
            manY -= velocity;
            if (manY <= 0) {
                manY = 0;
            }
        } else if (gameState == 0) {//waiting to start
            if (Gdx.input.justTouched()) {
                gameState = 1;
            }
        } else if (gameState == 2) {// game over
            if (Gdx.input.justTouched()) {
                gameState = 1;
                manY = Gdx.graphics.getHeight() / 2;
                score = 0;
                velocity = 0;
                coinsX.clear();
                coinsY.clear();
                coinRectangle.clear();
                coinCount = 0;

                bombsX.clear();
                bombsY.clear();
                bombRectangle.clear();
                bombCount = 0;


            }
        }
        if (gameState == 2) {
            batch.draw(dizzy, Gdx.graphics.getWidth() / 2 - man[manState].getWidth() / 2, manY);
        } else {
            batch.draw(man[manState], Gdx.graphics.getWidth() / 2 - man[manState].getWidth() / 2, manY);

        }

        manRectangle = new Rectangle(Gdx.graphics.getWidth() / 2 - man[manState].getWidth() / 2, manY, man[manState].getWidth(), man[manState].getHeight());
        for (int i = 0; i < coinRectangle.size(); i++) {
            if (Intersector.overlaps(manRectangle, coinRectangle.get(i))) {
                Gdx.app.log("Coin", "Collision!");
                score++;
                coinsX.remove(i);
                coinsY.remove(i);
                coinRectangle.remove(i);

                break;
            }
        }
        for (int i = 0; i < bombRectangle.size(); i++) {
            if (Intersector.overlaps(manRectangle, bombRectangle.get(i))) {
                Gdx.app.log("Bomb", "Collision!");
                gameState = 2;
                score--;
                bombsX.remove(i);
                bombsY.remove(i);
                bombRectangle.remove(i);
//                gameState
                break;

            }
        }

        bitmapFont.draw(batch, String.valueOf(score), 100, 200);
        batch.end();
    }

    @Override
    public void dispose() {
        batch.dispose();

    }
}
