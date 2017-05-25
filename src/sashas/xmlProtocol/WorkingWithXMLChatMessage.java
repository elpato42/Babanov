package sashas.xmlProtocol;

/**
 * Created by Екатерина on 12.05.2017.
 */


import org.w3c.dom.*;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;
import java.io.*;
import java.util.Base64;

public class WorkingWithXMLChatMessage {
    public Document makeChatMessage(String login, String message) throws ParserConfigurationException,
            TransformerException, IOException {
        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = dbf.newDocumentBuilder();
        Document document = builder.newDocument();

        document.setXmlVersion("1.0");
        document.setXmlStandalone(false);
        Element root = document.createElement("message");
        document.appendChild(root);

        Attr author = document.createAttribute("author");
        author.setValue(login);
        root.setAttributeNode(author);

        if(message.startsWith("/sendfile")) {
            String[] messageParts = message.split(" ");
            File f = new File(messageParts[1]);
            FileInputStream fileInput = new FileInputStream(f);
            byte[] fileData = new byte[(int) f.length()];
            fileInput.read(fileData);

            Attr type = document.createAttribute("type");
            type.setValue("file");
            root.setAttributeNode(type);
            Element file = document.createElement("file");
            Attr name = document.createAttribute("name");
            name.setValue(messageParts[1]);
            file.setAttributeNode(name);

            CDATASection cdata = document.createCDATASection(Base64.getEncoder().encodeToString(fileData));
            file.appendChild(cdata);

            root.appendChild(file);
        } else {
            Text messageText = document.createTextNode(message);
            root.appendChild(messageText);
        }

        return document;
    }

    public String getAuthor(String message) throws ParserConfigurationException, IOException, SAXException {
        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = dbf.newDocumentBuilder();
        Document document = builder.parse(new InputSource(new StringReader(message)));
        Element root = document.getDocumentElement();

        return root.getAttribute("author");
    }

    public String getMessage(String message) throws ParserConfigurationException, IOException, SAXException {
        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = dbf.newDocumentBuilder();
        Document document = builder.parse(new InputSource(new StringReader(message)));
        Element root = document.getDocumentElement();

        if (root.hasAttribute("type") && root.getAttribute("type").equals("file")) {
            Node file = root.getFirstChild();
            String path = file.getAttributes().getNamedItem("name").getNodeValue();
            CDATASection cdata = (CDATASection) file.getFirstChild();

            byte[] fileData = Base64.getDecoder().decode(cdata.getData());

            FileOutputStream out = new FileOutputStream(path);
            out.write(fileData);
            out.flush();
            out.close();
            return "отправил файл";
        } else {
            return root.getFirstChild().getTextContent();
        }
    }
}