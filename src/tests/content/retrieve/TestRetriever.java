package tests.content.retrieve;

import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.net.ServerSocket;
import java.net.Socket;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.actor.Props;
import content.core.Content;
import content.retrieve.LocalRetrieveContentRequest;
import content.retrieve.PeerRetrieveContentRequest;
import content.retrieve.RetrievedContent;
import content.retrieve.Retriever;
import content.retrieve.TransferInfo;
import filemanagement.filewrapper.FileUnwrapper;
import peer.core.ActorNames;
import peer.core.PeerToPeerActorInit;
import peer.core.UniversalId;
import tests.core.ActorTestLogger;
import tests.core.DummyInit;

public class TestRetriever {    
    public static void main(String[] args) throws Exception {
        UniversalId peerOneId = new UniversalId("localhost:10001");
        UniversalId peerTwoId = new UniversalId("localhost:10002");
        
        ActorSystem actorSystem = ActorSystem.create("ContentSystem");
        
        final ActorRef dummyDatabaser = actorSystem.actorOf(Props.create(DummyDatabaser.class), ActorNames.DATABASER);
        PeerToPeerActorInit peerIdInit = new PeerToPeerActorInit(peerOneId, ActorNames.DATABASER);
        dummyDatabaser.tell(peerIdInit, null);
        
        final ActorRef dummySimilaritor = actorSystem.actorOf(Props.create(DummySimilaritor.class), ActorNames.SIMILARITOR);
        peerIdInit = new PeerToPeerActorInit(peerOneId, ActorNames.SIMILARITOR);
        dummySimilaritor.tell(peerIdInit, null);
        
        final ActorRef dummyOutComm = actorSystem.actorOf(Props.create(DummyOutboundCommunicator.class), ActorNames.OUTBOUND_COMM);
        peerIdInit = new PeerToPeerActorInit(peerOneId, ActorNames.OUTBOUND_COMM);
        dummyOutComm.tell(peerIdInit, null);
        
        final ActorRef dummyViewer = actorSystem.actorOf(Props.create(DummyViewer.class), ActorNames.VIEWER);
        peerIdInit = new PeerToPeerActorInit(peerOneId, ActorNames.VIEWER);
        dummyViewer.tell(peerIdInit, null);
        
        // Logger
        ActorTestLogger logger = new ActorTestLogger();
        DummyInit loggerInit = new DummyInit(logger);
        dummyDatabaser.tell(loggerInit, null);
        dummySimilaritor.tell(loggerInit, null);
        dummyOutComm.tell(loggerInit, null);
        dummyViewer.tell(loggerInit, null);
        
        logger.logMessage("Test Begins: ");
        
        final ActorRef retrieverToTest = actorSystem.actorOf(Props.create(Retriever.class), ActorNames.RETRIEVER);
        peerIdInit = new PeerToPeerActorInit(peerOneId, ActorNames.RETRIEVER);
        retrieverToTest.tell(peerIdInit, null);
        
        Content content = new Content("UniqueId", "Filename", "FileFormat", 10);
        
        logger.logMessage("Testing LocalRetrieveContentRequest: \n");
        LocalRetrieveContentRequest localRequest = new LocalRetrieveContentRequest(peerOneId, peerTwoId, content);
        retrieverToTest.tell(localRequest, null);
        
        Thread.sleep(1000);
        logger.logMessage("Testing PeerRetrieveContentRequest where Similaritor will be required because the file is not locally held: \n");
        PeerRetrieveContentRequest peerRequest = new PeerRetrieveContentRequest(peerTwoId, peerOneId, content);
        retrieverToTest.tell(peerRequest, null);
        
        Thread.sleep(2000);
        logger.logMessage("Testing PeerRetrieveContentRequest where the file is held locally and a Transferer transfer will be setup: \n");
        peerRequest = new PeerRetrieveContentRequest(peerTwoId, peerOneId, content);
        retrieverToTest.tell(peerRequest, null);
        
        Thread.sleep(3000);
        logger.logMessage("Receiving the byte transfer from the transferer");
        {
            Socket receiveSocket = new Socket("localhost", 10002);
            DataInputStream stream = new DataInputStream(receiveSocket.getInputStream());
            ByteArrayOutputStream bytes = new ByteArrayOutputStream();
            byte[] buffer = new byte[1024];
            int k = -1;
            while ((k = stream.read(buffer, 0, buffer.length)) > 0) {
                bytes.write(buffer, 0, k);
            }
            logger.logMessage("Test Bytes received: " + TestHeaderMediaFile.getContentFileBytesAsString(bytes.toByteArray()));
            logger.logMessage("Test Media String in ContentFile: " + new String(FileUnwrapper.extractFileArray(bytes.toByteArray())));
            logger.logMessage("");
            receiveSocket.close();
        }
        
        Thread.sleep(3000);
        logger.logMessage("Testing RetrievedContent where we will receive this content by transfer: \n");
        TransferInfo transferInfo = new TransferInfo(peerOneId, "10001");
        RetrievedContent retrievedContent = new RetrievedContent(peerOneId, peerTwoId, content, transferInfo);
        retrieverToTest.tell(retrievedContent, null);
        logger.logMessage("Setting up a transfer from an imaginary Transferer for the RetrievedContent Test\n");
        {
            ServerSocket serverSocket = new ServerSocket(10001);
            Socket sendSocket = serverSocket.accept();
            DataOutputStream stream = new DataOutputStream(sendSocket.getOutputStream());
            stream.write(TestHeaderMediaFile.getHeaderMediaFile());
            sendSocket.close();
            serverSocket.close();
        }
        
        Thread.sleep(2000);
        
        for (String message : logger) {
            System.out.println(message);
        }
    }
}
