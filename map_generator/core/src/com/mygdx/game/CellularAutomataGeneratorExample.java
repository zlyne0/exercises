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
import com.badlogic.gdx.utils.IntArray;
import com.github.czyzby.noise4j.map.Grid;
import com.github.czyzby.noise4j.map.generator.cellular.CellularAutomataGenerator;

public class CellularAutomataGeneratorExample extends ApplicationAdapter {
    
    enum Direction {
        NORTH(0, -1), 
        SOUTH(0, 1), 
        WEST(-1, 0), 
        EAST(1, 0);
        
        final int x;
        final int y;
        
        private Direction(int x, int y) {
            this.x = x;
            this.y = y;
        }
    }
    
    private SpriteBatch batch;
    private Texture texture;

    private static final float WATER = 0f;
    private static final float LAND = 1f;
    
    float aliveChance = 0.8f;
    int radius = 3;
    int birthLimit = 11;
    int deathLimit = 11;
    int iterationAmount = 3;

    @Override
    public void create() {
        createTexture();

        iterationAmount = 1;

        batch = new SpriteBatch();
        Gdx.input.setInputProcessor( new InputAdapter() {
            @Override
            public boolean keyUp(int keycode) {
                if (keycode == Input.Keys.Q) {
                    aliveChance += 0.1f;
                }
                if (keycode == Input.Keys.W) {
                    radius += 1;
                }
                if (keycode == Input.Keys.E) {
                    birthLimit += 1;
                }
                if (keycode == Input.Keys.R) {
                    deathLimit += 1;
                }
                if (keycode == Input.Keys.T) {
                    iterationAmount += 1;
                }

                if (keycode == Input.Keys.A) {
                    aliveChance -= 0.1f;
                }
                if (keycode == Input.Keys.S) {
                    radius -= 1;
                }
                if (keycode == Input.Keys.D) {
                    birthLimit -= 1;
                }
                if (keycode == Input.Keys.F) {
                    deathLimit -= 1;
                }
                if (keycode == Input.Keys.G) {
                    iterationAmount -= 1;
                }
                System.out.println("##############");
                System.out.println("aliveChance: " + aliveChance);
                System.out.println("radius     : " + radius);
                System.out.println("birthLimit : " + birthLimit);
                System.out.println("deathLimit : " + deathLimit);
                System.out.println("iterAmount : " + iterationAmount);
                createTexture();
                return super.keyUp(keycode);
            }
        });
    }

    void generateHighSea(Grid landGrid) {
        Grid gridRange = new Grid(landGrid.getWidth(), landGrid.getHeight());

        float maxDistance = 6;
        
        // init Float.MAX_VALUE
        // -1 land
        // 0 zerowy zasieg
        // wyznacza najwiekszy zasieg ale nie wiekszy niz
        int x, y;
        for (y=0; y<landGrid.getHeight(); y++) {
            for (x=0; x<landGrid.getWidth(); x++) {
                if (landGrid.get(x, y) == LAND) {
                    gridRange.set(x, y, -1);
                } else {
                    gridRange.set(x, y, Float.MAX_VALUE);
                }
            }
        }
        
        float v;
        int cellIndex;
        
        IntArray indexPool = new IntArray(false, landGrid.getWidth() * landGrid.getHeight());
        int px, py, pCellIndex;
        float pv, newValue;
        
        for (y=0; y<gridRange.getHeight(); y++) {
            for (x=0; x<gridRange.getWidth(); x++) {
                v = gridRange.get(x, y);
                // take only land
                if (v != -1) {
                    continue;
                }
                
                for (Direction d : Direction.values()) {
                    if (gridRange.isIndexValid(x + d.x, y + d.y)) {
                        cellIndex = gridRange.toIndex(x + d.x, y + d.y);
                        v = gridRange.getArray()[cellIndex];
                        if (v == -1) {
                            continue;
                        }
                        newValue = 1;
                        if (newValue < v) {
                            gridRange.getArray()[cellIndex] = newValue;
                            indexPool.add(cellIndex);
                        }
                    }
                }
                
                while (indexPool.size > 0) {
                    cellIndex = indexPool.removeIndex(indexPool.size - 1);
                    v = gridRange.getArray()[cellIndex];
                    px = gridRange.toX(cellIndex);
                    py = gridRange.toY(cellIndex);
                    
                    for (Direction d : Direction.values()) {
                        if (gridRange.isIndexValid(px + d.x, py + d.y)) {
                            pCellIndex = gridRange.toIndex(px + d.x, py + d.y);
                            pv = gridRange.getArray()[pCellIndex];
                            if (pv == -1) {
                                continue;
                            }
                            newValue = v + 1;
                            if (newValue < maxDistance && newValue < pv) {
                                gridRange.getArray()[pCellIndex] = newValue;
                                indexPool.add(pCellIndex);
                            }
                        }
                    }
                }
            }
        }
        
        float closeSeaMapRange = 0.20f;
        for (y=0; y<gridRange.getHeight(); y++) {
            for (x=0; x<gridRange.getWidth(); x++) {
                v = gridRange.get(x, y);
                if (v == -1) {
                    continue;
                }
                if (x > gridRange.getWidth() * closeSeaMapRange && x < gridRange.getWidth() * (1 - closeSeaMapRange)) {
                    landGrid.set(x, y, 0.5f);
                }
                if (v < Float.MAX_VALUE) {
                    landGrid.set(x, y, 0.5f);
                }
            }
        }
    }

    void createTexture() {
        final Grid grid = new Grid(40, 100);

        final CellularAutomataGenerator cellularGenerator = new CellularAutomataGenerator();
        cellularGenerator.setAliveChance(aliveChance);
        cellularGenerator.setRadius(radius);
        cellularGenerator.setBirthLimit(birthLimit);
        cellularGenerator.setDeathLimit(deathLimit);
        cellularGenerator.setIterationsAmount(iterationAmount);

        CellularAutomataGenerator.initiate(grid, cellularGenerator);
        for (int x=0; x<3; x++) {
            grid.fillColumn(x, 0f);
            grid.fillColumn(grid.getWidth()-x-1, 0f);
        }

        for (int i=0; i<6; i++) {
            grid.fillRow(grid.getHeight()/4 - 3 + i, 0f);
        }
        for (int i=0; i<6; i++) {
            grid.fillRow((int)(grid.getHeight()*0.75f) - 3 + i, 0f);
        }
        for (int i=0; i<3; i++) {
            grid.fillColumn(grid.getWidth()/2-1+i, 0f);
        }

        cellularGenerator.setInitiate(false);
        cellularGenerator.generate(grid);

        generateHighSea(grid);
        
        if (texture != null) {
            texture.dispose();
        }
        texture = map2Texture(grid);
    }

    int pixSize = 7;
    Texture map2Texture(Grid grid) {
        final Pixmap map = new Pixmap(
                (grid.getWidth()) * pixSize,
                grid.getHeight() * pixSize,
                Pixmap.Format.RGBA8888
        );

        final Color color = new Color();
        for (int x = 0; x < grid.getWidth(); x++) {
            for (int y = 0; y < grid.getHeight(); y++) {
                final float cell = grid.get(x, y);
                color.set(cell, cell, cell, 1f);
                map.setColor(color);
                map.fillRectangle(x*pixSize, y*pixSize, pixSize, pixSize);
            }
        }

        map.setColor(Color.RED);
        for (int x = 0; x < grid.getWidth(); x++) {
            map.drawLine(x*pixSize, 0, x*pixSize, map.getHeight());
            for (int y = 0; y < grid.getHeight(); y++) {
                map.drawLine(0, y*pixSize, map.getWidth(), y*pixSize);
            }
        }

        texture = new Texture(map);
        map.dispose();
        return texture;
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
