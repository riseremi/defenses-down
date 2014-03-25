package net.defensesdown.framework.network;

import java.io.IOException;
import net.defensesdown.framework.network.messages.Message;
import net.defensesdown.framework.network.messages.MessageConnected;
import net.defensesdown.framework.network.messages.MessageCreateUnit;
import net.defensesdown.framework.network.messages.MessageDealDamage;
import net.defensesdown.framework.network.messages.MessageDeleteUnit;
import net.defensesdown.framework.network.messages.MessageSetPlayerId;
import net.defensesdown.framework.network.messages.MessageSetPosition;
import net.defensesdown.framework.network.messages.MessageSwapTeams;
import net.defensesdown.main.DefensesDown;
import net.defensesdown.player.Entity;
import net.defensesdown.player.GameClient;
import net.defensesdown.player.Unit;
import net.defensesdown.player.UnitCreator;
import net.defensesdown.screens.Game;

/**
 * @author riseremi
 */
public class Protocol {

    private static MessageConnected tempMessageConnected;
    private static int id1 = 0;

    public static void processOnServer(Message message, int id) throws IOException {
        Message.Type type = message.getType();

        switch (type) {
            case CONNECTION:
                System.out.println(id + " cntcd");
                MessageConnected messageConnected = ((MessageConnected) message);

                if (Server.getInstance().isAllPlayersConnected()) {
                    Server.getInstance().sendToOne(tempMessageConnected, id);
                    DefensesDown.getLobby().getUnitsList().add(new GameClient(messageConnected.getName(), messageConnected.getFraction()));
                    DefensesDown.getLobby().revalidateList();

                    Server.getInstance().getUnits().add((Unit) UnitCreator.getUnit(Entity.Type.TOWER, 7, 0, id1++, id));

                    Server.getInstance().getUnits().add((Unit) UnitCreator.getUnit(Entity.Type.RANGER, 6, 0, id1++, id));
                    Server.getInstance().getUnits().add((Unit) UnitCreator.getUnit(Entity.Type.RANGER, 7, 1, id1++, id));
                    Server.getInstance().getUnits().add((Unit) UnitCreator.getUnit(Entity.Type.KNIGHT, 4, 0, id1++, id));
                    Server.getInstance().getUnits().add((Unit) UnitCreator.getUnit(Entity.Type.KNIGHT, 5, 2, id1++, id));
                    Server.getInstance().getUnits().add((Unit) UnitCreator.getUnit(Entity.Type.KNIGHT, 7, 3, id1++, id));

                    for (int i = 0; i < Server.getInstance().getUnits().size(); i++) {
                        Unit unit = Server.getInstance().getUnits().get(i);
                        MessageCreateUnit messageCreateUnit = new MessageCreateUnit(unit.getType(), unit.getX(), unit.getY(), unit.getId(), unit.getOwner());
                        Server.getInstance().sendToAll(messageCreateUnit);
                    }
                    Server.getInstance().sendToAll(new MessageSwapTeams());
                } else {
                    tempMessageConnected = (MessageConnected) message;
                }

                Server.getInstance().getUnits().add((Unit) UnitCreator.getUnit(Entity.Type.TOWER, 0, 7, id1++, id));

                Server.getInstance().getUnits().add((Unit) UnitCreator.getUnit(Entity.Type.RANGER, 0, 6, id1++, id));
                Server.getInstance().getUnits().add((Unit) UnitCreator.getUnit(Entity.Type.RANGER, 1, 7, id1++, id));

                Server.getInstance().getUnits().add((Unit) UnitCreator.getUnit(Entity.Type.KNIGHT, 0, 4, id1++, id));
                Server.getInstance().getUnits().add((Unit) UnitCreator.getUnit(Entity.Type.KNIGHT, 3, 7, id1++, id));
                Server.getInstance().getUnits().add((Unit) UnitCreator.getUnit(Entity.Type.KNIGHT, 2, 5, id1++, id));
                break;
            default:
                Server.getInstance().sendToAll(message);
                break;
            case END_TURN:
                Server.getInstance().sendToAllExcludingOne(message, id);
                break;
        }
    }

    public static void processOnClient(Message message) {
        Message.Type type = message.getType();
        final Game game = DefensesDown.getGame();

        switch (type) {
            case CONNECTION:
                MessageConnected messageConnected = ((MessageConnected) message);
                DefensesDown.getLobby().getUnitsList().add(new GameClient(messageConnected.getName(), messageConnected.getFraction()));
                DefensesDown.getLobby().revalidateList();

                game.getGameClient().setFraction(messageConnected.getFraction());
                game.getGameClient().setName(messageConnected.getName());
                break;
            case CREATE_UNIT:
                MessageCreateUnit messageCreateUnit = ((MessageCreateUnit) message);
                Entity.Type unitType = messageCreateUnit.getUnitType();
                int ownerId = messageCreateUnit.getOwnerId();
                int id = messageCreateUnit.getId();
                int x = messageCreateUnit.getX();
                int y = messageCreateUnit.getY();
                game.getUnits().add((Unit) UnitCreator.getUnit(unitType, x, y, id, ownerId));
                break;
            case SET_PLAYER_ID:
                MessageSetPlayerId messageSetPlayerId = ((MessageSetPlayerId) message);
                game.getGameClient().setId(messageSetPlayerId.getId());
                break;
            case START_GAME:
                DefensesDown.startGame();
                final int fraction2 = game.getGameClient().getFraction();
                String title = fraction2 == GameClient.WHITE ? "Defenses Down - Your turn" : "Defenses Down - Enemy turn";
                DefensesDown.getFrames()[0].setTitle(title);
                game.setMyTurn(fraction2 == GameClient.WHITE);
                break;
            case SWAP_TEAMS:
                int fraction = DefensesDown.getLobby().getUnitsList().get(0).getFraction();
                int opposite = fraction == GameClient.BLACK ? GameClient.WHITE : GameClient.BLACK;
                DefensesDown.getLobby().getUnitsList().get(0).setFraction(opposite);
                DefensesDown.getLobby().getUnitsList().get(1).setFraction(fraction);
                DefensesDown.getLobby().revalidateList();
                game.getGameClient().setFraction(opposite);
                break;
            case SET_POSITION:
                MessageSetPosition messageSetPosition = ((MessageSetPosition) message);
                final int xx = messageSetPosition.getX();
                final int yy = messageSetPosition.getY();

                game.getUnits().get(messageSetPosition.getId()).setX(xx);
                game.getUnits().get(messageSetPosition.getId()).setY(yy);
                game.repaint();
                break;
            case END_TURN:
                DefensesDown.getFrames()[0].setTitle("Defenses Down - Your turn");
                game.setMyTurn(!game.isMyTurn());
                game.setTurnText("YOUR TURN");
                game.setDrawTurnText(true);
                break;
            case DEAL_DAMAGE:
                MessageDealDamage messageDealDamage = ((MessageDealDamage) message);
                final Unit toDamage = game.getUnits().get(messageDealDamage.getId());
                if (toDamage == null) {
                    game.repaint();
                    break;
                }
                toDamage.dealPureDamage(messageDealDamage.getDamage());
                game.repaint();
                break;
            case DELETE_UNIT:
                MessageDeleteUnit messageDeleteUnit = ((MessageDeleteUnit) message);
//                game.removeUnitById(messageDeleteUnit.getId());
                Unit toRemove = game.getUnits().get(messageDeleteUnit.getId());
                toRemove.disable();
//                game.getUnits().remove(toRemove);
                game.repaint();
                break;
        }
    }

}
