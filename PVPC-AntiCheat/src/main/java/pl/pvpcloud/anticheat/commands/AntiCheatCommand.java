package pl.pvpcloud.anticheat.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import pl.pvpcloud.anticheat.AntiCheatPlugin;

public class AntiCheatCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {
        if (commandSender.hasPermission("anticheat.command.manage")) {
            AntiCheatPlugin.getAntiCheatConfig().read(AntiCheatPlugin.getPlugin().getConfig());
            commandSender.sendMessage("Przeladowano config");
            return true;
        }
        return false;
    }
}
