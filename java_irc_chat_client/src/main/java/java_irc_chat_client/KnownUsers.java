package java_irc_chat_client;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.File;
import java.util.HashSet;
import java.util.Set;

public class KnownUsers {

    private static final String FILE_PATH = System.getProperty("user.home") + "/.jirchat/usuarios_conocidos.xml";

    public static Set<String> loadKnownNicks() {
        Set<String> knownNicks = new HashSet<>();
        try {
            File file = new File(FILE_PATH);
            if (!file.exists()) return knownNicks;

            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document doc = builder.parse(file);

            NodeList nodeList = doc.getElementsByTagName("usuario");
            for (int i = 0; i < nodeList.getLength(); i++) {
                Element usuarioElem = (Element) nodeList.item(i);
                String nick = usuarioElem.getAttribute("nick").trim(); // ✅ atributo, no contenido
                if (!nick.isEmpty()) knownNicks.add(nick.toLowerCase());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return knownNicks;
    }


    // limpiar prefijos como @, +, %, etc.
    public static String cleanNick(String nick) {
        if (nick == null) return "";
        return nick.replaceAll("^[+%@~&*!]+", "").trim();
    }
}
