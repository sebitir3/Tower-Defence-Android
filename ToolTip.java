package com.ztd;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;


public class ToolTip {
    Texture ttex;
    Button btn;
    float x, y, w, h;
    float px, py, pw, ph; //parent
    int variant;
    BitmapFont font = new BitmapFont();
    boolean hidden = true;
    String type;

    /*
     * 1) TOOLTIP INFO (tips, cost details for unlocking / using) + CLOSE BUTTON
     * 2) Double Tap Button to purchase / perform action (tap 1x to show tooltip & cost, tap again to unlock)
     * 3) Disabling ToolTips
     */

    ToolTip(String type, float x, float y, float w, float h){
        ttex = resources.tooltip_bg;
        this.type = type;

        px = x;
        py = y;
        pw = w;
        ph = h;

        this.w = 200;
        this.h = 100;
        this.x = (x + w / 2) - this.w / 2;
        this.y = y - this.h - 15;
        btn = new Button(resources.x, "no", this.x + this.w - 26, this.y + this.h - 26);
        variant = 1;
    }

    void draw(SpriteBatch batch){
        if(!hidden) {
            batch.draw(ttex, x, y, w, h);
            font.setColor(Color.MAGENTA);
            font.draw(batch, "Details.", x + 5, y + h - 5);

            font.setColor(Color.GOLDENROD);
            font.draw(batch, "Unlock: " + UI.unlockCosts.get(type), x + 5, y + 34);

            font.setColor(Color.GOLDENROD);
            font.draw(batch, "Placement: " + UI.placeCosts.get(type), x + 90, y + 34);

            font.setColor(Color.BLACK);
            font.draw(batch, "(tap again to unlock)", x + 38, y + 16);

            btn.draw(batch);
        }
    }

    Rectangle hitbox(){
        return new Rectangle(x, y, w, h);
    }
}
