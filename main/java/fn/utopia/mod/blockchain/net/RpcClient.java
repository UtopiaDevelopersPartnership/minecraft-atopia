package fn.utopia.mod.blockchain.net;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.http.DefaultHttpHeaders;
import io.netty.handler.codec.http.HttpClientCodec;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import io.netty.handler.codec.http.websocketx.WebSocketClientHandshakerFactory;
import io.netty.handler.codec.http.websocketx.WebSocketFrame;
import io.netty.handler.codec.http.websocketx.WebSocketVersion;

import java.net.URI;
import java.util.List;
import java.util.Map;

import com.thetransactioncompany.jsonrpc2.JSONRPC2Error;
import com.thetransactioncompany.jsonrpc2.JSONRPC2ParseException;
import com.thetransactioncompany.jsonrpc2.JSONRPC2Request;
import com.thetransactioncompany.jsonrpc2.JSONRPC2Response;

import fn.utopia.mod.blockchain.BlockchainManager;
import fn.utopia.mod.blockchain.actions.impl.BroadcastPlayerEventAction;
import fn.utopia.mod.blockchain.data.BlockchainData.PlayerEventData;


/**
 * Does json-rpc over a websocket connection. Netty client is already
 * MC dependency so using that.
 * 
 * @author androlo
 *
 */
public class RpcClient {
	
	protected static final int PARSE_ERROR = -32700;
	protected static final int INVALID_REQUEST = -32600;
	protected static final int METHOD_NOT_FOUND = -32601;
	protected static final int INVALID_PARAMS = -32602;
	protected static final int INTERNAL_ERROR = -32603;
	
	protected int currentId = 1;
	
	protected URI uri;
	protected BlockchainNetwork bcn;
	
	protected Channel ch;
	protected EventLoopGroup group;
	
	public RpcClient(BlockchainNetwork bcn, URI uri) {
		this.uri = uri;
		this.bcn = bcn;
	}
	
	public void start() throws InterruptedException {
		
        String scheme = uri.getScheme() == null ? "http" : uri.getScheme();
        final String host = uri.getHost() == null? "127.0.0.1" : uri.getHost();
        final int port;
        if (uri.getPort() == -1) {
            if ("http".equalsIgnoreCase(scheme)) {
                port = 80;
            } else if ("https".equalsIgnoreCase(scheme)) {
                port = 443;
            } else {
                port = -1;
            }
        } else {
            port = uri.getPort();
        }
        
        if (!"ws".equalsIgnoreCase(scheme)) {
            System.err.println("Only WS is supported.");
            return;
        }
        
        group = new NioEventLoopGroup();
        
        final WebSocketClientHandler handler =
                new WebSocketClientHandler(this,
                        WebSocketClientHandshakerFactory.newHandshaker(
                                uri, WebSocketVersion.V13, null, false, new DefaultHttpHeaders()));
        
        Bootstrap b = new Bootstrap();
        b.group(group)
         .channel(NioSocketChannel.class)
         .handler(new ChannelInitializer<SocketChannel>() {
             @Override
             protected void initChannel(SocketChannel ch) {
                 ChannelPipeline p = ch.pipeline();
                 p.addLast(
                         new HttpClientCodec(),
                         new HttpObjectAggregator(8192),
                         handler);
             }
         });

        ch = b.connect(uri.getHost(), port).sync().channel();
        handler.handshakeFuture().sync();
	}
	
	public void shutdown(){
		group.shutdownGracefully();
	}

	public void sendJson(String method, List params) {
		JSONRPC2Request reqOut = new JSONRPC2Request(method, params, _currentId());
		String jsonString = reqOut.toString();
		System.out.println("Request: " + jsonString);
		WebSocketFrame frame = new TextWebSocketFrame(jsonString);
        ch.writeAndFlush(frame);
	}

	protected int _currentId() {
		return currentId++;
	}

	protected Request _getRequest(int id, String method, List params) {
		Request req = new Request();
		req.id = id;
		req.jsonrpc = "2.0";
		req.method = method;
		req.params = params;
		return req;
	}

	public static class Request {
		public int id;
		public String jsonrpc;
		public String method;
		public List params;
	}
	
	public static class Response {
		public int id;
		public String jsonrpc;
		public String result;
		public String error;
	}

	protected void _handleIncoming(String message) {
		String msg = message.substring(1, message.length() - 1);
		String jsonString = msg.replaceAll("\\\\","");
		// Parse response string
		JSONRPC2Response respIn = null;
		System.out.println("Incoming: " + jsonString);
		try {
			respIn = JSONRPC2Response.parse(jsonString);
		} catch (JSONRPC2ParseException e) {
			System.out.println(e.getMessage());
			// Handle exception...
		}
		// Check for success or error
		if (respIn.indicatesSuccess()) {
			System.out.println("The request succeeded :");
			System.out.println("\tresult : " + respIn.getResult());
			System.out.println("\tid     : " + respIn.getID());
			Map<String,Object> result = (Map<String,Object>)respIn.getResult();
			bcn.handleIncoming((String)respIn.getID(), result);
		} else {
			System.out.println("The request failed :");
			JSONRPC2Error err = respIn.getError();
			System.out.println("\terror.code    : " + err.getCode());
			System.out.println("\terror.message : " + err.getMessage());
			System.out.println("\terror.data    : " + err.getData());
		}
		
	}
	
}