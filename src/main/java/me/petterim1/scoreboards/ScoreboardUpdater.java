package me.petterim1.scoreboards;

import cn.nukkit.Player;

import com.creeperface.nukkit.placeholderapi.api.PlaceholderAPI;

import de.theamychan.scoreboard.api.ScoreboardAPI;
import de.theamychan.scoreboard.network.DisplaySlot;
import de.theamychan.scoreboard.network.Scoreboard;
import de.theamychan.scoreboard.network.ScoreboardDisplay;

public class ScoreboardUpdater extends Thread {

    private Main plugin;

    private int line = 0;

    public ScoreboardUpdater(Main plugin) {
        this.plugin = plugin;
        setName("ScoreboardUpdater");
    }

    @Override
    public void run() {
        for (Player p : plugin.getServer().getOnlinePlayers().values()) {
            try {
                Main.scoreboards.get(p).hideFor(p);
            } catch (Exception e) {}

            Scoreboard scoreboard = ScoreboardAPI.createScoreboard();
            ScoreboardDisplay scoreboardDisplay = scoreboard.addDisplay(DisplaySlot.SIDEBAR, "dumy", Main.config.getString("title"));

            Main.config.getStringList("text").forEach((text) -> {
                scoreboardDisplay.addLine(PlaceholderAPI.getInstance().translateString(text.replaceAll("%economy_money%", Main.getMoney(p)).replaceAll("%factions_name%", Main.getFaction(p)), p).replaceAll("§", "\u00A7"), line++);
            });

            scoreboard.showFor(p);
            Main.scoreboards.put(p, scoreboard);
            line = 0;
        }
    }
}