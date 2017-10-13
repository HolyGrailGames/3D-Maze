package com.ru.tgra.utilities;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;

public class Sounds {
	public static final Sound COIN_PICKUP = Gdx.audio.newSound(Gdx.files.internal("sounds/coin.wav"));
	
	/**
	 * Private constructor to prevent anyone from creating an instance of this class.
	 * This is done because the Settings class is to be used as a static class.
	 */
	private Sounds() {}
}
