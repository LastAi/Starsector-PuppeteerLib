package data.scripts.weapons.effects.anim;

import com.fs.starfarer.api.util.Pair;

import java.awt.Color;

public class AnimDataRow {
    protected Integer frame;
    protected Color color;
    protected Color colorGlow;
    protected Float rotateBy;
    protected Pair<Float,Float> moveBy;
    protected Pair<Float,Float> moveTo;
    protected Float rotateTo;

    public Pair<Float,Float> getMoveBy() {
        return moveBy;
    }

    public void setMoveBy(Pair<Float,Float> moveBy) {
        this.moveBy = moveBy;
    }

    public AnimDataRow(){}

    public Float getRotateBy() {
        return rotateBy;
    }

    public Color getColorGlow() {
        return colorGlow;
    }

    public Integer getFrame() {
        return frame;
    }

    public Color getColor() {
        return color;
    }

    public void setFrame(Integer frame) {
        this.frame = frame;
    }

    public void setColor(Color color) {
        this.color = color;
    }

    public void setColorGlow(Color colorGlow) {
        this.colorGlow = colorGlow;
    }

    public void setRotateBy(Float rotateBy) {
        this.rotateBy = rotateBy;
    }
}
