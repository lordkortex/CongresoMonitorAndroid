package monitor.congreso.com.hn.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

/**
 * Created by CortesMoncada on 08/02/2015.
 */
public class XpathUtil {

    public static  List<String> getListObjects(final String xml, final int position, final String expresion,final Boolean duplicatedValues, final Context context) {
        SharedPreferences GetPrefs = PreferenceManager.getDefaultSharedPreferences(context);
        String xmlSesion = "";
        if (GetPrefs.contains(xml)) {
            xmlSesion = GetPrefs.getString(xml, xml);
        }
        NodeList nodes = XpathUtil.getXptathResult(xmlSesion, "/NewDataSet/" + xml + expresion);

        List<String> planlistValores = new ArrayList<String>();
        Set<String> uniqueValues = new HashSet<String>();

        planlistValores.add("");
        for (int i = 0; i < nodes.getLength(); i++) {
            Node node = nodes.item(i);
            NodeList venueChildNodes = node.getChildNodes();
            NodeList nodeItem = venueChildNodes.item(position).getChildNodes();
            if(nodeItem.getLength()>0){
                String valor = nodeItem.item(0).getNodeValue();
                planlistValores.add(valor);
                uniqueValues.add(valor);
            }
        }

        if (!duplicatedValues){
            planlistValores.clear();
            planlistValores.add("");
            planlistValores.addAll(uniqueValues);
        }

        return planlistValores;
    }



    public static  NodeList getXptathResult(final String xml, final String expression  ) {
        NodeList nodes = null;
        try {
            DocumentBuilderFactory builderFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = null;
            builder = builderFactory.newDocumentBuilder();
            Document xmlDocument = builder.parse(new ByteArrayInputStream(xml.getBytes()));
            XPath xPath = XPathFactory.newInstance().newXPath();
            Object result = xPath.compile(expression).evaluate(xmlDocument, XPathConstants.NODESET);
            nodes = (NodeList) result;
        } catch (SAXException e) {
        } catch (IOException e) {
        } catch (ParserConfigurationException e) {
        } catch (XPathExpressionException e) {
        }

        return nodes;

    }

    public static String  buildXmlProyectos(final String colid,final String  colSesion,
                                            final String colNombre,final String colTipoActo,
                                            final String colDebate,final String colStatus,
                                            final String colComision,final String colPartido,final String  colProponente,
                                            final String colFecha,final String colDescripcion){

        StringBuilder Cadena = new StringBuilder();
        Cadena.append("<proyecto>");
        Cadena.append("<id>" + colid + "</id>");
        Cadena.append("<sesion>" + colSesion + "</sesion>");
        Cadena.append("<nombre>" +colNombre + " </nombre>");
        Cadena.append("<tipoacto>" +colTipoActo + " </tipoacto>");
        Cadena.append("<debate>" + colDebate + " </debate>");
        Cadena.append("<status>" + colStatus + " </status>");
        Cadena.append("<comision>" + colComision + " </comision>");
        Cadena.append("<partido>" + colPartido + " </partido>");
        Cadena.append("<proponente>" + colProponente + " </proponente>");
        Cadena.append("<fecha>" + colFecha + " </fecha>");
        Cadena.append("<descripcion>" + colDescripcion + " </descripcion>");
        Cadena.append("</proyecto>");

        return Cadena.toString();
    }

    public static String  buildXmlIntervenciones(final String colid,final String colNombre,final String colDebate,final String colDiputado,final String colIntervencion,final String colFecha){

        StringBuilder Cadena = new StringBuilder();
        Cadena.append("<intervencion>");
        Cadena.append("<id>" + colid + "</id>");
        Cadena.append("<nombre>" +colNombre + " </nombre>");
        Cadena.append("<debate>" +colDebate + " </debate>");
        Cadena.append("<diputado>" + colDiputado + " </diputado>");
        Cadena.append("<textointervencion>" + colIntervencion + " </textointervencion>");
        Cadena.append("<fecha>" + colFecha + " </fecha>");
        Cadena.append("</intervencion>");

        return Cadena.toString();
    }

}
