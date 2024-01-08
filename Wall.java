package com.ztd;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;

import java.util.ArrayList;


public class Wall {
    Texture wtx;
    float x, y, w, h;
    int durability;
    boolean active;
    ArrayList<Cannon> wall_cannons = new ArrayList<Cannon>();

    Wall(float x, float y){
        wtx = resources.wall;
        this.x = x;
        this.y = y;
        w = wtx.getWidth();
        h = wtx.getHeight();
        durability = 10;
        active = true;
    }

    void update(){
        for(Cannon c : wall_cannons) c.update();
        active = durability > 0;
    }

    void draw(SpriteBatch batch){
        batch.draw(wtx, x, y);
        for(Cannon c : wall_cannons) c.draw(batch);
    }

    Rectangle getHitbox(){
        return new Rectangle(x, y, w, h);
    }
}
