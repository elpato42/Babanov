package sashas;

/**
 * Created by Екатерина on 12.05.2017.
 */


import sashas.xmlProtocol.AuthorizationForClient;
import sashas.xmlProtocol.WorkingWithXMLChatMessage;
import org.w3c.dom.*;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.*;

class XMLClientEditor {
    public String makeClientMessage(String login, String message) throws ParserConfigurationException, TransformerException, IOException {
        WorkingWithXMLChatMessage makeMessage = new WorkingWithXMLChatMessage();
        Document document = makeMessage.makeChatMessage(login, message);

        TransformerFactory tf = TransformerFactory.newInstance();
        Transformer transformer = tf.newTransformer();
        StringWriter outWriter = new StringWriter();
        transformer.transform(new DOMSource(document), new StreamResult(outWriter));
        StringBuffer sb = outWriter.getBuffer();
        return sb.toString();
    }

    public String makeClientAuthorisation(String login, String password) throws ParserConfigurationException, TransformerException {
        AuthorizationForClient auth = new AuthorizationForClient();
        Document document = auth.makeClientAuthorisation(login, password);

        TransformerFactory tf = TransformerFactory.newInstance();
        Transformer transformer = tf.newTransformer();
        StringWriter outWriter = new StringWriter();
        transformer.transform(new DOMSource(document), new StreamResult(outWriter));
        StringBuffer sb = outWriter.getBuffer();
        return sb.toString();
    }

    public boolean isAuthorised(String message) throws ParserConfigurationException, IOException, SAXException {
        AuthorizationForClient auth = new AuthorizationForClient();
        return auth.isAuthorised(message);
    }

    public String getMessage(String message) throws ParserConfigurationException, IOException, SAXException {
        WorkingWithXMLChatMessage makeMessage = new WorkingWithXMLChatMessage();
        return makeMessage.getAuthor(message) + " : " + makeMessage.getMessage(message);
    }
}