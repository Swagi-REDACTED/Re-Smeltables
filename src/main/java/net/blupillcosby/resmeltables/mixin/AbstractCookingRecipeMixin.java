package net.blupillcosby.resmeltables.mixin;

import net.blupillcosby.resmeltables.ReSmeltables;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.Identifier;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.AbstractCookingRecipe;
import net.minecraft.world.item.crafting.SingleItemRecipe;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(AbstractCookingRecipe.class)
public abstract class AbstractCookingRecipeMixin extends SingleItemRecipe {

    public AbstractCookingRecipeMixin() {
        super(null, null, null);
    }

    @Inject(method = "experience", at = @At("HEAD"), cancellable = true)
    private void resmeltables$overrideExperience(CallbackInfoReturnable<Float> cir) {
        if (!ReSmeltables.CONFIG.allowResmelting.get()) return;

        Item result = this.result().item().value();
        
        // We identify our recipes by their specific Input -> Output pair
        // This is safer than checking IDs and works with any mod that might add similar recipes
        boolean isResmelt = false;
        float xp = 0.0f;

        if (result == Items.RAW_IRON) {
            // Vanilla Iron Ingot
            if (this.input().acceptsItem(BuiltInRegistries.ITEM.wrapAsHolder(Items.IRON_INGOT))) {
                isResmelt = true;
                xp = ReSmeltables.CONFIG.ironXp.get();
            } else {
                // TechReborn Refined Iron Ingot
                Item refined = BuiltInRegistries.ITEM.getValue(Identifier.fromNamespaceAndPath("techreborn", "refined_iron_ingot"));
                if (refined != null && refined != Items.AIR && this.input().acceptsItem(BuiltInRegistries.ITEM.wrapAsHolder(refined))) {
                    isResmelt = true;
                    xp = ReSmeltables.CONFIG.ironXp.get();
                }
            }
        } else if (result == Items.RAW_GOLD) {
            if (this.input().acceptsItem(BuiltInRegistries.ITEM.wrapAsHolder(Items.GOLD_INGOT))) {
                isResmelt = true;
                xp = ReSmeltables.CONFIG.goldXp.get();
            }
        } else if (result == Items.RAW_COPPER) {
            if (this.input().acceptsItem(BuiltInRegistries.ITEM.wrapAsHolder(Items.COPPER_INGOT))) {
                isResmelt = true;
                xp = ReSmeltables.CONFIG.copperXp.get();
            }
        }

        if (isResmelt) {
            cir.setReturnValue(xp);
        }
    }
}
