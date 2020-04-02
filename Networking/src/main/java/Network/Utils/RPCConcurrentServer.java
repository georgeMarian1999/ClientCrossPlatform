package Network.Utils;

import Network.RPCProtocol.ClientRPCWorker;
import Services.IServices;
import Services.ServerException;

import java.net.Socket;

public class RPCConcurrentServer extends AbstractConcurrentServer {

    private IServices server;

    public RPCConcurrentServer(int port ,IServices server1){
        super(port);
        this.server=server1;
        System.out.println("Building the concurrent server");
    }

    @Override
    protected Thread createWorker(Socket client) {
        ClientRPCWorker worker=new ClientRPCWorker(server,client);
        Thread tw=new Thread(worker);
        return tw;
    }

    @Override
    public void stop() throws ServerException {
        System.out.println("Stopping server....");
        super.stop();
    }
}
