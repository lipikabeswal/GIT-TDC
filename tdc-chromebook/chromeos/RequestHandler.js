/**
 * Created by Shayan Roychoudhury on 5/23/14.
 */

function RequestHandler()
{
    var persistenceAction= new PersistenceAction();
    var contentAction= new ContentAction();

     this.handleRequest = function(method,xml)
        {   console.log("Handle request :"+method);
            if(method=='login'){
                persistenceAction.login(xml);
            }
            else if(method=='save'){
                persistenceAction.save(xml);
            }
            else if(method=='getSubtest'){
                tdc.contentAction.getSubtest(xml);
            }
            else if(method=='downloadItem'){
                tdc.contentAction.downloadItem(xml);
            }
            else if(method=='getItem'){
                tdc.contentAction.getItem(xml);
            }
            else if(method=='getImage'){
                tdc.contentAction.getImage(xml);
            }
        }
}