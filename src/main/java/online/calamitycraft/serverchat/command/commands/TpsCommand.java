package online.calamitycraft.serverchat.command.commands;

import net.minecraft.core.net.command.TextFormatting;
import net.minecraft.server.entity.player.EntityPlayerMP;
import online.calamitycraft.serverchat.command.Command;
import online.calamitycraft.serverchat.util.TickrateUtil;

import java.util.Locale;

public class TpsCommand extends Command {

    public TpsCommand() {
        super("tps");
    }

    @Override
    public void onCommand(EntityPlayerMP player, boolean hasArgs, String[] args) {
        float currentTps = TickrateUtil.getTps();
        double avgTps = TickrateUtil.getAverageTps();
        player.addChatMessage(TextFormatting.GRAY + "Instantaneous TPS: " + chooseColor(currentTps) + String.format(Locale.US, "%.2f", currentTps));
        player.addChatMessage(TextFormatting.GRAY + "Average TPS (last 1200 ticks): " + chooseColor(currentTps) + String.format(Locale.US, "%.2f", avgTps));
    }

    private TextFormatting chooseColor(double tps) {
        if (tps > 15) return TextFormatting.GREEN;
        if (tps > 10) return TextFormatting.YELLOW;
        if (tps > 7) return TextFormatting.ORANGE;
        return TextFormatting.RED;
    }
}
