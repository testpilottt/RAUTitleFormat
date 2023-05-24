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
import java.util.Objects;
import static com.intellij.openapi.util.text.StringUtil.containsIgnoreCase;
import static com.intellij.openapi.util.text.StringUtil.indexOfIgnoreCase;

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
        String pomVersion = null;
        StringBuilder rauTitle = new StringBuilder()
                .append("*[");

        try {
            VirtualFile tempVirtualFile = virtualFile;
            VirtualFile previousVirtualFile = virtualFile;

            while (Objects.nonNull(tempVirtualFile) && !tempVirtualFile.getName().equals(project.getName())) {
                previousVirtualFile = tempVirtualFile;
                tempVirtualFile = tempVirtualFile.getParent();
            }

            pomVersion = readPomVersion(previousVirtualFile.getPath());

            boolean isNotFushionProject = false;

            if (Objects.nonNull(pomVersion) && !pomVersion.matches(".*\\d.*")) {
                pomVersion = readPomVersion(project.getBasePath());
                isNotFushionProject = true;
            }

            if (!isNotFushionProject) {
                rauTitle.append(previousVirtualFile.getName())
                        .append(" ^ ");
            }

            rauTitle.append(pomVersion)
                    .append("]* - ")
                    .append(super.getFileTitle(project, virtualFile));


        } catch (Exception ignored) {

        }

        title = Objects.isNull(pomVersion) ? title.replace("{DEFAULT}", super.getFileTitle(project, virtualFile)) : title.replace("{DEFAULT}", rauTitle.toString());

        return title;
    }

    private String readPomVersion(String path) {
        try {
            String pomPath = path + "/pom.xml";
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
                    return eElement.getElementsByTagName("version").item(0).getTextContent();
                }
            }
        } catch (Exception ignored) {

        }
        return null;
    }

    private String determineMavenVersion(VirtualFile pomFile) {
        String mavenVersion = null;
        try {
            String pomAsString = new String(pomFile.contentsToByteArray());
            if (containsIgnoreCase(pomAsString, "<parent>") && containsIgnoreCase(pomAsString, "</parent>")) {
                pomAsString = pomAsString.substring(0, indexOfIgnoreCase(pomAsString, "<parent>", 0)) + pomAsString.substring(indexOfIgnoreCase(pomAsString, "</parent>", 0));
            }
            if (containsIgnoreCase(pomAsString, "<version>") && containsIgnoreCase(pomAsString, "</version>")) {
                mavenVersion = pomAsString.substring(indexOfIgnoreCase(pomAsString, "<version>", 0) + "<version>".length(), indexOfIgnoreCase(pomAsString, "</version>", 0)).trim();
            }
            if ("".equals(mavenVersion)) {
                mavenVersion = null;
            }
        } catch (Exception e) {
            // ignore...
        }
        return mavenVersion;
    }

}
