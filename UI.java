package com.ztd;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import java.util.Hashtable;

public class UI {
    public static int money = 30;
    public static int wave = 0;
    public static int life = 20;
    public static int cost = 10;
    public static int score = 0;
    public static String currentType;
    public static String last_zombie = "zzz";

    //Tables
    public static Hashtable<String, Texture> cannon_selector = new Hashtable<String, Texture>();                  // cannon texture table
    public static Hashtable<String, Integer> columns = new Hashtable<String, Integer>();                          // animation columns table
    public static Hashtable<String, Texture> bullet_selector = new Hashtable<String, Texture>();                  // bullet texture table
    public static Hashtable<String, Integer> bullet_damage = new Hashtable<String, Integer>();                    // bullet damage table
    public static Hashtable<String, Integer> bullet_speed = new Hashtable<String, Integer>();                     // bullet speed table
    public static Hashtable<String, Texture> loot_table = new Hashtable<String, Texture>();                       // loot texture table
    public static Hashtable<String, Integer> unlockCosts = new Hashtable<String, Integer>();                       // loot texture table
    public static Hashtable<String, Integer> placeCosts = new Hashtable<String, Integer>();                       // loot texture table
    //zombies
    public static Hashtable<String, Texture> zombie_selector = new Hashtable<String, Texture>();                  // zombie texture table
    public static Hashtable<String, Integer> zombie_health = new Hashtable<String, Integer>();                    // zombie health table
    public static Hashtable<String, Integer> zombie_score = new Hashtable<String, Integer>();                     // zombie speed table
    public static Hashtable<String, Integer> zombie_speed = new Hashtable<String, Integer>();                     // zombie score table
    public static Hashtable<String, Integer> zombie_value = new Hashtable<String, Integer>();        // zombie gold table
    public static Hashtable<String, Integer> zombie_damage = new Hashtable<String, Integer>();        // zombie gold table


    public static BitmapFont font = new BitmapFont();

    public static void draw(SpriteBatch batch) {

            font.setColor(Color.GOLD);
            font.getData().setScale(1.15f);
            font.draw(batch, "Gold : " + money, 97, 525);

            font.setColor(Color.GREEN);
            font.getData().setScale(1.15f);
            font.draw(batch, "Life : " + life, 10, 543);

            font.setColor(Color.CYAN);
            font.getData().setScale(1.15f);
            font.draw(batch, "Wave : " + wave, 10, 568);

            font.setColor(Color.PINK);
            font.getData().setScale(1.15f);
            font.draw(batch, "Z-Score : " + score, 10, 593);

            font.getData().setScale(1);
        if (life <= 0){
            batch.draw(resources.blackout, 0, 0);
            font.getData().setScale(5f);
            font.setColor(Color.SCARLET);
            font.draw(batch, "GAME OVER", 512 -215, 325);
            font.getData().setScale(11f);
            Main.paused = true;


        }
    }
}
