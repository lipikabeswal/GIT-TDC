package com.ctb.tdc.web.listener;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import com.ctb.tdc.web.utils.ServletUtils;


/**
 * Simple listener to initialize common properties used by all servlets.
 * 
 * @author Tai_Truong
 *
 * Start up listener for local servlets
 */
public class StartupListener implements ServletContextListener {
    // -- members --
    private ServletContext ctx = null;

    /**
     * Default constructor.
     */
    public StartupListener()
    {
        super();
        ctx = null;
    }
    
    /**
     * <code>ServletContextListener</code> interface required method.
     *
     * @param event The servlet context event
     */
    public void contextInitialized( ServletContextEvent event )
    {
        try {
            readServletSettings( event );
        }
        catch( Exception e ) {
            e.printStackTrace();
        }
    }

    /**
     * <code>ServletContextListener</code> interface required method.
     *
     * @param event The servlet context event
     */
    public void contextDestroyed( ServletContextEvent event )
    {
        ctx = null;
    }


    protected void readServletSettings( ServletContextEvent event ) throws Exception
    {
        // set the context
        ctx = event.getServletContext();
        ServletUtils.readServletSettings();
    }
    
}
