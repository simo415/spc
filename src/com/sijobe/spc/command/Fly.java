package com.sijobe.spc.command;

import com.sijobe.spc.core.IPlayerSP;
import com.sijobe.spc.util.KeyListener;
import com.sijobe.spc.util.KeyboardHandler;
import com.sijobe.spc.wrapper.CommandException;
import com.sijobe.spc.wrapper.CommandSender;
import com.sijobe.spc.wrapper.Coordinate;
import com.sijobe.spc.wrapper.Minecraft;
import com.sijobe.spc.wrapper.Player;

import java.util.List;

import org.lwjgl.input.Keyboard;

@Command (
   name = "fly",
   description = "Enables and disables flying for the player",
   example = "",
   videoURL = "http://www.youtube.com/watch?v=4ZOvu3hf7k0",
   enabled = true
)
public class Fly extends StandardCommand implements IPlayerSP, KeyListener {
   
   private static final int KEY_JUMP = Keyboard.KEY_SPACE;
   private static final int KEY_CROUCH = Keyboard.KEY_LSHIFT;
   
   private static final KeyboardHandler HANDLER = KeyboardHandler.getInstance();
   
   private Player player;
   
   private boolean isJumping;
   private boolean isCrouching;
   
   public Fly() {
      HANDLER.addKeyPressedListener(KEY_JUMP, this);
      HANDLER.addKeyPressedListener(KEY_CROUCH, this);
      isJumping = false;
      isCrouching = false;
   }
   
   @Override
   public void execute(CommandSender sender, List<?> params) throws CommandException {
      // TODO Auto-generated method stub
      
   }
   
   @Override
   public boolean isEnabled() {
      return false;
      // return Minecraft.isSinglePlayer();
   }

   @Override
   public void init(Object... params) {}

   @Override
   public void onTick(Player player) {}

   @Override
   public void movePlayer(Player player, float forward, float strafe, float speed) {
      this.player = player;
      Coordinate motion = player.getMotion();
      float pitch = player.getPitch();
      if (player.getMovementForward() == 0 && !isJumping && !isCrouching) {
         player.setMotion(new Coordinate(motion.getX(), 0, motion.getZ()));
         return;
      } else if (player.getMovementForward() > 0) {
         pitch = -pitch;
      }
      double motiony = ((pitch / 360) * 1);
      if (isJumping) {
         motiony++;
      } else if (isCrouching) {
         motiony--;
      }
      
      
      player.setMotion(new Coordinate(motion.getX(), motiony, motion.getZ()));
      //player.setMotion(new Coordinate(motion.getX(), 0, motion.getZ()));
      //System.out.println(motion.getY());
      
      /*
       * float pitch = ep.rotationPitch;
      GameSettings g = mc.gameSettings;
      boolean horizontal = false;
      boolean vertical = false;
      ep.onGround = true;
      if (key == g.keyBindForward.keyCode) {
         pitch = -pitch;
         horizontal = true;
      } else if (key == g.keyBindBack.keyCode) {
         horizontal = true;
      } else if (key == g.keyBindSneak.keyCode) {
         ep.jump();
         ep.motionY = -ep.motionY;
         vertical = true;
         return;
      } else if (key == g.keyBindJump.keyCode) {
         ep.jump();
         vertical = true;
         return;
      }
      if (flymode.equalsIgnoreCase("standard")) {
         ep.motionY = 0;
         return;
      }
       */
   }

   @Override
   public void keyPressed(int key) {
      if (key == KEY_JUMP) {
         isJumping = true;
      } else if (key == KEY_CROUCH) {
         isCrouching = true;
      }
   }

   @Override
   public void keyReleased(int key) { 
      if (key == KEY_JUMP) {
         isJumping = false;
      } else if (key == KEY_CROUCH) {
         isCrouching = false;
      }
   }
}
