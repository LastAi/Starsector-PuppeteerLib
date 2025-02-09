package data.scripts.weapons.effects.anim;

import com.fs.starfarer.api.AnimationAPI;
import com.fs.starfarer.api.combat.WeaponAPI;
import com.fs.starfarer.api.graphics.SpriteAPI;
import com.fs.starfarer.api.loading.WeaponSlotAPI;

public class AnimPuppeteer {
    //this should say pre not per
    public static int perAnimation(WeaponAPI weapon, AnimDataContainer animation){
        if(animation.getColorGlow() != null) {
            weapon.setGlowAmount(1f, animation.getColorGlow());
        }
        if(animation.getColor() != null){
            weapon.getSprite().setColor(animation.getColor());
        }
        return animation.getStart();
    }

    public static void executeAnimation(ProgressTracker progressTracker, WeaponAPI weapon, WeaponSlotAPI slot, AnimationAPI imageSet, AnimDataContainer animation){

        if(progressTracker.getProgress() != 0){
            AnimDataRow dopeRow = null;
            do{
                dopeRow = animation.getDopesheet().get(progressTracker.getCurrentDopeFrame());
                if(dopeRow.getRotateBy() != null){
                    computeSlotRotate(slot, dopeRow.getRotateBy());
                }
                if(dopeRow.getMoveBy() != null){
                    computeSlotMove(weapon.getSprite(),dopeRow.getMoveBy().one,dopeRow.getMoveBy().two);
                }
            }
            while(animation.getAnimationType().advance(progressTracker));

            if(imageSet !=null){
                imageSet.setFrame(dopeRow.getFrame());
            }

            if(animation.getColorGlow() != null) {
                weapon.setGlowAmount(1f, animation.getColorGlow());
            }
            if(animation.getColor() != null){
                weapon.getSprite().setColor(animation.getColor());
            }
        }
    }

    private static void computeSlotMove(SpriteAPI sprite, float x, float y){
        //if im not dumb offseting the center should be oppoite an actual move.
        sprite.setCenterX(sprite.getCenterX()-x);
        sprite.setCenterY(sprite.getCenterY()-y);
        //slot.getLocation().setX(x + slot.getLocation().getX());
        //slot.getLocation().setY(y + slot.getLocation().getY());
    }
    private static void  computeSlotRotate(WeaponSlotAPI slot, float amount){
        float currentAngle =  slot.getAngle() % 360f;
        float newAngle = (currentAngle + amount) % 360f;
        slot.setAngle(newAngle);
    }


}
