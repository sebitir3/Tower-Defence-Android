package com.ztd;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;

public class Zombie {
    public float x, y, w, h, speed;
    public Texture zombieTexture;
    public int hp, zcore;
    public boolean active = true, drop = false;
    public float hchunk;
    String type;
    int damage;

    //animations
    int cols, rows;
    Animation animation;
    TextureRegion[] frames;
    TextureRegion frame;
    float frame_time;

    public Zombie(String type, float x, float y)
    {
        this.type = type;
        this.x = x;
        this.y = y;
        this.cols = UI.columns.get(type) == null? 4 : UI.columns.get(type);
        this.rows = 1;
        zombieTexture = UI.zombie_selector.get(type) == null? resources.zombieTexture : UI.zombie_selector.get(type);
        this.damage = UI.zombie_damage.get(type) == null ? 1 : UI.zombie_damage.get(type);
        w = zombieTexture.getWidth() / cols;
        h = zombieTexture.getHeight() / rows;
        this.hp = UI.zombie_health.get(type) == null ? 5 : UI.zombie_health.get(type);
        hchunk = ((w - 6) / hp);
        this.speed = UI.zombie_speed.get(type) == null? 1 : UI.zombie_speed.get(type);
        this.zcore = UI.zombie_score.get(type) == null? 1 : UI.zombie_score.get(type);


        animate();
    }

    void draw(SpriteBatch batch)
    {
        frame_time += Gdx.graphics.getDeltaTime();
        frame = (TextureRegion)animation.getKeyFrame(frame_time, true);

        batch.draw(resources.redBar, x - (w/2) + 3, y + (h/2) + 3, w-6, 3);
        batch.draw(resources.greenBar, x - (w/2) + 3, y + (h/2) + 3, hp * hchunk , 3);
        batch.draw(frame, x - w/2, y - h/2);
    }

    void update()
    {
        if(this.hp <= 0){
            active = false;
            drop = true;
            UI.score += zcore;
        } else if (this.x < -25) {
            active = false;
            UI.life--;
            UI.score -= 10;
        }
        x -= speed;
    }

    Rectangle getHitbox(){
        return new Rectangle(x, y, w, h);
    }

    void animate() {
        TextureRegion[][] sheet = TextureRegion.split(zombieTexture, (int)w, (int)h);

        frames = new TextureRegion[rows * cols];

        int index = 0;
        for(int r = 0; r < rows; r++)
            for(int c = 0; c < cols; c++)
                frames[index++] = sheet[r][c];

            animation = new Animation(0.2f, frames);
    }
}
