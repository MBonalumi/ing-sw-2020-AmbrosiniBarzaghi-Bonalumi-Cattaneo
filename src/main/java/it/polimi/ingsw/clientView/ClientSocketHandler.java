package it.polimi.ingsw.clientView;

import it.polimi.ingsw.Observable;
import it.polimi.ingsw.events.clientToServer.ClientEvent;
import it.polimi.ingsw.events.serverToClient.Disconnect;
import it.polimi.ingsw.events.serverToClient.ServerEvent;
import it.polimi.ingsw.serverView.PingSender;
import it.polimi.ingsw.serverView.Server;
import org.w3c.dom.Document;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.*;
import java.net.Socket;
import java.util.Scanner;

public class ClientSocketHandler extends Observable<ServerEvent> implements Runnable {

    Socket socket;
    private ObjectInputStream in;
    private ObjectOutputStream out;

    public ClientSocketHandler(){
        try {
            connectionConfigParser();
            in = new ObjectInputStream(socket.getInputStream());
            out = new ObjectOutputStream(socket.getOutputStream());
            PingReceiver ping = new PingReceiver(this);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public ClientSocketHandler(Socket socket) {
        this.socket = socket;

        ObjectInputStream tempin = null;
        ObjectOutputStream tempout = null;

        try {
            tempout = new ObjectOutputStream(socket.getOutputStream());
            tempin = new ObjectInputStream(new BufferedInputStream(socket.getInputStream()));
        } catch (IOException e) {
            e.printStackTrace();
        }

        this.in = tempin;
        this.out = tempout;

        PingReceiver ping = new PingReceiver(this);
    }


    @Override
    public void run() {
        System.out.println("running...");

        try {
            while(true) {
                ServerEvent event = (ServerEvent) in.readObject();
                //System.out.println("received event...");

                if(event != null) {
                    //System.out.println("event not null");
                    ServerEvent finalEvent = event;
                    new Thread(()->{
                        receiveEvent(finalEvent);
                    }).run();
                    //notify(event);
                }
                else {
                    System.out.println("null event");
                }
            }
        } catch(Exception e) {

            notify(new Disconnect());
        }

    }

    public void close() {
        try {
           in.close();
           out.close();
           socket.close();
        }
        catch(Exception e) {
            e.printStackTrace();
        }
    }



    private void receiveEvent(ServerEvent event) {
        notify(event);
    }

    public void sendEvent(ClientEvent event) {
        try {
            out.writeObject(event);
            out.flush();
        } catch (IOException e) {
            notify(new Disconnect());
        }
    }






    private void connectionConfigParser() throws IOException {
        File xmlFile = new File("resources/connection_config.xml");

        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = null;
        try {
            builder = factory.newDocumentBuilder();
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        }
        Document doc = null;
        try {
            if (builder != null) {
                doc = builder.parse(xmlFile);
            }
        } catch (SAXException | IOException e) {
            e.printStackTrace();
        }

        String hostname = doc.getDocumentElement().getElementsByTagName("hostname").item(0).getTextContent();
        int port = Integer.parseInt(doc.getDocumentElement().getElementsByTagName("port").item(0).getTextContent());
        System.out.println("Config red: " + hostname + " " + port);

        socket = new Socket(hostname, port);
    }

}
