// Decompiled by DJ v3.6.6.79 Copyright 2004 Atanas Neshkov  Date: 7/31/2012 12:35:58 PM
// Home Page : http://members.fortunecity.com/neshkov/dj.html  - Check often for new version!
// Decompiler options: packimports(3) 

package com.ctb.tdc.web.TTSservlet.utils;

import com.ctb.tdc.web.dto.*;
import java.util.ArrayList;
import java.util.HashMap;
import org.jdom.input.SAXBuilder;

public class MemoryCache
{
    private static final class MemoryCacheHolder
    {

        private static final MemoryCache instance = new MemoryCache();



        private MemoryCacheHolder()
        {
        }
    }


    public TTSSettings getTTSSettings()
    {
        MemoryCache memorycache = this;
        //JVM INSTR monitorenter ;
       // JVM 
        
        return ttsSettings;
       // Exception exception;
       // exception;
       // throw exception;
    }

    public void setTTSSettings(TTSSettings ttssettings)
    {
        synchronized(this)
        {
            ttsSettings = ttssettings;
        }
    }

    private MemoryCache()
    {
        loaded = false;
        loaded = false;
        stateMap = new HashMap();
        subtestInfoMap = new HashMap();
        srvSettings = new ServletSettings();
        clearContent();
        saxBuilder = new SAXBuilder();
        imageMap = new HashMap();
    }

    public static MemoryCache getInstance()
    {
        /*<invalid signature>*//*java.lang.Object local = com/ctb/tdc/web/utils/MemoryCache;*/
        //JVM INSTR monitorenter ;
        return MemoryCacheHolder.instance;
        //Exception exception;
        //exception;
        //throw exception;
    }

    public ServletSettings getSrvSettings()
    {
        MemoryCache memorycache = this;
        //JVM INSTR monitorenter ;
        return srvSettings;
       // Exception exception;
       // exception;
      //  throw exception;
    }

    public void setSrvSettings(ServletSettings servletsettings)
    {
        synchronized(this)
        {
            srvSettings = servletsettings;
        }
    }

    public HashMap getStateMap()
    {
        MemoryCache memorycache = this;
       // JVM INSTR monitorenter ;
        return stateMap;
       // Exception exception;
      //  exception;
      //  throw exception;
    }

    public void setStateMap(HashMap hashmap)
    {
        synchronized(this)
        {
            stateMap = hashmap;
        }
    }

    public boolean isLoaded()
    {
        MemoryCache memorycache = this;
       // JVM INSTR monitorenter ;
        return loaded;
       // Exception exception;
       // exception;
       // throw exception;
    }

    public void setLoaded(boolean flag)
    {
        synchronized(this)
        {
            loaded = flag;
        }
    }

    public HashMap getImageMap()
    {
        MemoryCache memorycache = this;
        //JVM INSTR monitorenter ;
        return imageMap;
       // Exception exception;
       // exception;
       // throw exception;
    }

    public void setImageMap(HashMap hashmap)
    {
        synchronized(this)
        {
            imageMap = hashmap;
        }
    }

    public StateVO setPendingState(String s, String s1)
    {
        /*<invalid signature>*///java.lang.Object local = com/ctb/tdc/web/utils/MemoryCache;
       // JVM INSTR monitorenter ;
        StateVO statevo;
        boolean flag = false;
        statevo = null;
        if(srvSettings.isTmsAckRequired())
        {
            ArrayList arraylist = (ArrayList)stateMap.get(s);
            if(arraylist == null)
                arraylist = new ArrayList();
            for(int i = 0; i < arraylist.size(); i++)
            {
                statevo = (StateVO)arraylist.get(i);
                if(Integer.parseInt(s1) == statevo.getMseq())
                    flag = true;
            }

            if(!flag)
            {
                statevo = new StateVO(Integer.parseInt(s1), "pending", s1, s1);
                arraylist.add(statevo);
                stateMap.put(s, arraylist);
            }
        }
        return statevo;
       // Exception exception;
       // exception;
        //throw exception;
    }

    public void setAcknowledgeState(StateVO statevo)
    {
        synchronized(this)
        {
            if(statevo != null && srvSettings.isTmsAckRequired())
                statevo.setState("ackknowledge");
        }
    }

    public void removeAcknowledgeStates(String s)
    {
        synchronized(this)
        {
            ArrayList arraylist = (ArrayList)stateMap.get(s);
            if(arraylist != null)
            {
                for(int i = arraylist.size() - 1; i >= 0; i--)
                {
                    StateVO statevo = (StateVO)arraylist.get(i);
                    if(statevo.getState().equals("ackknowledge"))
                        arraylist.remove(i);
                }

            }
        }
    }

    public void clearContent()
    {
        synchronized(this)
        {
            itemMap = new HashMap();
            assetMap = new HashMap();
        }
    }

    public HashMap getAssetMap()
    {
        MemoryCache memorycache = this;
       // JVM INSTR monitorenter ;
        return assetMap;
       // Exception exception;
       // exception;
       // throw exception;
    }

    public HashMap getItemMap()
    {
        MemoryCache memorycache = this;
        //JVM INSTR monitorenter ;
        return itemMap;
       // Exception exception;
       // exception;
       // throw exception;
    }

    public HashMap getSubtestInfoMap()
    {
        MemoryCache memorycache = this;
        //JVM INSTR monitorenter ;
        return subtestInfoMap;
       // Exception exception;
       // exception;
       // throw exception;
    }


    static final long serialVersionUID = 1L;
    private HashMap stateMap;
    private HashMap subtestInfoMap;
    private ServletSettings srvSettings;
    private TTSSettings ttsSettings;
    private HashMap itemMap;
    private HashMap assetMap;
    public SAXBuilder saxBuilder;
    private boolean loaded;
    private HashMap imageMap;
}