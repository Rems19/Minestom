package net.minestom.server.network.packet.client.login;

import net.minestom.server.MinecraftServer;
import net.minestom.server.chat.ChatColor;
import net.minestom.server.chat.ColoredText;
import net.minestom.server.extras.MojangAuth;
import net.minestom.server.network.ConnectionManager;
import net.minestom.server.network.ConnectionState;
import net.minestom.server.network.packet.PacketReader;
import net.minestom.server.network.packet.client.ClientPreplayPacket;
import net.minestom.server.network.packet.server.login.EncryptionRequestPacket;
import net.minestom.server.network.packet.server.login.LoginDisconnect;
import net.minestom.server.network.packet.server.login.LoginSuccessPacket;
import net.minestom.server.network.player.PlayerConnection;

import java.util.UUID;

public class LoginStartPacket implements ClientPreplayPacket {

    private static final String ALREADY_CONNECTED_JSON =
            ColoredText.of(ChatColor.RED, "You are already on this server").toString();

    public String username;

    @Override
    public void process(PlayerConnection connection, ConnectionManager connectionManager) {
        if (MojangAuth.isUsingMojangAuth()) {
            if (connectionManager.getPlayer(username) != null) {
                connection.sendPacket(new LoginDisconnect(ALREADY_CONNECTED_JSON));
                connection.disconnect();
                return;
            }

            connection.setConnectionState(ConnectionState.LOGIN);
            connection.setLoginUsername(username);
            EncryptionRequestPacket encryptionRequestPacket = new EncryptionRequestPacket(connection);
            connection.sendPacket(encryptionRequestPacket);
        } else {
            UUID playerUuid = connectionManager.getPlayerConnectionUuid(connection, username);

            int threshold = MinecraftServer.COMPRESSION_THRESHOLD;

            if (threshold > 0) {
                connection.enableCompression(threshold);
            }

            LoginSuccessPacket successPacket = new LoginSuccessPacket(playerUuid, username);
            connection.sendPacket(successPacket);

            connection.setConnectionState(ConnectionState.PLAY);
            connectionManager.createPlayer(playerUuid, username, connection);
        }
    }

    @Override
    public void read(PacketReader reader) {
        this.username = reader.readSizedString();
    }

}
