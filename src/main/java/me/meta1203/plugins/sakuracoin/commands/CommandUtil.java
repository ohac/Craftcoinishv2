package me.meta1203.plugins.sakuracoin.commands;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

public class CommandUtil {
	public static void error(String msg, CommandSender cs) {
		cs.sendMessage(ChatColor.RED + msg + ChatColor.RESET);
	}
	
	public static void info(String msg, CommandSender cs) {
		cs.sendMessage(ChatColor.AQUA + msg + ChatColor.RESET);
	}
	
	public static void action(String msg, CommandSender cs) {
		cs.sendMessage(ChatColor.GREEN + msg + ChatColor.RESET);
	}
}
