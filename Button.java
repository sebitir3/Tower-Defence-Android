package com.ztd;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;

public class Button {
    public float x, y, w, h;
    public String type; //type of button represented as a string, ex. a cannon button will have a type of cannon
    public Texture btex;
    public boolean selected, locked;
    public int cost;
    ToolTip t;

    public Button(Texture btex, String type, float x, float y){
        this.btex = btex;
        this.type = type;
        this.x = x;
        this.y = y;
        this.w = btex.getWidth();
        this.h = btex.getHeight();
        cost = UI.unlockCosts.get(type) == null? 50 : UI.unlockCosts.get(type);

        switch(type){
            case "cannon":
                selected = true;
                locked = false;
                break;
            case "fire":
            case "saw":
                selected = false;
                locked = true;
            case "super":
                selected = false;
                locked = true;
            case "double":
                selected = false;
                locked = true;
            case "laser":
                selected = false;
                locked = true;
                break;
            case "wall":
                selected = false;
                locked = false;
                break;
            case "mounted":
                selected = false;
                locked = true;
                break;
            case "pause":
                selected = false;
                locked = false;
            case "play":
                selected = false;
                locked = false;
        }

        if(!type.equals("no")) t = new ToolTip(type, x, y, w, h);

    }

    public void draw(SpriteBatch batch){
        batch.draw(btex, x, y);
        if(locked) batch.draw(resources.locked, x, y);
        if (selected) batch.draw(resources.selection, x -15/2, y - 15/2);
        if(t != null) t.draw(batch);
    }

    public Rectangle getHitbox(){
        return new Rectangle(x, y, w, h);
    }
}
