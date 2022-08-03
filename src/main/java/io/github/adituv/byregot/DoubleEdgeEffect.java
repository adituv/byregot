package io.github.adituv.byregot;

import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.attribute.AttributeContainer;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectCategory;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.scoreboard.Scoreboard;
import net.minecraft.scoreboard.ScoreboardObjective;
import net.minecraft.scoreboard.ScoreboardPlayerScore;
import net.minecraft.text.Text;

public class DoubleEdgeEffect extends StatusEffect
{
	private static final float DE_BASE_SELF_DAMAGE = 0.5f;
	private static final int DE_EFFECT_COLOR = 0xFF0000;
	private static final int DE_TICK_RATE = 60;
	private static final int DE_MAX_STACKS = 16;
	public DoubleEdgeEffect()
	{
		super(StatusEffectCategory.BENEFICIAL, DE_EFFECT_COLOR);
	}

	@Override
	public boolean canApplyUpdateEffect(int duration, int amplifier)
	{
		return (duration % DE_TICK_RATE) == 0;
	}

	@Override
	public void applyUpdateEffect(LivingEntity entity, int amplifier)
	{
		if ((entity instanceof PlayerEntity) && !entity.world.isClient()) {
			PlayerEntity player = (PlayerEntity)entity;

			int stacks = getStacks(player);

			MinecraftClient.getInstance().inGameHud.getChatHud().addMessage(Text.of(String.format("Tick!  %d stacks", stacks)));

			int oldHurtTime = entity.hurtTime;
			entity.damage(DamageSource.GENERIC, DE_BASE_SELF_DAMAGE * stacks);
			entity.hurtTime = oldHurtTime;

			if(stacks < DE_MAX_STACKS) {
				setStacks(player, stacks+1);
			}
		}
	}

	@Override
	public void onApplied(LivingEntity entity, AttributeContainer attributes, int amplifier)
	{
		super.onApplied(entity, attributes, amplifier);

		if ((entity instanceof PlayerEntity) && !entity.world.isClient()) {
			// Set stacks to 1 if this isn't a reapplied status
			PlayerEntity player = (PlayerEntity)entity;

			if (getStacks(player) < 1) {
				setStacks(player, 1);
			}
		}

	}

	@Override
	public void onRemoved(LivingEntity entity, AttributeContainer attributes, int amplifier)
	{
		super.onRemoved(entity, attributes, amplifier);

		if ((entity instanceof PlayerEntity) && !entity.world.isClient()) {
			// Set stacks to 1 if this isn't a reapplied status
			PlayerEntity player = (PlayerEntity)entity;

			setStacks(player, 0);
		}
	}

	private ScoreboardPlayerScore getDeStacksScore(PlayerEntity player) {
		Scoreboard scoreboard = player.world.getScoreboard();
		ScoreboardObjective deObjective = scoreboard.getObjective(Constants.DOUBLE_EDGE_SCOREBOARD_NAME);

		return scoreboard.getPlayerScore(player.getName().getString(), deObjective);
	}

	private int getStacks(PlayerEntity player) {
		return getDeStacksScore(player).getScore();
	}

	private void setStacks(PlayerEntity player, int stacks) {
		getDeStacksScore(player).setScore(stacks);
	}
}
