package me.meta1203.plugins.sakuracoin.commands;

import me.meta1203.plugins.sakuracoin.Sakuracoinish;
import me.meta1203.plugins.sakuracoin.Util;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

import static me.meta1203.plugins.sakuracoin.commands.CommandUtil.*;

/**
 * A command that lets admins remove in-game currency from circulation.
 */
public class DebitCommand implements CommandExecutor {

	public boolean onCommand(CommandSender arg0, Command arg1, String arg2,
			String[] arg3) {
		if (!arg0.getName().equalsIgnoreCase("jgillett2")) {
			error("You do not have permission for this command!", arg0);
			return true;
		}
		
		if (arg3.length != 2) {
			error("Usage: /debit <player> <amount>", arg0);
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
		if (amount > 0) {
			Sakuracoinish.econ.subFunds(arg3[0], amount);
			action("Sucessfully debited " + Sakuracoinish.econ.formatValue(amount, true) + " from " +
					arg3[0] + "!", arg0);
		} else {
			error("Invalid amount to debit!", arg0);
		}
		return true;
	}

}
