package com.mygdx.game;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.github.czyzby.noise4j.map.Grid;
import com.github.czyzby.noise4j.map.generator.noise.NoiseGenerator;
import com.github.czyzby.noise4j.map.generator.util.Generators;

public class NoiseExample extends ApplicationAdapter {
    
    public static final int WIDTH = 40;
    public static final int HEIGHT = 100;
    
    private SpriteBatch batch;
    private Texture texture;

    float water = 0;
    float hills = 0.5f;

    final Grid grid = new Grid(WIDTH, HEIGHT);

    @Override
    public void create() {

        final NoiseGenerator noiseGenerator = new NoiseGenerator();
        noiseStage(grid, noiseGenerator, 32, 0.6f);
        noiseStage(grid, noiseGenerator, 16, 0.2f);
        noiseStage(grid, noiseGenerator, 8, 0.1f);
        noiseStage(grid, noiseGenerator, 4, 0.1f);
        noiseStage(grid, noiseGenerator, 1, 0.05f);

        createTexture();
        batch = new SpriteBatch();


        Gdx.input.setInputProcessor( new InputAdapter() {
            @Override
            public boolean keyUp(int keycode) {
                if (keycode == Input.Keys.Q) {
                    water += 0.05f;
                }
                if (keycode == Input.Keys.W) {
                    hills += 0.05f;
                }

                if (keycode == Input.Keys.A) {
                    water -= 0.051f;
                }
                if (keycode == Input.Keys.S) {
                    hills -= 0.05f;
                }
                System.out.println("##############");
                System.out.println("water: " + water);
                System.out.println("hils     : " + hills);
                createTexture();
                return super.keyUp(keycode);
            }
        });
    }

    private void createTexture() {
        final Pixmap map = new Pixmap(grid.getWidth(), grid.getHeight(), Pixmap.Format.RGBA8888);
        float min = 100;
        float max = -100;

        final Color color = new Color();
        for (int x = 0; x < grid.getWidth(); x++) {
            for (int y = 0; y < grid.getHeight(); y++) {
                final float cell = grid.get(x, y);
                min = Math.min(min, cell);
                max = Math.max(max, cell);
                if (cell < water) {
                    color.set(Color.BLUE);
                }
                if (cell >= water && cell <hills) {
                    color.set(Color.GREEN);
                }
                if (cell > hills) {
                    color.set(Color.WHITE);
                }
                //color.set(cell, cell, cell, 1f);
                map.drawPixel(x, y, Color.rgba8888(color));
            }
        }
        System.out.println("cell min/max = " + min + "/" + max);
        texture = new Texture(map);
        map.dispose();
    }

    private static void noiseStage(final Grid grid, final NoiseGenerator noiseGenerator, final int radius,
                                   final float modifier) {
        noiseGenerator.setRadius(radius);
        noiseGenerator.setModifier(modifier);
        // Seed ensures randomness, can be saved if you feel the need to
        // generate the same map in the future.
        noiseGenerator.setSeed(Generators.rollSeed());
        noiseGenerator.generate(grid);
    }

    @Override
    public void render() {
        Gdx.gl.glClearColor(0f, 0f, 0f, 1f);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        batch.begin();
        batch.draw(texture, 0f, 0f);
        batch.end();
    }

    @Override
    public void dispose() {
        texture.dispose();
        batch.dispose();
    }
}