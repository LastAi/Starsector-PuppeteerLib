package data.scripts.weapons.effects;

import com.fs.starfarer.api.Global;
import com.fs.starfarer.api.combat.CombatEngineAPI;
import com.fs.starfarer.api.combat.EveryFrameWeaponEffectPlugin;
import com.fs.starfarer.api.combat.WeaponAPI;
import com.fs.starfarer.api.loading.WeaponSlotAPI;
import com.fs.starfarer.api.loading.WeaponSpecAPI;
import data.scripts.weapons.effects.anim.AnimDataContainer;
import data.scripts.weapons.effects.anim.AnimPuppeteer;
import data.scripts.weapons.effects.anim.ParseAnimationDataUtil;
import data.scripts.weapons.effects.anim.ProgressTracker;

public class DecoAnimated implements EveryFrameWeaponEffectPlugin {

    private boolean setup = false;
    private boolean setupFailed = false;
    private WeaponSlotAPI slot;
    private AnimDataContainer animationData;
    WeaponSpecAPI origWep;
    private float elapsedTime = 0;

    private ProgressTracker progressTracker;

    @Override
    public void advance(float amount, CombatEngineAPI engine, WeaponAPI weapon) {
        if(setupFailed || (progressTracker != null && progressTracker.getStop())) { // no point in doing anymore if it failed.
            //Global.getLogger(DecoAnimated.class).warn(origWep.getWeaponName() + setupFailed + progressTracker);
            return;
        }
        if (engine.isPaused()) return;

        if(!setup){
            progressTracker = new ProgressTracker();
            origWep = weapon.getOriginalSpec();
            slot = weapon.getSlot();
            try{
                animationData = ParseAnimationDataUtil.parseTags(origWep.getTags());
                assert animationData != null;
                if(animationData.getDopesheet().isEmpty()){
                    throw new Exception("Dope Can't be empty!");
                }
                ParseAnimationDataUtil.parseName(animationData,slot.getId()); //if nothing is found it just moves on.
                progressTracker.setTotalDopeFrames(animationData.getDopesheet().size());
                progressTracker.setCurrentDopeFrame(AnimPuppeteer.perAnimation(weapon,animationData));

                AnimPuppeteer.executeAnimation(progressTracker,weapon,slot,weapon.getAnimation(),animationData);
                setup = true;
                setupFailed = false;
            }catch (Exception e){
                setupFailed = true;
                Global.getLogger(DecoAnimated.class).warn("Setup failed for ["+ origWep.getWeaponName()+"] in slot ["+slot.getId()+"]");
                if(weapon.getAnimation() != null){
                    weapon.getAnimation().setFrame(0);
                }
                return;
            }
        }
        elapsedTime += amount;

        if (elapsedTime > animationData.getRate()) {
            progressTracker.setProgress((int) (elapsedTime / animationData.getRate())); ;
            elapsedTime = elapsedTime % animationData.getRate();

            if(!progressTracker.getStop()){
                AnimPuppeteer.executeAnimation(progressTracker,weapon,slot,weapon.getAnimation(),animationData);
            }
            progressTracker.updatePrevious();
        }
        Global.getLogger(DecoAnimated.class).warn(origWep.getWeaponName() + progressTracker.toString());
    }
}
