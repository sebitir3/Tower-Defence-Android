package com.ztd;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;

public class Saw {
    float x, y, w, h, xspeed, angle = 360f;
    Sprite sprite;
    boolean active = true;

    Saw(float x, float y){
        sprite = new Sprite(resources.saw);
        this.x = x;
        this.y = y;
        this.w = sprite.getWidth();
        this.h = sprite.getHeight();
        this.xspeed = 4;

    }

    void draw(SpriteBatch batch){ sprite.draw(batch); }

    void update(){
        x += xspeed;
        sprite.setPosition(x, y);
        angle -= xspeed;
        if(angle <= 0) angle = 360;
        sprite.setRotation(angle);
        active = x < 1100;
    }

    Rectangle getHitbox(){
        return new Rectangle(x, y, w, h);
    }

}
