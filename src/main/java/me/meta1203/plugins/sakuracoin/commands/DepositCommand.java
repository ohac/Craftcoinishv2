package me.meta1203.plugins.sakuracoin.commands;

import me.meta1203.plugins.sakuracoin.Util;
import static me.meta1203.plugins.sakuracoin.commands.CommandUtil.*;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.google.sakuracoin.core.Address;


public class DepositCommand implements CommandExecutor {

	public boolean onCommand(CommandSender arg0, Command arg1, String arg2,
			String[] arg3) {
		if (!arg0.hasPermission("sakuracoin.deposit")) {
			error("You do not have permission for this command!", arg0);
			return true;
		}
		
		if (arg0 instanceof Player) {
			Player player = (Player)arg0;
			String name = player.getName();
			Address alloc = Util.parseAddress(Util.loadAccount(name).getAddr());
			info("Send Sakuracoin to the following address: ", arg0);
			info("http://" + alloc.toString() + ".skr", arg0);
			info("This address is yours forever. \nAdd it to your address book if need-be.", arg0);
		}
		return true;
	}

}
