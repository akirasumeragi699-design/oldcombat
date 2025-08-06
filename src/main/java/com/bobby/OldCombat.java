package com.bobby;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.TitleScreen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.text.Text;
import net.minecraft.entity.LivingEntity;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(TitleScreen.class)
public abstract class OldCombat extends Screen {

    protected OldCombat(Text title) {
        super(title);
    }

    private static boolean is1_8 = false;

    @Inject(method = "initWidgetsNormal", at = @At("RETURN"))
    private void addPvPButtons(int y, int spacingY, CallbackInfo ci) {
        this.addDrawableChild(ButtonWidget.builder(
            Text.literal("PvP: " + (is1_8 ? "1.8" : "1.9+")),
            (button) -> {
                is1_8 = !is1_8;
                button.setMessage(Text.literal("PvP: " + (is1_8 ? "1.8" : "1.9+")));
            }
        ).dimensions(this.width / 2 + 110, y, 80, 20).build());
    }

    @Mixin(LivingEntity.class)
    public static class CooldownMixin {
        @Inject(method = "getAttackCooldownProgress", at = @At("HEAD"), cancellable = true)
        private void overrideCooldown(float baseTime, CallbackInfoReturnable<Float> cir) {
            if (is1_8) {
                cir.setReturnValue(1.0F); // full cooldown like 1.8
            }
        }
    }
}
