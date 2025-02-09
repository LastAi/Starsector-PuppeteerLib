# Starsector MOD - Puppeteer Library

v0.1

## Description

This is a library for controlling complex animations with tags rather than needing either excessive frame animation or bespoke one off class animation controls
*a better discription will come later.

### How it works:
Decoration Weapon is created like normal with "everyFrameEffect":"data.scripts.weapons.effects.DecoAnimated", 

The first time DecoAnimated is called by an object it sets up the animation data only once, then executes based on the rules.
<br/><br/>
From the example the doors open based on a set of rules.

Lets read these instruction from the weapon tags "anim.dope:12:0;26:0@mov-0.5:0, anim.rate:0.06"<br/>
> anim.dope:12:0;26:0@mov-0.5:0

Tag Prefix "anim.dope:" Create a dopesheet (dopesheets were used by to 2d animators for frame timing, also know in film as exposure sheets)<br/>
<ins>First instruction</ins> "12:0" for 12 frames us the image 0 from the sequence<br/>
<ins>Instruction Separator</ins> ";"<br/>
<ins>Second instruction</ins> "26:0@mov-0.5:0" for 26 frames us the image 0 from the sequence AND execute a Move of -0.5 pixels




### Example
![it worked.gif](readme_files/it%20worked.gif)
#### Only 7 images for the Decoration Slots + the hull
![src-images.png](readme_files/src-images.png)
![WeaponCSV.png](readme_files/WeaponCSV.png)

### Current Supported Actions
#### Actions have a tag prefixs and attribute prefixs, tags start with "anim.****:"
"anim.type:","at" = Sets the way it animation is executed [Forwad, Once, Pingpong]<br/>
"anim.rate:","r" = Sets the Animation Timing Rate the fasted the game ticks go is 0.0166~, due to size you probably cant see a difference between 0.05 and 0.016<br/>
"anim.start:","s" = Set what frame of the dope to start on, this is great for off setting animations. Think blink lights that seem to move in a direction because they are offset.<br/>
"anim.color:","c" = Sets Color. needs more testing.<br/>
"anim.glow:","g" = Sets Glow Color. needs more testing.<br/>
"anim.rotate:","rot" = Set a rotation. As a Tag this sets a constant rate per dope frame. As an attribute this sets an amount to rotate by. <br/>
null,"mov" = As an attribute this sets an amount to move by. There is not Tag for this. You should never have constant movement. Move is probably the most likely to cause issues.<br/>
### Future Planned Actions
Rotate to exact position.
Move to exact position.
Spawn effects like particles when called in the dope.
Arbitrary Function execution when called in the dope.
External Triggers, think if an animation set to ONCE, could be triggered to start again, based on external actions.

## Thanks to Wisp
Author of https://github.com/wispborne/Starsector-IntelliJ-Template
Debugging would not have been possible without Starsector-IntelliJ-Template project