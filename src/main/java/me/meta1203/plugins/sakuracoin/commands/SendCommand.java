package me.meta1203.plugins.sakuracoin.commands;

import me.meta1203.plugins.sakuracoin.Sakuracoinish;
import me.meta1203.plugins.sakuracoin.Util;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import static me.meta1203.plugins.sakuracoin.commands.CommandUtil.*;

public class SendCommand implements CommandExecutor {

	public boolean onCommand(CommandSender arg0, Command arg1, String arg2,
			String[] arg3) {
		if (!arg0.hasPermission("sakuracoin.transact")) {
			error("You do not have permission for this command!", arg0);
			return true;
		}
		
		if (!(arg0 instanceof Player)) {
			error("You must be a player to execute this command!", arg0);
			return true;
		}
		Player player = (Player)arg0;
		if (arg3.length != 2) {
			error("Usage: /transact <player> <amount>", arg0);
			return true;
		}
		if (!Util.testAccount(arg3[0])) {
			error("That player has not yet joined, or does not exist!", arg0);
			return true;
		}
		double amount = 0.0;
		try {
			amount = Double.parseDouble(arg3[1]);
		} catch (NumberFormatException e) {
			error("Amount must be a number!", arg0);
			return true;
		}
		if (Sakuracoinish.econ.hasMoney(player.getName(), amount) && amount > 0) {
			Sakuracoinish.econ.transact(player.getName(), arg3[0], amount);
			action("Sucessfully sent " + Sakuracoinish.econ.formatValue(amount, true) + " to " +
					arg3[0] + "!", arg0);
Bukkit.getServer().broadcastMessage(arg0 + " sent " + Sakuracoinish.econ.formatValue(amount, true) + " to " + arg3[0] + "!");
		} else {
			error("Invalid amount to send!", arg0);
		}
		return true;
	}

}
