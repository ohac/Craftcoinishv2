package me.meta1203.plugins.sakuracoin.commands;

import java.math.BigInteger;

import me.meta1203.plugins.sakuracoin.Sakuracoinish;
import static me.meta1203.plugins.sakuracoin.commands.CommandUtil.*;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

import com.google.litecoin.core.ScriptException;
import com.google.litecoin.core.Transaction;
import com.google.litecoin.core.Wallet;

public class AdminCommand implements CommandExecutor {

	public boolean onCommand(CommandSender arg0, Command arg1, String arg2,
			String[] arg3) {
		if (!arg0.hasPermission("sakuracoin.admin")) {
			error("You do not have permission for this command!", arg0);
			return true;
		}
		
		if (arg3.length != 1) {
			error("Syntax: /sakuracoin <info>|<reset>", arg0);
			return true;
		}
		if (arg3[0].equalsIgnoreCase("info"))
			printInfo(arg0);
		else if (arg3[0].equalsIgnoreCase("reset"))
			Sakuracoinish.bapi.reloadWallet();
		else
			error("Syntax: /satoshis <info>|<reset>", arg0);
		
		return true;
	}
	
	private void printInfo(CommandSender arg0) {
		info("INFO:", arg0);
		info("Wallet:", arg0);
		
		Wallet tmp = Sakuracoinish.bapi.getWallet();
		BigInteger bitcoinBalance = tmp.getBalance();
		double inGameValue = Sakuracoinish.econ.bitcoinToInGame(bitcoinBalance);
		info("Total balance: " + bitcoinBalance.longValue() + " Satoshi = " + Sakuracoinish.econ.formatValue(inGameValue, true), arg0);
		info("Recent transactions:", arg0);
		for (Transaction t : tmp.getRecentTransactions(3, false)) {
			try {
				info(t.getHashAsString() + " value: +" + t.getValueSentToMe(tmp) + ", -" + t.getValueSentFromMe(tmp), arg0);
				info("Confirmations: " + t.getConfidence().getDepthInBlocks(), arg0);
			} catch (ScriptException e) {
				error("Transaction " + t.getHashAsString() + " errored out!", arg0);
			} catch (IllegalStateException e) {
				continue;
			}
		}
	}
}
