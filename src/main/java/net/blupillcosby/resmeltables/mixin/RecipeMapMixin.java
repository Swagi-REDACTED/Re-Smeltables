package net.blupillcosby.resmeltables.mixin;

import net.blupillcosby.resmeltables.ReSmeltables;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.world.item.crafting.*;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Optional;
import java.util.stream.Stream;

@Mixin(RecipeMap.class)
public abstract class RecipeMapMixin {

    @Inject(method = "getRecipesFor", at = @At("RETURN"), cancellable = true)
    private <I extends RecipeInput, T extends Recipe<I>> void resmeltables$filterRecipes(RecipeType<T> type, I input, Level level, CallbackInfoReturnable<Stream<RecipeHolder<T>>> cir) {
        if (ReSmeltables.CONFIG == null) return;
        
        boolean allow = ReSmeltables.CONFIG.allowResmelting.get();
        boolean hasTR = FabricLoader.getInstance().isModLoaded("techreborn");

        // Optimization: If allowed and no TechReborn, we don't need to filter anything from the stream
        if (allow && !hasTR) return;

        Stream<RecipeHolder<T>> original = cir.getReturnValue();
        
        Stream<RecipeHolder<T>> filtered = original.filter(holder -> {
            if (holder.id().identifier().getNamespace().equals(ReSmeltables.MOD_ID)) {
                // Master toggle
                if (!allow) return false;
                
                // TechReborn conflict handling
                if (hasTR) {
                    if (holder.id().identifier().getPath().equals("smelting/iron_ingot_to_raw_iron") || holder.id().identifier().getPath().equals("iron_ingot_to_raw_iron")) {
                        // Return false to hide direct iron -> raw if TR is present, 
                        // allowing the stream to continue to TR's iron -> refined recipe.
                        return false;
                    }
                }
            }
            return true;
        });

        cir.setReturnValue(filtered);
    }
}
