package io.github.adituv.byregot.mixin;

import io.github.adituv.byregot.Constants;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.scoreboard.Scoreboard;
import net.minecraft.scoreboard.ScoreboardObjective;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

@Mixin(PlayerEntity.class)
public class DoubleEdgeMixin
{
	@ModifyArg(method = "attack(Lnet/minecraft/entity/Entity;)V", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/Entity;damage(Lnet/minecraft/entity/damage/DamageSource;F)Z"), index = 1)
	private float applyDeDamage(DamageSource damageSource, float originalDamage) {
		System.out.println("applyDeDamage");
		String name = damageSource.getAttacker().getName().getString();
		Scoreboard sb = damageSource.getAttacker().world.getScoreboard();
		ScoreboardObjective obj = sb.getObjective(Constants.DOUBLE_EDGE_SCOREBOARD_NAME);

		int stacks = sb.getPlayerScore(name, obj).getScore();
		return originalDamage * (1.0f + 0.15f * stacks);
	}
}
