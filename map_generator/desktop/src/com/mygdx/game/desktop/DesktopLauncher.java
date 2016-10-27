package com.mygdx.game.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.mygdx.game.CellularAutomataGeneratorExample;
import com.mygdx.game.NoiseExample;

public class DesktopLauncher {
	public static void main (String[] arg) {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		config.width = 800;
		config.height = 800;
		//new LwjglApplication(new MyGdxGame(), config);
		new LwjglApplication(new CellularAutomataGeneratorExample(), config);
	}
}
