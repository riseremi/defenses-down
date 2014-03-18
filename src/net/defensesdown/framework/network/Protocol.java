package net.defensesdown.framework.network;

import net.defensesdown.framework.network.messages.Message;
import net.defensesdown.framework.network.messages.MessageConnected;
import net.defensesdown.framework.network.messages.MessageCreateUnit;
import net.defensesdown.framework.network.messages.MessageSetPlayerId;
import net.defensesdown.main.DefensesDown;
import net.defensesdown.player.Entity;
import net.defensesdown.player.GameClient;
import net.defensesdown.player.Unit;
import net.defensesdown.player.UnitCreator;

import java.io.IOException;

/**
 * @author riseremi
 */
public class Protocol {
    public static void processOnServer(Message message, int id) throws IOException {
        Message.Type type = message.getType();

        switch (type) {
            case CONNECTION:
                MessageConnected messageConnected = ((MessageConnected) message);

                Server.getInstance().getUnits().add((Unit) UnitCreator.getUnit(Entity.Type.KNIGHT, 0, 0, id));
                Server.getInstance().getUnits().add((Unit) UnitCreator.getUnit(Entity.Type.TOWER, 1, 0, id));
                Server.getInstance().getUnits().add((Unit) UnitCreator.getUnit(Entity.Type.KNIGHT, 2, 0, id));

                Server.getInstance().getUnits().add((Unit) UnitCreator.getUnit(Entity.Type.KNIGHT, 5, 0, id));
                Server.getInstance().getUnits().add((Unit) UnitCreator.getUnit(Entity.Type.RANGER, 6, 0, id));
                Server.getInstance().getUnits().add((Unit) UnitCreator.getUnit(Entity.Type.RANGER, 7, 0, id));

                if (Server.getInstance().isAllPlayersConnected()) {
                    Server.getInstance().sendToOne(message, id);
                    DefensesDown.getLobby().getUnitsList().add(new GameClient(messageConnected.getName(), messageConnected.getFraction()));
                    DefensesDown.getLobby().revalidateList();

                    Server.getInstance().getUnits().add((Unit) UnitCreator.getUnit(Entity.Type.KNIGHT, 0, 7, id));
                    Server.getInstance().getUnits().add((Unit) UnitCreator.getUnit(Entity.Type.TOWER, 1, 7, id));
                    Server.getInstance().getUnits().add((Unit) UnitCreator.getUnit(Entity.Type.KNIGHT, 2, 7, id));

                    Server.getInstance().getUnits().add((Unit) UnitCreator.getUnit(Entity.Type.KNIGHT, 5, 7, id));
                    Server.getInstance().getUnits().add((Unit) UnitCreator.getUnit(Entity.Type.RANGER, 6, 7, id));
                    Server.getInstance().getUnits().add((Unit) UnitCreator.getUnit(Entity.Type.RANGER, 7, 7, id));

                    for (int i = 0; i < Server.getInstance().getUnits().size(); i++) {
                        Unit unit = Server.getInstance().getUnits().get(i);
                        MessageCreateUnit messageCreateUnit = new MessageCreateUnit(unit.getType(), unit.getX(), unit.getY(), id);
                        Server.getInstance().sendToAll(messageCreateUnit);
                    }
                }
                break;
        }
    }

    public static void processOnClient(Message message) {
        Message.Type type = message.getType();
        switch (type) {
            case CONNECTION:
                MessageConnected messageConnected = ((MessageConnected) message);
                DefensesDown.getLobby().getUnitsList().add(new GameClient(messageConnected.getName(), messageConnected.getFraction()));
                DefensesDown.getLobby().revalidateList();
                break;
            case CREATE_UNIT:
                MessageCreateUnit messageCreateUnit = ((MessageCreateUnit) message);
                Entity.Type unitType = messageCreateUnit.getUnitType();
                int id = messageCreateUnit.getId();
                int x = messageCreateUnit.getX();
                int y = messageCreateUnit.getY();
                DefensesDown.getGame().getUnits().add((Unit) UnitCreator.getUnit(unitType, x, y, id));
                break;
            case SET_PLAYER_ID:
                MessageSetPlayerId messageSetPlayerId = ((MessageSetPlayerId) message);
                DefensesDown.getGame().getGameClient().setId(messageSetPlayerId.getId());
                break;
            case START_GAME:
                DefensesDown.startGame();
                break;
        }
    }

}