package me.meta1203.plugins.sakuracoin.sakuracoin;

import java.math.BigInteger;
import java.util.List;

import me.meta1203.plugins.sakuracoin.Sakuracoinish;
import me.meta1203.plugins.sakuracoin.Util;

import com.google.sakuracoin.core.*;
import com.google.sakuracoin.core.TransactionConfidence.ConfidenceType;
import com.google.sakuracoin.store.BlockStoreException;
import com.google.sakuracoin.core.Address;


public class CoinListener extends AbstractWalletEventListener {

	@Override
	public void onCoinsReceived(Wallet wallet, Transaction tx,
			BigInteger prevBalance, BigInteger newBalance) {
		
		
		BigInteger SKRAdded = newBalance.subtract(prevBalance); 
		BigInteger SKRAddedc = BigInteger.ZERO;
		if(SKRAdded.compareTo(SKRAddedc) != -1)
		{
				Sakuracoinish.checker.addCheckTransaction(tx);
		}
		
		

	
		
	}

}
