package ap.scrabble.gameclient.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Observable;

import ap.scrabble.gameclient.model.board.Word;
import ap.scrabble.gameclient.model.properties.DictionaryServerConfig;
import ap.scrabble.gameclient.model.properties.HostServerConfig;
import ap.scrabble.gameclient.model.recipient.AllRecipient;
import ap.scrabble.gameclient.model.recipient.GameRecipient;
import ap.scrabble.gameclient.model.recipient.LocalRecipient;

public class GameManager extends Observable {

    private static GameManager GameManagerInstance;
    private List<Player> playerList = new ArrayList<>();
    private GameState gameState = GameState.MAIN_MENU;
    private DictionaryServerConfig dictionaryServerConfig;
    private HostServerConfig hostServerConfig;
    private TurnManager turnManager;
    private Game game;

    SocketHostServer socketHostServer;
    public static enum GameState {
        MAIN_MENU,
        CREATE_GAME,
        JOIN_GAME,
        PLAY
    }

    public static enum MessageType {
        PLAYER_ALREADY_EXISTS,
        PLAYER_ADDED,
        CURRENT_PLAYER,
        LOCAL_TURN,
        REMOTE_TURN,
        ILLEGAL_WORD,
        PLAY_NEXT_TURN,
        UPDATE_GAME_DATA,
        GAME_OVER,
    }

    public static class Message extends ap.scrabble.gameclient.util.Message<MessageType> {
        public Message(MessageType type, Object arg) {
            super(type, arg);
        }
    }




    public static GameManager get() {
        if (GameManagerInstance == null) {
            GameManagerInstance = new GameManager();
        }
        return GameManagerInstance;
    }

    public void setConfig(DictionaryServerConfig dictionaryServerConfig, HostServerConfig hostServerConfig) {
        this.dictionaryServerConfig = dictionaryServerConfig;
        this.hostServerConfig = hostServerConfig;
    }

    public void init() {
        // TODO: Initialize stuff if necessary
    }

    public GameState getGameState() {
        return gameState;
    }

    public Game getGame() {
        return game;
    }


    public void CreateGame(String HostName){
        //Start server
        //add Host player
        turnManager = new hostTurnManager(playerList);
        this.game = new Game(playerList);
        this.socketHostServer = new SocketHostServer(hostServerConfig.getPort(),new RemoteClientHandler(),6);
        socketHostServer.start();
        AddPlayer(LocalRecipient.get(), HostName,true);

    }
    public void JoinGame(String ClientName){

    }
    public void AddPlayer(GameRecipient requester, String PlayerName,boolean IsLocal){
        synchronized (playerList) {
            if (playerList.contains(PlayerName)) {
                requester.sendMessage(MessageType.PLAYER_ALREADY_EXISTS, PlayerName);
                return;
            }
            Player player = PlayerFactory.GetInstance().CreatePlayer(PlayerName,IsLocal);
            playerList.add(player);
            AllRecipient.get().sendMessage(MessageType.PLAYER_ADDED, PlayerName);
        }
    }
    public void StartGame(){
        
    }
    public void addWord(GameRecipient requester, Word word) {
        turnManager.getCurrentPlayer().PlaceWord(requester, word);
        // assuming the word was actually placed... not sure how to handle it otherwise...
        turnManager.PlayNextTurn(); // This needs to be called after the player successfully placed a word
    }
    @Override
    public synchronized void setChanged() {
        super.setChanged();
    }

}
