package com.jadic.ws.czsmk;

import java.net.MalformedURLException;
import java.net.URL;
import javax.xml.namespace.QName;
import javax.xml.ws.WebEndpoint;
import javax.xml.ws.WebServiceClient;
import javax.xml.ws.WebServiceFeature;
import javax.xml.ws.Service;

/**
 * gSOAP 2.8.0 generated service definition
 *
 * This class was generated by Apache CXF 3.0.0
 * 2014-07-21T10:09:30.594+08:00
 * Generated source version: 3.0.0
 * 
 */
@WebServiceClient(name = "CenterProcess", 
                  wsdlLocation = "file:/e:/CenterProcess.wsdl",
                  targetNamespace = "http://10.0.4.116:9900/CenterProcess.wsdl") 
public class CenterProcess extends Service {

    public final static URL WSDL_LOCATION;

    public final static QName SERVICE = new QName("http://10.0.4.116:9900/CenterProcess.wsdl", "CenterProcess");
    public final static QName CenterProcess = new QName("http://10.0.4.116:9900/CenterProcess.wsdl", "CenterProcess");
    static {
        URL url = null;
        try {
            url = new URL("file:/e:/CenterProcess.wsdl");
        } catch (MalformedURLException e) {
            java.util.logging.Logger.getLogger(CenterProcess.class.getName())
                .log(java.util.logging.Level.INFO, 
                     "Can not initialize the default wsdl from {0}", "file:/e:/CenterProcess.wsdl");
        }
        WSDL_LOCATION = url;
    }

    public CenterProcess(URL wsdlLocation) {
        super(wsdlLocation, SERVICE);
    }

    public CenterProcess(URL wsdlLocation, QName serviceName) {
        super(wsdlLocation, serviceName);
    }

    public CenterProcess() {
        super(WSDL_LOCATION, SERVICE);
    }
    

    /**
     *
     * @return
     *     returns CenterProcessPortType
     */
    @WebEndpoint(name = "CenterProcess")
    public CenterProcessPortType getCenterProcess() {
        return super.getPort(CenterProcess, CenterProcessPortType.class);
    }

    /**
     * 
     * @param features
     *     A list of {@link javax.xml.ws.WebServiceFeature} to configure on the proxy.  Supported features not in the <code>features</code> parameter will have their default values.
     * @return
     *     returns CenterProcessPortType
     */
    @WebEndpoint(name = "CenterProcess")
    public CenterProcessPortType getCenterProcess(WebServiceFeature... features) {
        return super.getPort(CenterProcess, CenterProcessPortType.class, features);
    }

}