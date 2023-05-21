package com.example.fushionplugin;

import com.intellij.openapi.wm.impl.PlatformFrameTitleBuilder;
import org.apache.commons.lang3.StringUtils;
import org.jetbrains.annotations.NotNull;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.vfs.VirtualFile;
import javax.xml.parsers.DocumentBuilder;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.IOException;

public class SimpleFrameTitleBuilder extends PlatformFrameTitleBuilder {

    TitleSettingState state = TitleSettingState.getInstance();

    @Override
    public String getProjectTitle(@NotNull Project project) {
        String title = state.projectFormat;
        if (title.contains("{DEFAULT}")) {
            title = title.replace("{DEFAULT}", super.getProjectTitle(project));
        }
        if (title.contains("{SIMPLE}")) {
            title = title.replace("{SIMPLE}", project.getName());
        }
        return title;
    }

    @Override
    public String getFileTitle(@NotNull Project project, @NotNull VirtualFile virtualFile) {
        String title = state.fileFormat;

        VirtualFile tempVirtualFile = virtualFile;
        VirtualFile previousVirtualFile = virtualFile;

        while(!tempVirtualFile.getName().equals(project.getName())) {
            previousVirtualFile = tempVirtualFile;
            tempVirtualFile = tempVirtualFile.getParent();
        }

        String pomVersion = null;

        try {
            String pomPath = previousVirtualFile.getPath() + "/pom.xml";
            File file = new File(pomPath);
            DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
            DocumentBuilder db = dbf.newDocumentBuilder();
            Document doc = db.parse(file);

            Element element = (Element) doc.getElementsByTagName("parent").item(0);

            Node projectNode = element.getParentNode();

            projectNode.removeChild(element);

            doc.getDocumentElement().normalize();

            NodeList nodeList = doc.getElementsByTagName("project");

            for (int itr = 0; itr < nodeList.getLength(); itr++) {
                Node node = nodeList.item(itr);
                if (node.getNodeType() == Node.ELEMENT_NODE) {
                    Element eElement = (Element) node;
                    pomVersion = eElement.getElementsByTagName("version").item(0).getTextContent();
                }
            }
        } catch (Exception ignored) {

        }

        StringBuilder rauTitle = new StringBuilder()
                .append("*[")
                .append(previousVirtualFile.getName());

        if (pomVersion != null) {
            rauTitle.append(" ^ ")
                    .append(pomVersion);
        }
        rauTitle.append("]* - ")
                .append(super.getFileTitle(project, virtualFile));

        title = title.replace("{DEFAULT}", rauTitle.toString());

        return title;
    }
}
