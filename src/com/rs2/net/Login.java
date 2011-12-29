package com.rs2.net;

import java.nio.ByteBuffer;
import java.security.SecureRandom;

import com.rs2.Constants;
import com.rs2.model.players.Player;
import com.rs2.model.players.Player.LoginStages;
import com.rs2.util.NameUtil;
import com.rs2.util.PlayerSave;
import com.rs2.util.PunishmentManager;

public class Login {
	
	public void handleLogin(Player player, ByteBuffer inData) throws Exception {
		switch (player.getLoginStage()) {
		case CONNECTED:
			if (inData.remaining() < 2) {
				inData.compact();
				return;
			}

			// Validate the request.
			int request = inData.get() & 0xff;
			inData.get(); // Name hash.
			if (request != 14) {
				System.err.println("Invalid login request: " + request);
				player.disconnect();
				return;
			}

			// Write the response.
			StreamBuffer.OutBuffer out = StreamBuffer.newOutBuffer(17);
			out.writeLong(0); // First 8 bytes are ignored by the client.
			out.writeByte(0); // The response opcode, 0 for logging in.
			out.writeLong(new SecureRandom().nextLong()); // SSK.
			player.send(out.getBuffer());

			player.setLoginStage(LoginStages.LOGGING_IN);
			break;
		case LOGGING_IN:
			if (inData.remaining() < 2) {
				inData.compact();
				return;
			}

			// Validate the login type.
			int loginType = inData.get();
			if (loginType != 16 && loginType != 18) {
				System.err.println("Invalid login type: " + loginType);
				player.disconnect();
				return;
			}

			// Ensure that we can read all of the login block.
			int blockLength = inData.get() & 0xff;
			if (inData.remaining() < blockLength) {
				inData.flip();
				inData.compact();
				return;
			}

			// Read the login block.
			StreamBuffer.InBuffer in = StreamBuffer.newInBuffer(inData);
			in.readByte(); // Skip the magic ID value 255.

			// Validate the client version.
			int clientVersion = in.readShort();
			if (clientVersion != 317) {
				System.err.println("Invalid client version: " + clientVersion);
				player.disconnect();
				return;
			}

			in.readByte(); // Skip the high/low memory version.

			// Skip the CRC keys.
			for (int i = 0; i < 9; i++) {
				in.readInt();
			}

			in.readByte(); // Skip RSA block length.
			// If we wanted to, we would decode RSA at this point.

			// Validate that the RSA block was decoded properly.
			int rsaOpcode = in.readByte();
			if (rsaOpcode != 10) {
				System.err.println("Unable to decode RSA block properly!");
				player.disconnect();
				return;
			}

			// Set up the ISAAC ciphers.
			long clientHalf = in.readLong();
			long serverHalf = in.readLong();
			int[] isaacSeed = { (int) (clientHalf >> 32), (int) clientHalf, (int) (serverHalf >> 32), (int) serverHalf };
			player.setDecryptor(new ISAACCipher(isaacSeed));
			for (int i = 0; i < isaacSeed.length; i++) {
				isaacSeed[i] += 50;
			}
			player.setEncryptor(new ISAACCipher(isaacSeed));
			// Read the user authentication.
			in.readInt(); // Skip the user ID.
			int playerMacAddress = in.readInt();
			String username = in.readString();
			String password = in.readString();
			player.setUsername(username);
			PlayerSave.load(player);
			player.setMacAddress(playerMacAddress);
			if (password != null && player.getPassword() != null && player.getPassword() != "" && 
					!player.getPassword().equals(password)) {
				player.setReturnCode(Constants.LOGIN_RESPONSE_INVALID_CREDENTIALS);
			}
			else if (PunishmentManager.getPunishmentStatus(username, playerMacAddress, player.getHost(), PunishmentManager.Punishments.BAN)) {
				player.setReturnCode(Constants.LOGIN_RESPONSE_ACCOUNT_DISABLED);
			}
			else {
				player.setPassword(password);
			}
			player.setUsername(NameUtil.uppercaseFirstLetter(player.getUsername()));
			player.login();
			player.setLoginStage(LoginStages.LOGGED_IN);
			break;
		}
	}

}
