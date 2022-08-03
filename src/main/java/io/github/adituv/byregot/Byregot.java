package io.github.adituv.byregot;

import net.fabricmc.api.ModInitializer;
import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.scoreboard.Scoreboard;
import net.minecraft.scoreboard.ScoreboardCriterion;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

public class Byregot implements ModInitializer
{
	public static final StatusEffect DOUBLE_EDGE = new DoubleEdgeEffect();

	@Override
	public void onInitialize()
	{
		Scoreboard sb = MinecraftClient.getInstance().world.getScoreboard();
		if(!sb.containsObjective(Constants.DOUBLE_EDGE_SCOREBOARD_NAME)) {
			sb.addObjective(Constants.DOUBLE_EDGE_SCOREBOARD_NAME, ScoreboardCriterion.DUMMY, Text.of(Constants.DOUBLE_EDGE_SCOREBOARD_NAME), ScoreboardCriterion.RenderType.INTEGER);
		}
		Registry.register(Registry.STATUS_EFFECT, new Identifier("byregot", "double_edge"), DOUBLE_EDGE);
	}
}
