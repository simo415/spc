package com.sijobe.spc.command;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * This annotation should be used to easily define attributes of a command
 * rather than overriding methods. 
 * <br><br>
 * An example of this annotation is below:
 * 
 * <pre>@Command (
 *    name = "",
 *    description = "",
 *    example = "",
 *    videoURL = "",
 *    version = "1.0",
 *    enabled = true,
 *    alias = {}
 * )</pre>
 *
 * @author simo_415
 */
@Retention(RetentionPolicy.RUNTIME)
public @interface Command {
   /**
    * The name of the command - ideally this should not contain any white 
    * space.
    * 
    * @return A string representing the name of the command
    */
   String name();
   
   /**
    * The description of what the command does
    * 
    * @return A string containing a description that the command executes
    */
   String description() default "";
    
   /**
    * An example of how the command can be used, this usually contains the 
    * syntax of a simple command
    * 
    * @return A string containing an example of the command in use
    */
   String example() default "";
   
   /**
    * A link to a video demonstration of the command that can be opened up to 
    * show the command in action and what exactly it does
    * 
    * @return A string containing a video URL, ie: http://youtube.com/watch?v=123456
    */
   String videoURL() default "";
   
   /**
    * Contains a simple version String. An example would be "1.0" or "Version 1"
    * 
    * @return A string containing the version of the command
    */
   String version() default "";
   
   /**
    * Specifies whether the command is active or not. If the command is not
    * enabled then it will not be processed.
    * 
    * @return True is returned when the command is enabled, false otherwise
    */
   boolean enabled() default true;
   
   /**
    * Returns true if the command can be processed asynchronously in the 
    * background. 
    * 
    * @return True is returned when the command can be processed in the 
    * background, false otherwise.
    */
   boolean async() default false;
   
   /**
    * Contains an array of aliases to this command. If the array is empty then
    * only the command name is used, if the array contains 1 or more elements
    * then the command name is ignored and only the command aliases are used to
    * match a command. To maintain using the command name and aliases the 
    * command name should appear as part of this array. 
    * 
    * Example 1:
    * name = "command"
    * alias = {"execute"}
    * 
    * Only the command "execute" would work, using the command "command" would 
    * result in nothing happening.
    * 
    * Example 2:
    * name = "command"
    * alias = {"execute","command"}
    * 
    * Both the command "execute" and "command" would execute.
    * 
    * Example 3:
    * name = "command"
    * alias = {}
    * 
    * The command "command" would execute
    * 
    * @return An array containing the aliases to this command
    */
   String[] alias() default {};
}
