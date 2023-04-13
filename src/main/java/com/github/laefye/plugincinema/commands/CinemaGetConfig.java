package com.github.laefye.plugincinema.commands;

import com.github.laefye.plugincinema.PluginCinema;
import com.sk89q.worldedit.IncompleteRegionException;
import com.sk89q.worldedit.LocalSession;
import com.sk89q.worldedit.WorldEdit;
import com.sk89q.worldedit.bukkit.BukkitAdapter;
import com.sk89q.worldedit.session.SessionManager;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

import java.util.List;

public class CinemaGetConfig implements CommandExecutor, TabCompleter {
    public static String ID = PluginCinema.id("getConfig");

    private float getCinemaAngle(Player player) {
        var f = Math.round((-player.getEyeLocation().getYaw() + 180) / 90) * 90;
        return f >= 360 ? 0 : f;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        var actor = BukkitAdapter.adapt(sender);
        SessionManager manager = WorldEdit.getInstance().getSessionManager();
        LocalSession localSession = manager.get(actor);
        try {
            var region = localSession.getSelection();
            var min = Math.min(region.getWidth(), region.getLength());
            if (min != 1) {
                sender.sendMessage("Invalid select (you need select flat screen)");
            }
            var width = Math.max(region.getWidth(), region.getLength());
            var height = region.getHeight();
            var angle = getCinemaAngle((Player) sender);
            var angleInt = Math.round(angle / 90) * 90;
            var position = new Vector(
                    region.getCenter().getX(),
                    region.getCenter().getY() + 0.5f,
                    region.getCenter().getZ());
            if (angleInt == 180) {
                position.setZ(position.getZ() - 0.01f);
                position.setX(position.getX() + 0.5f);
            }
            if (angleInt == 0) {
                position.setZ(position.getZ() + 1.01f);
                position.setX(position.getX() + 0.5f);
            }
            if (angleInt == 90) {
                position.setX(position.getX() + 1.01f);
                position.setZ(position.getZ() + 0.5f);
            }
            if (angleInt == 270) {
                position.setX(position.getX() - 0.01f);
                position.setZ(position.getZ() + 0.5f);
            }

            sender.sendMessage("Size: " + width + "x" + height);
            sender.sendMessage("Rotation: " + angle);
            sender.sendMessage("Position: ");
            sender.sendMessage(" X: " + Math.round(position.getX() * 100) / 100.0);
            sender.sendMessage(" Y: " + Math.round(position.getY() * 100) / 100.0);
            sender.sendMessage(" Z: " + Math.round(position.getZ() * 100) / 100.0);
        } catch (IncompleteRegionException e) {
            sender.sendMessage("Need to select region via WorldEdit");
        }
        return true;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String label, String[] args) {
        return null;
    }
}
