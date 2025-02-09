package data.scripts.weapons.effects.anim;

import com.fs.starfarer.api.Global;
import com.fs.starfarer.api.util.Pair;

import java.awt.Color;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;

/**
 * Takes in coming data
 * Tags are parsed first,
 * Slot Data from the slotname can override existing data
 */
public class ParseAnimationDataUtil {

    private static final String NAME_PREFIX_REGEX = "anim\\[";
    private static final String NAME_SUFFIX_REGEX = "]mate";
    /**
     * used by both dopesheet and name data
     */
    private static final String ATTRIBUTE_DELIMITER = "@";
    private static final String DATA_DELIMITER = ":";
    private static final String DOPE_DELIMITER = ";";


    public enum AnimationDataEnum {
        REF("anim.ref:","ref", false) {
            @Override
            void parse(String input, AnimDataRow container) {
                //Stubbed for future use will make it so input is a key to look up a json of the AnimDataContainer
            }
        },
        DOPE("anim.dope:",null,false){
            @Override
            void parse(String input, AnimDataRow container) throws Exception {
                String[] frameInstructions = input.split(DOPE_DELIMITER);
                for(String frameInstruction :frameInstructions){
                    List<String> attributes = new ArrayList<String>(Arrays.asList(frameInstruction.split(ATTRIBUTE_DELIMITER)));
                    String frameData = attributes.remove(0);
                    String[] frameDataSplit = frameData.split(DATA_DELIMITER);
                    int times = Integer.parseInt(frameDataSplit[0]);
                    Integer frame = Integer.parseInt(frameDataSplit[1]);
                    AnimDataRow dope = new AnimDataRow();
                    parseAttributes(dope,attributes);
                    dope.setFrame(frame);
                    for(int i = 0; i < times; i++){
                        ((AnimDataContainer)container).getDopesheet().add(dope);
                    }
                }
            }
        },
        ANIM_TYPE("anim.type:","at",true) {
            @Override
            void parse(String input, AnimDataRow container) {
                ((AnimDataContainer)container).setAnimationType(AnimationType.valueOf(input));
            }
        },
        RATE("anim.rate:","r",true) {
            @Override
            void parse(String input, AnimDataRow container) {
                ((AnimDataContainer)container).setRate(Float.parseFloat(input));
            }
        },
        START("anim.start:","s",true) {
            @Override
            void parse(String input, AnimDataRow container) {
                container.setFrame(Integer.parseInt(input));
            }
        },
        COLOR("anim.color:","c",true) {
            @Override
            void parse(String input, AnimDataRow container) throws Exception {
                container.setColor(createColor(input.split(DATA_DELIMITER)));
            }
        },
        GLOW("anim.glow:","g",true) {
            @Override
            void parse(String input, AnimDataRow container) throws Exception {
                container.setColorGlow(createColor(input.split(DATA_DELIMITER)));
            }
        },
        /**
         * If used by itself its considered a rate of rotation and is added to current rotation.
         */
        ROTATE("anim.rotate:","rot",true) {
            @Override
            void parse(String input, AnimDataRow container) {
                container.setRotateBy(Float.parseFloat(input));
            }
        },
        /**
         * Is only used for the dopesheet
         */
        MOVE(null,"mov",true) {
            @Override
            void parse(String input, AnimDataRow container) {
                String[] data = input.split(DATA_DELIMITER);
                container.setMoveBy(new Pair<Float,Float>(Float.parseFloat(data[0]),Float.parseFloat(data[1])));
            }
        },
//        /**
//         * Is only used for the dopesheet
//         */
//        ROTATE_GOTO(null,"gotorot",true) {
//            @Override
//            void parse(String input, AnimDataRow container) {
//                container.setRotateBy(Float.parseFloat(input));
//            }
//        },
//        /**
//         * Is only used for the dopesheet
//         */
//        MOVE_GOTO(null,"gotomov",true) {
//            @Override
//            void parse(String input, AnimDataRow container) {
//                String[] data = input.split(DATA_DELIMITER);
//                container.setMoveBy(new Pair<>(Float.parseFloat(data[0]),Float.parseFloat(data[1])));
//            }
//        };
        ;

        private final String tagPrefix;
        private final String attributePrefix;
        private final boolean isAttribute;

        AnimationDataEnum(String tagPrefix, String attributePrefix, boolean isAttribute) {
            this.tagPrefix = tagPrefix;
            this.attributePrefix = attributePrefix;
            this.isAttribute = isAttribute;
        }

        public String getTagPrefix(){
            return tagPrefix;
        }

        public String getAttributePrefix(){
            return attributePrefix;
        }

        public void doParse(String input, AnimDataRow container) throws Exception{
            try{
                parse(input,container);
            }catch (Exception e){
                throw new Exception("Failed parse:"+this, e);
            }
        }

        public static List<AnimationDataEnum> valuesAttributes(Boolean prefix){
            List<AnimationDataEnum> filteredList = new ArrayList<>();
            for (AnimationDataEnum obj : AnimationDataEnum.values()) {
                if (obj.isAttribute) {
                    if(obj.getTagPrefix() != null && !prefix){
                        continue;
                    }
                    filteredList.add(obj);
                }
            }
            return filteredList;
        }

        abstract void parse(String input, AnimDataRow container) throws Exception;
    }

    /**
     * Format - anim.ANIMDATATYPE:DATA
     * Example - anim.dope:1:2@c0:0:0@g0:0:0;5:0
     *      This reads Dopesheet for 1 time use frame 2, use color 0,0,0 and glow 0,0,0
     *      then for 5 times use frame 0
     * Example2 - anim.stat:3
     *      This tells the animation to start on time 3
     * @param tags - these are the tags that come from the WeaponSpec
     */
    public static AnimDataContainer parseTags(Set<String> tags){
        try {
            AnimDataContainer container = new AnimDataContainer();
            for (String tag : tags) {
                if (tag !=null && tag.startsWith("anim.")) {
                    for (AnimationDataEnum prefix : AnimationDataEnum.values()) {
                        if(prefix.getTagPrefix() == null){
                            continue;
                        }
                        String data = removeMatch(tag, prefix.getTagPrefix());
                        if (data != null) {
                            prefix.doParse(data, container);
                        }
                    }
                }
            }
            return container;
        }catch (Exception e){
            Global.getLogger(ParseAnimationDataUtil.class).error("Failed parsing animation Tags", e);
            return null;
        }
    }

    /**
     * mutates existing container
     * @param container
     * @param name
     */
    public static  void parseName(AnimDataContainer container, String name) {
        try {
            if (name.contains("anim[")) {
                String[] beginning = name.split(NAME_PREFIX_REGEX);
                String[] ending = beginning[1].split(NAME_SUFFIX_REGEX);
                parseAttributes(container,ending[0]);
            }
        } catch (Exception e) {
            Global.getLogger(ParseAnimationDataUtil.class).warn("Failed parsing animation data from Slot Name", e);
        }
    }

    public static void parseAttributes(AnimDataContainer container, String row){
        try{
            List<String> rowData = new ArrayList<String>(Arrays.asList(row.split(ATTRIBUTE_DELIMITER)));
            parseAttributes(container,rowData);
        }catch (Exception e){
            Global.getLogger(ParseAnimationDataUtil.class).error("Failed parsing animation Attributes", e);
        }
    }
    public static void parseAttributes(AnimDataRow container, List<String> row) throws Exception {
        List<AnimationDataEnum> attributes = new ArrayList<AnimationDataEnum>(AnimationDataEnum.valuesAttributes(true));
        for(String data : row){
            if(!data.isEmpty()){
                for(AnimationDataEnum prefix : attributes){
                    String attribute = removeMatch(data,prefix.getAttributePrefix());
                    if(attribute !=null){
                        attributes.remove(prefix);
                        prefix.doParse(attribute, container);
                        break;
                    }
                }
            }
        }
    }

    private static boolean startsWith(String tag, String prefix){
        return tag.startsWith(prefix);
    }
    private static String removeMatch(String tag, String prefix){
        if(startsWith(tag, prefix)){
            return tag.replace(prefix,"");
        }else return null;
    }

    private static Color createColor(String[] rgba) throws Exception {
        if(rgba.length==3){
            return new Color(Integer.parseInt(rgba[0]),
                    Integer.parseInt(rgba[1]),
                    Integer.parseInt(rgba[2]));
        }else if(rgba.length==4){
            return new Color(Integer.parseInt(rgba[0]),
                    Integer.parseInt(rgba[1]),
                    Integer.parseInt(rgba[2]),
                    Integer.parseInt(rgba[3]));
        }else{
            throw new Exception("Color was not 3 or 4 values. " + Arrays.toString(rgba));
        }
    }
}
