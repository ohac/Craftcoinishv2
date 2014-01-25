package me.meta1203.plugins.sakuracoin.commands;

import static me.meta1203.plugins.sakuracoin.commands.CommandUtil.*;
import me.meta1203.plugins.sakuracoin.Sakuracoinish;
import me.meta1203.plugins.sakuracoin.Util;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class MoneyCommand implements CommandExecutor {

	public boolean onCommand(CommandSender arg0, Command arg1, String arg2,
			String[] arg3) {
		
		if (!arg0.hasPermission("sakuracoin.money")) {
			error("You do not have permission for this command!", arg0);
			return true;
		}
		
		if (arg0 instanceof Player) {
			if (arg3.length == 0) {
				
				String amount = Sakuracoinish.econ.listMoney(((Player)arg0).getName());
				info("You have " + amount, arg0);
				return true;
			}
		} 
			
		if (arg3.length == 1) {
		
				double amount = Sakuracoinish.econ.getMoney(arg3[0]);
				if (amount == 0) {
					error("Player not found!", arg0);
					return true;
				}
				info(arg3[0] + " has " + amount, arg0);
			} else {
				error("Syntax: money <player>", arg0);
			}
		return true;
	}

}
