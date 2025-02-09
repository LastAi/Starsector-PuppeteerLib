package data.scripts.weapons.effects.anim;

import com.fs.starfarer.api.util.Pair;

import java.util.ArrayList;
import java.util.List;

import static data.scripts.weapons.effects.anim.AnimationType.FORWARD;

public class AnimDataContainer extends AnimDataRow {
    private List<AnimDataRow> dopesheet = new ArrayList<>();
    private AnimationType animationType = FORWARD;;
    private float rate = 1f;

    public float getRate() {
        return rate;
    }

    public void setRate(float rate) {
        this.rate = rate;
    }

    public List<AnimDataRow> getDopesheet() {
        return dopesheet;
    }

    public AnimationType getAnimationType() {
        return animationType;
    }

    public void setAnimationType(AnimationType animationType) {
        this.animationType = animationType;
    }

    public void setRotateConstant(Float rotateConstant) {
        rotateBy = rotateConstant;
    }

    public Float getRotateConstant() {
        return rotateBy;
    }

    public Integer getStart() {
        if(frame == null) return 0;
        return frame;
    }

    /**
     * We have made it clear that what's at the AnimDataContainer
     * is a rate not a single turn
     */
    @Deprecated
    @Override
    public Float getRotateBy(){
        return null;
    }

    /**
     * AnimDataContainer Doesn't handle moves
     */
    @Deprecated
    @Override
    public Pair<Float,Float> getMoveBy(){
        return null;
    }

}
