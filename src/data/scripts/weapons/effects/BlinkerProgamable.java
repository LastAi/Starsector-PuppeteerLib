package data.scripts.weapons.effects;

import java.util.*;

import com.fs.starfarer.api.combat.CombatEngineAPI;
import com.fs.starfarer.api.combat.EveryFrameWeaponEffectPlugin;
import com.fs.starfarer.api.combat.ShipAPI;
import com.fs.starfarer.api.combat.WeaponAPI;
import com.fs.starfarer.api.loading.WeaponSlotAPI;
import com.fs.starfarer.api.loading.WeaponSpecAPI;
import com.fs.starfarer.api.Global;
import java.awt.Color;

/**
 * Programible Blinker require a mininmum of 2 frames
 */
@Deprecated
public class BlinkerProgamable implements EveryFrameWeaponEffectPlugin {

	private float elapsed = 0;
	private int currentFrame = 0;
	private float frameRate = 1f;

	private boolean setup = false;
	private boolean setupFailed = false;

	private boolean forwards = true;
	/**
	 * You must have at least 2 frames in your dopesheet.
	 */
	private List<Integer> dopesheet ;
	//Create and enum for this later
	private boolean pingpong = false;

	private Color color;
	private Color colorGlow;

	private boolean on = true;

	public void advance(float amount, CombatEngineAPI engine, WeaponAPI weapon) {

		if (engine.isPaused()) return;
//		Global.getLogger(BlinkerProgamable.class).warn("doing anim");
		WeaponSpecAPI origWeap = weapon.getOriginalSpec();
		WeaponSlotAPI slot = weapon.getSlot();

		if(setupFailed) {
			// if setup failed, dont crash the whole game because of it. its just decrative
			Global.getLogger(BlinkerProgamable.class).warn("Failed: "+origWeap.getWeaponName());
			weapon.getAnimation().setFrame(0);
			return;
		}

		if(!setup){
			//todo remove loggers later
//			Global.getLogger(BlinkerProgamable.class).warn("doing setup for anim");
			Set<String> tags = origWeap.getTags();
//			Global.getLogger(BlinkerProgamable.class).warn("Getting tags");
			for (String tag : tags) {
//				Global.getLogger(BlinkerProgamable.class).warn("Tag: "+ tag);
				if(tag.startsWith("anim.dope:")){
//					Global.getLogger(BlinkerProgamable.class).warn("Seting up dope.");
					if(!createDopesheet(tag.replace("anim.dope:",""))){
						setupFailed = true;
//						Global.getLogger(BlinkerProgamable.class).warn("Dope setup failed.");
						return;
					}
				}
				if(tag.startsWith("anim.start:")){
//					Global.getLogger(BlinkerProgamable.class).warn("Setting up start.");
					currentFrame = Integer.parseInt(tag.replace("anim.start:",""));
				}
				if(tag.startsWith("anim.rate:")){
//					Global.getLogger(BlinkerProgamable.class).warn("Setting rate.");
					int rate = Integer.parseInt(tag.replace("anim.rate:",""));
					switch(rate) {
						case 4:
							frameRate = 0.25f;
							break;
						case 3:
							frameRate = 0.33f;
							break;
						case 2:
							frameRate = 0.5f;
							break;
						case 1:
						default:
							frameRate = 1f;
							break;
					}
//					Global.getLogger(BlinkerProgamable.class).warn("Rate: "+frameRate);
				}
			}
			if(dopesheet == null){
				dopesheet = new ArrayList<Integer>();
				dopesheet.add(0);
				dopesheet.add(1);
//				Global.getLogger(BlinkerProgamable.class).warn("No dope found: "+origWeap.getWeaponName());
			}
//			Global.getLogger(BlinkerProgamable.class).warn("Start: "+ currentFrame);
//			Global.getLogger(BlinkerProgamable.class).warn("Rate: "+ frameRate);

			getAdditionalInfo(slot.getId());

			if(color != null){
				weapon.getSprite().setColor(color);
			}
			if(colorGlow != null){
				weapon.setGlowAmount(1f, colorGlow);
			}

			setup = true;
			setupFailed = false;
			Global.getLogger(BlinkerProgamable.class).warn("Setup: "+ setup);
			Global.getLogger(BlinkerProgamable.class).warn("setupFailed: "+ setupFailed);
		}

		elapsed += amount;
		Global.getLogger(BlinkerProgamable.class).warn("elapsed:"+elapsed);
		if (elapsed > frameRate) {
			float framesProgessed = elapsed / frameRate;

			elapsed = elapsed % frameRate;
			if(forwards){
				currentFrame += (int) framesProgessed;
			}else{
				currentFrame -= (int) framesProgessed;
			}
			if(pingpong){ //needs work
				if(currentFrame >= dopesheet.size()){
					currentFrame = (currentFrame - dopesheet.size());
					forwards = false;
				}
				if(currentFrame < 0){
					currentFrame = Math.abs(currentFrame);
					forwards = true;
				}
			}else{
				if(currentFrame >= dopesheet.size()){
					//should protect timing form lag
					currentFrame = (currentFrame - dopesheet.size());
//					Global.getLogger(BlinkerProgamable.class).warn("anim restart "+currentFrame);
				}
			}
		}

//		Global.getLogger(BlinkerProgamable.class).warn("cf="+currentFrame);

		ShipAPI ship = weapon.getShip();
		if (ship.getFluxTracker().isVenting()) {
			on = false;
		} else if (ship.getFluxTracker().isOverloaded()) {
			on = new Random().nextInt(4) == 3;
		}


//		Global.getLogger(BlinkerProgamable.class).warn("ds"+dopesheet);
		if (on) {
			int frameToSet = (Integer) dopesheet.get(currentFrame);
			weapon.getAnimation().setFrame(frameToSet);
		} else {
			weapon.getAnimation().setFrame(0);
		}


	}

	private boolean createDopesheet(String dopeSheetString){
//		Global.getLogger(BlinkerProgamable.class).warn("Dope: "+ dopeSheetString);
		try{
			dopesheet = new ArrayList<Integer>();
			for(String row : dopeSheetString.split(";")){
//				Global.getLogger(BlinkerProgamable.class).warn("row: "+ row);
				String[] instruct = row.split(":");

				int times = Integer.parseInt(instruct[0]);
				int frame = Integer.parseInt(instruct[1]);

//				Global.getLogger(BlinkerProgamable.class).warn("T:"+times+" F:"+frame);
				for(int i = 0; i < times; i++){
//					Global.getLogger(BlinkerProgamable.class).warn("I:"+i);
					dopesheet.add(frame);
				}
			}

			if(dopesheet.size() < 2){
				return false;
			}
		}catch(Exception e){
			Global.getLogger(BlinkerProgamable.class).warn("Dope Fail: ",e);
			return false;
		}
		return true;
	}

	private boolean getAdditionalInfo(String data) {
		try {
			if (data.contains("anim[")) {
				String[] beginning = data.split("anim\\[");
				String[] ending = beginning[1].split("]mate");
				setAdditionalData(ending[0]);

			}
		} catch (Exception e) {
			Global.getLogger(BlinkerProgamable.class).warn("Name info Fail: ", e);
			return false;
		}
		return true;
	}
	private void setAdditionalData(String data){
		Global.getLogger(BlinkerProgamable.class).warn("reading: "+data);
		String[] dataArray = data.split(";");
		for(String info : dataArray){
			if(info.startsWith("c")){
				String[] colors = info.replace("c","").split(":");
				Global.getLogger(BlinkerProgamable.class).warn("c: "+ Arrays.toString(colors));
				color = new Color(Integer.parseInt(colors[0]),Integer.parseInt(colors[1]),Integer.parseInt(colors[2]));
			}
			if(info.startsWith("g")){
				String[] colors = info.replace("g","").split(":");
				Global.getLogger(BlinkerProgamable.class).warn("g: "+ Arrays.toString(colors));
				colorGlow = new Color(Integer.parseInt(colors[0]),Integer.parseInt(colors[1]),Integer.parseInt(colors[2]));
			}
			if(info.startsWith("b")){
				String[] colors = info.replace("b","").split(":");
				Global.getLogger(BlinkerProgamable.class).warn("b: "+ Arrays.toString(colors));
				color = new Color(Integer.parseInt(colors[0]),Integer.parseInt(colors[1]),Integer.parseInt(colors[2]));
				colorGlow = new Color(Integer.parseInt(colors[0]),Integer.parseInt(colors[1]),Integer.parseInt(colors[2]));
			}
			if(info.startsWith("s")){
				currentFrame = Integer.parseInt(info.replace("s",""));
				Global.getLogger(BlinkerProgamable.class).warn("s: "+ currentFrame);
			}
			if(info.startsWith("at")){
				Global.getLogger(BlinkerProgamable.class).warn("at: "+ info.replace("at",""));
				if(info.replace("at","").equals("PINGPONG")){
					pingpong = true;
				}
			}
		}
	}

}
