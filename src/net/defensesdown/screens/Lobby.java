package net.defensesdown.screens;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;
import javax.swing.JButton;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JTextField;
import net.defensesdown.framework.Utils;
import net.defensesdown.framework.network.Client;
import net.defensesdown.framework.network.Server;
import net.defensesdown.framework.network.messages.MessageConnected;
import net.defensesdown.framework.network.messages.MessageStartGame;
import net.defensesdown.framework.network.messages.MessageSwapTeams;
import net.defensesdown.player.GameClient;

/**
 * User: riseremi Date: 18.03.14 Time: 3:04
 */
public class Lobby extends JPanel implements ActionListener {
    public static final Random rnd = new Random();
    private JButton host = new JButton("Host");
    private JButton join = new JButton("Join");
    private JButton swapTeams = new JButton("Swap teams");
    private JButton start = new JButton("Start");
    //
    private JTextField name = new JTextField(Utils.getRandomName());
    //
    private JList<GameClient> playersList;
    private ArrayList<GameClient> unitsList;

    public Lobby() {
        unitsList = new ArrayList<>();
//        unitsList.add(new GameClient("Remi", GameClient.BLACK));
//        unitsList.add(new GameClient("White Oak", GameClient.WHITE));

        playersList = new JList<>();
        playersList.setListData(unitsList.toArray(new GameClient[unitsList.size()]));

        this.setLayout(null);

        name.setBounds(Game.FRAME, Game.GHEIGHT - Game.FRAME, Game.GWIDTH, 24);
        this.add(name);

        playersList.setBounds(Game.FRAME, Game.FRAME, Game.GWIDTH, Game.GHEIGHT / 2);
        this.add(playersList);

        host.setBounds(Game.FRAME, Game.GHEIGHT / 2 + Game.FRAME * 2, Game.GWIDTH / 2, 24);
        this.add(host);
        host.addActionListener(this);

        join.setBounds(Game.FRAME + Game.GWIDTH / 2, Game.GHEIGHT / 2 + Game.FRAME * 2, Game.GWIDTH / 2, 24);
        this.add(join);
        join.addActionListener(this);

        swapTeams.setBounds(Game.FRAME, Game.GHEIGHT / 2 + Game.FRAME * 2, Game.GWIDTH, 24);
        this.add(swapTeams);
        swapTeams.addActionListener(this);
        swapTeams.setVisible(false);

        start.setBounds(Game.FRAME, Game.GHEIGHT / 2 + Game.FRAME * 3 + 24, Game.GWIDTH, 24);
        this.add(start);
        start.addActionListener(this);
        start.setVisible(false);
        //start.setEnabled(false);
    }

    public void revalidateList() {
        playersList.setListData(unitsList.toArray(new GameClient[unitsList.size()]));
        if (unitsList.size() == 2) {
            start.setEnabled(true);
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getActionCommand().equals(host.getActionCommand())) {
            unitsList.add(new GameClient(name.getText(), GameClient.BLACK));
            revalidateList();

            Server.getInstance();
            //Client.getInstance();
            try {
                Client.getInstance().send(new MessageConnected(name.getText(), GameClient.BLACK));
            } catch (IOException e1) {
            }
            host.setVisible(false);
            join.setVisible(false);

            start.setVisible(true);
            swapTeams.setVisible(true);
        }

        if (e.getActionCommand().equals(join.getActionCommand())) {
            unitsList.add(new GameClient(name.getText(), GameClient.WHITE));
            revalidateList();

            //Client.getInstance();
            try {
                Client.getInstance().send(new MessageConnected(name.getText(), GameClient.WHITE));
            } catch (IOException e1) {
            }
            host.setVisible(false);
            join.setVisible(false);
        }

        if (e.getActionCommand().equals(start.getActionCommand())) {
            try {
                Server.getInstance().sendToAll(new MessageStartGame());
            } catch (IOException e1) {
            }
        }

        if (e.getActionCommand().equals(swapTeams.getActionCommand())) {
            try {
                Server.getInstance().sendToAll(new MessageSwapTeams());
            } catch (IOException ex) {
            }
        }
    }

    public ArrayList<GameClient> getUnitsList() {
        return unitsList;
    }
}
