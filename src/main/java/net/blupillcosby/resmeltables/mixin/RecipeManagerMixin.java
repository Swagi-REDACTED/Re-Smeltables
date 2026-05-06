package net.blupillcosby.resmeltables.mixin;

import net.blupillcosby.resmeltables.ReSmeltables;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.world.item.crafting.*;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(RecipeManager.class)
public abstract class RecipeManagerMixin {

    /**
     * Filters the full recipe list (used by REI/JEI and recipe book).
     * The actual search logic (getRecipeFor) is handled in RecipeMapMixin.
     */
    @Inject(method = "getRecipes", at = @At("RETURN"), cancellable = true)
    private void resmeltables$filterAllRecipes(CallbackInfoReturnable<java.util.Collection<RecipeHolder<?>>> cir) {
        if (ReSmeltables.CONFIG == null) return;

        boolean allow = ReSmeltables.CONFIG.allowResmelting.get();
        boolean hasTR = FabricLoader.getInstance().isModLoaded("techreborn");
        
        // Optimization: If allowed and no TechReborn, we don't need to filter the collection
        if (allow && !hasTR) return;

        java.util.Collection<RecipeHolder<?>> recipes = cir.getReturnValue();
        java.util.List<RecipeHolder<?>> filtered = new java.util.ArrayList<>(recipes);
        
        filtered.removeIf(holder -> {
            if (holder.id().identifier().getNamespace().equals(ReSmeltables.MOD_ID)) {
                if (!allow) return true;
                if (hasTR && (holder.id().identifier().getPath().equals("smelting/iron_ingot_to_raw_iron") || holder.id().identifier().getPath().equals("iron_ingot_to_raw_iron"))) return true;
            }
            return false;
        });
        
        cir.setReturnValue(filtered);
    }
}
