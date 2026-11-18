package eu.pb4.moredelightpatch.impl.item;

import eu.pb4.polymer.core.api.item.PolymerItem;
import net.fabricmc.fabric.api.networking.v1.context.PacketContext;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.component.DataComponents;
import net.minecraft.resources.Identifier;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;

/**
 * Shows one More Delight item to a client that does not have the mod.
 *
 * <p>The client still gets a vanilla item, but its {@code minecraft:item_model} component names the
 * mod's own item definition, and the mod's asset tree is added to the Polymer resource pack. The
 * client therefore draws the real model and texture, and reads the real translation key, instead of
 * a stand-in icon.
 *
 * <p>The stand-in is chosen to match what the item does on the client, because a few behaviours are
 * decided there: whether the eating animation plays, and how large a stack the inventory and hand
 * allow. Bowl dishes are handed over as stew and the rest of the food as bread, while the knives,
 * which are single-stack tools, are handed over as a sword so the swing reads correctly.
 */
public record PolyMoreDelightItem(Item item, Identifier model) implements PolymerItem {
    @Override
    public Item getPolymerItem(ItemStack itemStack, PacketContext context) {
        var components = this.item.components();
        boolean singleStack = this.item.getDefaultMaxStackSize() <= 1;

        if (components.has(DataComponents.FOOD)) {
            return singleStack ? Items.MUSHROOM_STEW : Items.BREAD;
        }

        return singleStack ? Items.IRON_SWORD : Items.PAPER;
    }

    @Override
    public Identifier getPolymerItemModel(ItemStack stack, PacketContext context, HolderLookup.Provider lookup) {
        // The id of the item definition file the mod ships at assets/moredelight/items/<path>.json.
        return this.model;
    }
}
