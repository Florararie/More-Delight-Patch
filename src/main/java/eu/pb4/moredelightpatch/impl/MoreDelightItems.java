package eu.pb4.moredelightpatch.impl;

import eu.pb4.moredelightpatch.MoreDelightPolymerPatch;
import eu.pb4.moredelightpatch.impl.item.PolyMoreDelightItem;
import eu.pb4.polymer.core.api.item.PolymerItem;
import eu.pb4.polymer.core.impl.InternalServerRegistry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.item.Item;

/**
 * Finds everything More Delight adds and makes it show up for unmodded clients.
 *
 * <p>The mod registers its items through Delight Lib, which builds them at runtime rather than
 * exposing a registry of names, so they are picked up by namespace. That has to happen during mod
 * initialisation: Polymer freezes its overlay registries before a server starts, so later
 * registration is rejected. This patch declares More Delight as a dependency, which is what puts
 * that mod's initialiser ahead of this one.
 */
public final class MoreDelightItems {
    private MoreDelightItems() {}

    public static void setup() {
        register();
    }

    private static void register() {

        int items = 0;
        for (Item item : BuiltInRegistries.ITEM) {
            var id = BuiltInRegistries.ITEM.getKey(item);
            if (!id.getNamespace().equals(MoreDelightPolymerPatch.TARGET_MOD_ID)) {
                continue;
            }

            PolymerItem.registerOverlay(item, new PolyMoreDelightItem(item, id));
            items++;
        }

        int tabs = 0;
        for (var tab : BuiltInRegistries.CREATIVE_MODE_TAB) {
            var id = BuiltInRegistries.CREATIVE_MODE_TAB.getKey(tab);
            if (id == null || !id.getNamespace().equals(MoreDelightPolymerPatch.TARGET_MOD_ID)) {
                continue;
            }
            // Polymer only sends the packet that makes a client create a tab for tabs in its own
            // registry; otherwise it sends the tab's contents and the client drops them because it
            // has no such tab. The public helper refuses any id that is already in the vanilla
            // registry, which a mod's own tab always is, so this inserts into that registry directly
            // - exactly what the helper does for ids it accepts. Keeping the real id also means a
            // client with the mod keeps its own tab instead of growing a duplicate.
            InternalServerRegistry.ITEM_GROUPS.set(id, tab);
            MoreDelightPolymerPatch.LOGGER.info("Serving creative tab {} to Polymer clients", id);
            tabs++;
        }

        MoreDelightPolymerPatch.LOGGER.info("Virtualised {} More Delight items and {} creative tab(s).",
                items, tabs);
    }
}
