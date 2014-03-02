package cleverbeta.server;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.util.Random;

import com.google.code.chatterbotapi.ChatterBot;
import com.google.code.chatterbotapi.ChatterBotFactory;
import com.google.code.chatterbotapi.ChatterBotSession;
import com.google.code.chatterbotapi.ChatterBotThought;
import com.google.code.chatterbotapi.ChatterBotType;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;
import com.sun.xml.internal.ws.util.ByteArrayBuffer;

public class Server {
	public static void main(String[] args) throws Exception {
		HttpServer server = HttpServer.create(new InetSocketAddress(80), 0);
		server.createContext("/index.html", new MyHandler());
		for (String s:(new File("img")).list()) {
			server.createContext("/"+s, new ImageHandler("img/"+s));
		}
		server.createContext("/logo.png", new ImageHandler("logo.png"));
		server.createContext("/logo2.png", new ImageHandler("logo2.png"));
		server.createContext("/logo3.png", new ImageHandler("logo3.png"));
		server.createContext("/logo4.png", new ImageHandler("logo4.png"));
		server.setExecutor(null);
		server.start();
	}

	public static class ImageHandler implements HttpHandler {

		private String file;
		
		public ImageHandler(String s) {
			file = s;
		}
		
		@Override
		public void handle(HttpExchange ex) throws IOException {
			FileInputStream fis = new FileInputStream(new File(file));
			ByteArrayBuffer bab = new ByteArrayBuffer();
			byte[] b = new byte[1024];
			while (fis.available() > 0) {
				fis.read(b);
				bab.write(b);
			}
			b = bab.toByteArray();
			ex.sendResponseHeaders(200, b.length);
			OutputStream os = ex.getResponseBody();
			os.write(b);
			os.close();
		}
		
	}
	
	public static class MyHandler implements HttpHandler {

		ChatterBot cb;
		ChatterBotSession session;
		ChatterBotThought thought = new ChatterBotThought();

		public MyHandler() throws Exception {
			ChatterBotFactory cbf = new ChatterBotFactory();
			cb = cbf.create(ChatterBotType.CLEVERBOT);
			session = cb.createSession();

		}

		@Override
		public void handle(HttpExchange ex) throws IOException {
			String response;
			if (ex.getRequestMethod().equals("POST")) {
				InputStream is = ex.getRequestBody();
				StringBuilder sb = new StringBuilder();
				for (int i=0; i<Integer.parseInt(ex.getRequestHeaders().get("Content-length").get(0)); i++) {
					sb.append((char)is.read());
				}
				try {
					response = getPage(handleContent(sb.toString()));
				} catch (Exception e) {
					response = e.getMessage();
					e.printStackTrace();
				}
			} else {
				response = getPage();
			}
			ex.sendResponseHeaders(200, response.length());
			OutputStream os = ex.getResponseBody();
			os.write(response.getBytes());
			os.close();
		}

		public String handleContent(String content) throws Exception {
			String query = content.substring(6);
			query = java.net.URLDecoder.decode(query, "UTF-8");
			return query;
		}

		public String getPage() {
			return "<html><head><title>CleverBeta</title>" +
					"<style>" +
					"#calculatecontain {"+
					"background-color: #FFF;"+
					"border: 1px solid #FDA566;"+
					"margin: 70px auto 23px;"+
					"position: relative;"+
					"border-radius: 10px;"+
					"box-shadow: 0px 0px 3px 1px #CCC;"+
					"width: 570px;"+
					"z-index: 200;"+
					"min-height: 79px;"+
					"}" +
					"#input {"+
					"background-color: #FFF;"+
					"margin: 0px auto;"+
					"padding: 0px;"+
					"width: 530px;"+
					"height: 29px;"+
					"position: absolute;"+
					"left: 21px;"+
					"top: 24px;"+
					"border: 1px solid #FA6800;"+
					"border-radius: 6px;"+
					"box-shadow: 0px 0px 0px 2px #FED36E, 0px 0px 0px 4px #FFC104;"+
					"z-index: 10;"+
					"}" +
					"#i {"+
					"border: 0px none;"+
					"vertical-align: middle;"+
					"width: 373px;"+
					"margin: 4px 0px 0px 6px;"+
					//"position: relative;"+
					"top: 0px;"+
					"font-family: Tahoma,sans-serif;"+
					"line-height: 16px;"+
					"outline: medium none;"+
					"font-size: 14px;" +
					"}"+
					"body {text-align:center}" +
					"#logo {"+
					"width: 351px;"+
					"margin-left: auto;"+
					"margin-right: auto;"+
					"background-position: left center;"+
					"}"+
					"</style></head><body>"+
					"<img src=\"logo4.png\" id=\"logo\" />" +
					"<div id=\"calculatecontain\">" +
					"<form action=\"index.html\" method=\"post\">" +
					"<fieldset id=\"input\">" +
					"<input type=\"text\" name=\"query\" id=\"i\"/><input type=\"submit\"/>" +
					"</fieldset></form></div></body></html>";
		}
		

		public String getPage(String query) throws Exception {
			String ret = "<html><head><title>CleverBeta</title>" +
					"<style>" +
					"#calculatecontain {"+
					"background-color: #FFF;"+
					"border: 1px solid #FDA566;"+
					"margin: 70px auto 23px;"+
					"position: relative;"+
					"border-radius: 10px;"+
					"box-shadow: 0px 0px 3px 1px #CCC;"+
					"width: 570px;"+
					"z-index: 200;"+
					"min-height: 79px;"+
					"}" +
					"#input {"+
					"background-color: #FFF;"+
					"margin: 0px auto;"+
					"padding: 0px;"+
					"width: 530px;"+
					"height: 29px;"+
					"position: absolute;"+
					"left: 21px;"+
					"top: 24px;"+
					"border: 1px solid #FA6800;"+
					"border-radius: 6px;"+
					"box-shadow: 0px 0px 0px 2px #FED36E, 0px 0px 0px 4px #FFC104;"+
					"z-index: 10;"+
					"}" +
					"#i {"+
					"border: 0px none;"+
					"vertical-align: middle;"+
					"width: 373px;"+
					"margin: 4px 0px 0px 6px;"+
					//"position: relative;"+
					"top: 0px;"+
					"font-family: Tahoma,sans-serif;"+
					"line-height: 16px;"+
					"outline: medium none;"+
					"font-size: 14px;" +
					"}"+
					"#answers {" +
					//"max-width:566px;" +
					"box-shadow: 0px 0px 15px #CCC inset, 0px 0px 1px #666 inset, 0px 0px 1px #CCC;" +
					"border-radius: 15px;" +
					//"margin: 0px;" +
					"margin-left: auto;"+
					"margin-right: auto;"+
					"padding: 12px 0px 1px;" +
					//"position: relative;" +
					"width: 566px;" +
					//"float: left;" +
					//"z-index: 202;" +
					//"text-align: left;" +
					"}" +
					"body {text-align:center}" +
					".pod {"+
					"text-align:left;"+
					"clear: both;"+
					"position: relative;"+
					"z-index: 2;"+
					"min-height: 11px;"+
					"background-image: none;"+
					"background-color: #FFF;"+
					"margin: 0px auto 15px;"+
					"width: 546px;"+
					"padding: 6px 0px 4px;"+
					"border-radius: 6px;"+
					"border: 1px solid #CCC;"+
					"font-family: Tahoma,sans-serif;"+
					"}"+
					".pod h2 {"+
					"text-align: left;"+
					"float: left;"+
					"padding: 0px 2px 0px 15px;"+
					"min-height: 17px;"+
					"position: relative;"+
					"clear: both;"+
					"overflow: visible;"+
					"z-index: 400;"+
					"font-family: Verdana,sans-serif;"+
					"font-weight: normal;"+
					"font-size: 10px;"+
					"line-height: 14px;"+
					"}" +
					".pod h4 {"+
					"text-align: left;"+
					//"float: left;"+
					"padding: 0px 2px 0px 30px;"+
					//"min-height: 17px;"+
					//"position: relative;"+
					//"clear: both;"+
					//"overflow: visible;"+
					"font-family: Tahoma,sans-serif;"+
					"font-weight: normal;"+
					"font-size: 14px;"+
					"line-height: 14px;"+
					"}" +
					"#logo {"+
					"width: 351px;"+
					"margin-left: auto;"+
					"margin-right: auto;"+
					"background-position: left center;"+
					"}"+
					"</style></head><body>"+
					"<img src=\"logo4.png\" id=\"logo\" />" +
					"<div id=\"calculatecontain\">" +
					"<form action=\"index.html\" method=\"post\">" +
					"<fieldset id=\"input\">" +
					"<input type=\"text\" name=\"query\" id=\"i\"/><input type=\"submit\"/>" +
					"</fieldset></form></div>";
			ret += generateAnswers(query);
			ret += "</body></html>";
			return ret;
		}
		public String askCleverbot(String question) throws Exception {
			thought.setText(question);
			thought = session.think(thought);
			return thought.getText();
		}
		public String generateAnswers(String query) throws Exception {
			System.out.println("genning");
			String content = askCleverbot(query);
			Random r = new Random(content.hashCode());
			String ret = "";
			ret += "<div id=\"answers\">";
			ret += generateTextAnswer("Input interpretation", query);
			ret += generateTextAnswer("Result", content);
			boolean bio = false;
			for (int i=0; i<r.nextInt(3)+2;i++) {
				int rr = r.nextInt(5);
				if (rr == 0) {
					if (bio) {
						i--;
					} else if (r.nextBoolean()) {
						ret += generateBioAnswer(query);
						bio = true;
					}
				} else if (rr == 1) {
					ret += generateListAnswers(query, r);
				} else if (rr == 2) {
					ret += generateImageAnswer(r);
				} else {
					ret += generateTextAnswer(query, r);
				}
			}
			System.out.println("genned");
			ret += "</div>";
			return ret;
		}
		private static final String[] textHeads = new String[] {
			"Toxicity Properties", "Planet of origin", "Median mass", "Author", "Periodic number", "First known use in English", "Rhymes with", "Scrabble score"
		};
		public String generateTextAnswer(String query, Random r) throws Exception {
			return generateTextAnswer(textHeads[r.nextInt(textHeads.length)],askCleverbot(query));
		}
		public String generateTextAnswer(String head, String body) {
			return "<div class=\"pod\"><h2>"+head+":</h2><br><h4>"+body+"<h4></div>";
		}
		private static final String[] listHeads = new String[] {
			"Notable facts", "Famous quotes", "In-laws", "Homonyms", "Complimentary flavours"
		};
		public String generateListAnswers(String topic, Random r) throws Exception {
			String ret = "<div class=\"pod\"><h2>"+listHeads[r.nextInt(listHeads.length)]+":</h2><br><h4>";
			for (int i=0; i<r.nextInt(3)+2; i++) {
				ret += askCleverbot("fact about "+topic)+"<hr>";
			}
			ret += askCleverbot("fact about "+topic);
			return ret + "</h4></div>";
		}
		public String generateBioAnswer(String topic) throws Exception {
			return "<div class=\"pod\"><h2>Basic Information:</h2><br><table><h4>" +
					"<tr><td>full name</td><td>"+askCleverbot("name of "+topic)+"</td></tr>" +
					"<tr><td>date of birth</td><td>"+askCleverbot("date of birth of "+topic)+"</td></tr>" +
					"<tr><td>place of birth</td><td>"+askCleverbot("place of birth of "+topic)+"</td></tr>" +
					"<tr><td>date of death</td><td>"+askCleverbot("date of death of "+topic)+"</td></tr>" +
					"<tr><td>place of death</td><td>"+askCleverbot("place of death of "+topic)+"</td></tr>" +
					"</h4></table></div>";
		}
		
		private static final String[] imageHeads = new String[] {
			"Portrait", "Graph", "Chemical structure", "Artist's rendering", "Reticulated spline", "NFPA label"
		};
		public String generateImageAnswer(Random r) {
			return "<div class=\"pod\"><h2>"+imageHeads[r.nextInt(imageHeads.length)]+":</h2><br><img src=\""+pickRandomImage(r)+"\" /></div>";
		}
		public static String pickRandomImage(Random r) {
			String[] files = (new File("img")).list();
			return files[r.nextInt(files.length)];
		}
	}
}
