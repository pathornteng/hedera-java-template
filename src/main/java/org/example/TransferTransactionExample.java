package org.example;

import com.hedera.hashgraph.sdk.*;
import io.github.cdimascio.dotenv.Dotenv;
import com.hedera.hashgraph.sdk.Hbar;

import java.util.concurrent.TimeoutException;

public class TransferTransactionExample {

    public static void main(String[] args) throws PrecheckStatusException, TimeoutException, ReceiptStatusException {

        //Grab your Hedera Testnet account ID and private key
        AccountId myAccountId = AccountId.fromString(Dotenv.load().get("MY_ACCOUNT_ID"));
        PrivateKey myPrivateKey = PrivateKey.fromString(Dotenv.load().get("MY_PRIVATE_KEY"));

        //Create your Hedera Testnet client
        Client client = Client.forTestnet();
        client.setOperator(myAccountId, myPrivateKey);

        // Set default max transaction fee & max query payment
        client.setDefaultMaxTransactionFee(new Hbar(100));

        Hbar amount = new Hbar(10);
        AccountId receiver = new AccountId(3);

        TransferTransaction tx = new TransferTransaction()
                .addHbarTransfer(myAccountId, amount.negated())
                .addHbarTransfer(receiver, amount)
                .setTransactionMemo("transfer test")
                .freezeWith(client);

        TransferTransaction signedTx = tx.sign(myPrivateKey);
        TransactionResponse txResponse = signedTx.execute(client);

        System.out.println("transaction ID: " + txResponse);

        TransactionReceipt receipt = txResponse.getReceipt(client);

        System.out.println("transferred " + amount + "...");
        System.out.println(receipt.toString());
    }
}