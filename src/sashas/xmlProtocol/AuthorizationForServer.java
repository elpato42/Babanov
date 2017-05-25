package sashas.xmlProtocol;

/**
 * Created by Екатерина on 12.05.2017.
 */


import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;
import java.io.IOException;
import java.io.StringReader;

public class AuthorizationForServer {
    public String getLogin(String message) throws ParserConfigurationException, IOException, SAXException {
        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = dbf.newDocumentBuilder();
        Document document = builder.parse(new InputSource(new StringReader(message)));

        Element root = document.getDocumentElement();
        return root.getAttribute("password");
    }

    public String getPassword(String message) throws ParserConfigurationException, IOException, SAXException {
        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = dbf.newDocumentBuilder();
        Document document = builder.parse(new InputSource(new StringReader(message)));

        Element root = document.getDocumentElement();
        return root.getAttribute("login");
    }

    public Document makeAuthorisationMessage(boolean loginChecked) throws ParserConfigurationException, TransformerException {
        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = dbf.newDocumentBuilder();
        Document document = builder.newDocument();

        Element root = document.createElement("authorisation");
        document.appendChild(root);

        Attr statusAttr = document.createAttribute("status");
        if (loginChecked)
            statusAttr.setValue("accepted");
        else
            statusAttr.setValue("rejected");
        root.setAttributeNode(statusAttr);

        return document;
    }
}