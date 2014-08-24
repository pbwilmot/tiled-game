package com.wilmot.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by pbwilmot on 8/5/14.
 */
public class DwarfSoldier extends Character {

    Map<String, IsometricAction> actions = new HashMap<String, IsometricAction>();
    private static final float ANIMATION_SPEED = 0.1f;

    public DwarfSoldier() {
        final Texture axeAttackSheet = new Texture(Gdx.files.internal("Dwarf Soldier/axe attack.png"));
        int cols = 10;
        registerAction("axeAttack", axeAttackSheet, cols);

        final Texture axeIdleSheet = new Texture(Gdx.files.internal("Dwarf Soldier/axe idle.png"));
        cols = 5;
        registerAction("axeIdle", axeIdleSheet, cols);

        final Texture axeStaySheet = new Texture(Gdx.files.internal("Dwarf Soldier/axe stay.png"));
        cols = 1;
        registerAction("axeStay", axeStaySheet, cols);

        final Texture axeWalkSheet = new Texture(Gdx.files.internal("Dwarf Soldier/axe walk.png"));
        cols = 10;
        registerAction("axeWalk", axeWalkSheet, cols);
    }

    private void registerAction(String name, Texture sheet, int cols) {
        IsometricAction action = new IsometricAction(sheet, cols, ANIMATION_SPEED);
        actions.put(name, action);
    }
}
