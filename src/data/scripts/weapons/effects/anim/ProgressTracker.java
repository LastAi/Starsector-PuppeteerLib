package data.scripts.weapons.effects.anim;

public class ProgressTracker {
    private Boolean stop = false;
    private Integer currentDopeFrame = 0;
    private Integer perviousDopeFrame = 0;
    private Boolean isProgessingForward = true;
    private Integer progress = 0;
    private Integer totalDopeFrames = 0;

    public Integer getTotalDopeFrames() {
        return totalDopeFrames;
    }

    public void setTotalDopeFrames(Integer totalDopeFrames) {
        this.totalDopeFrames = totalDopeFrames;
    }

    public void updatePrevious(){
        perviousDopeFrame = currentDopeFrame;
    }
    public Boolean getStop() {
        return stop;
    }

    public void setStop(Boolean stop) {
        this.stop = stop;
    }

    public void stop() {
        this.stop = true;
    }

    public Integer getCurrentDopeFrame() {
        return currentDopeFrame;
    }

    public void setCurrentDopeFrame(Integer currentDopeFrame) {
        this.currentDopeFrame = currentDopeFrame;
    }

    public void currentDopeFrameFoward(){
        this.currentDopeFrame++;
    }
    public void currentDopeFrameBackward(){
        this.currentDopeFrame--;
    }

    public Integer getPerviousDopeFrame() {
        return perviousDopeFrame;
    }

    public void setPerviousDopeFrame(Integer perviousDopeFrame) {
        this.perviousDopeFrame = perviousDopeFrame;
    }

    public Boolean isProgessingForward() {
        return isProgessingForward;
    }

    public void setProgessingForward(Boolean progessingForward) {
        isProgessingForward = progessingForward;
    }

    public Integer getProgress() {
        return progress;
    }

    public void setProgress(Integer progress) {
        this.progress = progress;
    }
    @Override
    public String toString(){
        return "\n" +
                "Stop :"+stop+"\n" +
                "Cur  :"+currentDopeFrame+"\n" +
                "Pre  :"+perviousDopeFrame+"\n" +
                "Prog :"+progress+"\n" +
                "Forw :"+isProgessingForward+"\n" +
                "Total:"+totalDopeFrames+"\n";
    }

}
