package bouldercreek.jumpingboulder;

import java.io.IOException;

/**
 * Created by jakob on 17-06-2016.
 */
public class Client {
    public GameThread game;
    private int clientId;


    public Client() {

    }

    public void act(byte[] data){
        switch (data[4] & 0b10000000) {
            case 0b00000000: notInGame(data);
                break;
            case 0b10000000: inGame(data);
                break;
        }

    }


    private void notInGame(byte[] data){
        switch (data[4] & 0b01111111){
            case 0b00000001: newGame();
        }
    }

    private void newGame() {
        game = Main.newGame(this);

    }


    private void inGame(byte[] data) {


    }


    public int getClientId() {
        return clientId;
    }
}
