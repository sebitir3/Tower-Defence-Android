package com.ztd;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.ScreenUtils;

import java.util.ArrayList;
import java.util.Random;

public class Main extends ApplicationAdapter {
	//Lists
	public static ArrayList<Zombie> zlist = new ArrayList<Zombie>();
	public static ArrayList<Cannon> clist = new ArrayList<Cannon>();
	public static ArrayList<Bullet> blist = new ArrayList<Bullet>();
	public static ArrayList<Button> bulist = new ArrayList<Button>();
	public static ArrayList<Loot> lootList = new ArrayList<Loot>();
	public static ArrayList<Wall> wlist = new ArrayList<Wall>();
	public static ArrayList<Saw> slist = new ArrayList<Saw>();



	//Other Variables
	SpriteBatch batch;
	Texture bg;
	public static boolean paused = true, started = false;
	OrthographicCamera camera;
	
	@Override
	public void create () {
		batch = new SpriteBatch();
		bg = resources.bgTexture;
		camera = new OrthographicCamera();
		camera.setToOrtho(false, 1024,600);
		bulist.add(new Button(resources.startbutton, "start", 512 - 250 , 50));
		bulist.add(new Button(resources.exitbutton, "exit", 512 + 50, 50));
	}

	@Override
	public void render () {
		ScreenUtils.clear(0, 0, 0, 1);
		update();
		camera.update();
		batch.setProjectionMatrix(camera.combined);
		batch.begin();
		if(!started){
			batch.draw(resources.title, 0, 0);
			for(Button bu : bulist) bu.draw(batch);
		} else {
			batch.draw(bg, 0, 0);
			UI.draw(batch);
			for(Zombie z : zlist) z.draw(batch);
			for(Wall w : wlist) w.draw(batch);
			for(Cannon c : clist) c.draw(batch);
			for(Bullet b : blist) b.draw(batch);
			for(Saw s : slist) s.draw(batch);
			for(Loot l : lootList) l.draw(batch);
			for(Button bu : bulist) bu.draw(batch);
		}
		batch.end();
	}

	public void update(){
		controls();
		if(paused) return;
		spawnZombies();
		bulletZombieCollision();
		zombieWallCollision();
		zombieCannonCollision();
		coinPouchCollision();
		sawZombieCollision();
		housekeeping();

		for(Zombie z : zlist) z.update();
		for(Cannon c : clist) c.update();
		for(Bullet b : blist) b.update();
		for(Wall w : wlist) w.update();
		for(Saw s : slist) s.update();
		for(Loot l : lootList) {
			if(l.type.equals("coin")) l.moveToTarget(lootList.get(0));
		}

	}

	public void housekeeping()
	{
		for(int i = 0; i < zlist.size(); i++) if(!zlist.get(i).active) {
			if(zlist.get(i).drop) {
				UI.last_zombie = zlist.get(i).type;
				lootList.add(new Loot("coin", zlist.get(i).x, zlist.get(i).y, 0, 0));
			}
			zlist.remove(i);
		}
		for(int i = 0; i < wlist.size(); i++) if(!wlist.get(i).active) {
			wlist.get(i).wall_cannons.clear();
			wlist.remove(i);
		}
		for(int i = 0; i < blist.size(); i++) if(!blist.get(i).active) blist.remove(i);
		for(int i = 0; i < clist.size(); i++) if(!clist.get(i).active) clist.remove(i);
		for(int i = 0; i < slist.size(); i++) if(!slist.get(i).active) slist.remove(i);
		for(int i = 0; i < lootList.size(); i++) if(!lootList.get(i).active) lootList.remove(i);
	}

	public void controls(){
		if(Gdx.input.justTouched()){
			float x, y;
			Vector3 touchpos = new Vector3();
			touchpos.set(Gdx.input.getX(), Gdx.input.getY(),0);
			camera.unproject(touchpos);
			x = touchpos.x;
			y = touchpos.y;

			for(Button b : bulist)
				if(b.t != null && !b.t.hidden && b.t.hitbox().contains(x,y)){
					if(b.t.btn.getHitbox().contains(x,y)) b.t.hidden = true;
					return;
				}

			for(Cannon c : clist) if(c.getHitbox().contains(x,y)) return;

			for(Button b : bulist)
				if(b.getHitbox().contains(x,y)){
					if(b.t !=null && b.t.hidden && b.locked) { hidett(); b.t.hidden = false; return;}
					switch(b.type){
						case "cannon":
						case "fire":
						case "super":
						case "laser":
						case "double":
						case "saw":
							if(b.locked) {
								if(!(UI.money >= b.cost)) return;
								b.locked = false;
								UI.money -= b.cost;
								if(b.t != null) b.t.hidden = true;
							} else {
								selector(b.type);
								UI.currentType = b.type;
							}
							break;
						case "mounted":
							if(UI.money >= b.cost && b.locked){
								b.locked = false;
								UI.money -= b.cost;
								if(b.t !=null) b.t.hidden = true;
								break;
							}
						case "wall":
							if(b.type.equals("wall") && UI.money >= b.cost && wlist.size() < 1) {
								wlist.add(new Wall(wlist.size() * 50, 0));
								UI.money -= b.cost;
							} else {
								if(wlist.size() < 1 && UI.money >= b.cost) {
									wlist.add(new Wall(wlist.size() * 50, 0));
									for (int i = 0; i < 10; i++) {
										wlist.get(wlist.size() - 1).wall_cannons.add(new Cannon(UI.cannon_selector.get("mounted"), wlist.size() * 50 - 50, i * 50, 4));
									}
									UI.money -= b.cost;
								}
							}
							break;
						case "play":
						case "pause":
							if(b.type.equals("pause")) b.btex = resources.playButton; else b.btex = resources.pauseButton;
							if(b.type.equals("pause")) b.type = "play"; else b.type = "pause";
							paused = !paused;
							break;
						case "start":
						case "restart":
							createGame();
							return;
						case "exit":
							System.exit(0);
							break;
					}
				}

			if ( (y <= 200 && y >= 0) || (y <= 500 && y >= 300)) {
				if(UI.money >= UI.placeCosts.get(UI.currentType) && !UI.currentType.equals("saw")) {
					clist.add(new Cannon(UI.cannon_selector.get(UI.currentType),x ,y, 11));
					UI.money -= UI.placeCosts.get(UI.currentType);
				}
			}
			if (y >= 0 && y <= 500 && UI.currentType.equals("saw") && UI.money >= UI.placeCosts.get("saw")){
				slist.add(new Saw(0,y - resources.saw.getHeight()/2));
				UI.money -= UI.placeCosts.get("saw");
			}
		}


	}

	public void selector(String type){
		for(Button b : bulist) b.selected = b.type.equals(type);
	}

	public void hidett(){
		for(Button b : bulist) if(b.t != null) b.t.hidden = true;
	}

	@Override
	public void dispose () {
		batch.dispose();
	}

	public void createGame() {
		//control variable
		started = true;
		paused = false;
		UI.money = 2000;
		UI.score = 0;
		UI.life = 20;
		UI.wave = 0;


		//clear everything
		clist.clear();
		wlist.clear();
		zlist.clear();
		blist.clear();
		bulist.clear();
		lootList.clear();
		slist.clear();

		UI.unlockCosts.put("fire", 100);
		UI.unlockCosts.put("super", 100);
		UI.unlockCosts.put("double", 75);
		UI.unlockCosts.put("saw", 450);
		UI.unlockCosts.put("mounted", 200);
		UI.unlockCosts.put("laser", 500);
		UI.unlockCosts.put("wall", 30);

		int buttonStart = 200;
		bulist.add(new Button(resources.cannonIcon, "cannon", buttonStart + (bulist.size()* 75), 525));
		bulist.add(new Button(resources.doubleCannonIcon, "double", buttonStart + (bulist.size()* 75), 525));
		bulist.add(new Button(resources.fireCannonIcon, "fire", buttonStart + (bulist.size()* 75), 525));
		bulist.add(new Button(resources.superCannonIcon, "super", buttonStart + (bulist.size()* 75), 525));
		bulist.add(new Button(resources.laserCannonIcon, "laser", buttonStart + (bulist.size()* 75), 525));
		bulist.add(new Button(resources.wallIcon, "wall", buttonStart + (bulist.size()* 75), 525));
		bulist.add(new Button(resources.mountedCannonIcon, "mounted", buttonStart + (bulist.size()* 75), 525));
		bulist.add(new Button(resources.sawIcon, "saw", buttonStart + (bulist.size()* 75), 525));
		bulist.add(new Button(resources.pauseButton, "pause", 1024 - 75, 525));
		bulist.add(new Button(resources.restartButton, "restart", 1024 - 150, 525));

		//Tables

		//Cannon Resources
		UI.cannon_selector.put("cannon", resources.cannonTexture);
		UI.cannon_selector.put("fire", resources.fireCannonTexture);
		UI.cannon_selector.put("super", resources.superCannonTexture);
		UI.cannon_selector.put("double", resources.doubleCannonTexture);
		UI.cannon_selector.put("laser", resources.laserCannonTexture);
		UI.cannon_selector.put("mounted", resources.mountedCannonTexture);

		//Place Costs
		UI.placeCosts.put("cannon", 20);
		UI.placeCosts.put("fire", 100);
		UI.placeCosts.put("super", 100);
		UI.placeCosts.put("double", 50);
		UI.placeCosts.put("laser", 300);
		UI.placeCosts.put("mounted", 500);
		UI.placeCosts.put("saw", 500);



		//Animation Columns
		UI.columns.put("laser", 16);
		UI.columns.put("speedy", 6);

		//Zombie Textures
		UI.zombie_selector.put("grey", resources.greyZombieTexture);
		UI.zombie_selector.put("fast", resources.fastZombieTexture);
		UI.zombie_selector.put("speedy", resources.speedyZombieTexture);
		UI.zombie_selector.put("riot", resources.riotZombieTexture);
		UI.zombie_selector.put("riotB", resources.bigRiotZombieTexture);

		//Zombie Health
		UI.zombie_health.put("fast", 7);
		UI.zombie_health.put("speedy", 5);
		UI.zombie_health.put("riot", 30);
		UI.zombie_health.put("riotB", 500);

		//Zombie Speed
		UI.zombie_speed.put("fast", 4);
		UI.zombie_speed.put("speedy", 6);
		UI.zombie_speed.put("riot", 1);
		UI.zombie_speed.put("riotB", 1);

		//Zombie Score
		UI.zombie_score.put("fast", 2);
		UI.zombie_score.put("speedy", 3);
		UI.zombie_score.put("riot", 5);
		UI.zombie_score.put("riotB", 10);

		//Zombie Value
		UI.zombie_value.put("grey", 2);
		UI.zombie_value.put("fast", 3);
		UI.zombie_value.put("speedy", 5);
		UI.zombie_value.put("riot", 10);
		UI.zombie_value.put("riotB", 25);

		//Zombie Damage
		UI.zombie_damage.put("riot", 10);
		UI.zombie_damage.put("riotB", 1000);


		//Bullet Resources
		UI.bullet_selector.put("fire", resources.fireBulletTexture);
		UI.bullet_selector.put("super", resources.superBulletTexture);
		UI.bullet_selector.put("laser", resources.laserBulletTexture);

		//Bullet Damage
		UI.bullet_damage.put("fire", 4);
		UI.bullet_damage.put("laser", 50);
		UI.bullet_damage.put("super", 2);
		UI.bullet_damage.put("mounted", 1);

		//Bullet Speed
		UI.bullet_speed.put("fire", 3);
		UI.bullet_speed.put("super", 5);
		UI.bullet_speed.put("laser", 80);
		UI.bullet_speed.put("mounted", 1);



		//Type Tracking
		UI.currentType = "cannon";
		UI.loot_table.put("pouch", resources.zcoinPouch);

		lootList.add(new Loot("pouch", 115, 530, 0, 0));



	}


	//TODO: Complete zombie spawning logic


	public void spawnZombies() {
		if(!zlist.isEmpty()) return;
		String[] types = {"zzz", "grey", "fast", "speedy", "riot", "riotB"};
		UI.wave++;
		int zombiecap = 3; //PER WAVE
		Random r = new Random();
		for(int i=0; i < UI.wave * zombiecap; i++){
			switch(UI.wave){
				case 1:
					zombiecap = 1;
					zlist.add(new Zombie("zzz", 1024 + i * 36, r.nextInt(450)));
					break;
				case 2:
				case 3:
					zombiecap = 2;
					zlist.add(new Zombie("zzz", 1024 + i * 36, r.nextInt(450)));
					break;
				case 10:
					zlist.add(new Zombie("riotB", 1024 + i * 36, r.nextInt(450)));
					return;
				case 20:
					zlist.add(new Zombie("riotB", 1024 + i * 36, r.nextInt(450)));
					zlist.add(new Zombie("riotB", 1024 + i * 36, r.nextInt(450)));
					return;
				default:
					zlist.add(new Zombie(types[r.nextInt(types.length - 1)], 1024 + i * 36, r.nextInt(450)));
					break;
			}
		}
	}
	public void bulletZombieCollision(){
		for(Zombie z : zlist)
			for(Bullet b : blist)
				if(z.getHitbox().contains(b.x, b.y)){
						if(z.hp - b.damage <= 0) z.hp = 0; else z.hp -= b.damage;
						b.active = false;
				}
	}
	public void zombieCannonCollision(){
		for(Cannon c : clist)
			for(Zombie z : zlist)
				if(c.getHitbox().contains(z.x, z.y)){
					c.health-= z.damage;
					if(z.damage < 999) z.active = false;
				}
	}
	public void coinPouchCollision(){
		for(int i=1; i < lootList.size(); i++){
			if(lootList.get(i).type.equals("coin") && lootList.get(0).getHitbox().contains(lootList.get(i).x, lootList.get(i).y)){
				UI.money += lootList.get(i).item_value;
				lootList.get(i).active = false;
			}
		}
	}
	public void zombieWallCollision(){
		if(wlist.isEmpty()) return;

		for(Zombie z : zlist)
			if(wlist.get(wlist.size() - 1).getHitbox().contains(z.x, z.y)){
				wlist.get(wlist.size() - 1).durability--;
				z.active = false;
			}
	}
	public void sawZombieCollision(){
		for(Saw s : slist)
			for(Zombie z : zlist)
				if(s.getHitbox().contains(z.x, z.y))
					z.hp = 0;

	}
}
