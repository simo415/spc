package com.sijobe.spc.asm;

import java.util.Map;

import cpw.mods.fml.relauncher.IFMLLoadingPlugin;
import cpw.mods.fml.relauncher.IFMLLoadingPlugin.MCVersion;
import cpw.mods.fml.relauncher.IFMLLoadingPlugin.Name;
import cpw.mods.fml.relauncher.IFMLLoadingPlugin.TransformerExclusions;
import cpw.mods.fml.relauncher.IFMLLoadingPlugin.SortingIndex;

/**
 * the spc coremod class
 * @author aucguy
 * @version 1.0
 */
@MCVersion("1.7.2")
@Name("spc Coremod")
@TransformerExclusions("com.sijobe.spc.asm")
@SortingIndex() //ensure notch names
public class SpcCoreMod implements IFMLLoadingPlugin {
   @Override
   public String[] getASMTransformerClass() {
      return new String[] { Transformer.class.getName() };
   }
   
   @Override
   public String getModContainerClass() {
      return null;
   }
   
   @Override
   public String getSetupClass() {
      return null;
   }
   
   @Override
   public void injectData(Map<String, Object> data) {
      Processor processor = Processor.getInstance();
      processor.obfuscated = (Boolean) data.get("runtimeDeobfuscationEnabled");
      System.out.println("running in a" + (processor.obfuscated ? "n " : " de") + "obfuscated enviroment.");
      Transformer.instance.hookTransformers();
   }
   
   @Override
   public String getAccessTransformerClass() {
      return null;
   }
}
