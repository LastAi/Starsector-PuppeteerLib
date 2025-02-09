package data.scripts.weapons.effects.anim;

public enum AnimationType {
    FORWARD {// start from frame 0 when reaching the end
        @Override
        public boolean advance(ProgressTracker progressTracker) {

            if(progressTracker.getProgress() == 0){
                return false;
            }
            progressTracker.setProgress(progressTracker.getProgress()-1);

            if(progressTracker.isProgessingForward()){
                progressTracker.currentDopeFrameFoward();
                if(progressTracker.getCurrentDopeFrame() >= progressTracker.getTotalDopeFrames()){
                    progressTracker.setCurrentDopeFrame(0);
                }
                return true;
            }else {
                progressTracker.currentDopeFrameBackward();
                if(progressTracker.getCurrentDopeFrame() < 0){
                    progressTracker.setCurrentDopeFrame(progressTracker.getTotalDopeFrames()-1);
                }
                return true;
            }



//            Integer newCurFrame;
//            if(progressTracker.isProgessingForward()){
//                newCurFrame = progressTracker.getCurrentDopeFrame() + progressTracker.getProgress();
//                if(newCurFrame >= totalFrames) {
//                    newCurFrame = (newCurFrame - totalFrames);
//                }
//            }else{
//                newCurFrame = progressTracker.getCurrentDopeFrame() - progressTracker.getProgress();
//                if(newCurFrame < 0){
//                    newCurFrame = totalFrames-newCurFrame;
//                }
//            }

        }
    },
    ONCE {// FORWARD but holds the last frame and doesn't animate any longer after reaching the end
        @Override
        public boolean advance(ProgressTracker progressTracker) {

            if(progressTracker.getProgress() == 0){
                return false;
            }
            progressTracker.setProgress(progressTracker.getProgress()-1);

            if(progressTracker.isProgessingForward()){
                progressTracker.currentDopeFrameFoward();
                if(progressTracker.getCurrentDopeFrame() >= progressTracker.getTotalDopeFrames()){
                    progressTracker.stop();
                    return false;
                }
                return true;
            }else {
                progressTracker.currentDopeFrameBackward();
                if(progressTracker.getCurrentDopeFrame() < 0){
                    progressTracker.stop();
                    return false;
                }
                return true;
            }

//            Integer newCurFrame;
//            if(progressTracker.isProgessingForward()){
//                newCurFrame = progressTracker.getCurrentDopeFrame() + progressTracker.getProgress();
//                if(newCurFrame >= totalFrames) {
//                    progressTracker.stop();
//                }
//            }else{
//                newCurFrame = progressTracker.getCurrentDopeFrame() - progressTracker.getProgress();
//                if(newCurFrame < 0){
//                    progressTracker.stop();
//                }
//            }

        }
    },
    PINGPONG {// apon reaching the end of frame repeat but going in backwards, and then forwards apon reaching frame 0
        @Override
        public boolean advance(ProgressTracker progressTracker) {
//            Integer newCurFrame;
//            if(progressTracker.isProgessingForward()){
//                newCurFrame = progressTracker.getCurrentDopeFrame() + progressTracker.getProgress();
//                if(newCurFrame >= totalFrames){
//                    newCurFrame = (totalFrames-1)-(newCurFrame-totalFrames);
//                    progressTracker.setCurrentDopeFrame(newCurFrame);
//                    progressTracker.setProgessingForward(false);
//                }
//            }else{
//                newCurFrame = progressTracker.getCurrentDopeFrame() + progressTracker.getProgress();
//                if(newCurFrame < 0){
//                    newCurFrame = -newCurFrame;
//                    progressTracker.setCurrentDopeFrame(newCurFrame);
//                    progressTracker.setProgessingForward(true);
//                }
//            }

            return false;
        }
    };

    AnimationType(){}

    public abstract boolean advance(ProgressTracker progressTracker);
}
