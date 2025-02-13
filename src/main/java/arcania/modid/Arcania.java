package arcania.modid;

import arcania.modid.block.ArcaniaBlocks;
import arcania.modid.item.ArcaniaItems;
import net.fabricmc.api.ModInitializer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Arcania implements ModInitializer {
	public static final String MOD_ID = "arcania";
	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

	@Override
	public void onInitialize() {
		//Very important Sachen die ich mache
		ArcaniaItems.registerModItems();
		ArcaniaBlocks.registerModBlocks();
	}
}