package me.meta1203.plugins.sakuracoin.commands;

import me.meta1203.plugins.sakuracoin.Sakuracoinish;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.google.sakuracoin.core.Address;
import com.google.sakuracoin.core.AddressFormatException;
import com.google.sakuracoin.core.WrongNetworkException;

import static me.meta1203.plugins.sakuracoin.commands.CommandUtil.*;

public class WithdrawCommand implements CommandExecutor {

	public boolean onCommand(CommandSender arg0, Command arg1, String arg2,
			String[] arg3) {
		if (!arg0.hasPermission("sakuracoin.withdraw")) {
			error("You do not have permission for this command!", arg0);
			return true;
		}
		
		if (arg0 instanceof Player) {
			Player player = (Player)arg0;
double value = 0;
			
			// Withdraw exact amount
			if (arg3.length == 2) {
				try {
				
					Address withdrawTo = new Address(Sakuracoinish.network, arg3[0]);
					double withdraw = Double.parseDouble(arg3[1]);
					if (!Sakuracoinish.econ.hasMoney(player.getName(), Sakuracoinish.minWithdraw)) {
						error("Oops! You must have " + Sakuracoinish.econ.formatValue(Sakuracoinish.minWithdraw, true) + " to withdraw!", arg0);
						return true;
					}
					
					if (!Sakuracoinish.econ.hasMoney(arg0.getName(), withdraw - Sakuracoinish.econ.priceOfTax(withdraw))) {
						error("Oops! You cannot withdraw more money than you have!", arg0);
						return true;
					}
					value = Sakuracoinish.bapi.localSendCoins(withdrawTo, withdraw);


					
					Sakuracoinish.econ.subFunds(arg0.getName(), withdraw - Sakuracoinish.econ.priceOfTax(withdraw));
				} catch (WrongNetworkException e) {
					error("Oops! That address was for the TestNet!", arg0);
				} catch (AddressFormatException e) {
					error("Oops! Is that the correct address?", arg0);
				} catch (NumberFormatException e) {
					error("Usage: /withdraw <address> [amount]", arg0);
					error("Amount must be a number!",arg0);
				}
} else {
error("Usage: /withdraw <address> <amount>", arg0);
}
info("Withdrew " + Double.toString(value) + " to " + arg3[0] + " .", arg0);
			} 
		return true;
	}
}
