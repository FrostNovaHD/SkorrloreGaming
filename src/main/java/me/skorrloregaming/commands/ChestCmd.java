package me.skorrloregaming.commands;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Sound;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

import me.skorrloregaming.$;
import me.skorrloregaming.Server;
import me.skorrloregaming.SolidStorage;

public class ChestCmd implements CommandExecutor {

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if (!(sender instanceof Player))
			return true;
		Player player = ((Player) sender);
		if (!Server.getKitpvp().contains(player.getUniqueId()) && !Server.getFactions().contains(player.getUniqueId()) && !Server.getSkyblock().contains(player.getUniqueId())) {
			player.sendMessage($.getMinigameTag(player) + ChatColor.RED + "This minigame prevents use of this command.");
			return true;
		}
		if (Server.getPlayersInCombat().containsKey(player.getUniqueId())) {
			player.sendMessage($.getMinigameTag(player) + ChatColor.RED + "You cannot use this command during combat.");
			return true;
		}
		String subDomain = $.getMinigameDomain(player);
		if (Server.getSaveOtherInventory().containsKey(player))
			Server.getSaveOtherInventory().remove(player);
		int chestNumber = 1;
		if (args.length > 0) {
			try {
				chestNumber = Math.abs(Integer.parseInt(args[0]));
			} catch (Exception ig) {
			}
		}
		if (chestNumber < 1) {
			player.sendMessage($.Legacy.tag + ChatColor.RED + "Failed. " + ChatColor.GRAY + "The specified vault does not exist.");
			return true;
		}
		if (args.length < 2 || (!(Bukkit.getPlayer(args[1]) == null) && Bukkit.getPlayer(args[1]).getName().equals(player.getName()))) {
			if (chestNumber > 1) {
				int rankId = $.getRankId(player);
				switch (chestNumber) {
					case 2:
						if (!(rankId < -1)) {
							player.sendMessage($.Legacy.tag + ChatColor.RED + "Sorry, you need a donor rank to use this vault.");
							return true;
						}
						break;
					case 3:
						if (!(rankId < -2)) {
							if (rankId < -1) {
								player.sendMessage($.Legacy.tag + ChatColor.RED + "Sorry, you need a higher donor rank to use this.");
							} else {
								player.sendMessage($.Legacy.tag + ChatColor.RED + "Sorry, you need a donor rank to use this vault.");
							}
						}
						break;
					case 4:
						if (!(rankId < -3)) {
							if (rankId < -1) {
								player.sendMessage($.Legacy.tag + ChatColor.RED + "Sorry, you need a higher donor rank to use this.");
							} else {
								player.sendMessage($.Legacy.tag + ChatColor.RED + "Sorry, you need a donor rank to use this vault.");
							}
						}
						break;
					case 5:
						if (!(rankId < -4)) {
							if (rankId < -1) {
								player.sendMessage($.Legacy.tag + ChatColor.RED + "Sorry, you need a higher donor rank to use this.");
							} else {
								player.sendMessage($.Legacy.tag + ChatColor.RED + "Sorry, you need a donor rank to use this vault.");
							}
							return true;
						}
						break;
					default:
						player.sendMessage($.Legacy.tag + ChatColor.RED + "Failed. " + ChatColor.GRAY + "The specified vault does not exist.");
						return true;
				}
			}
			Inventory inv = SolidStorage.restorePersonalChest(player, subDomain, true, chestNumber);
			player.playSound(player.getLocation(), Sound.BLOCK_CHEST_OPEN, 1, 1);
			player.openInventory(inv);
		} else {
			Player tp = Bukkit.getPlayer(args[1]);
			if (tp == null) {
				player.sendMessage($.Legacy.tag + ChatColor.RED + "Failed. " + ChatColor.GRAY + "The specified player could not be found.");
			} else {
				boolean hasControl = false;
				if (player.isOp())
					hasControl = true;
				String tpSubDomain = $.getMinigameDomain(player);
				Inventory inv = SolidStorage.restorePersonalChest(tp, tpSubDomain, hasControl, chestNumber);
				if (hasControl) {
					Server.getSavePersonalChest().put(player, tp);
				}
				player.playSound(player.getLocation(), Sound.BLOCK_CHEST_OPEN, 1, 1);
				player.openInventory(inv);
			}
		}
		return true;
	}

}
