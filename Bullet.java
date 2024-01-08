package com.ztd;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;

import java.util.Arrays;
import java.util.Hashtable;

public class Bullet {
    public float x, y, w, h, speed, angle;
    public Texture bulletTexture;
    public boolean active;
    public int damage = 1;
    public String type;

    public Bullet(String type, float x, float y)
    {
        this.type = type;
        bulletTexture = UI.bullet_selector.get(type) == null ? resources.bulletTexture : UI.bullet_selector.get(type);
        speed = UI.bullet_speed.get(type) == null ? 5 : UI.bullet_speed.get(type);
        damage = UI.bullet_damage.get(type) == null ? 1 : UI.bullet_damage.get(type);
        this.x = x;
        this.y = y;
        w = bulletTexture.getWidth();
        h = bulletTexture.getHeight();
        speed = 5;
        active = true;
        getAngle();
    }

    void draw(SpriteBatch batch)
    {
        batch.draw(bulletTexture, x , y);
    }

    void update()
    {
        x += (float)Math.cos(angle) * speed;
        y += (float)Math.sin(angle) * speed;
    }

    public void getAngle(){
        if(Main.zlist.isEmpty()) return;

        float[] da = new float[Main.zlist.size()];
        Hashtable<Float, Zombie> closest_zombie_catcher = new Hashtable<Float, Zombie>();

        for(int i=0; i < Main.zlist.size(); i++){
            da[i] = Math.abs(x - (Main.zlist.get(i).x) + Main.zlist.get(i).w/2) +
                    Math.abs((y - Main.zlist.get(i).y + y - Main.zlist.get(i).h/2));
            closest_zombie_catcher.put(da[i], Main.zlist.get(i));
        }

        Arrays.sort(da);
        Zombie z = closest_zombie_catcher.get(da[0]);


        angle = (float)Math.atan((y-(z.y + z.h/2)) / (x- (z.x + z.w/2)));
        if(x >= z.x) angle += Math.PI;
        this.angle = angle;
    }

    Rectangle getHitbox(){
        return new Rectangle(x, y, w, h);
    }
}
