package net.defensesdown.screens;

import net.defensesdown.framework.Utils;
import net.defensesdown.framework.network.Client;
import net.defensesdown.framework.network.Server;
import net.defensesdown.framework.network.messages.MessageConnected;
import net.defensesdown.framework.network.messages.MessageStartGame;
import net.defensesdown.player.GameClient;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;

/**
 * User: riseremi
 * Date: 18.03.14
 * Time: 3:04
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

        name.setBounds(Game.FRAME, Game.HEIGHT - Game.FRAME, Game.WIDTH, 24);
        this.add(name);

        playersList.setBounds(Game.FRAME, Game.FRAME, Game.WIDTH, Game.HEIGHT / 2);
        this.add(playersList);

        host.setBounds(Game.FRAME, Game.HEIGHT / 2 + Game.FRAME * 2, Game.WIDTH / 2, 24);
        this.add(host);
        host.addActionListener(this);

        join.setBounds(Game.FRAME + Game.WIDTH / 2, Game.HEIGHT / 2 + Game.FRAME * 2, Game.WIDTH / 2, 24);
        this.add(join);
        join.addActionListener(this);

        swapTeams.setBounds(Game.FRAME, Game.HEIGHT / 2 + Game.FRAME * 2, Game.WIDTH, 24);
        this.add(swapTeams);
        swapTeams.addActionListener(this);
        swapTeams.setVisible(false);


        start.setBounds(Game.FRAME, Game.HEIGHT / 2 + Game.FRAME * 3 + 24, Game.WIDTH, 24);
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
        if (e.getActionCommand() == host.getActionCommand()) {
            unitsList.add(new GameClient(name.getText(), GameClient.WHITE));
            revalidateList();

            Server.getInstance();
            //Client.getInstance();
            try {
                Client.getInstance().send(new MessageConnected(name.getText(), GameClient.WHITE));
            } catch (IOException e1) {
            }
            host.setVisible(false);
            join.setVisible(false);

            start.setVisible(true);
            swapTeams.setVisible(true);
        }

        if (e.getActionCommand() == join.getActionCommand()) {
            unitsList.add(new GameClient(name.getText(), GameClient.BLACK));
            revalidateList();

            //Client.getInstance();
            try {
                Client.getInstance().send(new MessageConnected(name.getText(), GameClient.BLACK));
            } catch (IOException e1) {
            }
            host.setVisible(false);
            join.setVisible(false);
        }

        if (e.getActionCommand() == start.getActionCommand()) {
            try {
                Server.getInstance().sendToAll(new MessageStartGame());
            } catch (IOException e1) {
            }
        }

        if (e.getActionCommand() == swapTeams.getActionCommand()) {
            int fraction = unitsList.get(0).getFraction();
            int opposite = fraction == GameClient.BLACK ? GameClient.WHITE : GameClient.BLACK;
            unitsList.get(0).setFraction(opposite);
            unitsList.get(1).setFraction(fraction);
            revalidateList();
        }
    }

    public ArrayList<GameClient> getUnitsList() {
        return unitsList;
    }
}
