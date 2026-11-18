package eu.pb4.moredelightpatch;

import eu.pb4.moredelightpatch.impl.MoreDelightItems;
import eu.pb4.polymer.resourcepack.api.PolymerResourcePackUtils;
import net.fabricmc.api.ModInitializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MoreDelightPolymerPatch implements ModInitializer {
    public static final String MOD_ID = "moredelight-polymer-patch";
    /** The patched mod, whose assets carry the item models, textures and translations. */
    public static final String TARGET_MOD_ID = "moredelight";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

    @Override
    public void onInitialize() {
        // More Delight ships a full item definition, model and texture for every item it adds, so
        // its own assets are the whole client-side look; nothing has to be generated here.
        if (PolymerResourcePackUtils.addModAssets(TARGET_MOD_ID)) {
            LOGGER.info("Registered More Delight assets as a Polymer resource pack source.");
        } else {
            LOGGER.error("Could not read assets from {}, its items will be sent as plain vanilla stand-ins.",
                    TARGET_MOD_ID);
        }

        MoreDelightItems.setup();
    }
}
