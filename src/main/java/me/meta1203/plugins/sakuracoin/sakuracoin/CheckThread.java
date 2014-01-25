package me.meta1203.plugins.sakuracoin.sakuracoin;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.google.sakuracoin.core.*;
import com.google.sakuracoin.core.TransactionConfidence.ConfidenceType;
import com.google.sakuracoin.store.BlockStoreException;

import me.meta1203.plugins.sakuracoin.Sakuracoinish;
import me.meta1203.plugins.sakuracoin.Util;

public class CheckThread extends Thread {
	private List<Transaction> toCheck = new ArrayList<Transaction>();

	private int waitTime = 0;
	private int confirmations = 0;

	public CheckThread(int wait, int confirmations) {
		Sakuracoinish.log.info("Checking for " + Integer.toString(confirmations) + " confirmations every " + Integer.toString(wait) + " seconds.");
		waitTime = wait;
		this.confirmations = confirmations;
		List<Transaction> toAdd = Util.loadChecking();
		Sakuracoinish.log.info("Adding " + toAdd.size() + " old transactions to the check pool!");
		for (Transaction current : toAdd) {
			Sakuracoinish.log.info("Added: " + current.getHashAsString());
			toCheck.add(current);
		}
	}

	public void run() {
		while (true) {
			check();
			try {
				synchronized (this) {
					this.wait(waitTime*1000);
				}
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}

	public synchronized void addCheckTransaction(Transaction tx) {
		toCheck.add(tx);
		Sakuracoinish.log.warning("Added transaction " + tx.getHashAsString() + " to check pool!");
	}

	public synchronized void serialize() {
		Util.serializeChecking(toCheck);
	}

	// Loop checks and outputs

	private void check() {
		synchronized (this) {
			Sakuracoinish.log.info("Checking 1"); 
			List<Transaction> toRemove = new ArrayList<Transaction>();
			for (Transaction current : toCheck) {
				
				
				Transaction currents = Sakuracoinish.bapi.getWallet().getTransaction(current.getHash());
			
				if (!currents.getConfidence().getConfidenceType().equals(ConfidenceType.BUILDING)) {
					Sakuracoinish.log.info("Still building");
					continue;
				}
				int conf = currents.getConfidence().getDepthInBlocks();
				Sakuracoinish.log.info(conf + " CONFIRMS");
				if (conf >= confirmations) {
						double value = Sakuracoinish.econ.bitcoinToInGame(currents.getValueSentToMe(Sakuracoinish.bapi.getWallet()));
						List<Address> receivers = null;
						try {
							Sakuracoinish.log.info(currents.getOutputs().toString());
							receivers = Util.getContainedAddress(currents.getOutputs());
						
						} catch (ScriptException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						
						for (Address x : receivers) {
							String pName = Util.searchAddress(x);
							Sakuracoinish.econ.addFunds(pName, value);
							Sakuracoinish.log.warning("Added " + Sakuracoinish.econ.formatValue(value, true) + " to " + pName + "!");
						}
						
						Sakuracoinish.bapi.saveWallet();
					toRemove.add(currents);
				}
			}
			toCheck.removeAll(toRemove);
		}
	}

}
