package com.sijobe.spc.command;

import com.sijobe.spc.validation.Parameter;
import com.sijobe.spc.validation.ParameterInteger;
import com.sijobe.spc.validation.ParameterString;
import com.sijobe.spc.validation.Parameters;
import com.sijobe.spc.wrapper.CommandException;
import com.sijobe.spc.wrapper.CommandSender;
import com.sijobe.spc.wrapper.Coordinate;
import com.sijobe.spc.wrapper.Entity;
import com.sijobe.spc.wrapper.Player;

import java.util.List;

/**
 * The spawn command spawns the specified quantity of mobs at the location 
 * the player is looking at. If no position it specified the mobs will spawn 
 * nearby (under 5 blocks from) the player.
 *
 * @author simo_415
 */
@Command (
   name = "spawn",
   description = "",
   example = "creeper 5",
   videoURL = "http://www.youtube.com/watch?v=4R8z-NFUCkM",
   version = "1.0"
)
public class Spawn extends StandardCommand {

   /**
    * The parameters of the command
    */
   private static final Parameters PARAMETERS = new Parameters (
      new Parameter[] {
         new ParameterString("<NAME|ID|random|list>",false),
         new ParameterInteger("[QUANTITY]",true)
      }
   );

   /**
    * @see com.sijobe.spc.wrapper.CommandBase#execute(com.sijobe.spc.wrapper.CommandSender, java.util.List)
    */
   @Override
   public void execute(CommandSender sender, List<?> params) throws CommandException {
      String argument = (String)params.get(0);
      if (argument.equalsIgnoreCase("list")) {
         String list = "";
         for (String name : Entity.getNameToIdEntityList().keySet()) {
            list += name + " (" + Entity.getNameToIdEntityList().get(name) + "), ";
         }
         list = list.substring(0, list.length() - 2);
         sender.sendMessageToPlayer(list);
         return;
      } else if (argument.equalsIgnoreCase("random")) {
         argument = Entity.getLoadedEntities().get((int)(Math.random() * Entity.getLoadedEntities().size()));
      }
      try {
         argument = Entity.getEntityName(Integer.parseInt(argument));
         if (argument == null) {
            throw new CommandException("Invalid ID specified " + params.get(0));
         }
      } catch (Exception e) {
      }
      int quantity = 1;
      if (params.size() > 1) {
         quantity = (Integer)params.get(1);
      }
      Player player = super.getSenderAsPlayer(sender);
      Coordinate coord = player.trace(128);
      if (coord == null) {
         coord = player.getPosition();
         coord = new Coordinate(coord.getX() + (Math.random() * 10) - 5, coord.getY(), coord.getZ() + (Math.random() * 10) - 5);
      }
      for (int i = 0; i < quantity; i++) {
         if (!Entity.spawnEntity(argument, coord, player.getWorld())) {
            throw new CommandException("Could not spawn entity named " + argument);
         }
      }
   }

   /**
    * @see com.sijobe.spc.wrapper.CommandBase#getParameters()
    */
   @Override
   public Parameters getParameters() {
      return PARAMETERS;
   }
}
