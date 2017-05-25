package sashas;

/**
 * Created by Екатерина on 12.05.2017.
 */


import sashas.xmlProtocol.AuthorizationForServer;

import org.w3c.dom.Document;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.*;

class XMLServerEditor {
    public String getLogin(String message) throws ParserConfigurationException, IOException, SAXException {
        AuthorizationForServer auth = new AuthorizationForServer();
        return auth.getLogin(message);
    }

    public String getPassword(String message) throws ParserConfigurationException, IOException, SAXException {
        AuthorizationForServer auth = new AuthorizationForServer();
        return auth.getPassword(message);
    }

    public String makeAuthorisationMessage(boolean loginChecked) throws ParserConfigurationException, TransformerException {
        AuthorizationForServer auth = new AuthorizationForServer();
        Document document =auth.makeAuthorisationMessage(loginChecked);

        TransformerFactory tf = TransformerFactory.newInstance();
        Transformer transformer = tf.newTransformer();
        StringWriter outWriter = new StringWriter();
        transformer.transform(new DOMSource(document), new StreamResult(outWriter));
        StringBuffer sb = outWriter.getBuffer();
        return sb.toString();
    }
}