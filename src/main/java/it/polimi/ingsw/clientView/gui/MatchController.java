package it.polimi.ingsw.clientView.gui;

import it.polimi.ingsw.Pair;
import it.polimi.ingsw.clientView.BoardRepresentation;
import it.polimi.ingsw.clientView.ClientView;
import it.polimi.ingsw.model.Action;

import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.DialogPane;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;


import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.lang.reflect.Type;
import java.util.*;

public class MatchController {
    private static ClientView clientView;

    @FXML
    private Label message;
    @FXML
    private VBox possibleActionsBox;
    @FXML
    private GridPane board;
    @FXML
    private HBox hBox;
    @FXML
    private VBox vBoxLeft;
    @FXML
    private VBox vBoxRight;

    private final int LVL1=0, LVL2=1, LVL3=2, DOME=3, WORKER=4, SHADOW=5;
    private String actualAction;
    private Map<String, Image> workerColors;
    private List<Image> tileLevel;
    boolean isInitialized = false;

    private List<Pair<Integer,Integer>> workerPlacement = new ArrayList<>();



    public MatchController() {
    }

    @FXML
    public void initialize() {
        createBoard();
        board.prefHeightProperty().bind(hBox.heightProperty().subtract(50));
        board.prefWidthProperty().bind(board.heightProperty());
        hBox.setMinHeight(200);
        hBox.setMinWidth(400);

        //Testing
        //placeWorkers(2,2,3,4);
//        for(int i=0; i<5; i++)
//            toggleSelectable(i,i);
        //actualAction = "build";

        vBoxLeft.setSpacing(40);
        vBoxLeft.setAlignment(Pos.CENTER);

        workerColors = Map.of(
                "blue" , new Image("/graphics/blue.png"),
                "cream", new Image("/graphics/cream.png"),
                "white", new Image("/graphics/white.png"));

        tileLevel = Arrays.asList(
                new Image("/graphics/lvl1.png"),
                new Image("/graphics/lvl2.png"),
                new Image("/graphics/lvl3.png"),
                new Image("/graphics/dome.png"));

        isInitialized = true;
    }

    public static void setClientView(ClientView clientView) {
        MatchController.clientView = clientView;
    }

    public void createBoard() {
        for(int i=0; i<board.getColumnCount(); i++) {
            for(int j=0; j<board.getRowCount(); j++) {
                StackPane s = new StackPane();
                s.prefHeightProperty().bind(board.heightProperty().divide(board.getRowCount()).subtract(10));
                s.prefWidthProperty().bind(s.heightProperty());

                for(int k=0; k<3; k++) {
                    ImageView lvl = new ImageView();
                    lvl.fitHeightProperty().bind(board.heightProperty().divide(board.getRowCount()).subtract(10).multiply(1-(0.175*k)));
                    lvl.fitWidthProperty().bind(lvl.fitHeightProperty());
                    s.getChildren().add(lvl);
                }
                ImageView dome = new ImageView();
                dome.fitHeightProperty().bind(board.heightProperty().divide(board.getRowCount()).subtract(10).multiply(0.5));
                dome.fitWidthProperty().bind(dome.fitHeightProperty());
                s.getChildren().add(dome);

                ImageView worker = new ImageView();
                worker.fitHeightProperty().bind(board.heightProperty().divide(board.getRowCount()).subtract(10).multiply(0.95));
                worker.fitWidthProperty().bind(worker.fitHeightProperty());
                s.getChildren().add(worker);

                StackPane selectableShade = new StackPane();
                selectableShade.setBackground(new Background(new BackgroundFill(Color.YELLOW, CornerRadii.EMPTY, Insets.EMPTY)));
                selectableShade.setOpacity(0.4);
                selectableShade.setVisible(false);
                s.getChildren().add(selectableShade);



                board.add(s, i, j);

                s.setOnMouseClicked((e) -> action(s));
            }
        }
    }

    public void action(StackPane s) {

        //TODO: add worker selection

        switch(actualAction) {
            case "move":
                moveOnClickedTile(s);
                break;

            case "build":
                buildOnClickedTile(s);
                break;

            case "builddome":
                buildDomeOnClickedTile(s);
                break;

            case "end":
                //nothing
                break;

            case "selectworker":
                selectWorker(s);
                break;
            case "placeworkers":
                placeWorkerOnClickedTile(s);
                break;
            default: break;
        }

    }

    /*
    methods that manage click on tiles --> invocation of methods on clientView
    commented methods can be useful to modify the board after model state changes
     */

    public void placeWorkerOnClickedTile(StackPane s) {
        int x = GridPane.getRowIndex(s);
        int y = GridPane.getColumnIndex(s);


        System.out.println(x + "-" + y);

        workerPlacement.add(new Pair<>(x,y));

        s.getChildren().get(SHADOW).setVisible(true);

        if(workerPlacement.size()==2) {
            System.out.println("workers placed in: " + workerPlacement.get(0).getFirst() + "-" + workerPlacement.get(0).getSecond() + "and " + workerPlacement.get(1).getFirst() + "-" + workerPlacement.get(1).getSecond());
            actualAction = "default";
            clientView.workerPlacement(workerPlacement.get(0).getFirst(),workerPlacement.get(0).getSecond(),workerPlacement.get(1).getFirst(),workerPlacement.get(1).getSecond());
        }
    }

    public void moveOnClickedTile(StackPane s) {
        int x = GridPane.getRowIndex(s);
        int y = GridPane.getColumnIndex(s);

        System.out.println(x + "-" + y);

        actualAction = "default";

        clientView.actionQuestion(Action.MOVE,x,y);
    }

    public void buildOnClickedTile(StackPane s) {
        int x = GridPane.getRowIndex(s);
        int y = GridPane.getColumnIndex(s);

        System.out.println(x + "-" + y);

        actualAction = "default";

        clientView.actionQuestion(Action.BUILD,x,y);
    }

    public void buildDomeOnClickedTile(StackPane s) {
        int x = GridPane.getRowIndex(s);
        int y = GridPane.getColumnIndex(s);

        System.out.println(x + "-" + y);

        actualAction = "default";

        clientView.actionQuestion(Action.BUILDDOME,x,y);
    }

    public void handlePossibleActions(List<Action> possibleActions) {
        message.setText("Choose the action\nto perform: ");

        List<Button> actionButtons = new ArrayList<>();
        for(Action action : possibleActions) {
            System.out.println(action.toString());

            Button actionBtn = new Button(action.toString());
            actionBtn.setOnMouseClicked((event) -> {
                actualAction = action.toString().toLowerCase();
                emptyPossibleActions();
                if(action != Action.END)
                    message.setText("Choose tile to\n" + action.toString());
                else
                    message.setText("Your turn is over");
            });
            actionButtons.add(actionBtn);
        }

        possibleActionsBox.getChildren().addAll(actionButtons);
    }

    private void emptyPossibleActions() {
        possibleActionsBox.getChildren().clear();
    }


    /*

     */

    public void selectWorker(StackPane s) {
        ImageView v = (ImageView) s.getChildren().get(WORKER);

        int x = GridPane.getRowIndex(s);
        int y = GridPane.getColumnIndex(s);

        if(v.getImage() != null){
            System.out.println("Worker Selected");
            clientView.selectWorkerQuestion(x,y);

        }

    }

    public void placeWorkers(String username, int x1, int y1, int x2, int y2) {

        StackPane s1 = (StackPane) getBoardCell(x1,y1);
        s1.getChildren().get(SHADOW).setVisible(false);
        ImageView w1 = (ImageView) s1.getChildren().get(WORKER);

        StackPane s2 = (StackPane) getBoardCell(x2,y2);
        s2.getChildren().get(SHADOW).setVisible(false);
        ImageView w2 = (ImageView) s2.getChildren().get(WORKER);

        String color = clientView.getBoard().getPlayersMap().get(username).getColor().toString().toLowerCase();


        Image image = workerColors.get(color);
        w1.setImage(image);
        w2.setImage(image);
    }

    public void moveUpdate(String player, int x1, int y1, int x2, int y2) {
        System.out.println("update move");

        String color = clientView.getBoard().getPlayersMap().get(player).getColor().toString().toLowerCase();

        message.setText(player + " has moved from " + x1+"-"+y1 + " to " + x2+"-"+y2);

        StackPane stackPaneFrom = (StackPane) board.getChildren().get(y1*board.getRowCount() + x1);   //TODO: check if x and y must be inverted
        StackPane stackPaneTo = (StackPane) board.getChildren().get(y2*board.getRowCount() + x2);   //TODO: check if x and y must be inverted

        ImageView workerFrom = (ImageView) stackPaneFrom.getChildren().get(WORKER);
        Image workerImg = workerColors.get(color);
        if(workerImg == null) {
            System.out.println("Huston we have a problem, a move arrived from an empty tile");
            return;
        }

        //List<ImageView> l = workersForColor.get(color);

        if(workerFrom.getImage().equals(workerColors.get(color))) workerFrom.setImage(null);



        //workerFrom.setImage(null);

        ImageView workerTo = (ImageView) stackPaneTo.getChildren().get(WORKER);
        workerTo.setImage(null);
        workerTo.setImage(workerImg);
    }

    public void buildUpdate(String player, int x, int y) {
        message.setText(player + " has built on tile " + x+"-"+y);
        System.out.println(x + "-" + y);
        StackPane s = (StackPane) board.getChildren().get(y*board.getRowCount() + x);   //TODO: check if x and y must be inverted

        ImageView imageView;

        switch(clientView.getBoard().getBoard()[x][y]) {
            case 1:
                imageView = (ImageView) s.getChildren().get(LVL1);
                imageView.setImage(tileLevel.get(LVL1));
                break;
            case 2:
                imageView = (ImageView) s.getChildren().get(LVL2);
                imageView.setImage(tileLevel.get(LVL2));
                break;
            case 3:
                imageView = (ImageView) s.getChildren().get(LVL3);
                imageView.setImage(tileLevel.get(LVL3));
                break;
            case 4:
            case 5:
            case 6:
            case 7:
                imageView = (ImageView) s.getChildren().get(DOME);
                imageView.setImage(tileLevel.get(DOME));
                break;
            default:
                System.out.println("boh");
        }
    }


    /*
    methods that ask user to do something and change actualAction --> invoked by gui
     */

    public void setActionPlaceWorkers() {
        System.out.println("Now you PLACEWORKERS");
        message.setText("CLICK ON TILE WHERE YOU WANT TO PLACE WORKERS");
        //userInteractionLabel.setVisible(true);
        actualAction = "placeworkers";

    }

    public void setActionSelectWorker() {
        System.out.println("Now you SELECTWORKER");
        message.setText("CLICK THE WORKER YOU WANT TO PLAY WITH");
        //userInteractionLabel.setVisible(true);
        actualAction = "selectworker";
        focusWorkers(true);
    }




    public void setPlayers() {
        List<String> players = clientView.getBoard().getPlayersNames();
        List<String> divinities = new ArrayList<>();

        clientView.getBoard().getDivinities().keySet().forEach(x -> divinities.add(x));



        for(int i=0;i<divinities.size();i++) {
            ImageView god = new ImageView();

            String url = "/graphics/" + divinities.get(i).toLowerCase() + ".png";
            Image godImage = new Image(url);

            god.setImage(godImage);
            god.setFitHeight(150);
            god.setFitWidth(100);

            VBox box = new VBox();
            box.setSpacing(5);
            box.setAlignment(Pos.CENTER);

            box.getChildren().addAll(god,new Label(players.get(i)));

            vBoxLeft.getChildren().add(box);
        }
    }

    public void setSelectable(int x, int y, boolean val) {
        StackPane s = (StackPane) board.getChildren().get(board.getRowCount()*y + x);
        StackPane shadow = (StackPane) s.getChildren().get(SHADOW);

        //shadow.setVisible(val);
    }

    public void focusWorkers(boolean value) {
        String color = clientView.getBoard().getPlayersMap().get(clientView.getUsername()).getColor().toString().toLowerCase();
        StackPane x;
        ImageView v;
        for(int i=0; i<25; i++) {
            Label l = new Label(i%5 + "-" + i/5);
            x = (StackPane) board.getChildren().get(i);
            x.getChildren().add(l);
            v = (ImageView) x.getChildren().get(WORKER);
            if(v.getImage()!=null && v.getImage().equals(workerColors.get(color)))
                setSelectable(i%5, i/5, value);
        }
    }

    /*
    return the tile in (x,y) position
    from StackOverflow: https://stackoverflow.com/questions/20655024/javafx-gridpane-retrieve-specific-cell-content
     */
    public Node getBoardCell(int x, int y) {
        for (Node node : board.getChildren()) {
            if (GridPane.getColumnIndex(node) == y && GridPane.getRowIndex(node) == x) {
                return node;
            }
        }
        return null;
    }

//    public void textMessage(String msg) {
//        if(isInitialized)
//            userInteractionLabel.setText("system message: \n" + msg);
//    }
}
