package me.meta1203.plugins.sakuracoin;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;

import com.google.sakuracoin.core.Address;
import com.google.sakuracoin.core.AddressFormatException;
import com.google.sakuracoin.core.ScriptException;
import com.google.sakuracoin.core.Sha256Hash;
import com.google.sakuracoin.core.Transaction;
import com.google.sakuracoin.core.TransactionOutput;
import com.google.sakuracoin.core.WrongNetworkException;

public class Util {

	public static Sakuracoinish plugin;
	public static final Logger log = Logger.getLogger("Minecraft");

	public static double roundTo(double input, int place) {
		return Math.round(input * Math.pow(10, place)) / Math.pow(10, place);
	}
	
	public static double getBitcoin(BigInteger raw) {
		return raw.longValue() / Math.pow(10, 8);
	}

	public static boolean testAccount(String name) {
		if (plugin == null) {
			Plugin p = Bukkit.getPluginManager().getPlugin("Sakuracoinish");
			plugin = (Sakuracoinish) p;
		}
		AccountEntry ae = plugin.getAccount(name);
		if (ae == null) {
			return false;
		}
		return true;
	}

	public static AccountEntry loadAccount(String accName) {
		if (plugin == null) {
			Plugin p = Bukkit.getPluginManager().getPlugin("Sakuracoinish");
			plugin = (Sakuracoinish) p;
		}
		AccountEntry ae = plugin.getAccount(accName);
		if (ae == null) {
			ae = new AccountEntry();
			ae.setPlayerName(accName);
			ae.setAmount(0.0);
			ae.setAddr(Sakuracoinish.bapi.genAddress().toString());
		} else if (ae.getAddr() == null) {
			ae.setAddr(Sakuracoinish.bapi.genAddress().toString());
			saveAccount(ae);
		}
		return ae;
	}

	public static void saveAccount(AccountEntry ae) {
		if (plugin == null) {
			Plugin p = Bukkit.getPluginManager().getPlugin("Sakuracoinish");
			plugin = (Sakuracoinish) p;
		}
		plugin.saveAccount(ae);
	}
	
	public static String searchAddress(Address addr) {
		if (plugin == null) {
			Plugin p = Bukkit.getPluginManager().getPlugin("Sakuracoinish");
			plugin = (Sakuracoinish) p;
		}
		AccountEntry ae = plugin.getDatabase().find(AccountEntry.class).where().eq("addr", addr.toString()).findUnique();
		if (ae == null) {
			return null;
		}
		return ae.getPlayerName();
	}

	public static void serializeChecking(List<Transaction> toSerialize) {
		File save = new File("plugins/Sakuracoinish/tx.temp");
		PrintWriter pw = null;
		try {
            pw = new PrintWriter(save);
            for (Transaction current : toSerialize) {
                pw.println(current.getHash().toString());
            }
            pw.flush();
            pw.close();
        } catch (Exception ex) {
			ex.printStackTrace();
		}
	}
	
	public static Address parseAddress(String addr) {
		try {
			return new Address(Sakuracoinish.network, addr);
		} catch (WrongNetworkException e) {
			e.printStackTrace();
			return null;
		} catch (AddressFormatException e) {
			e.printStackTrace();
			return null;
		}
	}

	public static List<Transaction> loadChecking() {
		File open = new File("plugins/Sakuracoinish/tx.temp");
		List<Transaction> ret = new ArrayList<Transaction>();
		BufferedReader in = null;
		try {
			in = new BufferedReader(new FileReader(open));
		} catch (FileNotFoundException e) {
			return ret;
		}
		String strLine;
		try {
			while ((strLine = in.readLine()) != null) {
				ret.add(new Transaction(Sakuracoinish.network, 0, new Sha256Hash(strLine)));
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		try {
			in.close();
			open.delete();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return ret;
	}
	
	public static Sakuracoinish retrieveInstance() {
		if (plugin == null) {
			Plugin p = Bukkit.getPluginManager().getPlugin("Sakuracoinish");
			plugin = (Sakuracoinish) p;
		}
		return plugin;
	}
	
	public static List<Address> getContainedAddress(List<TransactionOutput> tx) throws ScriptException {
		List<Address> ret = new ArrayList<Address>();
		for (TransactionOutput current : tx) {
			System.out.println(current.getScriptPubKey().getToAddress());
			if (current.isMine(Sakuracoinish.bapi.getWallet())) {
				ret.add(current.getScriptPubKey().getToAddress());
			}
		}
		return ret;
	}
}
