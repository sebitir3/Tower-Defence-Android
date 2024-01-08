package com.ztd;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;

import java.util.Arrays;
import java.util.Hashtable;

public class Loot {
    float x, y, w, h, speed, ix, iy, fx, fy, angle; //i=initial, f=final
    Texture ltx;
    int item_value;
    String type;
    boolean active = true;

    public Loot(String type, float x, float y, float fx, float fy){
        this.type = type;
        this.x = x;
        this.y = y;
        this.fx = fx;
        this.fy = fy;
        ltx = UI.loot_table.get(type) == null ? resources.zcoin : UI.loot_table.get(type);
        w = ltx.getWidth();
        h = ltx.getHeight();
        item_value = UI.zombie_value.get(UI.last_zombie) == null ? 1 : UI.zombie_value.get(UI.last_zombie);
        speed = 4;

    }

    void draw(SpriteBatch batch){
        batch.draw(ltx, x, y);
    }

    void moveToTarget(Loot l){
        getAngle(l);
        x += (float)Math.cos(angle) * speed;
        y += (float)Math.sin(angle) * speed;
    }

    public void getAngle(Loot l) {
        angle = (float) Math.atan((y - (l.y + l.h / 2)) / (x - (l.x + l.w / 2)));
        if (x >= l.x + l.w/2) angle += Math.PI;
    }

    Rectangle getHitbox(){
        return new Rectangle(x, y, w, h);
    }
}

