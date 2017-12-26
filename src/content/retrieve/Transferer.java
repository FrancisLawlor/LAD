package content.retrieve;

import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

import akka.actor.ActorRef;
import akka.actor.ActorSelection;
import akka.actor.PoisonPill;
import content.core.Content;
import content.core.ContentFile;
import peer.core.ActorPaths;
import peer.core.PeerToPeerActor;
import peer.core.PeerToPeerActorInit;
import peer.core.xcept.UnknownMessageException;

/**
 * Temporary child actor Retriever delegates to for web socket file transfer
 *
 */
public class Transferer extends PeerToPeerActor {
    private TransferInfo transferInfo;
    
    @Override
    public void onReceive(Object message) throws Throwable {
        if (message instanceof PeerToPeerActorInit) {
            PeerToPeerActorInit init  = (PeerToPeerActorInit) message;
            this.initialisePeerToPeerActor(init);
        }
        else if (message instanceof TransfererInit) {
            TransfererInit init = (TransfererInit) message;
            this.transferInfo = init.getTransferInfo();
        }
        else if (message instanceof TransferSendStart) {
            TransferSendStart sendStart = (TransferSendStart) message;
            this.processTransferSendStart(sendStart);
        }
        else if (message instanceof TransferReceiveStart) {
            TransferReceiveStart receiveStart = (TransferReceiveStart) message;
            this.processTransferReceiveStart(receiveStart);
        }
        else {
            throw new UnknownMessageException();
        }
    }
    
    /**
     * 
     * @param sendStart
     */
    protected void processTransferSendStart(TransferSendStart sendStart) throws IOException {
        ContentFile contentFile = sendStart.getContentFile();
        int port = this.transferInfo.getTransferPort();
        ServerSocket serverSocket = new ServerSocket(port);
        Socket sendSocket = serverSocket.accept();
        DataOutputStream stream = new DataOutputStream(sendSocket.getOutputStream());
        stream.write(contentFile.getBytes());
        sendSocket.close();
        serverSocket.close();
        getSelf().tell(PoisonPill.getInstance(), ActorRef.noSender());
    }
    
    /**
     * Receives a stream of bytes from the host and port in the transfer info
     * Sends these bytes in a ContentFile wrapped in a RetrievedContent message back to Retriever
     * @param receiveStart
     */
    protected void processTransferReceiveStart(TransferReceiveStart receiveStart) throws IOException {
        String host = this.transferInfo.getTransferHostIp();
        int port = this.transferInfo.getTransferPort();
        Socket receiveSocket = new Socket(host, port);
        DataInputStream stream = new DataInputStream(receiveSocket.getInputStream());
        
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        byte[] buffer = new byte[1024];
        int k = -1;
        while ((k = stream.read(buffer, 0, buffer.length)) > 0) {
            bytes.write(buffer, 0, k);
        }
        RetrievedContent retrievedContent = receiveStart.getRetrievedContent();
        Content content = retrievedContent.getContent();
        ContentFile contentFile = new ContentFile(content, bytes.toByteArray());
        RetrievedContentFile retrievedContentFile = new RetrievedContentFile(retrievedContent, contentFile);
        ActorSelection retriever = getContext().actorSelection(ActorPaths.getPathToRetriever());
        retriever.tell(retrievedContentFile, getSelf());
        
        receiveSocket.close();
        getSelf().tell(PoisonPill.getInstance(), ActorRef.noSender());
    }
}
