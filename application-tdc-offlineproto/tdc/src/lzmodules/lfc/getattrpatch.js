LzView.prototype.getAttributeRelative = function(prop, refView ) {
    var tLink = this.getLinkage( refView );
    if ( ! this.__LZmovieClipRef ){
        this.makeContainerResource();
    }
    if ( ! refView.__LZmovieClipRef ){
        refView.makeContainerResource();
    }

    if (prop == "x" || prop == "y" ) {
        tLink.update ( prop );
        return (tLink.offset[prop] +  this.getProp( prop )) *
               tLink.scale[prop];
    } else if ( prop == "width" || prop == "height" ) {
        var axis = prop == "width" ? "x" : "y"
        tLink.update ( axis )
        return tLink.scale[axis] * this.getProp( prop );
    } else{
        //not yet implemented: rotation, alpha...
    }
}
