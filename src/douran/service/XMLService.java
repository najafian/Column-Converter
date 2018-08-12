package douran.service;

import javax.xml.bind.*;
import javax.xml.namespace.QName;
import javax.xml.transform.stream.StreamSource;
import java.io.*;

public class XMLService {
    public static <T> String getValue(Class<T> clazz, T instance) {
        StringWriter stringWriter = new StringWriter();
        try {
            JAXBContext context = JAXBContext.newInstance(clazz);
            Marshaller mar = context.createMarshaller();
            mar.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
            JAXBElement<T> jaxbElement = new JAXBElement<T>(new QName(clazz.getCanonicalName()), clazz, instance);
            mar.marshal(jaxbElement, stringWriter);
        } catch (JAXBException e) {
//			e.printStackTrace();
        }

        return stringWriter.toString();
    }

    public static <T> T getValue(Class<T> clazz, InputStream instance) {
        return getValue(clazz, getStringFromInputStream(instance));
    }

    public static <T> T getValue(Class<T> clazz, String instance) {
        if (instance != null) {
            StringReader reader = new StringReader(instance);
            try {
                JAXBContext context = JAXBContext.newInstance(clazz);
                Unmarshaller unMa = context.createUnmarshaller();
                JAXBElement<T> jaxbElement = unMa.unmarshal(new StreamSource(reader), clazz);
                return jaxbElement.getValue();
            } catch (JAXBException e) {
//				e.printStackTrace();
            }
        } else {
            return null;
        }
        return null;
    }

    public static String getStringFromInputStream(InputStream is) {
        BufferedReader br = null;
        StringBuilder sb = new StringBuilder();

        String line;
        try {
            br = new BufferedReader(new InputStreamReader(is));
            while ((line = br.readLine()) != null) {
                sb.append(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (br != null) {
                try {
                    br.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return sb.toString();
    }
}
